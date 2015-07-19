package com.leo.notes.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.image.HeaderImageView;
import scl.leo.library.utils.other.SPUtils;
import scl.leo.library.utils.other.StringUtil;
import scl.leo.library.utils.other.TimeUtils;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;

import com.leo.notes.R;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class PersonalInfoActivity extends BaseActivity {

	@ViewInject(id = R.id.personal_info)
	RelativeLayout info;

	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	int color;

	private static final String TAG = "PersonalInfoActivity";

	@ViewInject(id = R.id.img_info_add_photo)
	private HeaderImageView phone;

	private CircularProgressDialog loading;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.img_right, click = "edit")
	private ImageView edit;

	@ViewInject(id = R.id.tv_info_name)
	private TextView tvName;
	@ViewInject(id = R.id.tv_info_email)
	private TextView tvEmail;
	@ViewInject(id = R.id.tv_info_mobile)
	private TextView tvMobile;
	@ViewInject(id = R.id.tv_info_birthday)
	private TextView tvBirthday;
	@ViewInject(id = R.id.tv_info_gender)
	private TextView tvGender;
	@ViewInject(id = R.id.tv_info_age)
	private TextView tvAge;

	private String nowday;
	private String objectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		User current_user = BmobUser.getCurrentUser(context, User.class);
		objectId = current_user.getObjectId();

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		info.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.personal_info));
		ivTitleLeft.setImageResource(R.drawable.icon_back);
		edit.setImageResource(R.drawable.edit);

		nowday = TimeUtils.nowDate();

		loading = CircularProgressDialog.show(context);
		loading.show();
		getPersonalInfo(objectId);
	}

	/**
	 * 得到当前登录用户信息
	 * 
	 * @param objectId
	 */
	private void getPersonalInfo(String objectId) {
		BmobQuery<User> query = new BmobQuery<User>();
		query.getObject(this, objectId, new GetListener<User>() {

			@Override
			public void onSuccess(User user) {
				loading.dismiss();
				setResult(RESULT_OK);

				tvName.setText("用  户 名：" + user.getUsername());
				tvEmail.setText("邮      箱：" + user.getEmail());
				tvAge.setText("年      龄：" + user.getAge());
				tvMobile.setText("手机号码：" + user.getMobilePhoneNumber());

				if (!StringUtil.isEmpty(user.getGender())) {
					tvGender.setText("性      别：" + user.getGender());
				} else {
					tvGender.setText("性      别：" + getString(R.string.privacy));
				}
				if (!StringUtil.isEmpty(user.getBirthday())) {
					tvBirthday.setText("生      日：" + user.getBirthday());
				} else {
					tvBirthday.setText("生      日：" + nowday);
				}
			}

			@Override
			public void onFailure(int code, String msg) {
				loading.dismiss();
				Log.i(TAG, msg);
			}
		});
	}

	public void edit(View v) {
		openActivity(PersonalEditActivity.class, Constants.PERSONAL_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case Constants.PERSONAL_EDIT:
			loading.show();
			getPersonalInfo(objectId);
			break;
		}
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}
}
