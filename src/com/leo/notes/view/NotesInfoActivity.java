package com.leo.notes.view;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

import com.leo.notes.R;
import com.leo.notes.been.Notes;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class NotesInfoActivity extends BaseActivity {

	private static final String TAG = "NotesInfoActivity";

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.img_right, click = "edit")
	private ImageView imgRight;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.info_title)
	private TextView tvName;
	@ViewInject(id = R.id.info_time)
	private TextView tvTime;
	@ViewInject(id = R.id.info_content)
	private TextView tvContent;

	private CircularProgressDialog loading;

	int color;

	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_notes_info);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);

		id = getStringExtra("id");
		Log.i(TAG, id);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.app_name));
		imgLeft.setImageResource(R.drawable.icon_back);
		imgRight.setImageResource(R.drawable.edit);

		loading.show();
		getNotesInfo(id);
	}

	private void getNotesInfo(String id) {
		BmobQuery<Notes> query = new BmobQuery<Notes>();
		query.getObject(context, id, new GetListener<Notes>() {

			@Override
			public void onSuccess(Notes object) {
				loading.dismiss();
				setResult(RESULT_OK);
				showToast(getString(R.string.q_success));
				tvName.setText(object.getTitle());
				tvContent.setText(object.getContent());
				tvTime.setText(object.getCreatedAt());
			}

			@Override
			public void onFailure(int code, String arg0) {
				loading.dismiss();
				showToast(getString(R.string.q_fail) + arg0);
				Log.i(TAG, arg0);
			}
		});
	}

	public void back(View view) {
		finish();
	}

	public void edit(View view) {
		Bundle bundle = new Bundle();
		bundle.putString("tag", "edit");
		bundle.putString("id", id);
		openActivity(NotesAddAndEditActivity.class, bundle, Constants.INFO_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case Constants.INFO_EDIT:
			getNotesInfo(id);
			break;
		}
	}
}
