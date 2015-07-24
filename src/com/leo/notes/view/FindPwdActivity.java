package com.leo.notes.view;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
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
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

import com.leo.notes.R;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class FindPwdActivity extends BaseActivity {

	private static final String TAG = "FindPwdActivity";

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	MyCountTimer timer;

	@ViewInject(id = R.id.et_phone)
	EditText et_phone;
	@ViewInject(id = R.id.et_verify_code)
	EditText et_code;
	@ViewInject(id = R.id.et_pwd)
	EditText et_pwd;
	@ViewInject(id = R.id.btn_send, click = "send")
	Button btn_send;
	@ViewInject(id = R.id.btn_reset, click = "reset")
	Button btn_reset;

	int color;

	private CircularProgressDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_findpwd);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.find_pwd));
		imgLeft.setImageResource(R.drawable.icon_back);
	}

	public void send(View v) {
		String number = et_phone.getText().toString();
		if (!TextUtils.isEmpty(number)) {
			timer = new MyCountTimer(60000, 1000);
			timer.start();
			BmobSMS.requestSMSCode(context, number, "ResetPwd",
					new RequestSMSCodeListener() {

						@Override
						public void done(Integer smsId, BmobException ex) {
							if (ex == null) {// 验证码发送成功
								showToast("验证码发送成功");// 用于查询本次短信发送详情
							} else {// 如果验证码发送错误，可停止计时
								timer.cancel();
							}
						}
					});
		} else {
			showToast("请输入手机号码");
		}
	}

	public void reset(View v) {
		final String code = et_code.getText().toString();
		final String pwd = et_pwd.getText().toString();
		if (TextUtils.isEmpty(code)) {
			showToast(getString(R.string.input_verify_code));
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			showToast(getString(R.string.input_pwd));
			return;
		}

		loading.show();
		// V3.3.9提供的重置密码功能，只需要输入验证码和新密码即可
		BmobUser.resetPasswordBySMSCode(this, code, pwd,
				new ResetPasswordByCodeListener() {

					@Override
					public void done(BmobException ex) {
						loading.dismiss();
						if (ex == null) {
							showToast("密码重置成功");
							finish();
						} else {
							Log.i(TAG, "密码重置失败：code=" + ex.getErrorCode()
									+ "，错误描述：" + ex.getLocalizedMessage());
						}
					}
				});
	}

	public void back(View v) {
		finish();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}
}