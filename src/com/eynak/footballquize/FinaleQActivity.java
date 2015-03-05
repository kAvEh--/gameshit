package com.eynak.footballquize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.eynak.utils.DatabasHandler;

public class FinaleQActivity extends FragmentActivity {

	protected ImageView mainImageQuestion;
	protected TextView mainTextQuestion;
	protected Bitmap mFilterBitmap;

	boolean[] filters;

	private int _points;
	private int _coins;

	TextView _points_view;
	TextView _coins_view;

	ImageButton _replace_button;
	ImageButton _remove_button;

	int _levelId;
	int _type;
	int _state;
	int _used_freez = 0;
	int _used_hint = 0;
	long _start_time = 0;

	Button _ch1;
	Button _ch2;
	Button _ch3;
	Button _ch4;

	RelativeLayout time_bar;
	int time_bar_width;

	CountDownTimer counter;
	long currentTime;

	boolean game_flag = true;

	private String _correct_answer;

	final long _TOTAL_TIME_IMAGE = 15000;
	final long _TOTAL_TIME_Q = 25000;

	final int _COIN_REPLACE = 100;
	final int _COIN_REMOVE = 100;

	final int _SCORE_FULL = 100;

	final int _COINS_FULL = 200;

	private ArrayList<HashMap<String, String>> __levelData;
	private ArrayList<HashMap<String, String>> __backupData;

	boolean _replace_flag = true;
	boolean _remove_flag = true;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		_levelId = intent.getIntExtra(
				getResources().getString(R.string.KEY_ID), 0);
		__levelData = (ArrayList<HashMap<String, String>>) intent
				.getSerializableExtra("data");

		HashMap<String, Integer> gameData;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		gameData = db.getGameData();
		__backupData = db.getBackupData();
		db.setFinaleStat(
				Integer.parseInt(__levelData.get(_levelId).get(
						getResources().getString(R.string.KEY_ID))),
				getResources().getInteger(R.integer.STATE_ERROR));
		db.close();

		_points = gameData.get("finaleScore");
		_coins = gameData.get(getResources().getString(R.string.KEY_COINS));

		if (__levelData == null)
			finish();
		else {
			_type = Integer.parseInt(__levelData.get(_levelId).get(
					getResources().getString(R.string.KEY_TYPE)));
			_state = Integer.parseInt(__levelData.get(_levelId).get(
					getResources().getString(R.string.KEY_STATE)));
		}

		setContentView(R.layout.activity_finale_q);

		_replace_button = (ImageButton) findViewById(R.id.q_bar_replace);
		_remove_button = (ImageButton) findViewById(R.id.q_bar_remove_choice);

		if (__backupData != null && __backupData.size() > 0) {

		} else {
			_replace_flag = false;
			_replace_button.setImageResource(R.drawable.ic_final_replace_off);
		}

		Typeface face = Typeface.createFromAsset(getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");
		
		_points_view = (TextView) findViewById(R.id.question_header_points);
		_points_view.setTypeface(face);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_coins_view.setTypeface(face);
		time_bar = (RelativeLayout) findViewById(R.id.q_tima_bar);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		time_bar_width = metrics.widthPixels;

		setQuestion();

		_points_view.setText(String.valueOf(_points));
		_coins_view.setText(String.valueOf(_coins));
	}

	private void setQuestion() {
		HashMap<String, String> q_data = __levelData.get(_levelId);
		if (q_data == null)
			finish();

		_correct_answer = __levelData.get(_levelId).get("answer");

		List<String> tmp = new ArrayList<String>();
		tmp.add(__levelData.get(_levelId).get("ch1"));
		tmp.add(__levelData.get(_levelId).get("ch2"));
		tmp.add(__levelData.get(_levelId).get("ch3"));
		tmp.add(_correct_answer);
		setInitialSettings(tmp);

		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);
		mainTextQuestion = (TextView) findViewById(R.id.q_main_question);

