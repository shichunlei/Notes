package com.leo.notes.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.leo.notes.R;
import com.leo.notes.been.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {

	List<Group> group = new ArrayList<Group>();
	private LayoutInflater inflater;

	public GroupAdapter(Context context) {
		this.group = new ArrayList<Group>();
		this.inflater = LayoutInflater.from(context);
	}

	public void replaceWith(Collection<Group> newGroup) {
		this.group.clear();
		this.group.addAll(newGroup);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return group.size();
	}

	@Override
	public Group getItem(int position) {
		return group.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflateIfRequired(convertView, position, parent);
		bind(getItem(position), convertView);
		return convertView;
	}

	private void bind(Group item, View convertView) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.title.setText(item.getName());
		if (item.getName().equals("默认")) {
			holder.image.setBackgroundResource(R.drawable.bg_content_2);
		} else if (item.getName().equals("生活")) {
			holder.image.setBackgroundResource(R.drawable.bg_content_3);
		} else if (item.getName().equals("学习")) {
			holder.image.setBackgroundResource(R.drawable.bg_content_4);
		} else if (item.getName().equals("工作")) {
			holder.image.setBackgroundResource(R.drawable.bg_content_5);
		}
	}

	private View inflateIfRequired(View view, int position, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.item_grid_group, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}

	class ViewHolder {

		private TextView title;
		private ImageView image;

		public ViewHolder(View view) {
			title = (TextView) view.findViewById(R.id.tv_group_name);
			image = (ImageView) view.findViewById(R.id.img_group);
		}
	}
}
