package com.eynak.footballquize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eynak.utils.DatabasHandler;

public class StatDialog extends DialogFragment {

	int _level;

	int[] _level_data;
	int[] _total_data;

	ProgressBar pb_score;
	ProgressBar pb_player;
	ProgressBar pb_manager;
	ProgressBar pb_logo;
	ProgressBar pb_q;
	ProgressBar pb_shirt;

	ProgressBar pb_total_score;
	ProgressBar pb_total_player;
	ProgressBar pb_total_manager;
	ProgressBar pb_total_logo;
	ProgressBar pb_total_q;
	ProgressBar pb_total_shirt;

	TextView tv_score;
	TextView tv_player;
	TextView tv_manager;
	TextView tv_logo;
	TextView tv_q;
	TextView tv_shirt;

	TextView tv_total_score;
	TextView tv_total_player;
	TextView tv_total_manager;
	TextView tv_total_logo;
	TextView tv_total_q;
	TextView tv_total_shirt;

	int time = 200;

	@SuppressLint({ "InflateParams", "NewApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_stat, null);

		pb_score = (ProgressBar) rootView.findViewById(R.id.pb_score);
		pb_score.setRotation(180);
		pb_player = (ProgressBar) rootView.findViewById(R.id.pb_player);
		pb_player.setRotation(180);
		pb_manager = (ProgressBar) rootView.findViewById(R.id.pb_manager);
		pb_manager.setRotation(180);
		pb_logo = (ProgressBar) rootView.findViewById(R.id.pb_logo);
		pb_logo.setRotation(180);
		pb_q = (ProgressBar) rootView.findViewById(R.id.pb_q);
		pb_q.setRotation(180);
		pb_shirt = (ProgressBar) rootView.findViewById(R.id.pb_shirt);
		pb_shirt.setRotation(180);

		pb_total_score = (ProgressBar) rootView
				.findViewById(R.id.pb_total_score);
		pb_total_score.setRotation(180);
		pb_total_player = (ProgressBar) rootView
				.findViewById(R.id.pb_total_player);
		pb_total_player.setRotation(180);
		pb_total_manager = (ProgressBar) rootView
				.findViewById(R.id.pb_total_manager);
		pb_total_manager.setRotation(180);
		pb_total_logo = (ProgressBar) rootView.findViewById(R.id.pb_total_logo);
		pb_total_logo.setRotation(180);
		pb_total_q = (ProgressBar) rootView.findViewById(R.id.pb_total_q);
		pb_total_q.setRotation(180);
		pb_total_shirt = (ProgressBar) rootView
				.findViewById(R.id.pb_total_shirt);
		pb_total_shirt.setRotation(180);

		tv_score = (TextView) rootView.findViewById(R.id.stat_locale_score_p);
		tv_player = (TextView) rootView.findViewById(R.id.stat_locale_player_p);
		tv_manager = (TextView) rootView
				.findViewById(R.id.stat_locale_manager_p);
		tv_logo = (TextView) rootView.findViewById(R.id.stat_locale_logo_p);
		tv_q = (TextView) rootView.findViewById(R.id.stat_locale_q_p);
		tv_shirt = (TextView) rootView.findViewById(R.id.stat_locale_shirt_p);

		tv_total_score = (TextView) rootView
				.findViewById(R.id.stat_total_score_p);
		tv_total_player = (TextView) rootView
				.findViewById(R.id.stat_total_player_p);
		tv_total_manager = (TextView) rootView
				.findViewById(R.id.stat_total_manager_p);
		tv_total_logo = (TextView) rootView
				.findViewById(R.id.stat_total_logo_p);
		tv_total_q = (TextView) rootView.findViewById(R.id.stat_total_q_p);
		tv_total_shirt = (TextView) rootView
				.findViewById(R.id.stat_total_shirt_p);

		_level_data = new int[7];
		_total_data = new int[7];
		new TestAsync().execute();

		return rootView;
	}

	public void setLevel(int l) {
		this._level = l;
	}

	class TestAsync extends AsyncTask<Void, Integer, String> {
		protected void onPreExecute() {
		}

		protected String doInBackground(Void... arg0) {
			DatabasHandler db = new DatabasHandler(getActivity());
			ArrayList<HashMap<String, Integer>> levelStat = db
					.getLevelStat(_level);
			ArrayList<HashMap<String, Integer>> totalStat = db.getAllLevel();
			db.close();

			for (int i = 0; i < levelStat.size(); i++) {
				_level_data[levelStat.get(i).get("Type")] += levelStat.get(i)
						.get("Point");
				_level_data[0] += levelStat.get(i).get("Point");
			}
			for (int i = 0; i < totalStat.size(); i++) {
				_total_data[totalStat.get(i).get("Type")] += totalStat.get(i)
						.get("Point");
				_total_data[0] += totalStat.get(i).get("Point");
			}
			return "You are at PostExecute";
		}

		@SuppressLint("NewApi")
		protected void onPostExecute(String result) {
			AnimatorSet animSet = new AnimatorSet();
			List<Animator> animCollect = new ArrayList<Animator>();
			if (_level == 30) {
				tv_score.setText((int) (((float) _level_data[0] / (8 * 2400)) * 100)
						+ "%");
				tv_player.setText((int) (((float) _level_data[5] / 7200) * 100)
						+ "%");
				tv_manager
						.setText((int) (((float) _level_data[3] / 1600) * 100)
								+ "%");
				tv_logo.setText((int) (((float) _level_data[1] / 3200) * 100)
						+ "%");
				tv_q.setText((int) (((float) _level_data[6] / 4000) * 100)
						+ "%");
				tv_shirt.setText((int) (((float) _level_data[2] / 3200) * 100)
						+ "%");
				ObjectAnimator progressAnimation = ObjectAnimator.ofInt(
						pb_score, "progress", 0,
						(int) (((float) _level_data[0] / (8 * 2400)) * 100));

				progressAnimation.setDuration(time);
				progressAnimation.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation);

				ObjectAnimator progressAnimation1 = ObjectAnimator.ofInt(
						pb_player, "progress", 0,
						(int) (((float) _level_data[5] / 7200) * 100));

				progressAnimation1.setDuration(time);
				progressAnimation1
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation1);

				ObjectAnimator progressAnimation2 = ObjectAnimator.ofInt(
						pb_manager, "progress", 0,
						(int) (((float) _level_data[3] / 1600) * 100));

				progressAnimation2.setDuration(time);
				progressAnimation2
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation2);

				ObjectAnimator progressAnimation3 = ObjectAnimator.ofInt(
						pb_logo, "progress", 0,
						(int) (((float) _level_data[1] / 3200) * 100));

				progressAnimation3.setDuration(time);
				progressAnimation3
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation3);

