package com.leo.notes.fragment;

import scl.leo.library.button.ToggleButton.ToggleButton;
import scl.leo.library.button.ToggleButton.ToggleButton.OnToggleChanged;
import scl.leo.library.image.HeaderImageView;
import scl.leo.library.utils.other.AppUtils;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.bmob.BmobPro;
import com.leo.notes.R;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.view.*;
import com.leo.notes.view.base.BaseFragment;

public class MenuFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = "MenuFragment";

	private View view;

	/** 关于 */
	@ViewInject(id = R.id.tv_aboutme)
	private TextView aboutme;
	/** 反馈 */
	@ViewInject(id = R.id.tv_feedback)
	private TextView feedback;
	/** 设置 */
	@ViewInject(id = R.id.tv_setting)
	private TextView setting;
	/** 清理缓存 */
	@ViewInject(id = R.id.rl_clean)
	private RelativeLayout clean;
	/** 缓存大小 */
	@ViewInject(id = R.id.tv_clean_size)
	private TextView cleansize;
	/** 版本 */
	@ViewInject(id = R.id.rl_version)
	private RelativeLayout version;
	/** 版本名 */
	@ViewInject(id = R.id.tv_version)
	private TextView versionname;
	/** 夜间模式 */
	@ViewInject(id = R.id.tb_model)
	private ToggleButton model;
	/** 注销 */
	@ViewInject(id = R.id.tv_exit)
	private TextView exit;

	@ViewInject(id = R.id.tv_username)
	private TextView username;
	@ViewInject(id = R.id.img_headerpic)
	private HeaderImageView headpic;

	@ViewInject(id = R.id.ll_personal_info)
	private LinearLayout personal_info;

	private String versionName;
	private int versionCode;

	User userInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_menu, container, false);
		FinalActivity.initInjectedView(this, view);
		init();

		return view;
	}

	private void init() {
		versionName = AppUtils.getVersionName(getActivity());
		versionCode = AppUtils.getVersionCode(getActivity());
		Log.i(TAG, "版本名：" + versionName + "版本号：" + versionCode);
		versionname.setText(versionName);

		userInfo = BmobUser.getCurrentUser(getActivity(), User.class);
		username.setText(userInfo.getUsername());

		// 文件大小（单位：字节）
		String cacheSize = String.valueOf(BmobPro.getInstance(getActivity())
				.getCacheFileSize());
		Log.i("缓存大小", cacheSize);
		// 对文件大小进行格式化，转化为'B'、'K'、'M'、'G'等单位
		String formatSize = BmobPro.getInstance(getActivity())
				.getCacheFormatSize();

		if ((Boolean) SPUtils.get(getActivity(), "theme", false,
				Constants.SETTING_DATA)) {
			model.setToggleOn(true);
		} else {
			model.setToggleOff(true);
		}

		cleansize.setText(formatSize);
		aboutme.setOnClickListener(this);
		feedback.setOnClickListener(this);
		setting.setOnClickListener(this);
		clean.setOnClickListener(this);
		version.setOnClickListener(this);
		exit.setOnClickListener(this);
		personal_info.setOnClickListener(this);

		model.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "theme", true, Constants.SETTING_DATA);
				} else {
					SPUtils.put(context, "theme", false, Constants.SETTING_DATA);
				}
				openActivity(MainFragmentActivity.class, true);
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_aboutme:
			openActivity(AboutMeActivity.class, false);
			break;

		case R.id.tv_feedback:
			openActivity(FeedbackActivity.class, false);
			break;

		case R.id.tv_setting:
			openActivity(SettingActivity.class, true);
			MainFragmentActivity.mSlidingMenu.toggle();
			break;

		case R.id.rl_clean:
			BmobPro.getInstance(context).clearCache();
			showToast(getString(R.string.clean_success));
			cleansize.setText("0B");
			break;

		case R.id.rl_version:
			showToast(getString(R.string.version));
			versionname.setText(versionName);
			break;

		case R.id.tv_exit:
			BmobUser.logOut(getActivity()); // 清除缓存用户对象
			SPUtils.clear(getActivity(), Constants.PASSWORD);
			BmobUser currentUser = BmobUser.getCurrentUser(getActivity()); // 现在的currentUser是null了
			Log.i("currentUser", currentUser + "");
			openActivity(LoginActivity.class, true);
			break;

		case R.id.ll_personal_info:
			openActivity(PersonalInfoActivity.class, Constants.PERSONAL_INFO);
			break;

		default:
			break;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != getActivity().RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case Constants.PERSONAL_INFO:
			userInfo = BmobUser.getCurrentUser(getActivity(), User.class);
			username.setText(userInfo.getUsername());
			break;
		}
	}

}
