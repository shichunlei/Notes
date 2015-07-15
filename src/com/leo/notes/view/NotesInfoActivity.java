package com.leo.notes.view;

import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.view.base.BaseActivity;

public class NotesInfoActivity extends BaseActivity {

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
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray, "COLOR"));
		notesinfo.setBackgroundColor(color);
		tvTitle.setText("彩虹记事");
		imgLeft.setImageResource(R.drawable.back);
		imgRight.setImageResource(R.drawable.edit);
	}

	public void back(View view) {
		finish();
	}

	public void edit(View view) {

	}
}