		if (Integer.parseInt(__levelData.get(_levelId).get("Type")) == 6) {
			mainImageQuestion.setVisibility(View.INVISIBLE);
			mainTextQuestion.setVisibility(View.VISIBLE);

			mainTextQuestion.setText(__levelData.get(_levelId).get("body"));
			runTimer(_TOTAL_TIME_Q);
		} else {
			mainImageQuestion.setVisibility(View.VISIBLE);
			mainTextQuestion.setVisibility(View.INVISIBLE);

			try {
				final Bitmap bitmap = BitmapFactory.decodeStream(getAssets()
						.open("Finale/"
								+ __levelData.get(_levelId).get("imagePath")));
				mainImageQuestion.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
				finish();
			}
			runTimer(_TOTAL_TIME_IMAGE);
		}
	}

	public void setInitialSettings(List<String> choicesData) {
		_ch1 = (Button) findViewById(R.id.q_c_1);
		_ch2 = (Button) findViewById(R.id.q_c_2);
		_ch3 = (Button) findViewById(R.id.q_c_3);
		_ch4 = (Button) findViewById(R.id.q_c_4);
		_ch1.setTag(true);
		_ch2.setTag(true);
		_ch3.setTag(true);
		_ch4.setTag(true);
		Collections.shuffle(choicesData);
		_ch1.setText(choicesData.get(0));
		_ch2.setText(choicesData.get(1));
		_ch3.setText(choicesData.get(2));
		_ch4.setText(choicesData.get(3));
	}

	public void runTimer(final long time) {
		currentTime = time;
		counter = new CountDownTimer(time, 10) {

			public void onTick(long milli) {
				LayoutParams l = (LayoutParams) time_bar.getLayoutParams();
				currentTime = milli;
				l.width = (Math
						.min((int) (((double) milli / (double) time) * time_bar_width),
								time_bar_width));
				time_bar.setLayoutParams(l);
			}

			public void onFinish() {
				LayoutParams l = (LayoutParams) time_bar.getLayoutParams();
				l.width = 0;
				time_bar.setLayoutParams(l);
				currentTime = 0;
			}
		};
		counter.start();
	}

	public void selectChoice(View v) {
		String select = ((Button) v).getText().toString();
		if ((Boolean) v.getTag() && game_flag) {
			v.setTag(false);
			if (select.equals(_correct_answer)) {
				selectCorrect(v);
			} else {
				selectFalse(v, select);
			}
		}
	}

	public void selectCorrect(View v) {
		if (counter != null)
			counter.cancel();
		game_flag = false;

		_replace_button.setImageResource(R.drawable.ic_final_replace_off);
		_remove_button.setImageResource(R.drawable.ic_final_remove_off);
		v.setBackgroundResource(R.drawable.ch_correct_bg);

		// TODO set score
		int tmpScore;
		if (Integer.parseInt(__levelData.get(_levelId).get("Type")) == 6) {
			tmpScore = (int) (Math.min(_TOTAL_TIME_Q, currentTime) * 8 / 1000);
		} else {
			tmpScore = (int) (Math.min(_TOTAL_TIME_IMAGE, currentTime) * 4 / 300);
		}

		_points += tmpScore;
		_points_view.setText(String.valueOf(_points));

		_coins += _COINS_FULL;
		_coins_view.setText(String.valueOf(_coins));

		DatabasHandler db = new DatabasHandler(getApplicationContext());
		db.addFinalScore(Integer.parseInt(__levelData.get(_levelId).get("Id")),
				tmpScore);
		if (!_replace_flag && __backupData != null && __backupData.size() > 0) {
			db.addFinalScore(Integer.parseInt(__backupData.get(0).get("Id")),
					tmpScore);
		}
		db.addCoins(_COINS_FULL);
		db.close();

		showSuccessPage(tmpScore);
	}

	public void selectFalse(View v, String select) {
		v.setBackgroundResource(R.drawable.ch_error_bg);
		if (counter != null)
			counter.cancel();
		game_flag = false;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		db.addFinalScore(Integer.parseInt(__levelData.get(_levelId).get("Id")),
				0);
		db.close();

		showFailurePage();
	}

	private void showFailurePage() {
		// TODO
		FinaleSuccessDialog fr = new FinaleSuccessDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.setScore(0);
		fr.setCoins(0);
		fr.setFinaleScore(_points);
		fr.show(getSupportFragmentManager(), "Hello");
	}

	private void showSuccessPage(int score) {
		// TODO
		FinaleSuccessDialog fr = new FinaleSuccessDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.setScore(score);
		fr.setCoins(_COINS_FULL);
		fr.setFinaleScore(_points);
		fr.show(getSupportFragmentManager(), "Hello");
	}

	@Override
	protected void onDestroy() {
		if (mFilterBitmap != null) {
			mFilterBitmap.recycle();
			mFilterBitmap = null;
		}
		super.onDestroy();
	}

	public void onReplaceClick(View v) {
		if (game_flag && _replace_flag) {
			if (_coins >= _COIN_REPLACE) {
				_replace_button
						.setImageResource(R.drawable.ic_final_replace_off);
				if (counter != null)
					counter.cancel();

				_coins -= _COIN_REPLACE;

				_coins_view.setText(String.valueOf(_coins));

				DatabasHandler db = new DatabasHandler(getApplicationContext());
				db.addCoins(-_COIN_REPLACE);
				db.close();

				replaceQuestion();
			} else
				Toast.makeText(getApplicationContext(),
						"You don`t have enough coins.", Toast.LENGTH_LONG)
						.show();
		} else
			Toast.makeText(getApplicationContext(), "Really needs help??",
					Toast.LENGTH_LONG).show();
	}

	private void replaceQuestion() {
		// TODO Auto-generated method stub
		HashMap<String, String> q_data = __backupData.get(0);
		if (q_data == null)
			finish();

		DatabasHandler db = new DatabasHandler(getApplicationContext());
		db.setFinaleStat(
				Integer.parseInt(__backupData.get(0).get(
						getResources().getString(R.string.KEY_ID))),
				getResources().getInteger(R.integer.STATE_ERROR));
		db.close();

		_replace_flag = false;
		_remove_flag = true;
		_remove_button.setImageResource(R.drawable.ic_final_remove_on);
		_replace_button.setImageResource(R.drawable.ic_final_replace_off);

		_correct_answer = __backupData.get(0).get("answer");

		List<String> tmp = new ArrayList<String>();
		tmp.add(__backupData.get(0).get("ch1"));
		tmp.add(__backupData.get(0).get("ch2"));
		tmp.add(__backupData.get(0).get("ch3"));
		tmp.add(_correct_answer);
		setInitialSettings(tmp);

		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);
		mainTextQuestion = (TextView) findViewById(R.id.q_main_question);

		if (Integer.parseInt(__backupData.get(0).get("Type")) == 6) {
			mainImageQuestion.setVisibility(View.INVISIBLE);
			mainTextQuestion.setVisibility(View.VISIBLE);

			mainTextQuestion.setText(__backupData.get(0).get("body"));
			runTimer(_TOTAL_TIME_Q);
		} else {
			mainImageQuestion.setVisibility(View.VISIBLE);
			mainTextQuestion.setVisibility(View.INVISIBLE);

			try {
				final Bitmap bitmap = BitmapFactory
						.decodeStream(getAssets().open(
								"Finale/"
										+ __backupData.get(0).get("imagePath")));
				mainImageQuestion.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
				finish();
			}
			runTimer(_TOTAL_TIME_IMAGE);
		}
	}

	public void onRemoveClick(View v) {
		if (game_flag && _remove_flag) {
			if (_coins >= _COIN_REMOVE) {
				_remove_flag = false;
				_remove_button.setImageResource(R.drawable.ic_final_remove_off);
				List<Button> choicetmp = new ArrayList<Button>();
				choicetmp.add(_ch1);
				choicetmp.add(_ch2);
				choicetmp.add(_ch3);
				choicetmp.add(_ch4);

				Collections.shuffle(choicetmp);
				for (int i = 0; i < 4; i++) {
					if ((Boolean) choicetmp.get(i).getTag()
							&& !choicetmp.get(i).getText().toString()
									.equals(_correct_answer)) {

						choicetmp.get(i).setTag(false);
						choicetmp.get(i).setVisibility(View.INVISIBLE);

						break;
					}
				}
				_coins -= _COIN_REMOVE;
				_coins_view.setText(String.valueOf(_coins));

				DatabasHandler db = new DatabasHandler(getApplicationContext());
				db.addCoins(-_COIN_REMOVE);
				db.close();
			} else
				Toast.makeText(getApplicationContext(),
						"You don`t have enough coins.", Toast.LENGTH_LONG)
						.show();
		} else
			Toast.makeText(getApplicationContext(), "Really needs help??",
					Toast.LENGTH_LONG).show();
	}

	public void onCoinClick(View v) {
		Intent intent = new Intent(FinaleQActivity.this, ShopActivity.class);
		startActivity(intent);
	}

	public void backAction() {
		finish();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton("No", null)
				.setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (counter != null)
							counter.cancel();
						DatabasHandler db = new DatabasHandler(
								getApplicationContext());
						db.addFinalScore(
								Integer.parseInt(__levelData.get(_levelId).get(
										"Id")), 0);
						db.close();
						finish();
					}
				}).create().show();
	}

}