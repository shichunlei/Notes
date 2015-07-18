package com.leo.notes.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import scl.leo.library.utils.other.StringUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.leo.notes.R;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class RegistActivity extends BaseActivity {

	@ViewInject(id = R.id.regist)
	RelativeLayout regist;

	int color;

	private static final String TAG = "RegistActivity";

	private CircularProgressDialog loading;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.et_regist_name)
	private EditText etName;

	@ViewInject(id = R.id.et_regist_pwd)
	private EditText etPwd;
	@ViewInject(id = R.id.et_regist_repwd)
	private EditText etRePwd;

	@ViewInject(id = R.id.btn_regist, click = "regist")
	private Button btnRegist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		regist.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.regist));
		imgLeft.setImageResource(R.drawable.icon_back);

		loading = CircularProgressDialog.show(context);
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}

	/**
	 * 验证注册信息
	 * 
	 * @param v
	 */
	public void regist(View v) {
		String name = etName.getText().toString().trim();
		String pwd = etPwd.getText().toString().trim();
		String repwd = etRePwd.getText().toString().trim();

		if (StringUtil.isEmpty(name)) {
			showToast(getString(R.string.username_empty));
		} else if (StringUtil.isEmpty(pwd)) {
			showToast(getString(R.string.pwd_empty));
		} else if (!StringUtil.pwdVal(pwd, repwd)) {
			showToast(getString(R.string.inconsistent_password));
		} else {
			loading.show();
			postRegist(name, pwd);
		}
	}

	/**
	 * 提交注册信息
	 * 
	 * @param name
	 * @param pwd
	 */
	private void postRegist(String name, String pwd) {
		User user = new User();
		user.setUsername(name);
		user.setPassword(pwd);

		user.signUp(context, new SaveListener() {

			@Override
			public void onSuccess() {
				loading.dismiss();
				showToast(getString(R.string.regist_success));
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				loading.dismiss();
				showToast(getString(R.string.regist_fail));
				Log.i(TAG, msg);
			}
		});
	}

}
