package com.glass.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glass.footballquize.QuestionActivity;
import com.glass.footballquize.R;

public class GridLevelAdapter extends BaseAdapter {
	private Activity mActivity;
	ArrayList<HashMap<String, Integer>> data;
	private int _packageId;

	// Constructor
	public GridLevelAdapter(Activity c, ArrayList<HashMap<String, Integer>> d, int packageId) {
		this.mActivity = c;
		this.data = d;
		this._packageId = packageId;
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
		RelativeLayout main_layout;
		TextView levelTitle;
		ImageView levelIcon;
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
			viewHolder.main_layout = (RelativeLayout) vi
					.findViewById(R.id.grid_level_main);
			viewHolder.levelIcon = (ImageView) vi
					.findViewById(R.id.grid_level_icon);
			viewHolder.main_layout.setOnClickListener(mClickListener);

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
		} else if (data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
				.getResources().getInteger(R.integer.STATE_FALSE)) {
			viewHolder.levelTitle.setBackgroundColor(Color.RED);
		}
		switch (data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_TYPE))) {
		case 1:
			viewHolder.levelIcon.setImageResource(R.drawable.ic_logo);
			break;
			
		case 2:
			viewHolder.levelIcon.setImageResource(R.drawable.ic_shirt);
			break;
			
		case 3:
			viewHolder.levelIcon.setImageResource(R.drawable.ic_manager);
			break;
			
		case 5:
			viewHolder.levelIcon.setImageResource(R.drawable.ic_player);
			break;

		default:
			viewHolder.levelIcon.setImageResource(R.drawable.ic_question);
			break;
		}

		viewHolder.levelTitle.setTag(position);

		viewHolder.main_layout.setTag(position);

		return vi;
	}

	private View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int tag = (Integer) v.getTag();
			Intent i = new Intent(mActivity, QuestionActivity.class);
			i.putExtra(
					mActivity.getResources().getString(R.string.KEY_ID),
					data.get(tag)
							.get(mActivity.getResources().getString(
									R.string.KEY_ID)));
			i.putExtra(
					mActivity.getResources().getString(R.string.KEY_PACKAGEID),
					_packageId);
			i.putExtra("position", tag);
			i.putExtra("test", data);
			mActivity.startActivity(i);
		}
	};

}