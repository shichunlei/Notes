package com.leo.notes.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.leo.notes.R;
import com.leo.notes.been.Notes;
import com.leo.notes.util.Constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotesAdapter extends BaseAdapter {

	List<Notes> notes = new ArrayList<Notes>();
	private LayoutInflater inflater;

	public NotesAdapter(Context context) {
		this.notes = new ArrayList<Notes>();
		this.inflater = LayoutInflater.from(context);
	}

	public void replaceWith(Collection<Notes> newNotes) {
		this.notes.clear();
		this.notes.addAll(newNotes);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return notes.size();
	}

	@Override
	public Notes getItem(int position) {
		return notes.get(position);
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

	private void bind(Notes item, View convertView) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.llBG.setBackgroundResource(Constants.bgcolors[item.getColor()]);
		holder.content.setText(item.getContent());
		holder.title.setText(item.getTitle());
		holder.time.setText(item.getCreatedAt());
	}

	private View inflateIfRequired(View view, int position, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.item_list_notes, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}

	class ViewHolder {

		private TextView content;
		private TextView title;
		private TextView time;
		private LinearLayout llBG;

		public ViewHolder(View view) {
			llBG = (LinearLayout) view.findViewById(R.id.ll_text);
			content = (TextView) view.findViewById(R.id.notes_content);
			title = (TextView) view.findViewById(R.id.notes_title);
			time = (TextView) view.findViewById(R.id.notes_time);
		}
	}

}
