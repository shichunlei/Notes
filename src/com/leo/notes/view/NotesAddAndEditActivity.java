package com.leo.notes.view;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.dialog.AlertDialog;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.leo.notes.R;
import com.leo.notes.been.Group;
import com.leo.notes.been.Notes;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class NotesAddAndEditActivity extends BaseActivity implements
		OnClickListener {

	private static final String TAG = "NotesAddAndEditActivity";

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.layout_all, click = "title")
	private LinearLayout layout_all;

	@ViewInject(id = R.id.img_arrow)
	private ImageView imgArrow;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.img_right, click = "save")
	private ImageView imgRight;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.ll_text)
	private LinearLayout bg;
	@ViewInject(id = R.id.et_title)
	private EditText title;
	@ViewInject(id = R.id.et_content)
	private EditText content;

	private CircularProgressDialog loading;

	int color;

	private User current_user;
	private String id;
	private String tag;
	private int bgcolor;

	PopupWindow morePop;

	private TextView study;
	private TextView life;
	private TextView work;
	private TextView _default;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_note_addoredit);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		bgcolor = bundle.getInt("bgcolor");

		Log.i(TAG, bgcolor + "-----------------");

		tag = bundle.getString("tag");
		content.setBackgroundResource(Constants.bgcolors[bgcolor]);
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));

		loading = CircularProgressDialog.show(context);
		current_user = BmobUser.getCurrentUser(context, User.class);
		if (tag.equals("add")) {
			tvTitle.setText("默认");
		} else if (tag.equals("edit")) {
			id = getStringExtra("id");
			loading.show();
			getNotesInfo(id);
		}
		imgArrow.setVisibility(View.VISIBLE);

		title_bar.setBackgroundColor(color);
		imgLeft.setImageResource(R.drawable.delete);
		imgRight.setImageResource(R.drawable.save);
	}

	public void onColorClicked(View v) {
		bgcolor = Integer.valueOf(v.getTag().toString()) - 1;
		content.setBackgroundResource(Constants.bgcolors[bgcolor]);
	}

	private void getNotesInfo(String id) {
		BmobQuery<Notes> query = new BmobQuery<Notes>();
		query.include("group");
		query.getObject(context, id, new GetListener<Notes>() {

			@Override
			public void onSuccess(Notes object) {
				loading.dismiss();
				showToast(getString(R.string.q_success));
				title.setText(object.getTitle());
				content.setText(object.getContent());
				tvTitle.setText(object.getGroup().getName());
			}

			@Override
			public void onFailure(int code, String msg) {
				loading.dismiss();
				showToast(getString(R.string.q_fail));
				Log.i(TAG, msg);
			}
		});
	}

	public void back(View view) {
		showDialog();
	}

	private void showDialog() {
		new AlertDialog(context)
				.builder()
				.setTitle(getString(R.string.alert_hint))
				.setMsg(getString(R.string.exit))
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								finish();
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
	}

	public void save(View view) {
		String _title = title.getText().toString();
		String _content = content.getText().toString();
		String title_ = tvTitle.getText().toString();

		loading.show();
		save(id, _title, _content, bgcolor, title_);
	}

	public void title(View view) {
		showPop();
	}

	@SuppressWarnings("deprecation")
	private void showPop() {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_pop,
				null);
		// 注入
		study = (TextView) view.findViewById(R.id.layout_study);
		life = (TextView) view.findViewById(R.id.layout_life);
		_default = (TextView) view.findViewById(R.id.layout_default);
		work = (TextView) view.findViewById(R.id.layout_work);

		study.setOnClickListener(this);
		life.setOnClickListener(this);
		work.setOnClickListener(this);
		_default.setOnClickListener(this);
		morePop = new PopupWindow(view, mScreenWidth, 600);

		morePop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					morePop.dismiss();
					return true;
				}
				return false;
			}
		});

		morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		morePop.setTouchable(true);
		morePop.setFocusable(true);
		morePop.setOutsideTouchable(true);
		morePop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从顶部弹下
		morePop.setAnimationStyle(R.style.MenuPop);
		morePop.showAsDropDown(title_bar, 0, -dip2px(this, 2.0F));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_study:
			morePop.dismiss();
			tvTitle.setText("学习");
			break;
		case R.id.layout_life:
			morePop.dismiss();
			tvTitle.setText("生活");
			break;
		case R.id.layout_default:
			morePop.dismiss();
			tvTitle.setText("默认");
			break;
		case R.id.layout_work:
			morePop.dismiss();
			tvTitle.setText("工作");
			break;

		default:
			break;
		}
	}

	/**
	 * 保存
	 * 
	 * @param id
	 * @param title
	 * @param content
	 * @param bgcolor
	 * @param title_
	 */
	private void save(final String id, final String title,
			final String content, final int bgcolor, String title_) {

		BmobQuery<Group> query = new BmobQuery<Group>();
		query.addWhereEqualTo("name", title_);
		query.findObjects(this, new FindListener<Group>() {

			@Override
			public void onError(int code, String msg) {
				Log.i(TAG, msg);
			}

			@Override
			public void onSuccess(List<Group> object) {
				Log.i(TAG, object.toString());

				Notes notes = new Notes();
				notes.setTitle(title);
				notes.setContent(content);
				notes.setColor(bgcolor);
				notes.setAuthor(current_user);
				notes.setGroup(object.get(0));

				if (getStringExtra("tag").equals("add")) {
					notes.save(context, new SaveListener() {
						@Override
						public void onSuccess() {
							loading.dismiss();
							showToast(getString(R.string.i_success));
							setResult(RESULT_OK);
							finish();
						}

						@Override
						public void onFailure(int code, String msg) {
							loading.dismiss();
							showToast(getString(R.string.i_fail));
							Log.i(TAG, msg);
						}
					});
				} else if (getStringExtra("tag").equals("edit")) {
					notes.update(context, id, new UpdateListener() {
						@Override
						public void onSuccess() {
							loading.dismiss();
							showToast(getString(R.string.u_success));
							setResult(RESULT_OK);
							finish();
						}

						@Override
						public void onFailure(int code, String msg) {
							loading.dismiss();
							showToast(getString(R.string.u_fail));
							Log.i(TAG, msg);
						}
					});
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
