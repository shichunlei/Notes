package com.leo.notes.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.slidingmenu.SlidingMenu;
import scl.leo.library.utils.other.SPUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.fragment.MenuFragment;
import com.leo.notes.util.Constants;
import com.leo.notes.view.base.BaseActivity;

public class MainFragmentActivity extends BaseActivity {

	private static final String TAG = "MainFragmentActivity";

	public static SlidingMenu mSlidingMenu;

	@ViewInject(id = R.id.main)
	private LinearLayout main;

	@ViewInject(id = R.id.img_left, click = "menu")
	private ImageView imgLeft;
	@ViewInject(id = R.id.img_right, click = "menu")
	private ImageView imgRight;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(id = R.id.notes, click = "notes")
	private Button notes;

	int color;

	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FinalActivity.initInjectedView(this);

		init();
		initSlidingmenu();
	}

	private void init() {
		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		main.setBackgroundColor(color);
		tvTitle.setText("主页");

		if ((Boolean) SPUtils.get(context, "model", false,
				Constants.SETTING_DATA)) {
			imgRight.setImageResource(R.drawable.menu);
		} else {
			imgLeft.setImageResource(R.drawable.menu);
		}
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

	public void notes(View v) {
		openActivity(NotesListActivity.class, false);
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
