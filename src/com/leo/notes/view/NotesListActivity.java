package com.leo.notes.view;

import java.util.ArrayList;
import java.util.List;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.leo.notes.R;
import com.leo.notes.adapter.NotesAdapter;
import com.leo.notes.been.Notes;
import com.leo.notes.been.User;
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

	private static final String TAG = "NotesListActivity";

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

	private CircularProgressDialog loading;

	private NotesAdapter adapter;

	private List<Notes> notesList = new ArrayList<Notes>();

	private long mExitTime;
	private String author;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_list);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		noteslist.setBackgroundColor(color);
		tvTitle.setText("记事");
		imgLeft.setImageResource(R.drawable.add);
		imgRight.setImageResource(R.drawable.search);

		User current_user = BmobUser.getCurrentUser(context, User.class);
		author = current_user.getObjectId();

		loading.show();
		getList(author);
	}

	private void getList(String author) {
		BmobQuery<Notes> query = new BmobQuery<Notes>();
		query.addWhereEqualTo("author", author);
		// query.addWhereEqualTo("group", "ghvKCCCm");
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// 希望同时查询该Notes的发布者的信息，以及该Notes的分组信息group，这里用到上面`include`的并列对象查询和内嵌对象的查询
		query.include("group,author");
		// 执行查询方法
		query.findObjects(this, new FindListener<Notes>() {
			@Override
			public void onSuccess(List<Notes> object) {
				loading.dismiss();
				showToast("查询成功：共" + object.size() + "条数据。");
				Log.i(TAG, object.toString());

				if (object.size() > 0) {
					if (notesList != null) {
						notesList.clear();
					}

					notesList.addAll(object);
					object.clear();

					adapter = new NotesAdapter(context);
					adapter.replaceWith(notesList);
					listview.setAdapter(adapter);
				}
			}

			@Override
			public void onError(int code, String msg) {
				loading.dismiss();
				showToast("查询失败：" + msg);
			}
		});
	}

	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String notes_id = notesList.get(position).getObjectId();
		openActivity(NotesInfoActivity.class, "id", notes_id, false);
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
