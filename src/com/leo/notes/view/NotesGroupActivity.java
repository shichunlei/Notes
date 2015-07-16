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

/**
 * @title 我的分类
 * 
 * @content gridView展示分类，可添加分类、编辑、删除分类
 * 
 * @author shichunlei
 *
 */
public class NotesGroupActivity extends BaseActivity {

	@ViewInject(id = R.id.notesgroup)
	private LinearLayout notesgroup;

	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "add")
	private ImageView imgLeft;
	@ViewInject(id = R.id.ivTitleBtnRigh, click = "search")
	private ImageView imgRight;

	int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_group);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		notesgroup.setBackgroundColor(color);
		tvTitle.setText("我的分类");
		imgLeft.setImageResource(R.drawable.add);
		imgRight.setImageResource(R.drawable.search);
	}

	public void add(View view) {
		openActivity(NotesAddAndEditActivity.class, "tag", "add", false);
	}

	public void search(View view) {

	}

}
