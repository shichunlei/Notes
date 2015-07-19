package com.leo.notes.view;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import scl.leo.library.utils.other.StringUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.leo.notes.R;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class LoginActivity extends BaseActivity {

	private static final String TAG = "LoginActivity";

	@ViewInject(id = R.id.login)
	RelativeLayout login;

	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.btn_login, click = "login")
	private Button btnlogin;
	@ViewInject(id = R.id.btn_regist, click = "regist")
	private Button btnregist;

	@ViewInject(id = R.id.et_login_name)
	private EditText etName;
	@ViewInject(id = R.id.et_login_pwd)
	private EditText etPwd;

	int color;
	private CircularProgressDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		login.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.login));
		loading = CircularProgressDialog.show(context);
	}

	public void login(View v) {
		String name = etName.getText().toString().trim();
		String password = etPwd.getText().toString().trim();

		if (StringUtil.isEmpty(name)) {
			showToast(getString(R.string.username_empty));
		} else if (StringUtil.isEmpty(password)) {
			showToast(getString(R.string.pwd_empty));
		} else {
			loading.show();
			postLogin(name, password);
		}
	}

	private void postLogin(String name, String password) {
		// User user = new User();
		// user.setUsername(name);
		// user.setPassword(password);
		// user.login(this, new SaveListener() {
		// @Override
		// public void onSuccess() {
		// loading.dismiss();
		// showToast(getString(R.string.login_success));
		// openActivity(LoginPwdActivity.class, true);
		// }
		//
		// @Override
		// public void onFailure(int code, String msg) {
		// loading.dismiss();
		// showToast(getString(R.string.login_fail));
		// Log.i(TAG, msg);
		// }
		// });

		User.loginByAccount(context, name, password, new LogInListener<User>() {

			@Override
			public void done(User user, BmobException ex) {
				loading.dismiss();
				if (ex == null) {
					showToast(getString(R.string.login_success));
					openActivity(LoginPwdActivity.class, true);
				} else {
					showToast(getString(R.string.login_fail));
					Log.i(TAG,
							"登录失败：code=" + ex.getErrorCode() + "，错误描述："
									+ ex.getLocalizedMessage());
				}
			}
		});

	}

	public void regist(View v) {
		openActivity(RegistActivity.class, false);
	}
}
