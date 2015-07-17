package com.leo.notes.view;

import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

import com.leo.notes.R;
import com.leo.notes.been.Notes;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class NotesInfoActivity extends BaseActivity {

	private static final String TAG = "NotesInfoActivity";

	@ViewInject(id = R.id.notesinfo)
	private LinearLayout notesinfo;

	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.ivTitleBtnRigh, click = "edit")
	private ImageView imgRight;

	int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_info);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		String id = getStringExtra("id");
		Log.i(TAG, id);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		notesinfo.setBackgroundColor(color);
		tvTitle.setText("彩虹记事");
		imgLeft.setImageResource(R.drawable.back);
		imgRight.setImageResource(R.drawable.edit);

		getNotesInfo(id);
	}

	private void getNotesInfo(String id) {
		BmobQuery<Notes> query = new BmobQuery<Notes>();
		query.getObject(context, id, new GetListener<Notes>() {

			@Override
			public void onSuccess(Notes object) {
				showToast("查询成功：");

			}

			@Override
			public void onFailure(int code, String arg0) {
				showToast("查询失败：" + arg0);
			}

		});
	}

	public void back(View view) {
		finish();
	}

	public void edit(View view) {
		openActivity(NotesAddAndEditActivity.class, false);
	}
}
