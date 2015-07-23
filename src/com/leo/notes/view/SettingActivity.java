package com.leo.notes.view;

import scl.leo.library.button.ToggleButton.ToggleButton;
import scl.leo.library.button.ToggleButton.ToggleButton.OnToggleChanged;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class SettingActivity extends BaseActivity {

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.tv_synchronous)
	private TextView synchronous;
	@ViewInject(id = R.id.tv_synchronous_loading)
	private TextView synchronous_loading;

	@ViewInject(id = R.id.ll_synchronous, click = "synchronous")
	private LinearLayout ll_synchronous;
	@ViewInject(id = R.id.rl_model)
	private RelativeLayout rl_model;
	@ViewInject(id = R.id.rl_list_grid)
	private RelativeLayout rl_list_grid;
	@ViewInject(id = R.id.ll_clean, click = "clean")
	private LinearLayout ll_clean;
	@ViewInject(id = R.id.ll_default_setting, click = "setting")
	private LinearLayout ll_setting;

	@ViewInject(id = R.id.tb_model)
	private ToggleButton tb_model;
	@ViewInject(id = R.id.tb_list_grid)
	private ToggleButton tb_list_grid;

	int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		FinalActivity.initInjectedView(this);
		init();
		setToggleChanged();
	}

	private void init() {
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

	public void clean(View v) {

	}

	public void setting(View v) {
		tb_model.setToggleOff(true);
		SPUtils.put(context, "model", false, Constants.SETTING_DATA);
		tb_list_grid.setToggleOff(true);
		SPUtils.put(context, "list_grid", false, Constants.SETTING_DATA);
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
