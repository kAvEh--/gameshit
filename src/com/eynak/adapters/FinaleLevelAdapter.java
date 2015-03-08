package com.eynak.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eynak.footballquize.FinaleLevelActivity;
import com.eynak.footballquize.R;

public class FinaleLevelAdapter extends BaseAdapter {

	private Activity mActivity;
	ArrayList<HashMap<String, String>> data;
	boolean current_flag = false;
	int current_level = -1;

	Typeface face;

	// Constructor
	public FinaleLevelAdapter(Activity c, ArrayList<HashMap<String, String>> d) {
		this.mActivity = c;
		this.data = d;
		face = Typeface.createFromAsset(c.getAssets(), "font/"
				+ c.getResources().getString(R.string.font) + "");
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
		TextView level;
		TextView score;
	}

	// create a new ImageView for each item referenced by the Adapter
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = mActivity.getLayoutInflater();
			vi = inflater.inflate(R.layout.finale_grid_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.main_layout = (RelativeLayout) vi
					.findViewById(R.id.grid_level_main);
			viewHolder.levelIcon = (ImageView) vi
					.findViewById(R.id.grid_level_icon);
			viewHolder.level = (TextView) vi
					.findViewById(R.id.grid_level_number);
			viewHolder.score = (TextView) vi
					.findViewById(R.id.grid_level_score);
			viewHolder.score.setOnClickListener(mCloudListener);
			viewHolder.levelIcon.setOnClickListener(mClickListener);

			vi.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) vi.getTag();
		}

		if (Integer.parseInt(data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_IS_SENT))) == 0) {
			if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == 2) {
				viewHolder.levelIcon
						.setImageResource(R.drawable.ic_level_fail_not);
				viewHolder.score.setVisibility(View.VISIBLE);
			} else if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == 3) {
				viewHolder.levelIcon
						.setImageResource(R.drawable.ic_level_pass_not);
				viewHolder.score.setVisibility(View.VISIBLE);
			} else {
				viewHolder.score.setVisibility(View.INVISIBLE);
			}
		} else {
			if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == 2) {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_level_fail);
				viewHolder.score.setVisibility(View.VISIBLE);
			} else if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == 3) {
				viewHolder.levelIcon.setImageResource(R.drawable.ic_level_pass);
				viewHolder.score.setVisibility(View.VISIBLE);
			} else {
				viewHolder.score.setVisibility(View.INVISIBLE);
			}
		}

		viewHolder.score.setText(data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_POINT))
				+ " امتیاز");
		viewHolder.score.setTypeface(face);

		viewHolder.level.setText(" مرحله " + (position + 1));
		viewHolder.level.setTypeface(face);

		switch (Integer.parseInt(data.get(position).get(
				mActivity.getResources().getString(R.string.KEY_TYPE)))) {
		case 4:
			if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == mActivity
					.getResources().getInteger(R.integer.STATE_CORRECT)) {
				viewHolder.level.setVisibility(View.VISIBLE);
				viewHolder.levelIcon.setTag(false);
			} else if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == mActivity
					.getResources().getInteger(R.integer.STATE_ERROR)) {
				viewHolder.level.setVisibility(View.VISIBLE);
				viewHolder.levelIcon.setTag(false);
			} else {
				viewHolder.level.setVisibility(View.INVISIBLE);
				if (!current_flag || current_level == position) {
					current_level = position;
					viewHolder.levelIcon.setTag(true);
					viewHolder.levelIcon
							.setImageResource(R.drawable.ic_stad_finale_current);
					current_flag = true;
				} else {
					viewHolder.levelIcon.setTag(false);
					viewHolder.levelIcon
							.setImageResource(R.drawable.ic_stad_finale_off);
				}
			}
			break;
		case 5:
			if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == mActivity
					.getResources().getInteger(R.integer.STATE_CORRECT)) {
				viewHolder.level.setVisibility(View.VISIBLE);
				viewHolder.levelIcon.setTag(false);
			} else if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == mActivity
					.getResources().getInteger(R.integer.STATE_ERROR)) {
				viewHolder.level.setVisibility(View.VISIBLE);
				viewHolder.levelIcon.setTag(false);
			} else {
				viewHolder.level.setVisibility(View.INVISIBLE);
				if (!current_flag || current_level == position) {
					current_level = position;
					viewHolder.levelIcon.setTag(true);
					viewHolder.levelIcon
							.setImageResource(R.drawable.ic_player_f_current);
					current_flag = true;
				} else {
					viewHolder.levelIcon.setTag(false);
					viewHolder.levelIcon
							.setImageResource(R.drawable.ic_player_final_off);
				}
			}
			break;
		case 6:
			if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == mActivity
					.getResources().getInteger(R.integer.STATE_CORRECT)) {
				viewHolder.level.setVisibility(View.VISIBLE);
				viewHolder.levelIcon.setTag(false);
			} else if (Integer.parseInt(data.get(position).get(
					mActivity.getResources().getString(R.string.KEY_STATE))) == mActivity
					.getResources().getInteger(R.integer.STATE_ERROR)) {
				viewHolder.level.setVisibility(View.VISIBLE);
				viewHolder.levelIcon.setTag(false);
			} else {
				viewHolder.level.setVisibility(View.INVISIBLE);
				if (!current_flag || current_level == position) {
					current_level = position;
					viewHolder.levelIcon.setTag(true);
					viewHolder.levelIcon
							.setImageResource(R.drawable.ic_q_final_current);
					current_flag = true;
				} else {
					viewHolder.levelIcon.setTag(false);
					viewHolder.levelIcon
							.setImageResource(R.drawable.ic_q_final_off);
				}
			}
			break;

		}

		viewHolder.main_layout.setTag(position);

		return vi;
	}

	private View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			boolean tag = (Boolean) v.getTag();
			if (tag) {
				((FinaleLevelActivity) mActivity).onCurrentClick(current_level);
			}

		}
	};

	private View.OnClickListener mCloudListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			((FinaleLevelActivity) mActivity).uploadData();
		}
	};

}