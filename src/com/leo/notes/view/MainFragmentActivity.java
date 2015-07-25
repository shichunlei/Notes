package com.leo.notes.view;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.slidingmenu.SlidingMenu;
import scl.leo.library.utils.other.SPUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.leo.notes.R;
import com.leo.notes.adapter.GroupAdapter;
import com.leo.notes.been.Group;
import com.leo.notes.fragment.MenuFragment;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class MainFragmentActivity extends BaseActivity {

	private static final String TAG = "MainFragmentActivity";

	public static SlidingMenu mSlidingMenu;

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "menu")
	private ImageView imgLeft;
	@ViewInject(id = R.id.img_right, click = "menu")
	private ImageView imgRight;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.gridview, itemClick = "onItemClick")
	private GridView gridview;

	private GroupAdapter adapter;

	private CircularProgressDialog loading;

	private List<Group> groupList = new ArrayList<Group>();

	private int color;

	private String group_id;

	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_main);
		FinalActivity.initInjectedView(this);

		init();
		initSlidingmenu();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		tvTitle.setText("记事分类");

		if ((Boolean) SPUtils.get(context, "model", false,
				Constants.SETTING_DATA)) {
			imgRight.setImageResource(R.drawable.menu);
		} else {
			imgLeft.setImageResource(R.drawable.menu);
		}

		loading.show();
		getGroups();
	}

	private void getGroups() {
		BmobQuery<Group> query = new BmobQuery<Group>();
		query.findObjects(this, new FindListener<Group>() {
			@Override
			public void onSuccess(List<Group> object) {
				loading.dismiss();
				showToast("查询成功：共" + object.size() + "条数据。");
				if (object.size() > 0) {
					if (groupList != null) {
						groupList.clear();
					}

					groupList.addAll(object);
					object.clear();

					adapter = new GroupAdapter(context);
					adapter.replaceWith(groupList);
					gridview.setAdapter(adapter);
				}
			}

			@Override
			public void onError(int code, String msg) {
				loading.dismiss();
				showToast("查询失败");
				Log.i(TAG, msg);
			}
		});
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		group_id = groupList.get(position).getObjectId();
		openActivity(NotesListActivity.class, "group_id", group_id, false);
	}

	private void initSlidingmenu() {
		mSlidingMenu = new SlidingMenu(this);
		if ((Boolean) SPUtils.get(context, "model", false,
				Constants.SETTING_DATA)) {
			mSlidingMenu.setMode(SlidingMenu.RIGHT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
			mSlidingMenu.setShadowDrawable(R.drawable.right_shadow);// 设置左菜单阴影图片
		} else {
			mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
			mSlidingMenu.setShadowDrawable(R.drawable.left_shadow);// 设置左菜单阴影图片
		}
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置手势模式
		mSlidingMenu.setShadowWidth(30); // 阴影宽度
		mSlidingMenu.setBehindOffset(220);// 前面的视图剩下多少
		mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mSlidingMenu.setMenu(R.layout.menu_layout); // 设置menu容器

		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.menu_fragment, new MenuFragment())
				.commit();
	}

	public void menu(View v) {
		if (mSlidingMenu != null && mSlidingMenu.isMenuShowing()) {
			mSlidingMenu.showContent();
		} else {
			mSlidingMenu.toggle();
		}
	}

	// 按下菜单键时
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			mSlidingMenu.toggle();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mSlidingMenu != null && mSlidingMenu.isMenuShowing()) {
				mSlidingMenu.showContent();
			} else {
				// 判断两次点击的时间间隔（默认设置为2秒）
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					showToast(getString(R.string._exit));
					mExitTime = System.currentTimeMillis();
				} else {
					System.exit(0);
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
