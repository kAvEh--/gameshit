package com.eynak.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eynak.footballquize.QuestionActivity;
import com.eynak.footballquize.R;

public class GridLevelAdapter extends BaseAdapter {
	private Activity mActivity;
	ArrayList<HashMap<String, Integer>> data;
	private int _packageId;

	// Constructor
	public GridLevelAdapter(Activity c, ArrayList<HashMap<String, Integer>> d,
			int packageId) {
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
		if (data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
				.getResources().getInteger(R.integer.STATE_CORRECT)) {
		} else {
		}
		switch (data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_TYPE))) {
		case 1:
			if (data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
					.getResources().getInteger(R.integer.STATE_CORRECT)) {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_logo_on);
			} else {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_logo_off);
			}
			break;

		case 2:
			if (data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
					.getResources().getInteger(R.integer.STATE_CORRECT)) {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_shirt_on);
			} else {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_shirt_off);
			}
			break;

		case 3:
			if (data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
					.getResources().getInteger(R.integer.STATE_CORRECT)) {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_manager_on);
			} else {
				viewHolder.levelIcon
						.setImageResource(R.drawable.ic_manager_off);
			}
			break;

		case 5:
			if (data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
					.getResources().getInteger(R.integer.STATE_CORRECT)) {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_player_on);
			} else {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_player_off);
			}
			break;

		default:
			if (data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE)) == mActivity
					.getResources().getInteger(R.integer.STATE_CORRECT)) {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_q_on);
			} else {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_q_off);
			}
			break;
		}

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