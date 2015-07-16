package com.leo.notes.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.utils.other.SPUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class NotesAddAndEditActivity extends BaseActivity {

	@ViewInject(id = R.id.notesedit)
	private LinearLayout notesedit;

	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.ivTitleBtnRigh, click = "save")
	private ImageView imgRight;

	int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_edit);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		if (getStringExtra("tag").equals("add")) {
			tvTitle.setText("彩虹记事");
		} else if (getStringExtra("tag").equals("edit")) {
			tvTitle.setText("title");
		}
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		notesedit.setBackgroundColor(color);

		imgLeft.setImageResource(R.drawable.delete);
		imgRight.setImageResource(R.drawable.save);
	}

	public void back(View view) {
		finish();
	}

	public void save(View view) {

	}
}
