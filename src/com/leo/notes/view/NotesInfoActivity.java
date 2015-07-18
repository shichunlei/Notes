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
				showToast(getString(R.string.q_success));
				tvName.setText(object.getTitle());
				tvContent.setText(object.getContent());
				tvTime.setText(object.getCreatedAt());
			}

			@Override
			public void onFailure(int code, String arg0) {
				showToast(getString(R.string.q_fail) + arg0);
			}

		});
	}

	public void back(View view) {
		finish();
	}

	public void edit(View view) {
		openActivity(NotesAddAndEditActivity.class, "tag", "edit", 1);
	}
}
