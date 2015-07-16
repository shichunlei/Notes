package com.leo.notes.view;

import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.adapter.NotesAdapter;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

/**
 * @title 主页
 * 
 * @content ListView展示记事，可添加分类、编辑、删除、查询记事
 * 
 * @author shichunlei
 *
 */
public class NotesListActivity extends BaseActivity {

	@ViewInject(id = R.id.notes_list)
	private LinearLayout noteslist;

	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "add")
	private ImageView imgLeft;
	@ViewInject(id = R.id.ivTitleBtnRigh, click = "search")
	private ImageView imgRight;

	@ViewInject(id = R.id.listview, itemClick = "itemClick")
	private ListView listview;

	int color;

	private NotesAdapter adapter;

	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_list);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		noteslist.setBackgroundColor(color);
		tvTitle.setText("彩虹记事");
		imgLeft.setImageResource(R.drawable.add);
		imgRight.setImageResource(R.drawable.search);
	}

	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	public void add(View view) {
		openActivity(NotesAddAndEditActivity.class, "tag", "add", false);
	}

	public void search(View view) {

	}

	// 按下菜单键时
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 判断两次点击的时间间隔（默认设置为2秒）
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				showToast("再按一次退出程序");
				mExitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
