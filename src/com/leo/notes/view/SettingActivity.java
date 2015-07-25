package com.leo.notes.view;

import java.util.ArrayList;
import java.util.List;

import scl.leo.library.button.ToggleButton.ToggleButton;
import scl.leo.library.button.ToggleButton.ToggleButton.OnToggleChanged;
import scl.leo.library.dialog.AlertDialog;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.leo.notes.R;
import com.leo.notes.been.Notes;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class SettingActivity extends BaseActivity {

	private static final String TAG = "SettingActivity";

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	/** 笔记同步 */
	@ViewInject(id = R.id.tv_synchronous)
	private TextView synchronous;
	/** 笔记同步 */
	@ViewInject(id = R.id.tv_synchronous_loading)
	private TextView synchronous_loading;

	/** 笔记同步 */
	@ViewInject(id = R.id.ll_synchronous, click = "synchronous")
	private LinearLayout ll_synchronous;
	/** 右手操作模式 */
	@ViewInject(id = R.id.rl_model)
	private RelativeLayout rl_model;
	/** 卡片模式 */
	@ViewInject(id = R.id.rl_list_grid)
	private RelativeLayout rl_list_grid;
	/** 清空笔记 */
	@ViewInject(id = R.id.ll_clean, click = "clean")
	private LinearLayout ll_clean;
	/** 恢复默认设置 */
	@ViewInject(id = R.id.ll_default_setting, click = "setting")
	private LinearLayout ll_setting;

	/** 右手操作模式 */
	@ViewInject(id = R.id.tb_model)
	private ToggleButton tb_model;
	/** 卡片模式 */
	@ViewInject(id = R.id.tb_list_grid)
	private ToggleButton tb_list_grid;

	private CircularProgressDialog loading;

	private List<Notes> noteList = new ArrayList<Notes>();
	private int size;

	private int color;

	private User current_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_setting);
		FinalActivity.initInjectedView(this);
		init();
		setToggleChanged();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);

		current_user = BmobUser.getCurrentUser(context, User.class);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.setting));
		imgLeft.setImageResource(R.drawable.icon_back);

		if ((Boolean) SPUtils.get(context, "model", false,
				Constants.SETTING_DATA)) {
			tb_model.setToggleOn(true);
		} else {
			tb_model.setToggleOff(true);
		}

		if ((Boolean) SPUtils.get(context, "list_grid", false,
				Constants.SETTING_DATA)) {
			tb_list_grid.setToggleOn(true);
		} else {
			tb_list_grid.setToggleOff(true);
		}
	}

	private void setToggleChanged() {
		tb_model.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "model", true, Constants.SETTING_DATA);
				} else {
					SPUtils.put(context, "model", false, Constants.SETTING_DATA);
				}
			}
		});

		tb_list_grid.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "list_grid", true,
							Constants.SETTING_DATA);
				} else {
					SPUtils.put(context, "list_grid", false,
							Constants.SETTING_DATA);
				}
			}
		});
	}

	public void synchronous(View v) {

	}

	/**
	 * 清空笔记
	 * 
	 * @param v
	 */
	public void clean(View v) {
		// 注意：目前不提供查询条件方式的删除方法，删除数据只能通过objectId来删除。故使用查询所有数据，循环删除的方法
		if (null != noteList) {
			noteList.clear();
		}
		loading.show();
		BmobQuery<Notes> query = new BmobQuery<Notes>();
		// 查询user为当前用户的所有信息
		query.addWhereEqualTo("author", current_user);
		// 返回10000条数据(返回所有数据)，如果不加上这条语句，默认返回10条数据
		query.setLimit(10000);
		// 执行查询方法
		query.findObjects(this, new FindListener<Notes>() {
			@Override
			public void onSuccess(List<Notes> object) {
				loading.dismiss();
				noteList.addAll(object);
				object.clear();
				size = noteList.size();
				// 提示用户是否确定删除
				showDialog();
			}

			@Override
			public void onError(int code, String msg) {
				loading.dismiss();
				Log.i(TAG, msg);
			}
		});

	}

	/**
	 * 提示用户是否确定删除
	 */
	private void showDialog() {
		new AlertDialog(context)
				.builder()
				.setTitle(getString(R.string.alert_hint))
				.setMsg("共有" + size + "条笔记\n你确定要清空笔记吗？")
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								loading.show();
								cleanNotes();
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
	}

	/**
	 * 删除笔记
	 */
	private void cleanNotes() {
		for (Notes notes : noteList) {
			notes.setObjectId(notes.getObjectId());
			notes.delete(context, new DeleteListener() {

				@Override
				public void onSuccess() {
					size--;
					Log.i(TAG, "size = " + size);
				}

				@Override
				public void onFailure(int code, String msg) {
					loading.dismiss();
					showToast(getString(R.string.del_fail));
					Log.i(TAG, msg);
				}
			});
		}
		loading.dismiss();
		showToast(getString(R.string.del_success));
	}

	public void setting(View v) {
		// 右手模式恢复默认值左手操作
		tb_model.setToggleOff(true);
		SPUtils.put(context, "model", false, Constants.SETTING_DATA);
		// 卡片模式恢复默认值列表模式
		tb_list_grid.setToggleOff(true);
		SPUtils.put(context, "list_grid", false, Constants.SETTING_DATA);
		// 主题恢复默认值白天主题
		SPUtils.put(context, "theme", false, Constants.SETTING_DATA);
	}

	public void back(View v) {
		openActivity(MainFragmentActivity.class, true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			openActivity(MainFragmentActivity.class, true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
