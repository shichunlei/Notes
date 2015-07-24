package com.leo.notes.util;

import com.leo.notes.R;

import android.content.Context;
import scl.leo.library.utils.other.SPUtils;

public class ThemeUtil {

	public static void setTheme(Context context) {
		Boolean value = (Boolean) SPUtils.get(context, "theme", false,
				Constants.SETTING_DATA);

		if (value) {
			context.setTheme(R.style.Black_Theme);
		} else {
			context.setTheme(R.style.Light_Theme);
		}
	}

}
