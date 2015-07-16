package com.leo.notes.fragment;

import scl.leo.library.image.HeaderImageView;
import scl.leo.library.utils.other.AppUtils;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

	private View view;

	@ViewInject(id = R.id.tv_aboutme)
	private TextView aboutme;
	@ViewInject(id = R.id.tv_feedback)
	private TextView feedback;
	@ViewInject(id = R.id.tv_setting)
	private TextView setting;
	@ViewInject(id = R.id.rl_clean)
	private RelativeLayout clean;
	@ViewInject(id = R.id.tv_clean_size)
	private TextView cleansize;
	@ViewInject(id = R.id.tv_version)
	private TextView version;
	@ViewInject(id = R.id.tv_exit)
	private TextView exit;

	@ViewInject(id = R.id.tv_username)
	private TextView username;
	@ViewInject(id = R.id.img_headerpic)
	private HeaderImageView headpic;

	private String versionName;
	private int versionCode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_menu, container, false);
		FinalActivity.initInjectedView(this, view);
		init();

		return view;
	}

	private void init() {
		User userInfo = BmobUser.getCurrentUser(getActivity(), User.class);
		username.setText(userInfo.getUsername());
		// 文件大小（单位：字节）
		String cacheSize = String.valueOf(BmobPro.getInstance(getActivity())
				.getCacheFileSize());
		Log.i("缓存大小", cacheSize);
		// 对文件大小进行格式化，转化为'B'、'K'、'M'、'G'等单位
		String formatSize = BmobPro.getInstance(getActivity())
				.getCacheFormatSize();
		cleansize.setText(formatSize + "B");
		aboutme.setOnClickListener(this);
		feedback.setOnClickListener(this);
		setting.setOnClickListener(this);
		clean.setOnClickListener(this);
		version.setOnClickListener(this);
		exit.setOnClickListener(this);
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
			openActivity(SettingActivity.class, false);
			break;

		case R.id.rl_clean:
			BmobPro.getInstance(context).clearCache();
			showToast("缓存清理成功");
			cleansize.setText("0B");
			break;

		case R.id.tv_version:
			versionName = AppUtils.getVersionName(getActivity());
			versionCode = AppUtils.getVersionCode(getActivity());
			Log.i("AboutActivity", "版本名：" + versionName + "版本号：" + versionCode);
			break;

		case R.id.tv_exit:
			BmobUser.logOut(getActivity()); // 清除缓存用户对象
			SPUtils.clear(getActivity(), Constants.PASSWORD);
			BmobUser currentUser = BmobUser.getCurrentUser(getActivity()); // 现在的currentUser是null了
			Log.i("currentUser", currentUser + "");
			openActivity(LoginActivity.class, true);
			break;

		default:
			break;
		}
	}

}
