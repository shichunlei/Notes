package com.leo.notes.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.dialog.AlertDialog;
import scl.leo.library.utils.other.SPUtils;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class NotesAddAndEditActivity extends BaseActivity {

	@ViewInject(id = R.id.notesedit)
	private LinearLayout notesedit;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.img_right, click = "save")
	private ImageView imgRight;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

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
			tvTitle.setText(R.string.add_notes);
		} else if (getStringExtra("tag").equals("edit")) {
			tvTitle.setText(R.string.edit_notes);
		}
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		notesedit.setBackgroundColor(color);

		imgLeft.setImageResource(R.drawable.delete);
		imgRight.setImageResource(R.drawable.save);
	}

	public void back(View view) {
		showDialog();
	}

	private void showDialog() {
		new AlertDialog(context)
				.builder()
				.setTitle(getString(R.string.alert_hint))
				.setMsg(getString(R.string.exit))
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								finish();
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {

							}
						}).show();
	}

	public void save(View view) {

	}

	// 按下菜单键时
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
