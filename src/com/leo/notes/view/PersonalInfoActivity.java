package com.leo.notes.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.image.HeaderImageView;
import scl.leo.library.utils.other.SPUtils;
import scl.leo.library.utils.other.StringUtil;
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
import com.leo.notes.view.base.BaseActivity;

public class PersonalInfoActivity extends BaseActivity {

	@ViewInject(id = R.id.personal_info)
	RelativeLayout info;

	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	int color;

	private static final String TAG = "PersonalInfoActivity";

	@ViewInject(id = R.id.img_info_add_photo)
	private HeaderImageView phone;

	private CircularProgressDialog loading;

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleBtnRigh, click = "edit")
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

	private String gender = "保密";
	private int age;
	private String nowday;
	private String birthday;
	private String objectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray, "COLOR"));
		info.setBackgroundColor(color);
		tvTitle.setText("个人信息");
		ivTitleLeft.setImageResource(R.drawable.icon_back);
		edit.setImageResource(R.drawable.edit);

		loading = CircularProgressDialog.show(context);
		loading.show();
		getPersonalInfo(objectId);
	}

	private void getPersonalInfo(String objectId) {
		User current_user = BmobUser.getCurrentUser(this, User.class);
		objectId = current_user.getObjectId();

		BmobQuery<User> query = new BmobQuery<User>();
		query.getObject(this, objectId, new GetListener<User>() {

			@Override
			public void onSuccess(User user) {
				loading.dismiss();
				age = user.getAge();
				birthday = user.getBirthday();
				gender = user.getGender();
				String name = user.getUsername();
				String email = user.getEmail();
				String mobile = user.getMobilePhoneNumber();

				if (!StringUtil.isEmpty(name)) {
					tvName.setText(name);
				}
				if (!StringUtil.isEmpty(email)) {
					tvEmail.setText(email);
				}
				if (0 == age) {
					tvAge.setText("" + age);
				}
				if (!StringUtil.isEmpty(mobile)) {
					tvMobile.setText(mobile);
				}
				if (!StringUtil.isEmpty(gender)) {
					tvGender.setText(gender);
				} else {
					tvGender.setText("未知");
				}
				if (!StringUtil.isEmpty(birthday)) {
					tvBirthday.setText(birthday);
				} else {
					tvGender.setText(nowday);
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
		openActivity(EditPersonalActivity.class, 1);
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
