package com.leo.notes.fragment;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.leo.notes.R;
import com.leo.notes.view.base.BaseFragment;

public class MenuFragment extends BaseFragment implements OnClickListener {

	private View view;

	@ViewInject(id = R.id.tvMuseum)
	private TextView museun;
	@ViewInject(id = R.id.tvHall1)
	private TextView hall1;
	@ViewInject(id = R.id.tvHall2)
	private TextView hall2;
	@ViewInject(id = R.id.tvHall3)
	private TextView hall3;
	@ViewInject(id = R.id.tvHall4)
	private TextView hall4;
	@ViewInject(id = R.id.tvHall5)
	private TextView hall5;
	@ViewInject(id = R.id.tvHall6)
	private TextView hall6;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_menu, container, false);
		FinalActivity.initInjectedView(this, view);
		init();

		return view;
	}

	private void init() {
		museun.setOnClickListener(this);
		hall1.setOnClickListener(this);
		hall2.setOnClickListener(this);
		hall3.setOnClickListener(this);
		hall4.setOnClickListener(this);
		hall5.setOnClickListener(this);
		hall6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tvMuseum:

			break;

		case R.id.tvHall1:

			break;

		case R.id.tvHall2:

			break;

		case R.id.tvHall3:

			break;

		case R.id.tvHall4:

			break;

		case R.id.tvHall5:

			break;

		case R.id.tvHall6:

			break;

		default:
			break;
		}
	}

}
