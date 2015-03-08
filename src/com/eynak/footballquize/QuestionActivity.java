package com.eynak.footballquize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.eynak.objects.Logo;
import com.eynak.objects.Manager;
import com.eynak.objects.Player;
import com.eynak.objects.Question;
import com.eynak.objects.Shirt;
import com.eynak.objects.Stadium;
import com.eynak.utils.DatabasHandler;
import com.eynak.utils.SendDatatoServer;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class QuestionActivity extends FragmentActivity {

	protected ImageView mainImageQuestion;
	protected TextView mainTextQuestion;
	protected Bitmap mFilterBitmap;

	boolean[] filters;

	private int _points;
	private int _coins;
	private int _correct_in_raw;

	TextView _points_view;
	TextView _coins_view;

	ImageButton _skip_button;
	ImageButton _freez_button;
	ImageButton _remove_button;
	ImageButton _hint_button;

	int _levelId;
	int _packageId;
	int _relatedId;
	int _type;
	int _state;
	String _falseAnswers;
	String _removedChoices;
	int _false_num;
	int _used_freez = 0;
	int _used_hint = 0;
	long _start_time = 0;

	Button _ch1;
	Button _ch2;
	Button _ch3;
	Button _ch4;
	Button _ch5;
	Button _ch6;
	Button _ch7;
	Button _ch8;

	Logo _logo;
	Stadium _stadium;
	Manager _manager;
	Shirt _shirt;
	Question _question;
	Player _player;

	RelativeLayout time_bar;
	int time_bar_width;

	CountDownTimer counter;
	long currentTime;

	boolean game_flag = true;
	boolean free_hint = false;
	boolean free_remove = false;
	boolean free_freeze = false;
	boolean free_skip = false;

	private String _correct_answer;
	private String _hint_toshow;

	final long _TOTAL_TIME = 60000;
	final int _COIN_SKIP = 100;
	final int _COIN_FREEZ = 50;
	final int _COIN_REMOVE = 30;
	final int _COIN_HELP = 20;

	final int _SCORE_SKIP = 80;
	final int _SCORE_FULL = 80;
	final int _SCORE_BONUS_FULL = 20;
	final int _SCORE_BONUS_HALF = 13;
	final int _SCORE_BONUS_LESS = 6;

	final int _COINS_FULL = 30;
	final int _COINS_HALF = 20;
	final int _COINS_LESS = 10;

	private ArrayList<HashMap<String, Integer>> __levelData;
	private int __position;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		_packageId = intent.getIntExtra(
				getResources().getString(R.string.KEY_PACKAGEID), 0);
		_levelId = intent.getIntExtra(
				getResources().getString(R.string.KEY_ID), 0);
		__levelData = (ArrayList<HashMap<String, Integer>>) intent
				.getSerializableExtra("test");
		__position = intent.getIntExtra("position", 0);

		HashMap<String, String> tempData;
		HashMap<String, Integer> gameData;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		tempData = db.getQuestionData(_levelId);
		gameData = db.getGameData();
		// HashMap<String, String> pData = db.getPackageInfo(_packageId);
		db.close();

		_points = gameData.get(getResources().getString(R.string.KEY_POINT));
		_coins = gameData.get(getResources().getString(R.string.KEY_COINS));
		_correct_in_raw = gameData.get(getResources().getString(
				R.string.KEY_CORRECT_IN_RAW));

		if (tempData == null)
			finish();
		else {
			_relatedId = Integer.parseInt(tempData.get(getResources()
					.getString(R.string.KEY_RELATED)));
			_type = Integer.parseInt(tempData.get(getResources().getString(
					R.string.KEY_TYPE)));
			_state = Integer.parseInt(tempData.get(getResources().getString(
					R.string.KEY_STATE)));
			_falseAnswers = tempData.get(getResources().getString(
					R.string.KEY_FALSE_ANSWERS));
			_removedChoices = tempData.get(getResources().getString(
					R.string.KEY_REMOVED_CHOICES));
			_used_freez = Integer.parseInt(tempData.get(getResources()
					.getString(R.string.KEY_USED_FREEZ)));
			_used_hint = Integer.parseInt(tempData.get(getResources()
					.getString(R.string.KEY_USED_HINTS)));
			_start_time = Long.parseLong(tempData.get(getResources().getString(
					R.string.KEY_START_TIME)));
		}

		setContentView(R.layout.activity_question);

		_skip_button = (ImageButton) findViewById(R.id.q_bar_next);
		_freez_button = (ImageButton) findViewById(R.id.q_bar_freez);
		_remove_button = (ImageButton) findViewById(R.id.q_bar_remove_choice);
		_hint_button = (ImageButton) findViewById(R.id.q_bar_hint);

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

		if (_type == getResources().getInteger(R.integer.TYPE_LOGO)) {
			setLogo();
		} else if (_type == getResources().getInteger(R.integer.TYPE_MANAGER)) {
			setManager();
		} else if (_type == getResources().getInteger(R.integer.TYPE_PLAYER)) {
			setPlayer();
		} else if (_type == getResources().getInteger(R.integer.TYPE_QUESTION)) {
			setQuestion();
		} else if (_type == getResources().getInteger(R.integer.TYPE_SHIRT)) {
			setShirt();
		} else if (_type == getResources().getInteger(R.integer.TYPE_STADIUM)) {
			setStadium();
		}

		_points_view.setText(String.valueOf(_points));
		_coins_view.setText(String.valueOf(_coins));

		if (game_flag) {
			if (gameData.get(getResources().getString(R.string.KEY_FREE_HINT)) == 1
					&& _type != getResources().getInteger(
							R.integer.TYPE_QUESTION)) {
				_hint_button.setImageResource(R.drawable.ic_help_free);
				free_hint = true;
			}
			if (gameData
					.get(getResources().getString(R.string.KEY_FREE_REMOVE)) == 1) {
				_remove_button.setImageResource(R.drawable.ic_remove_free);
				free_remove = true;
			}
			if (gameData
					.get(getResources().getString(R.string.KEY_FREE_FREEZE)) == 1) {
				_freez_button.setImageResource(R.drawable.ic_freeze_free);
				free_freeze = true;
			}
			if (gameData.get(getResources().getString(R.string.KEY_FREE_SKIP)) == 1) {
				_skip_button.setImageResource(R.drawable.ic_skip_free);
				free_skip = true;
			}
		}
	}

	private void setStadium() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_stadium = db.getStadium(_relatedId);
		if (_stadium == null)
			finish();

		_correct_answer = _stadium.get_answer();

		List<String> tmp = new ArrayList<String>();
		tmp.add(_stadium.get_ch1());
		tmp.add(_stadium.get_ch2());
		tmp.add(_stadium.get_ch3());
		tmp.add(_stadium.get_ch4());
		tmp.add(_stadium.get_ch5());
		tmp.add(_stadium.get_ch6());
		tmp.add(_stadium.get_ch7());
		tmp.add(_correct_answer);
		setInitialSettings(tmp);

		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Stadium/" + _stadium.get_imagePath()));
			mainImageQuestion.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}

		setChoicesHistory();

		_hint_toshow = _stadium.get_nameFa();
	}

	private void setShirt() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_shirt = db.getShirt(_relatedId);
		_logo = db.getLogo(_shirt.get_logoId());
		if (_shirt == null || _logo == null)
			finish();

		_correct_answer = _logo.get_nameFa();

		List<String> tmp = new ArrayList<String>();
		tmp.add(_logo.get_ch1());
		tmp.add(_logo.get_ch2());
		tmp.add(_logo.get_ch3());
		tmp.add(_logo.get_ch4());
		tmp.add(_logo.get_ch5());
		tmp.add(_logo.get_ch6());
		tmp.add(_logo.get_ch7());
		tmp.add(_correct_answer);
		setInitialSettings(tmp);

		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Shirt/" + _shirt.get_imagePath()));
			mainImageQuestion.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}

		setChoicesHistory();

		_hint_toshow = "این باشگاه در لیگ " + _logo.get_hint() + " است.";
	}

	private void setQuestion() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_question = db.getQuestion(_relatedId);
		if (_question == null)
			finish();

		_correct_answer = _question.get_answer();

		List<String> tmp = new ArrayList<String>();
		tmp.add(_question.get_ch1());
		tmp.add(_question.get_ch2());
		tmp.add(_question.get_ch3());
		tmp.add(_question.get_ch4());
		tmp.add(_question.get_ch5());
		tmp.add(_question.get_ch6());
		tmp.add(_question.get_ch7());
		tmp.add(_correct_answer);
		setInitialSettings(tmp);

		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);
		mainTextQuestion = (TextView) findViewById(R.id.q_main_question);
		mainImageQuestion.setVisibility(View.INVISIBLE);
		mainTextQuestion.setVisibility(View.VISIBLE);

		mainTextQuestion.setText(_question.get_body());

		setChoicesHistory();

		// hiding hint
		_hint_button.setImageResource(R.drawable.ic_help_off);
	}

	private void setPlayer() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_player = db.getPlayer(_relatedId);
		if (_player == null)
			finish();

		_correct_answer = _player.get_nameFa();

		List<String> tmp = new ArrayList<String>();
		tmp.add(_player.get_ch1());
		tmp.add(_player.get_ch2());
		tmp.add(_player.get_ch3());
		tmp.add(_player.get_ch4());
		tmp.add(_player.get_ch5());
		tmp.add(_player.get_ch6());
		tmp.add(_player.get_ch7());
		tmp.add(_correct_answer);
		setInitialSettings(tmp);

		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Player/" + _player.get_imagePath()));
			mainImageQuestion.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}

		setChoicesHistory();

		_hint_toshow = "این بازیکن برای تیم ملی " + _player.get_nationality()
				+ " " + _player.get_playNum() + " بازی کرده است.";
	}

	private void setManager() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_manager = db.getManager(_relatedId);
		if (_manager == null)
			finish();

		_correct_answer = _manager.get_nameFa();

		List<String> tmp = new ArrayList<String>();
		tmp.add(_manager.get_ch1());
		tmp.add(_manager.get_ch2());
		tmp.add(_manager.get_ch3());
		tmp.add(_manager.get_ch4());
		tmp.add(_manager.get_ch5());
		tmp.add(_manager.get_ch6());
		tmp.add(_manager.get_ch7());
		tmp.add(_correct_answer);
		setInitialSettings(tmp);

		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Manager/" + _manager.get_imagePath()));
			mainImageQuestion.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}

		setChoicesHistory();

		_hint_toshow = "در سال‌های " + _manager.get_year() + " مربی "
				+ _manager.get_team() + " بوده است.";
	}

	public void setLogo() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_logo = db.getLogo(_relatedId);
		if (_logo == null)
			finish();

		_correct_answer = _logo.get_nameFa();

		List<String> tmp = new ArrayList<String>();
		tmp.add(_logo.get_ch1());
		tmp.add(_logo.get_ch2());
		tmp.add(_logo.get_ch3());
		tmp.add(_logo.get_ch4());
		tmp.add(_logo.get_ch5());
		tmp.add(_logo.get_ch6());
		tmp.add(_logo.get_ch7());
		tmp.add(_correct_answer);
		setInitialSettings(tmp);

		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Logo/" + _logo.get_imagePath()));
			mainImageQuestion.setImageBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}
		setChoicesHistory();

		_hint_toshow = "این باشگاه در لیگ " + _logo.get_hint() + " است.";
	}

	public void setInitialSettings(List<String> choicesData) {
		_ch1 = (Button) findViewById(R.id.q_c_1);
		_ch2 = (Button) findViewById(R.id.q_c_2);
		_ch3 = (Button) findViewById(R.id.q_c_3);
		_ch4 = (Button) findViewById(R.id.q_c_4);
		_ch5 = (Button) findViewById(R.id.q_c_5);
		_ch6 = (Button) findViewById(R.id.q_c_6);
		_ch7 = (Button) findViewById(R.id.q_c_7);
		_ch8 = (Button) findViewById(R.id.q_c_8);
		_ch1.setTag(true);
		_ch2.setTag(true);
		_ch3.setTag(true);
		_ch4.setTag(true);
		_ch5.setTag(true);
		_ch6.setTag(true);
		_ch7.setTag(true);
		_ch8.setTag(true);
		Collections.shuffle(choicesData);
		_ch1.setText(choicesData.get(0));
		_ch2.setText(choicesData.get(1));
		_ch3.setText(choicesData.get(2));
		_ch4.setText(choicesData.get(3));
		_ch5.setText(choicesData.get(4));
		_ch6.setText(choicesData.get(5));
		_ch7.setText(choicesData.get(6));
		_ch8.setText(choicesData.get(7));
	}

	public void setChoicesHistory() {
		_false_num = 0;
		if (_falseAnswers != null && _falseAnswers.length() > 0) {
			String[] parts = _falseAnswers.split(",");
			_false_num = parts.length;
			for (int i = 0; i < parts.length; i++) {
				if (_ch1.getText().equals(parts[i])) {
					_ch1.setBackgroundResource(R.drawable.ch_error_bg);
					_ch1.setTag(false);
				} else if (_ch2.getText().equals(parts[i])) {
					_ch2.setBackgroundResource(R.drawable.ch_error_bg);
					_ch2.setTag(false);
				} else if (_ch3.getText().equals(parts[i])) {
					_ch3.setBackgroundResource(R.drawable.ch_error_bg);
					_ch3.setTag(false);
				} else if (_ch4.getText().equals(parts[i])) {
					_ch4.setBackgroundResource(R.drawable.ch_error_bg);
					_ch4.setTag(false);
				} else if (_ch5.getText().equals(parts[i])) {
					_ch5.setBackgroundResource(R.drawable.ch_error_bg);
					_ch5.setTag(false);
				} else if (_ch6.getText().equals(parts[i])) {
					_ch6.setBackgroundResource(R.drawable.ch_error_bg);
					_ch6.setTag(false);
				} else if (_ch7.getText().equals(parts[i])) {
					_ch7.setBackgroundResource(R.drawable.ch_error_bg);
					_ch7.setTag(false);
				} else if (_ch8.getText().equals(parts[i])) {
					_ch8.setBackgroundResource(R.drawable.ch_error_bg);
					_ch8.setTag(false);
				}
			}
		}
		if (_removedChoices != null && _removedChoices.length() > 0) {
			_remove_button.setImageResource(R.drawable.ic_remove_off);
			String[] parts = _removedChoices.split(",");
			for (int i = 0; i < parts.length; i++) {
				if (_ch1.getText().equals(parts[i])) {
					_ch1.setVisibility(View.INVISIBLE);
					_ch1.setTag(false);
				} else if (_ch2.getText().equals(parts[i])) {
					_ch2.setVisibility(View.INVISIBLE);
					_ch2.setTag(false);
				} else if (_ch3.getText().equals(parts[i])) {
					_ch3.setVisibility(View.INVISIBLE);
					_ch3.setTag(false);
				} else if (_ch4.getText().equals(parts[i])) {
					_ch4.setVisibility(View.INVISIBLE);
					_ch4.setTag(false);
				} else if (_ch5.getText().equals(parts[i])) {
					_ch5.setVisibility(View.INVISIBLE);
					_ch5.setTag(false);
				} else if (_ch6.getText().equals(parts[i])) {
					_ch6.setVisibility(View.INVISIBLE);
					_ch6.setTag(false);
				} else if (_ch7.getText().equals(parts[i])) {
					_ch7.setVisibility(View.INVISIBLE);
					_ch7.setTag(false);
				} else if (_ch8.getText().equals(parts[i])) {
					_ch8.setVisibility(View.INVISIBLE);
					_ch8.setTag(false);
				}
			}
		}
		if (_state == getResources().getInteger(R.integer.STATE_CORRECT)) {
			_skip_button.setImageResource(R.drawable.ic_skip_off);
			_freez_button.setImageResource(R.drawable.ic_freeze_off);
			_remove_button.setImageResource(R.drawable.ic_remove_off);
			_hint_button.setImageResource(R.drawable.ic_help_off);
			game_flag = false;
			_ch1.setTag(false);
			_ch2.setTag(false);
			_ch3.setTag(false);
			_ch4.setTag(false);
			_ch5.setTag(false);
			_ch6.setTag(false);
			_ch7.setTag(false);
			_ch8.setTag(false);
			if (_ch1.getText().equals(_correct_answer))
				_ch1.setBackgroundResource(R.drawable.ch_correct_bg);
			else if (_ch2.getText().equals(_correct_answer))
				_ch2.setBackgroundResource(R.drawable.ch_correct_bg);
			else if (_ch3.getText().equals(_correct_answer))
				_ch3.setBackgroundResource(R.drawable.ch_correct_bg);
			else if (_ch4.getText().equals(_correct_answer))
				_ch4.setBackgroundResource(R.drawable.ch_correct_bg);
			else if (_ch5.getText().equals(_correct_answer))
				_ch5.setBackgroundResource(R.drawable.ch_correct_bg);
			else if (_ch6.getText().equals(_correct_answer))
				_ch6.setBackgroundResource(R.drawable.ch_correct_bg);
			else if (_ch7.getText().equals(_correct_answer))
				_ch7.setBackgroundResource(R.drawable.ch_correct_bg);
			else if (_ch8.getText().equals(_correct_answer))
				_ch8.setBackgroundResource(R.drawable.ch_correct_bg);
			DatabasHandler db = new DatabasHandler(getApplicationContext());
			HashMap<String, String> tempData = db.getQuestionData(_levelId);
			db.close();
			int tmpCoins = 0;
			if (_false_num < 1)
				tmpCoins = _COINS_FULL;
			else if (_false_num < 2)
				tmpCoins = _COINS_HALF;
			else if (_false_num < 3)
				tmpCoins = _COINS_LESS;
			showSuccess(
					Integer.parseInt(tempData.get(getResources().getString(
							R.string.KEY_POINT))), tmpCoins);
		} else {
			if (_used_hint == 1) {
				_hint_button.setImageResource(R.drawable.ic_help_off);
			}
			if (_used_freez > 0) {
				long tmpTime = (long) _used_freez;
				LayoutParams l = (LayoutParams) time_bar.getLayoutParams();
				currentTime = tmpTime;
				l.width = (Math
						.min((int) (((double) tmpTime / (double) _TOTAL_TIME) * time_bar_width),
								time_bar_width));
				time_bar.setLayoutParams(l);
				time_bar.setBackgroundColor(Color.GREEN);

			} else {
				long now_time = System.currentTimeMillis();
				if (_start_time > 0) {
					if (now_time - _start_time < _TOTAL_TIME) {
						currentTime = _TOTAL_TIME - (now_time - _start_time);
						runTimer(currentTime);
					} else {
						LayoutParams l = (LayoutParams) time_bar
								.getLayoutParams();
						l.width = 0;
						time_bar.setLayoutParams(l);
						currentTime = 0;
					}
				} else {
					DatabasHandler db = new DatabasHandler(
							getApplicationContext());
					db.setStartTime(_levelId, now_time);
					db.close();
					runTimer(_TOTAL_TIME);
				}
			}
		}
	}

	public void runTimer(long time) {
		currentTime = time;
		counter = new CountDownTimer(time, 10) {

			public void onTick(long milli) {
				LayoutParams l = (LayoutParams) time_bar.getLayoutParams();
				currentTime = milli;
				l.width = (Math
						.min((int) (((double) milli / (double) _TOTAL_TIME) * time_bar_width),
								time_bar_width));
				time_bar.setLayoutParams(l);
			}

			public void onFinish() {
				LayoutParams l = (LayoutParams) time_bar.getLayoutParams();
				l.width = 0;
				time_bar.setLayoutParams(l);
				currentTime = 0;
				_freez_button.setImageResource(R.drawable.ic_freeze_off);
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
		_skip_button.setImageResource(R.drawable.ic_skip_off);
		_freez_button.setImageResource(R.drawable.ic_freeze_off);
		_remove_button.setImageResource(R.drawable.ic_remove_off);
		_hint_button.setImageResource(R.drawable.ic_help_off);
		v.setBackgroundResource(R.drawable.ch_correct_bg);
		int tmpScore = (int) (Math.min(_TOTAL_TIME, currentTime) * 4 / 3000);

		if (_false_num < 1)
			tmpScore += _SCORE_BONUS_FULL;
		else if (_false_num < 2)
			tmpScore += _SCORE_BONUS_FULL;
		else if (_false_num < 3)
			tmpScore += _SCORE_BONUS_LESS;

		if (_packageId == 30) {
			tmpScore *= 8;
		}

		_points += tmpScore;
		_points_view.setText(String.valueOf(_points));

		int tmpCoins = 0;
		if (_false_num < 1)
			tmpCoins = _COINS_FULL;
		else if (_false_num < 2)
			tmpCoins = _COINS_HALF;
		else if (_false_num < 3)
			tmpCoins = _COINS_LESS;

		_coins += tmpCoins;
		_coins_view.setText(String.valueOf(_coins));

		DatabasHandler db = new DatabasHandler(getApplicationContext());
		db.addScore(tmpScore);
		db.addScoreLevel(tmpScore, _levelId);
		db.addCoins(tmpCoins);
		db.updatePackageInfoData(_packageId, tmpScore, tmpCoins);
		db.close();

		showSuccessPage(tmpScore, tmpCoins);
	}

	private void showSuccessPage(int score, int coins) {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		if (_false_num < 1) {
			db.setCorrectAnswer(_packageId, _levelId, true);
			if (_correct_in_raw > 3)
				checkInRawAchievement(_correct_in_raw + 1, db);
		} else
			db.setCorrectAnswer(_packageId, _levelId, false);
		HashMap<String, String> pData = db.getPackageInfo(_packageId);
		checkPackagePointAchievement(pData, db);
		if (_points > 5000)
			checkPointsAchievement(db);
		if (Integer.parseInt(pData.get(getResources().getString(
				R.string.KEY_LEVEL_COMPLETED))) > 23) {

			db.setPackageIsFinish(_packageId);

		}
		SuccessDialog fr = new SuccessDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.setScore(score);
		fr.setCoins(coins);
		fr.show(getSupportFragmentManager(), "Hello");
		db.close();
	}

	public void showSuccess(int score, int coins) {
		SuccessDialog fr = new SuccessDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.setScore(score);
		fr.setCoins(coins);
		fr.show(getSupportFragmentManager(), "Hello");
	}

	private void checkPointsAchievement(DatabasHandler db) {
		HashMap<String, String> data;
		if (_points >= 5000) {
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_POINTS_5000_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_POINTS_5000_ID));
				db.addFreeHint();
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_6_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
		}
		if (_points >= 10000) {
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_POINTS_10000_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_POINTS_10000_ID));
				db.addFreeRemove();
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_7_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
		}
		if (_points >= 20000) {
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_POINTS_20000_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_POINTS_20000_ID));
				db.addFreeFreeze();
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_8_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
		}
		if (_points >= 40000) {
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_POINTS_40000_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_POINTS_40000_ID));
				db.addFreeSkip();
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_9_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
		}
	}

	private void checkPackagePointAchievement(HashMap<String, String> pData,
			DatabasHandler db) {
		HashMap<String, String> data;
		if (Integer.parseInt(pData.get(getResources().getString(
				R.string.KEY_POINT))) >= 2000) {
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_PACkAGE_2000_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_coins += getResources().getInteger(R.integer.ACH_PACkAGE_2000);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_PACkAGE_2000_ID));
				db.addCoins(getResources().getInteger(
						R.integer.ACH_PACkAGE_2000));
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_10_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
		}
		if (Integer.parseInt(pData.get(getResources().getString(
				R.string.KEY_POINT))) >= 2100) {
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_PACkAGE_2100_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_coins += getResources().getInteger(R.integer.ACH_PACkAGE_2100);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_PACkAGE_2100_ID));
				db.addCoins(getResources().getInteger(
						R.integer.ACH_PACkAGE_2100));
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_11_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
		}
		if (Integer.parseInt(pData.get(getResources().getString(
				R.string.KEY_POINT))) >= 2200) {
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_PACkAGE_2200_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_coins += getResources().getInteger(R.integer.ACH_PACkAGE_2200);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_PACkAGE_2200_ID));
				db.addCoins(getResources().getInteger(
						R.integer.ACH_PACkAGE_2200));
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_12_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
		}
		if (Integer.parseInt(pData.get(getResources().getString(
				R.string.KEY_POINT))) >= 2300) {
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_PACkAGE_2300_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_coins += getResources().getInteger(R.integer.ACH_PACkAGE_2300);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_PACkAGE_2300_ID));
				db.addCoins(getResources().getInteger(
						R.integer.ACH_PACkAGE_2300));
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_13_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
		}
	}

	private void checkInRawAchievement(int count, DatabasHandler db) {
		HashMap<String, String> data;
		switch (count) {
		case 5:
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_INRAW_5_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_coins += getResources().getInteger(R.integer.ACH_INRAW_5);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_INRAW_5_ID));
				db.addCoins(getResources().getInteger(R.integer.ACH_INRAW_5));
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_2_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
			break;
		case 10:
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_INRAW_10_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_coins += getResources().getInteger(R.integer.ACH_INRAW_10);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_INRAW_10_ID));
				db.addCoins(getResources().getInteger(R.integer.ACH_INRAW_10));
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_3_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
			break;
		case 20:
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_INRAW_20_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_coins += getResources().getInteger(R.integer.ACH_INRAW_20);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_INRAW_20_ID));
				db.addCoins(getResources().getInteger(R.integer.ACH_INRAW_20));
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_4_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
			break;
		case 50:
			data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_INRAW_50_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_coins += getResources().getInteger(R.integer.ACH_INRAW_50);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_INRAW_50_ID));
				db.addCoins(getResources().getInteger(R.integer.ACH_INRAW_50));
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout
						.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_5_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
			break;
		}
	}

	public void selectFalse(View v, String select) {
		v.setBackgroundResource(R.drawable.ch_error_bg);
		if (counter != null)
			counter.cancel();
		runTimer(Math.max(currentTime - (_TOTAL_TIME / 4), 0));
		if (_falseAnswers == null)
			_falseAnswers = select;
		else
			_falseAnswers = _falseAnswers + "," + select;
		_false_num++;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		db.setFalseAnswer(_levelId, _falseAnswers);
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

	/**
	 * ModifyView set
	 */
	protected void setModifyView(int[] colors, int width, int height) {
		mainImageQuestion.setWillNotDraw(true);

		if (mFilterBitmap != null) {
			mFilterBitmap.recycle();
			mFilterBitmap = null;
		}

		mFilterBitmap = Bitmap.createBitmap(colors, 0, width, width, height,
				Bitmap.Config.ARGB_8888);
		mainImageQuestion.setImageBitmap(mFilterBitmap);

		mainImageQuestion.setWillNotDraw(false);
		mainImageQuestion.postInvalidate();
	}

	@Override
	protected void onDestroy() {
		if (mFilterBitmap != null) {
			mFilterBitmap.recycle();
			mFilterBitmap = null;
		}
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	public void removeCover(View v) {
		ImageView view = (ImageView) v;
		view.setBackground(null);
	}

	public void onSkipClick(View v) {
		if (game_flag) {
			if (_coins > _COIN_SKIP || free_skip) {
				if (counter != null)
					counter.cancel();
				if (_ch1.getText().toString().equals(_correct_answer))
					_ch1.setBackgroundResource(R.drawable.ch_correct_bg);
				else if (_ch2.getText().toString().equals(_correct_answer))
					_ch2.setBackgroundResource(R.drawable.ch_correct_bg);
				else if (_ch3.getText().toString().equals(_correct_answer))
					_ch3.setBackgroundResource(R.drawable.ch_correct_bg);
				else if (_ch4.getText().toString().equals(_correct_answer))
					_ch4.setBackgroundResource(R.drawable.ch_correct_bg);
				else if (_ch5.getText().toString().equals(_correct_answer))
					_ch5.setBackgroundResource(R.drawable.ch_correct_bg);
				else if (_ch6.getText().toString().equals(_correct_answer))
					_ch6.setBackgroundResource(R.drawable.ch_correct_bg);
				else if (_ch7.getText().toString().equals(_correct_answer))
					_ch7.setBackgroundResource(R.drawable.ch_correct_bg);
				else if (_ch8.getText().toString().equals(_correct_answer))
					_ch8.setBackgroundResource(R.drawable.ch_correct_bg);

				_skip_button.setImageResource(R.drawable.ic_skip_off);
				_freez_button.setImageResource(R.drawable.ic_freeze_off);
				_remove_button.setImageResource(R.drawable.ic_remove_off);
				_hint_button.setImageResource(R.drawable.ic_help_off);

				if (!free_skip)
					_coins -= _COIN_SKIP;

				int tmpCoins = 0;
				if (_false_num < 1)
					tmpCoins = _COINS_FULL;
				else if (_false_num < 2)
					tmpCoins = _COINS_HALF;
				else if (_false_num < 3)
					tmpCoins = _COINS_LESS;
				_coins += tmpCoins;
				_coins_view.setText(String.valueOf(_coins));

				int tmpScore = _SCORE_SKIP;
				if (_packageId == 30) {
					tmpScore *= 8;
				}

				DatabasHandler db = new DatabasHandler(getApplicationContext());
				if (free_skip) {
					db.addCoins(tmpCoins);
					db.updatePackageInfoData(_packageId, tmpScore, tmpCoins);
					free_skip = false;
					db.addFreeSkip();
				} else {
					db.addCoins(tmpCoins - _COIN_SKIP);
					db.usedSkip(_packageId);
					db.updatePackageInfoData(_packageId, tmpScore,
							(tmpCoins - _COIN_SKIP));
				}
				db.addScore(tmpScore);
				db.addScoreLevel(tmpScore, _levelId);
				db.close();
				showSuccessPage(tmpScore, tmpCoins);
			}
		}
	}

	public void onRemoveClick(View v) {
		if (game_flag) {
			if (_removedChoices == null) {
				if (_coins > _COIN_REMOVE || free_remove) {
					_remove_button.setImageResource(R.drawable.ic_remove_off);
					List<Button> choicetmp = new ArrayList<Button>();
					choicetmp.add(_ch1);
					choicetmp.add(_ch2);
					choicetmp.add(_ch3);
					choicetmp.add(_ch4);
					choicetmp.add(_ch5);
					choicetmp.add(_ch6);
					choicetmp.add(_ch7);
					choicetmp.add(_ch8);

					for (int j = 0; j < 4; j++) {
						Collections.shuffle(choicetmp);
						for (int i = 0; i < 8; i++) {
							if ((Boolean) choicetmp.get(i).getTag()
									&& !choicetmp.get(i).getText().toString()
											.equals(_correct_answer)) {

								choicetmp.get(i).setTag(false);
								choicetmp.get(i).setVisibility(View.INVISIBLE);

								if (_removedChoices == null)
									_removedChoices = choicetmp.get(i)
											.getText().toString();
								else
									_removedChoices = _removedChoices
											+ ","
											+ choicetmp.get(i).getText()
													.toString();

								break;
							}
						}
					}
					if (!free_remove) {
						_coins -= _COIN_REMOVE;
						_coins_view.setText(String.valueOf(_coins));
					}
					DatabasHandler db = new DatabasHandler(
							getApplicationContext());
					if (free_remove) {
						free_remove = false;
						db.addFreeRemove();
					} else {
						db.addCoins(-_COIN_REMOVE);
						db.updatePackageInfoData(_packageId, 0, -_COIN_REMOVE);
					}
					db.setRemovedChoices(_packageId, _levelId, _removedChoices);
					db.close();
				}
			} else {
				// Toast.makeText(getApplicationContext(),
				// "You can`t use it twice.", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void onFreezClick(View v) {
		if (game_flag) {
			if (_used_freez == 0) {
				if (currentTime > 0) {
					if (_coins > _COIN_FREEZ || free_freeze) {
						if (counter != null)
							counter.cancel();
						time_bar.setBackgroundColor(Color.GREEN);
						_freez_button
								.setImageResource(R.drawable.ic_freeze_off);

						_used_freez = (int) currentTime;
						if (!free_freeze) {
							_coins -= _COIN_FREEZ;
							_coins_view.setText(String.valueOf(_coins));
						}
						DatabasHandler db = new DatabasHandler(
								getApplicationContext());
						if (free_freeze) {
							free_freeze = false;
							db.addFreeFreeze();
						} else {
							db.addCoins(-_COIN_FREEZ);
							db.updatePackageInfoData(_packageId, 0,
									-_COIN_FREEZ);
						}
						db.usedFreez(_packageId, _levelId, currentTime);
						db.close();
					}
				}
			}
		}
	}

	public void onHintClick(View v) {
		if (game_flag
				&& _type != getResources().getInteger(R.integer.TYPE_QUESTION)) {
			if (_used_hint == 0) {
				if (_coins > _COIN_HELP || free_hint) {
					if (!free_hint) {
						_coins -= _COIN_HELP;
						_coins_view.setText(String.valueOf(_coins));
					}
					_hint_button.setImageResource(R.drawable.ic_help_off);
					DatabasHandler db = new DatabasHandler(
							getApplicationContext());
					if (free_hint) {
						free_hint = false;
						db.addFreeHint();
					} else {
						db.addCoins(-_COIN_HELP);
						db.updatePackageInfoData(_packageId, 0, -_COIN_HELP);
					}
					db.usedHint(_packageId, _levelId);
					db.close();
					_used_hint = 1;
					Toast.makeText(getApplicationContext(), _hint_toshow,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), _hint_toshow,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void gotoNextAction() {
		if (__levelData.size() > __position + 1) {
			Intent i = new Intent(QuestionActivity.this, QuestionActivity.class);
			i.putExtra(
					getResources().getString(R.string.KEY_ID),
					__levelData.get(__position + 1).get(
							getResources().getString(R.string.KEY_ID)));
			i.putExtra(getResources().getString(R.string.KEY_PACKAGEID),
					_packageId);
			i.putExtra("position", __position + 1);
			i.putExtra("test", __levelData);
			startActivity(i);
			finish();
		} else
			finish();
	}

	public void onCoinClick(View v) {
		Intent intent = new Intent(QuestionActivity.this, ShopActivity.class);
		startActivity(intent);
	}

	public void backAction() {
		finish();
	}

}