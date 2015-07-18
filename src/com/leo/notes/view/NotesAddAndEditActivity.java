package com.leo.notes.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.dialog.AlertDialog;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.leo.notes.R;
import com.leo.notes.been.Notes;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class NotesAddAndEditActivity extends BaseActivity {

	private static final String TAG = "NotesAddAndEditActivity";

	@ViewInject(id = R.id.notesedit)
	private LinearLayout notesedit;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.img_right, click = "save")
	private ImageView imgRight;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.et_title)
	private EditText title;
	@ViewInject(id = R.id.et_content)
	private EditText content;

	private CircularProgressDialog loading;

	int color;

	User current_user;
	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_edit);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);
		current_user = BmobUser.getCurrentUser(context, User.class);
		if (getStringExtra("tag").equals("add")) {
			tvTitle.setText(R.string.add_notes);
		} else if (getStringExtra("tag").equals("edit")) {
			tvTitle.setText(R.string.edit_notes);
			id = getStringExtra("id");
			loading.show();
			getNotesInfo(id);
		}
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		notesedit.setBackgroundColor(color);

		imgLeft.setImageResource(R.drawable.delete);
		imgRight.setImageResource(R.drawable.save);
	}

	private void getNotesInfo(String id) {
		BmobQuery<Notes> query = new BmobQuery<Notes>();
		query.getObject(context, id, new GetListener<Notes>() {

			@Override
			public void onSuccess(Notes object) {
				loading.dismiss();
				showToast(getString(R.string.q_success));
				title.setText(object.getTitle());
				content.setText(object.getContent());
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

		loading.show();
		save(id, _title, _content);
	}

	private void save(String id, String title, String content) {
		if (getStringExtra("tag").equals("add")) {
			Notes notes = new Notes();
			notes.setTitle(title);
			notes.setContent(content);
			notes.setAuthor(current_user);
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
			Notes notes = new Notes();
			notes.setTitle(title);
			notes.setContent(content);
			notes.update(this, id, new UpdateListener() {

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

	// 按下菜单键时
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
