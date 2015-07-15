package com.leo.notes.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.view.base.BaseActivity;

public class SettingActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		tvTitle.setText("设置");
		ivTitleLeft.setImageResource(R.drawable.icon_back);
	}

	public void back(View v) {
		finish();
	}
}
