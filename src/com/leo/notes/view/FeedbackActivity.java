package com.leo.notes.view;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.SPUtils;
import scl.leo.library.utils.other.StringUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.leo.notes.R;
import com.leo.notes.been.FeedBack;
import com.leo.notes.been.User;
import com.leo.notes.util.Constants;
import com.leo.notes.util.ThemeUtil;
import com.leo.notes.view.base.BaseActivity;

public class FeedbackActivity extends BaseActivity {

	private static final String TAG = "FeedbackActivity";

	@ViewInject(id = R.id.title_bar)
	private RelativeLayout title_bar;

	@ViewInject(id = R.id.img_left, click = "back")
	private ImageView imgLeft;
	@ViewInject(id = R.id.tv_title)
	private TextView tvTitle;

	/** 反馈内容 */
	@ViewInject(id = R.id.reply_content)
	private EditText tvcontent;
	/** 联系方式 */
	@ViewInject(id = R.id.reply_contacts)
	private EditText tvcontacts;
	@ViewInject(id = R.id.confirm_button, click = "submit")
	private Button btnSubmit;

	private CircularProgressDialog loading;

	int color;

	User current_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeUtil.setTheme(context);
		setContentView(R.layout.activity_feedback);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);

		current_user = BmobUser.getCurrentUser(context, User.class);

		color = getResources().getColor(
				(Integer) SPUtils.get(context, "color", R.color.gray,
						Constants.COLOR));
		title_bar.setBackgroundColor(color);
		tvTitle.setText(getString(R.string.feedback));
		imgLeft.setImageResource(R.drawable.icon_back);

		btnSubmit.setEnabled(false);

		tvcontent.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				btnSubmit.setEnabled(!TextUtils.isEmpty(s.toString()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}

	public void submit(View v) {
		String contacts = tvcontacts.getText().toString().trim();
		String content = tvcontent.getText().toString().trim();
		if (StringUtil.isEmpty(content)) {
			showToast("请输入反馈内容");
		} else {
			loading.show();
			FeedBack feedback = new FeedBack();
			feedback.setContacts(contacts);
			feedback.setContent(content);
			feedback.setUser(current_user);

			feedback.save(context, new SaveListener() {

				@Override
				public void onSuccess() {
					loading.dismiss();
					showToast(getString(R.string.f_success));
					finish();
				}

				@Override
				public void onFailure(int code, String msg) {
					loading.dismiss();
					showToast(getString(R.string.f_fail));
					Log.i(TAG, msg);
				}
			});
		}
	}

	public void back(View v) {
		finish();
	}
}