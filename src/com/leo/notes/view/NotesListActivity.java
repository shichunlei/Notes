package com.leo.notes.view;

import java.util.ArrayList;
import java.util.List;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.leo.notes.R;
import com.leo.notes.adapter.NotesAdapter;
import com.leo.notes.been.Notes;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
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

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.img_right, click = "search")
	private ImageView imgRight;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.img_add, click = "add")
	private ImageView imgAdd;

	@ViewInject(id = R.id.listview, itemClick = "itemClick")
	private ListView listview;

	private int color;
	private int bgcolor;

	private CircularProgressDialog loading;

	private NotesAdapter adapter;

	private List<Notes> notesList = new ArrayList<Notes>();

	private String author;
	private String group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_notes_list);

		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		tvTitle.setText(R.string.app_name);
		imgLeft.setImageResource(R.drawable.back);
		imgRight.setImageResource(R.drawable.search);

		User current_user = BmobUser.getCurrentUser(context, User.class);
		author = current_user.getObjectId();
		group = "KWRY555D";

		loading.show();
		getList(author, group);
	}

	/**
	 * 得到notes列表
	 * 
	 * @param author
	 * @param group
	 */
	private void getList(String author, String group) {
		BmobQuery<Notes> query = new BmobQuery<Notes>();
		query.addWhereEqualTo("author", author);
		query.addWhereEqualTo("group", group);
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		query.order("-createdAt");
		// 希望同时查询该Notes的发布者的信息，以及该Notes的分组信息group，这里用到上面`include`的并列对象查询和内嵌对象的查询
		query.include("group,author");
		// 执行查询方法
		query.findObjects(this, new FindListener<Notes>() {
			@Override
			public void onSuccess(List<Notes> object) {
				loading.dismiss();
				showToast("共" + object.size() + "条数据。");
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
		bgcolor = notesList.get(position).getColor();
		Bundle bundle = new Bundle();
		bundle.putString("id", notes_id);
		bundle.putInt("bgcolor", bgcolor);
		openActivity(NotesInfoActivity.class, bundle, Constants.LIST_INFO);
	}

	public void add(View view) {
		bgcolor = 7;
		Bundle bundle = new Bundle();
		bundle.putString("tag", "add");
		bundle.putInt("bgcolor", bgcolor);
		openActivity(NotesAddAndEditActivity.class, bundle, Constants.LIST_ADD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case Constants.LIST_ADD:
			loading.show();
			getList(author, group);
			break;
		case Constants.LIST_INFO:
			loading.show();
			getList(author, group);
			break;
		}
	}

	public void back(View view) {
		finish();
	}
}
