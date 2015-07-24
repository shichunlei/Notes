package com.leo.notes.view;

import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class LoginPwdActivity extends BaseActivity {

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.tip)
	private TextView tip;
	@ViewInject(id = R.id.passwordfild)
	private LinearLayout pfild;
	@ViewInject(id = R.id.password1)
	private EditText p1;
	@ViewInject(id = R.id.password2)
	private EditText p2;
	@ViewInject(id = R.id.password3)
	private EditText p3;
	@ViewInject(id = R.id.password4)
	private EditText p4;

	@ViewInject(id = R.id.point1)
	private ImageView point1;
	@ViewInject(id = R.id.point2)
	private ImageView point2;
	@ViewInject(id = R.id.point3)
	private ImageView point3;
	@ViewInject(id = R.id.point4)
	private ImageView point4;

	@ViewInject(id = R.id.layout_keyboard)
	private LinearLayout layout_keyboard;

	int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_login_pwd);

		FinalActivity.initInjectedView(this);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		layout_keyboard.setBackgroundColor(color);
		pfild.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.login));

		String password = (String) SPUtils.get(context, "password", "nothing",
				Constants.PASSWORD);
		if ("nothing".equals(password)) {
			tip.setText(getString(R.string.setting_pwd));
		} else {
			tip.setText(getString(R.string.input_pwd));
		}
	}

	public void one(View v) {
		setValue(1 + "");
	}

	public void tow(View v) {
		setValue(2 + "");
	}

	public void three(View v) {
		setValue(3 + "");
	}

	public void four(View v) {
		setValue(4 + "");
	}

	public void five(View v) {
		setValue(5 + "");
	}

	public void six(View v) {
		setValue(6 + "");
	}

	public void seven(View v) {
		setValue(7 + "");
	}

	public void eight(View v) {
		setValue(8 + "");
	}

	public void nine(View v) {
		setValue(9 + "");
	}

	public void zero(View v) {
		setValue(0 + "");
	}

	public void ok(View v) {

	}

	/**
	 * 删除
	 * 
	 * @param v
	 */
	public void del(View v) {
		if (!TextUtils.isEmpty(p4.getText())) {
			p4.setText("");
			addPoint(3);
		} else if (!TextUtils.isEmpty(p3.getText())) {
			p3.setText("");
			addPoint(2);
		} else if (!TextUtils.isEmpty(p2.getText())) {
			p2.setText("");
			addPoint(1);
		} else if (!TextUtils.isEmpty(p1.getText())) {
			p1.setText("");
			addPoint(0);
		}
	}

	/**
	 * 设值
	 * 
	 * @param 1、2、3、4、5、6、7、8、9、0
	 */
	private void setValue(String text) {
		if (TextUtils.isEmpty(p1.getText())) {
			p1.setText(text);
			addPoint(1);
		} else if (TextUtils.isEmpty(p2.getText())) {
			p2.setText(text);
			addPoint(2);
		} else if (TextUtils.isEmpty(p3.getText())) {
			p3.setText(text);
			addPoint(3);
		} else if (TextUtils.isEmpty(p4.getText())) {
			p4.setText(text);
			addPoint(4);
			submit();
		}
	}

	public void submit() {
		String password = p1.getText().toString() + p2.getText().toString()
				+ p3.getText().toString() + p4.getText().toString();
		if (!TextUtils.isEmpty(password) && password.length() == 4) {
			String pwd = (String) SPUtils.get(context, "password", "nothing",
					Constants.PASSWORD);
			if ("nothing".equals(pwd)) {
				SPUtils.put(context, "password", password, Constants.PASSWORD);
				showToast(getString(R.string.setting_pwd_success));
				openActivity(MainFragmentActivity.class, true);
			} else if (password.equals(pwd)) {
				openActivity(MainFragmentActivity.class, true);
			} else {
				Animation anim = AnimationUtils.loadAnimation(context,
						R.anim.myanim);
				pfild.startAnimation(anim);
				tip.setText(getString(R.string.pwd_fail));
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						tip.setText(getString(R.string.setting_pwd));
						p4.setText("");
						p3.setText("");
						p2.setText("");
						p1.setText("");
						addPoint(0);
					}
				});
			}
		}
	}

	private void addPoint(int position) {
		point1.setVisibility(View.GONE);
		point2.setVisibility(View.GONE);
		point3.setVisibility(View.GONE);
		point4.setVisibility(View.GONE);
		switch (position) {
		case 0:
			break;
		case 1:
			point1.setVisibility(View.VISIBLE);
			break;
		case 2:
			point2.setVisibility(View.VISIBLE);
			point1.setVisibility(View.VISIBLE);
			break;
		case 3:
			point3.setVisibility(View.VISIBLE);
			point1.setVisibility(View.VISIBLE);
			point2.setVisibility(View.VISIBLE);
			break;
		case 4:
			point4.setVisibility(View.VISIBLE);
			point1.setVisibility(View.VISIBLE);
			point2.setVisibility(View.VISIBLE);
			point3.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

}
