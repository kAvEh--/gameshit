package com.eynak.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eynak.footballquize.R;
import com.eynak.utils.PersianReshape;

public class LeadersAdapter extends BaseAdapter {

	private ArrayList<String[]> data;
	public LayoutInflater inflater;
	private Context context;
	int _userId;

	public LeadersAdapter(ArrayList<String[]> leadersData, Context context,
			int id) {
		this.data = leadersData;
		this.context = context;
		this._userId = id;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder {
		TextView name;
		TextView score;
		TextView id;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.leader_board_row, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.leader_name);
			viewHolder.id = (TextView) convertView.findViewById(R.id.leader_id);
			viewHolder.score = (TextView) convertView
					.findViewById(R.id.leader_score);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Typeface face = Typeface.createFromAsset(context.getAssets(), "font/"
				+ context.getResources().getString(R.string.font) + "");

		if (Integer.parseInt(data.get(position)[0]) == _userId) {
			viewHolder.name.setTextColor(context.getResources().getColor(
					R.color.ajori));
			viewHolder.score.setTextColor(context.getResources().getColor(
					R.color.ajori));
		} else {
			viewHolder.name.setTextColor(context.getResources().getColor(
					R.color.white));
			viewHolder.score.setTextColor(context.getResources().getColor(
					R.color.white));
		}

		viewHolder.name.setTypeface(face);
		viewHolder.id.setTypeface(face);
		if (data.get(position)[1] == "null")
			viewHolder.name.setText("-");
		else {
			viewHolder.name.setText(data.get(position)[1]);
			viewHolder.id.setText("-" + (position + 1));
		}

		viewHolder.score.setTypeface(face);
		viewHolder.score.setText(PersianReshape.reshape(data.get(position)[2]));
		return convertView;
	}

}
