package com.leo.notes.util;

import com.leo.notes.R;

import scl.leo.library.utils.other.TimeUtils;
import android.os.Environment;

public class Constants {

	/** Bomb APIKEY */
	public static String APPLICATION_ID = "ac40dc2b1d73ba5f2957eb43f095d3b9";

	/** SDcard根目录 */
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/** 根文件夹 */
	public static final String SCLUI_FOLDER_NAME = "/NOTES";
	/** 根文件夹路径 */
	public static final String SCLUI_FOLDER_PATH = SDCARD_PATH + "/NOTES/";

	/** 下载的图片的文件夹 */
	public static final String DOWNLOAD_FOLDER_NAME = "/download/";
	/** 下载的图片的文件夹路径 */
	public static final String DOWNLOAD_FOLDER_PATH = SCLUI_FOLDER_PATH
			+ DOWNLOAD_FOLDER_NAME;

	/** 拍照临时图片名称 */
	public static final String TEMPORARY_IMAGE_NAME = SCLUI_FOLDER_PATH
			+ "/temp.jpg";

	/** 裁剪后保存的图片的文件名称 */
	public static final String SAVE_IMAGE_NAME = "scl_" + TimeUtils.nowTime()
			+ ".jpg";

	/** 下载的图片的储存路径 */
	public static final String DOWNLOAD_IMAGE_NAME = DOWNLOAD_FOLDER_PATH
			+ "head_pic.jpg";

	/** 下载的个人头像的储存路径 */
	public static final String TEMP_IMAGE_NAME = DOWNLOAD_FOLDER_PATH
			+ "temp.jpg";

	/** 数字密码 */
	public static final String PASSWORD = "scl_login_password";
	/** 颜色值 */
	public static final String COLOR = "scl_color";
	public static final String SETTING_DATA = "scl_setting_data";

	public static final int LIST_ADD = 20001;
	public static final int INFO_EDIT = 20002;
	public static final int LIST_INFO = 20003;

	public static final int ALBUM = 10001;
	public static final int CAMERA = 10002;
	public static final int ZOOM = 10003;

	public static final int PERSONAL_INFO = 30001;
	public static final int PERSONAL_EDIT = 30002;

	public static final int[] colors = { R.color.purple, R.color.red,
			R.color.orange, R.color.yellow, R.color.green, R.color.blue,
			R.color.cyan };

	public static final int[] bgcolors = { R.drawable.bg_content_1,
			R.drawable.bg_content_2, R.drawable.bg_content_3,
			R.drawable.bg_content_4, R.drawable.bg_content_5,
			R.drawable.bg_content_6, R.drawable.bg_content_7,
			R.drawable.bg_content_8 };
}
