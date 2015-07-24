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
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class LoginActivity extends BaseActivity {

	private static final String TAG = "LoginActivity";

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.btn_login, click = "login")
	private Button btnlogin;
	@ViewInject(id = R.id.btn_regist, click = "regist")
	private Button btnregist;
	@ViewInject(id = R.id.btn_onekey, click = "loginOneKey")
	private Button btnonekey;

	@ViewInject(id = R.id.tv_reset_pwd, click = "resetPWD")
	private TextView resetpwd;

	@ViewInject(id = R.id.et_login_name)
	private EditText etName;
	@ViewInject(id = R.id.et_login_pwd)
	private EditText etPwd;

	int color;
	private CircularProgressDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_login);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
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

	public void loginOneKey(View v) {
		openActivity(LoginOneKeyActivity.class, false);
	}

	public void resetPWD(View v) {
		openActivity(FindPwdActivity.class, false);
	}
}
