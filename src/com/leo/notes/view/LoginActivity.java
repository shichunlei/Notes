package com.leo.notes.view;

import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.view.base.BaseActivity;

public class LoginActivity extends BaseActivity {

	@ViewInject(id = R.id.login)
	LinearLayout login;

	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		FinalActivity.initInjectedView(this);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray, "COLOR"));
		login.setBackgroundColor(color);
		tvTitle.setText("登录");
	}
}
