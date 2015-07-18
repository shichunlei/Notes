package com.leo.notes.view;

import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class SettingActivity extends BaseActivity {

	@ViewInject(id = R.id.setting)
	private LinearLayout setting;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		setting.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.setting));
		imgLeft.setImageResource(R.drawable.icon_back);
	}

	public void back(View v) {
		finish();
	}
}
