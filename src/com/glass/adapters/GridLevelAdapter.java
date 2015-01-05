package com.glass.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.glass.footballquize.R;

public class GridLevelAdapter extends BaseAdapter {
	private Activity mActivity;
	ArrayList<HashMap<String, Integer>> data;

	// Constructor
	public GridLevelAdapter(Activity c, ArrayList<HashMap<String, Integer>> d) {
		this.mActivity = c;
		this.data = d;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder {
		TextView levelTitle;
	}

	// create a new ImageView for each item referenced by the Adapter
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = mActivity.getLayoutInflater();
			vi = inflater.inflate(R.layout.level_grid_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.levelTitle = (TextView) vi
					.findViewById(R.id.grid_level_title);

			// viewHolder.levelTitle.setOnClickListener(moreClickListener);
			vi.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) vi.getTag();
		}
		viewHolder.levelTitle.setText("Stage " + (position + 1));
		if (data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
				.getResources().getInteger(R.integer.STATE_CORRECT)) {
			viewHolder.levelTitle.setBackgroundColor(Color.GREEN);
		} else if(data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
				.getResources().getInteger(R.integer.STATE_FALSE)) {
			viewHolder.levelTitle.setBackgroundColor(Color.RED);
		}

		viewHolder.levelTitle.setTag(position);

		return vi;
	}

}