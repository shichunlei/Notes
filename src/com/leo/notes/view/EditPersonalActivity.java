package com.leo.notes.view;

import java.io.File;
import java.io.FileOutputStream;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.dialog.ActionSheetDialog;
import scl.leo.library.dialog.ActionSheetDialog.OnSheetItemClickListener;
import scl.leo.library.dialog.ActionSheetDialog.SheetItemColor;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.image.HeaderImageView;
import scl.leo.library.utils.other.SPUtils;
import scl.leo.library.utils.other.StringUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.leo.notes.R;
import com.leo.notes.been.User;
import com.leo.notes.util.*;
import com.leo.notes.view.base.BaseActivity;

public class EditPersonalActivity extends BaseActivity {

	@ViewInject(id = R.id.personal_edit)
	RelativeLayout edit;

	int color;

	private static final String TAG = "EditPersonalActivity";

	private static final int ALBUM = 1;
	private static final int CAMERA = 2;
	private static final int ZOOM = 3;

	@ViewInject(id = R.id.img_edit_add_photo, click = "addPhone")
	private HeaderImageView phone;

	private CircularProgressDialog loading;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.img_right, click = "save")
	private ImageView save;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.et_edit_name)
	private EditText etName;
	@ViewInject(id = R.id.et_edit_email)
	private EditText etEmail;
	@ViewInject(id = R.id.et_edit_mobile)
	private EditText etMobile;
	@ViewInject(id = R.id.tv_edit_birthday, click = "selectBirthday")
	private TextView tvBirthday;
	@ViewInject(id = R.id.tv_edit_gender, click = "selectGender")
	private TextView tvGender;

	private String gender = getString(R.string.privacy);
	private int age;
	private String nowday;
	private String birthday;
	private String objectId;

	/** 是否选择头像 */
	private Boolean is_addpicture = false;

	private PopupWindow poppWindow;
	private View popview;
	/** 相册 */
	private TextView tvAlbum;
	/** 拍照 */
	private TextView tvCamera;
	/** 取消 */
	private TextView tvCacel;

	/** 封面图片名称（包括全路径） */
	private String coverName = "";
	/** 调用相机拍摄照片的名字(临时) */
	private String takePicturePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_edit);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		User current_user = BmobUser.getCurrentUser(this, User.class);
		objectId = current_user.getObjectId();

		getPersonalInfo(objectId);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray, "COLOR"));
		edit.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.edit_personal_info));
		ivTitleLeft.setImageResource(R.drawable.icon_back);
		save.setImageResource(R.drawable.save);

		loading = CircularProgressDialog.show(context);

		popview = LayoutInflater.from(context).inflate(R.layout.pop_add_phone,
				null);

		tvCacel = (TextView) popview.findViewById(R.id.app_cancle);
		tvAlbum = (TextView) popview.findViewById(R.id.app_album);
		tvCamera = (TextView) popview.findViewById(R.id.app_camera);

		poppWindow = new PopupWindow(popview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		/**
		 * 相册选择
		 */
		tvAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				takePicturePath = getIntent().getStringExtra("data");
				startActivityForResult(intent, ALBUM);
				poppWindow.dismiss();
			}
		});

		/**
		 * 拍照
		 */
		tvCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				takePicturePath = Constants.TEMPORARY_IMAGE_NAME;
				Log.i(TAG, "takePicturePath = " + takePicturePath);
				File image = new File(takePicturePath);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
				startActivityForResult(intent, CAMERA);
				poppWindow.dismiss();
			}
		});

		/**
		 * 取消
		 */
		tvCacel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				poppWindow.dismiss();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				// 如果是直接从相册获取
				case ALBUM:
					startPhotoZoom(data.getData());
					break;
				// 如果是调用相机拍照
				case CAMERA:
					File temp = new File(takePicturePath);
					startPhotoZoom(Uri.fromFile(temp));
					break;
				// 取得裁剪后的图片
				case ZOOM:
					if (data != null) {
						setPicToView(data);
					}
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 360);
		intent.putExtra("outputY", 360);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			FileOutputStream b = null;
			coverName = Constants.TEMP_IMAGE_NAME;
			try {
				b = new FileOutputStream(coverName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				is_addpicture = true;
				phone.setImageBitmap(bitmap);
			}
		}
	}

	private void getPersonalInfo(String objectId) {

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
					etName.setText(name);
				}
				if (!StringUtil.isEmpty(email)) {
					etEmail.setText(email);
				}
				if (!StringUtil.isEmpty(mobile)) {
					etMobile.setText(mobile);
				}
				if (!StringUtil.isEmpty(gender)) {
					tvGender.setText(gender);
				} else {
					tvGender.setText(getString(R.string.privacy));
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

	/**
	 * 验证信息
	 * 
	 * @param v
	 */
	public void save(View v) {
		String name = etName.getText().toString().trim();
		String email = etEmail.getText().toString().trim();
		String mobile = etMobile.getText().toString().trim();

		age = 26;

		loading.show();
		postSave(name, email, mobile, gender, birthday, age);
	}

	/**
	 * 提交个人信息
	 * 
	 * @param name
	 * @param email
	 * @param pwd
	 * @param mobile
	 * @param gender
	 * @param age
	 * @param birthday
	 */
	private void postSave(String name, String email, String mobile,
			String gender, String birthday, int age) {

		User user = new User();
		user.setUsername(name);
		user.setEmail(email);
		if (!StringUtil.isEmpty(email)) {
			user.setEmailVerified(true);
		}
		user.setMobilePhoneNumber(mobile);
		if (!StringUtil.isEmpty(mobile)) {
			user.setMobilePhoneNumberVerified(true);
		}
		user.setGender(gender);
		user.setAge(age);
		user.setBirthday(birthday);
		user.update(this, objectId, new UpdateListener() {

			@Override
			public void onSuccess() {
				loading.dismiss();
				finish();
				showToast(getString(R.string.u_success));
			}

			@Override
			public void onFailure(int code, String msg) {
				Log.i(TAG, msg);
				showToast(getString(R.string.u_fail));
				loading.dismiss();
			}
		});
	}

	/**
	 * 添加头像
	 * 
	 * @param v
	 */
	public void addPhone(View v) {
		poppWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#b0000000")));
		poppWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		poppWindow.setAnimationStyle(R.style.app_pop);
		poppWindow.setOutsideTouchable(true);
		poppWindow.setFocusable(true);
		poppWindow.update();
	}

	/**
	 * 选择性别
	 * 
	 * @param v
	 */
	public void selectGender(View v) {
		new ActionSheetDialog(context)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(true)
				.addSheetItem(getString(R.string.man), SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								gender = getString(R.string.man);
								tvGender.setText(getString(R.string.man));
							}
						})
				.addSheetItem(getString(R.string.women), SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								gender = getString(R.string.women);
								tvGender.setText(getString(R.string.women));
							}
						}).show();
	}

	/**
	 * 选择生日
	 * 
	 * @param v
	 */
	public void selectBirthday(View v) {

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