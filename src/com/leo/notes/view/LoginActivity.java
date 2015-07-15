package com.leo.notes.view;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import scl.leo.library.utils.other.StringUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.leo.notes.R;
import com.leo.notes.been.User;
import com.leo.notes.view.base.BaseActivity;

public class LoginActivity extends BaseActivity {

	@ViewInject(id = R.id.login)
	RelativeLayout login;

	@ViewInject(id = R.id.ivTitleName)
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
				(Integer) SPUtils.get(context, "color", R.color.gray, "COLOR"));
		login.setBackgroundColor(color);
		tvTitle.setText("登录");
		loading = CircularProgressDialog.show(context);
	}

	public void login(View v) {
		String name = etName.getText().toString().trim();
		String pwd = etPwd.getText().toString().trim();

		if (StringUtil.isEmpty(name)) {
			showToast("账户不能为空");
		} else if (StringUtil.isEmpty(pwd)) {
			showToast("密码不能为空");
		} else {
			loading.show();
			postLogin(name, pwd);
		}
	}

	private void postLogin(String name, String pwd) {
		User user = new User();
		user.setUsername(name);
		user.setPassword(pwd);
		user.login(this, new SaveListener() {
			@Override
			public void onSuccess() {
				loading.dismiss();
				showToast("登录成功");
				openActivity(LoginPwdActivity.class, true);
			}

			@Override
			public void onFailure(int code, String msg) {
				loading.dismiss();
				showToast("登录失败:" + msg);
			}
		});

	}

	public void regist(View v) {
		openActivity(RegistActivity.class, false);
	}
}
