package com.leo.notes.view;

import scl.leo.library.utils.other.SPUtils;
import scl.leo.library.utils.other.TimeUtils;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.leo.notes.R;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.util.Quotes;
import com.leo.notes.view.base.BaseActivity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends BaseActivity {

	@ViewInject(id = R.id.welcome)
	private LinearLayout welcome;
	@ViewInject(id = R.id.quote_txt)
	private TextView quoteTxt;

	private static int[] colors = { R.color.purple, R.color.red,
			R.color.orange, R.color.yellow, R.color.green, R.color.blue,
			R.color.cyan };

	private int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		FinalActivity.initInjectedView(this);
		Bmob.initialize(getApplicationContext(), Constants.APPLICATION_ID);

		// 获取当前时间为本周的第几天
		int day = TimeUtils.nowWeek();

		int random = (int) (Math.random() * 99 + 0);

		Log.i("===============", "random = " + random);

		String content = Quotes.quotes[random];

		color = getResources().getColor(colors[day]);
		SPUtils.put(getApplicationContext(), "color", colors[day],
				Constants.COLOR);
		welcome.setBackgroundColor(color);
		quoteTxt.setTextColor(color);
		quoteTxt.setText(content);

		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.logo_alpha);
		animation.setFillAfter(true);
		welcome.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				User current_user = BmobUser
						.getCurrentUser(context, User.class);
				if (null == current_user) {
					openActivity(LoginActivity.class, true);
				} else {
					openActivity(LoginPwdActivity.class, true);
				}
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});
	}
}
