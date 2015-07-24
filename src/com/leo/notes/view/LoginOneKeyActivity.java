package com.leo.notes.view;

import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import com.leo.notes.R;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

public class LoginOneKeyActivity extends BaseActivity {

	private static final String TAG = "LoginOneKeyActivity";

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	int color;

	MyCountTimer timer;

	@ViewInject(id = R.id.et_phone)
	EditText et_phone;
	@ViewInject(id = R.id.et_verify_code)
	EditText et_code;
	@ViewInject(id = R.id.btn_send, click = "send")
	Button btn_send;
	@ViewInject(id = R.id.btn_login, click = "login")
	Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_login_onekey);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		tvTitle.setText("手机号码一键登录");
		ivTitleLeft.setImageResource(R.drawable.icon_back);

	}

	public void send(View v) {
		requestSMSCode();
	}

	public void login(View v) {
		oneKeyLogin();
	}

	class MyCountTimer extends CountDownTimer {

		public MyCountTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_send.setText((millisUntilFinished / 1000) + "秒后重发");
		}

		@Override
		public void onFinish() {
			btn_send.setText("重新发送验证码");
		}
	}

	private void requestSMSCode() {
		String number = et_phone.getText().toString();
		if (!TextUtils.isEmpty(number)) {
			timer = new MyCountTimer(60000, 1000);
			timer.start();
			BmobSMS.requestSMSCode(this, number, "一键注册或登录模板",
					new RequestSMSCodeListener() {

						@Override
						public void done(Integer smsId, BmobException ex) {
							if (ex == null) {// 验证码发送成功
								showToast("验证码发送成功");// 用于查询本次短信发送详情
							} else {
								timer.cancel();
							}
						}
					});
		} else {
			showToast(getString(R.string.input_mobile));
		}
	}

	/**
	 * 一键登录操作
	 * 
	 * @method login
	 * @return void
	 * @exception
	 */
	private void oneKeyLogin() {
		final String phone = et_phone.getText().toString();
		final String code = et_code.getText().toString();

		if (TextUtils.isEmpty(phone)) {
			showToast(getString(R.string.input_mobile));
			return;
		}

		if (TextUtils.isEmpty(code)) {
			showToast(getString(R.string.input_verify_code));
			return;
		}
		final ProgressDialog progress = new ProgressDialog(
				LoginOneKeyActivity.this);
		progress.setMessage("正在验证短信验证码...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9提供的一键注册或登录方式，可传手机号码和验证码
		BmobUser.signOrLoginByMobilePhone(LoginOneKeyActivity.this, phone,
				code, new LogInListener<User>() {

					@Override
					public void done(User user, BmobException ex) {
						progress.dismiss();
						if (ex == null) {
							showToast(getString(R.string.login_success));
							openActivity(LoginPwdActivity.class, true);
						} else {
							showToast(getString(R.string.login_fail));
							Log.i(TAG, "登录失败：code=" + ex.getErrorCode()
									+ "，错误描述：" + ex.getLocalizedMessage());
						}
					}
				});
	}

	public void back(View v) {
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}
}
