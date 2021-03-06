package com.leo.notes.view;

import scl.leo.library.utils.other.AppUtils;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class AboutMeActivity extends BaseActivity {

	private static final String TAG = "AboutMeActivity";

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.tv_version)
	private TextView version;

	private int color;

	private String versionName;
	private int versionCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_about_me);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		tvTitle.setText(R.string.about_me);
		ivTitleLeft.setImageResource(R.drawable.icon_back);

		versionName = AppUtils.getVersionName(context);
		versionCode = AppUtils.getVersionCode(context);
		Log.i(TAG, "版本名：" + versionName + "版本号：" + versionCode);
		version.setText("版本：" + versionName);
	}

	public void back(View v) {
		finish();
	}
}