				ObjectAnimator progressAnimation4 = ObjectAnimator.ofInt(pb_q,
						"progress", 0,
						(int) (((float) _level_data[6] / 4000) * 100));

				progressAnimation4.setDuration(time);
				progressAnimation4
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation4);

				ObjectAnimator progressAnimation5 = ObjectAnimator.ofInt(
						pb_shirt, "progress", 0,
						(int) (((float) _level_data[2] / 3200) * 100));

				progressAnimation5.setDuration(time);
				progressAnimation5
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation5);
			} else {
				tv_score.setText((int) (((float) _level_data[0] / 2400) * 100)
						+ "%");
				tv_player.setText((int) (((float) _level_data[5] / 900) * 100)
						+ "%");
				tv_manager.setText((int) (((float) _level_data[3] / 200) * 100)
						+ "%");
				tv_logo.setText((int) (((float) _level_data[1] / 400) * 100)
						+ "%");
				tv_q.setText((int) (((float) _level_data[6] / 500) * 100) + "%");
				tv_shirt.setText((int) (((float) _level_data[2] / 400) * 100)
						+ "%");
				ObjectAnimator progressAnimation = ObjectAnimator.ofInt(
						pb_score, "progress", 0,
						(int) (((float) _level_data[0] / 2400) * 100));

				progressAnimation.setDuration(time);
				progressAnimation.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation);

				ObjectAnimator progressAnimation1 = ObjectAnimator.ofInt(
						pb_player, "progress", 0,
						(int) (((float) _level_data[5] / 900) * 100));

				progressAnimation1.setDuration(time);
				progressAnimation1
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation1);

				ObjectAnimator progressAnimation2 = ObjectAnimator.ofInt(
						pb_manager, "progress", 0,
						(int) (((float) _level_data[3] / 200) * 100));

				progressAnimation2.setDuration(time);
				progressAnimation2
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation2);

				ObjectAnimator progressAnimation3 = ObjectAnimator.ofInt(
						pb_logo, "progress", 0,
						(int) (((float) _level_data[1] / 400) * 100));

				progressAnimation3.setDuration(time);
				progressAnimation3
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation3);

				ObjectAnimator progressAnimation4 = ObjectAnimator.ofInt(pb_q,
						"progress", 0,
						(int) (((float) _level_data[6] / 500) * 100));

				progressAnimation4.setDuration(time);
				progressAnimation4
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation4);

				ObjectAnimator progressAnimation5 = ObjectAnimator.ofInt(
						pb_shirt, "progress", 0,
						(int) (((float) _level_data[2] / 400) * 100));

				progressAnimation5.setDuration(time);
				progressAnimation5
						.setInterpolator(new DecelerateInterpolator());
				animCollect.add(progressAnimation5);
			}

			tv_total_score
					.setText((int) (((float) _total_data[0] / (2400 * 37)) * 100)
							+ "%");
			tv_total_player
					.setText((int) (((float) _total_data[5] / (900 * 37)) * 100)
							+ "%");
			tv_total_manager
					.setText((int) (((float) _total_data[3] / (200 * 37)) * 100)
							+ "%");
			tv_total_logo
					.setText((int) (((float) _total_data[1] / (400 * 37)) * 100)
							+ "%");
			tv_total_q
					.setText((int) (((float) _total_data[6] / (500 * 37)) * 100)
							+ "%");
			tv_total_shirt
					.setText((int) (((float) _total_data[2] / (400 * 37)) * 100)
							+ "%");

			ObjectAnimator progressAnimation6 = ObjectAnimator.ofInt(
					pb_total_score, "progress", 0,
					(int) (((float) _total_data[0] / (2400 * 37)) * 100));

			progressAnimation6.setDuration(time);
			progressAnimation6.setInterpolator(new DecelerateInterpolator());
			animCollect.add(progressAnimation6);

			// pb_total_player
			// .setProgress((int) (((float) _total_data[5] / (900 * 37)) *
			// 100));
			ObjectAnimator progressAnimation7 = ObjectAnimator.ofInt(
					pb_total_player, "progress", 0,
					(int) (((float) _total_data[5] / (900 * 37)) * 100));

			progressAnimation7.setDuration(time);
			progressAnimation7.setInterpolator(new DecelerateInterpolator());
			animCollect.add(progressAnimation7);

			// pb_total_manager
			// .setProgress((int) (((float) _total_data[3] / (200 * 37)) *
			// 100));
			ObjectAnimator progressAnimation8 = ObjectAnimator.ofInt(
					pb_total_manager, "progress", 0,
					(int) (((float) _total_data[3] / (200 * 37)) * 100));

			progressAnimation8.setDuration(time);
			progressAnimation8.setInterpolator(new DecelerateInterpolator());
			animCollect.add(progressAnimation8);

			// pb_total_logo
			// .setProgress((int) (((float) _total_data[1] / (400 * 37)) *
			// 100));
			ObjectAnimator progressAnimation9 = ObjectAnimator.ofInt(
					pb_total_logo, "progress", 0,
					(int) (((float) _total_data[1] / (400 * 37)) * 100));

			progressAnimation9.setDuration(time);
			progressAnimation9.setInterpolator(new DecelerateInterpolator());
			animCollect.add(progressAnimation9);

			// pb_total_q
			// .setProgress((int) (((float) _total_data[6] / (500 * 37)) *
			// 100));
			ObjectAnimator progressAnimation10 = ObjectAnimator.ofInt(
					pb_total_q, "progress", 0,
					(int) (((float) _total_data[6] / (500 * 37)) * 100));

			progressAnimation10.setDuration(time);
			progressAnimation10.setInterpolator(new DecelerateInterpolator());
			animCollect.add(progressAnimation10);
			// pb_total_shirt
			// .setProgress((int) (((float) _total_data[2] / (400 * 37)) *
			// 100));
			ObjectAnimator progressAnimation11 = ObjectAnimator.ofInt(
					pb_total_shirt, "progress", 0,
					(int) (((float) _total_data[2] / (400 * 37)) * 100));

			progressAnimation11.setDuration(time);
			progressAnimation11.setInterpolator(new DecelerateInterpolator());
			animCollect.add(progressAnimation11);
			animSet.playSequentially(animCollect);
			animSet.start();
		}
	}
}