package com.glass.footballquize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glass.objects.Logo;
import com.glass.objects.Manager;
import com.glass.objects.Player;
import com.glass.objects.Question;
import com.glass.objects.Shirt;
import com.glass.objects.Stadium;
import com.glass.utils.AndroidUtils;
import com.glass.utils.DatabasHandler;
import com.glass.utils.DiffuseFilter;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class QuestionActivity extends FragmentActivity {

	// Hold a reference to the current animator,
	// so that it can be canceled mid-way.
	private Animator mCurrentAnimator;

	// The system "short" animation time duration, in milliseconds. This
	// duration is ideal for subtle animations or animations that occur
	// very frequently.
	private int mShortAnimationDuration;

	protected ImageView mOriginalImageView;
	protected ImageView mainImageQuestion;
	protected TextView mainTextQuestion;
	protected Bitmap mFilterBitmap;

	private int[] mColors;

	boolean[] filters;

	private int _points;
	private int _coins;

	TextView _points_view;
	TextView _coins_view;

	ImageButton _skip_button;
	ImageButton _freez_button;
	ImageButton _remove_button;
	ImageButton _hint_button;

	int _levelId;
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
	TextView time_text;
	int time_bar_width;

	CountDownTimer counter;
	long currentTime;

	boolean game_flag = true;

	private String _correct_answer;
	private String _hint_toshow;

	final long _TOTAL_TIME = 80000;
	final long _BONUS_TIME = 10000;
	final int _COIN_SKIP = 90;
	final int _COIN_FREEZ = 45;
	final int _COIN_REMOVE = 20;
	final int _COIN_HELP = 15;

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
		db.close();

		_points = gameData.get(getResources().getString(R.string.KEY_POINT));
		_coins = gameData.get(getResources().getString(R.string.KEY_COINS));

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

		_points_view = (TextView) findViewById(R.id.question_header_points);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		time_bar = (RelativeLayout) findViewById(R.id.q_tima_bar);
		time_text = (TextView) findViewById(R.id.q_time_text);

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

		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_longAnimTime);
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
			mainImageQuestion.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					zoomImageFromThumb(mainImageQuestion, bitmap);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}

		setChoicesHistory();

		// TODO generate text
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
			mainImageQuestion.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					zoomImageFromThumb(mainImageQuestion, bitmap);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}

		setChoicesHistory();

		// TODO generate text
		_hint_toshow = _logo.get_hint();
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
		_hint_button.setVisibility(View.INVISIBLE);
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

		// TODO generate text
		_hint_toshow = _player.get_nationality() + " -- "
				+ _player.get_playNum();
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

		// TODO generate text
		_hint_toshow = _manager.get_team() + " -- " + _manager.get_year();
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
			mainImageQuestion.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					zoomImageFromThumb(mainImageQuestion, bitmap);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}
		setChoicesHistory();

		// TODO generate text
		_hint_toshow = _logo.get_hint();
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
					_ch1.setBackgroundColor(Color.RED);
					_ch1.setTag(false);
				} else if (_ch2.getText().equals(parts[i])) {
					_ch2.setBackgroundColor(Color.RED);
					_ch2.setTag(false);
				} else if (_ch3.getText().equals(parts[i])) {
					_ch3.setBackgroundColor(Color.RED);
					_ch3.setTag(false);
				} else if (_ch4.getText().equals(parts[i])) {
					_ch4.setBackgroundColor(Color.RED);
					_ch4.setTag(false);
				} else if (_ch5.getText().equals(parts[i])) {
					_ch5.setBackgroundColor(Color.RED);
					_ch5.setTag(false);
				} else if (_ch6.getText().equals(parts[i])) {
					_ch6.setBackgroundColor(Color.RED);
					_ch6.setTag(false);
				} else if (_ch7.getText().equals(parts[i])) {
					_ch7.setBackgroundColor(Color.RED);
					_ch7.setTag(false);
				} else if (_ch8.getText().equals(parts[i])) {
					_ch8.setBackgroundColor(Color.RED);
					_ch8.setTag(false);
				}
			}
		}
		if (_removedChoices != null && _removedChoices.length() > 0) {
			_remove_button.setVisibility(View.INVISIBLE);
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
			_skip_button.setVisibility(View.INVISIBLE);
			_freez_button.setVisibility(View.INVISIBLE);
			_remove_button.setVisibility(View.INVISIBLE);
			_hint_button.setVisibility(View.INVISIBLE);
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
				_ch1.setBackgroundColor(Color.GREEN);
			else if (_ch2.getText().equals(_correct_answer))
				_ch2.setBackgroundColor(Color.GREEN);
			else if (_ch3.getText().equals(_correct_answer))
				_ch3.setBackgroundColor(Color.GREEN);
			else if (_ch4.getText().equals(_correct_answer))
				_ch4.setBackgroundColor(Color.GREEN);
			else if (_ch5.getText().equals(_correct_answer))
				_ch5.setBackgroundColor(Color.GREEN);
			else if (_ch6.getText().equals(_correct_answer))
				_ch6.setBackgroundColor(Color.GREEN);
			else if (_ch7.getText().equals(_correct_answer))
				_ch7.setBackgroundColor(Color.GREEN);
			else if (_ch8.getText().equals(_correct_answer))
				_ch8.setBackgroundColor(Color.GREEN);
		} else {
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
					runTimer(_TOTAL_TIME + _BONUS_TIME);
				}
			}
		}
	}

	public void runTimer(long time) {
		currentTime = time;
		counter = new CountDownTimer(time, 10) {

			public void onTick(long milli) {
				if (milli < _TOTAL_TIME) {
					LayoutParams l = (LayoutParams) time_bar.getLayoutParams();
					currentTime = milli;
					l.width = (Math
							.min((int) (((double) milli / (double) _TOTAL_TIME) * time_bar_width),
									time_bar_width));
					time_bar.setLayoutParams(l);
					time_text.setText((int) (milli / 1000) + "");
				} else {
					time_text
							.setText((int) ((milli - _TOTAL_TIME) / 1000) + "");
				}
				if (milli < _TOTAL_TIME / 4)
					time_bar.setBackgroundColor(Color.RED);
				else if (milli < _TOTAL_TIME / 4 * 2)
					time_bar.setBackgroundColor(Color.YELLOW);
				else if (milli < _TOTAL_TIME / 4 * 3)
					time_bar.setBackgroundColor(Color.CYAN);
				else
					time_bar.setBackgroundColor(Color.GREEN);
			}

			public void onFinish() {
				LayoutParams l = (LayoutParams) time_bar.getLayoutParams();
				l.width = 0;
				time_bar.setLayoutParams(l);
				currentTime = 0;
				_freez_button.setVisibility(View.INVISIBLE);
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
		_skip_button.setVisibility(View.INVISIBLE);
		_freez_button.setVisibility(View.INVISIBLE);
		_remove_button.setVisibility(View.INVISIBLE);
		_hint_button.setVisibility(View.INVISIBLE);
		v.setBackgroundColor(Color.GREEN);
		// TODO set score
		int tmpScore = (int) (Math.min(_TOTAL_TIME, currentTime) / 1000);

		if (_false_num < 1)
			tmpScore += _SCORE_BONUS_FULL;
		else if (_false_num < 2)
			tmpScore += _SCORE_BONUS_FULL;
		else if (_false_num < 3)
			tmpScore += _SCORE_BONUS_LESS;

		_points += tmpScore;
		_points_view.setText(String.valueOf(_points));
		// TODO set coins
		int tmpCoins = 0;
		if (_false_num < 1)
			tmpCoins = _COINS_FULL;
		else if (_false_num < 2)
			tmpCoins = _COINS_HALF;
		else if (_false_num < 4)
			tmpCoins = _COINS_LESS;

		_coins += tmpCoins;
		_coins_view.setText(String.valueOf(_coins));

		DatabasHandler db = new DatabasHandler(getApplicationContext());
		db.setCorrectAnswer(_levelId);
		db.addScore(tmpScore);
		db.addCoins(tmpCoins);
		db.close();

		showSuccessPage(tmpScore, tmpCoins);
	}

	private void showSuccessPage(int score, int coins) {
		SuccessDialog fr = new SuccessDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.setScore(score);
		fr.setCoins(coins);
		fr.show(getSupportFragmentManager(), "Hello");
	}

	public void selectFalse(View v, String select) {
		v.setBackgroundColor(Color.RED);
		counter.cancel();
		runTimer(Math.max(currentTime - ((_TOTAL_TIME + _BONUS_TIME) / 4), 0));
		if (_falseAnswers == null)
			_falseAnswers = select;
		else
			_falseAnswers = _falseAnswers + "," + select;
		_false_num++;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		db.setFalseAnswer(_levelId, _falseAnswers);
		db.close();
	}

	public void sssss() {
		filters = new boolean[6];

		// mOriginalImageView = (ImageView)
		// findViewById(R.id.q_main_image_null);
		mainImageQuestion = (ImageView) findViewById(R.id.q_main_image);

		mOriginalImageView.setImageResource(R.drawable.button);

		final int width = mOriginalImageView.getDrawable().getIntrinsicWidth();
		final int height = mOriginalImageView.getDrawable()
				.getIntrinsicHeight();

		mColors = AndroidUtils.drawableToIntArray(mOriginalImageView
				.getDrawable());

		Thread thread = new Thread() {
			public void run() {
				DiffuseFilter filter = new DiffuseFilter();
				filter.setScale(16);
				filters[0] = true;
				filters[3] = true;
				filters[5] = true;
				mColors = filter.filter(mColors, width, height, filters);

				QuestionActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setModifyView(mColors, width, height);
					}
				});
			}
		};
		thread.setDaemon(true);
		thread.start();
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

	private void zoomImageFromThumb(final View thumbView, Bitmap imageResId) {
		// If there's an animation in progress, cancel it
		// immediately and proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		// Load the high-resolution "zoomed-in" image.
		final ImageView expandedImageView = (ImageView) findViewById(R.id.question_expanded_image);
		expandedImageView.setImageBitmap(imageResId);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the final bounds are the global visible rectangle of the
		// container
		// view. Also set the container view's offset as the origin for the
		// bounds, since that's the origin for the positioning animation
		// properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		((View) expandedImageView.getParent()).getGlobalVisibleRect(
				finalBounds, globalOffset);
		// startBounds.offset(-globalOffset.x, -globalOffset.y);
		// finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the "center crop" technique. This prevents undesirable
		// stretching during the animation. Also calculate the start scaling
		// factor (the end scaling factor is always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			// startScale = 0.5f;
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}
		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins, it will position the zoomed-in view in the place of the
		// thumbnail.
		thumbView.setAlpha(0f);
		expandedImageView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations
		// to the top-left corner of the zoomed-in view (the default
		// is the center of the view).
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(expandedImageView, View.X,
						startBounds.left, finalBounds.left))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
						finalBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
						startScale, 1f))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
						startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		System.out.println();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down
		// to the original bounds and show the thumbnail instead of
		// the expanded image.
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}

				// Animate the four positioning/sizing properties in parallel,
				// back to their original values.
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(expandedImageView, View.X,
								startBounds.left))
						.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
								finalBounds.top))
						.with(ObjectAnimator.ofFloat(expandedImageView,
								View.SCALE_X, startScaleFinal))
						.with(ObjectAnimator.ofFloat(expandedImageView,
								View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}

	public void onSkipClick(View v) {
		if (game_flag) {
			if (_coins > _COIN_SKIP) {
				counter.cancel();
				if (_ch1.getText().toString().equals(_correct_answer))
					_ch1.setBackgroundColor(Color.GREEN);
				else if (_ch2.getText().toString().equals(_correct_answer))
					_ch2.setBackgroundColor(Color.GREEN);
				else if (_ch3.getText().toString().equals(_correct_answer))
					_ch3.setBackgroundColor(Color.GREEN);
				else if (_ch4.getText().toString().equals(_correct_answer))
					_ch4.setBackgroundColor(Color.GREEN);
				else if (_ch5.getText().toString().equals(_correct_answer))
					_ch5.setBackgroundColor(Color.GREEN);
				else if (_ch6.getText().toString().equals(_correct_answer))
					_ch6.setBackgroundColor(Color.GREEN);
				else if (_ch7.getText().toString().equals(_correct_answer))
					_ch7.setBackgroundColor(Color.GREEN);
				else if (_ch8.getText().toString().equals(_correct_answer))
					_ch8.setBackgroundColor(Color.GREEN);
				DatabasHandler db = new DatabasHandler(getApplicationContext());
				db.minusCoins(_COIN_SKIP);
				db.addScore(_SCORE_SKIP);
				db.close();
				_coins -= _COIN_SKIP;
				int tmpCoins = 0;
				if (_false_num < 1)
					tmpCoins = _COINS_FULL;
				else if (_false_num < 2)
					tmpCoins = _COINS_HALF;
				else if (_false_num < 4)
					tmpCoins = _COINS_LESS;
				_coins += tmpCoins;
				_coins_view.setText(String.valueOf(_coins));
				// TODO
				showSuccessPage(_SCORE_SKIP, tmpCoins);
			} else
				Toast.makeText(getApplicationContext(),
						"You don`t have enough coins.", Toast.LENGTH_LONG)
						.show();
		} else
			Toast.makeText(getApplicationContext(), "Really needs help??",
					Toast.LENGTH_LONG).show();
	}

	public void onRemoveClick(View v) {
		if (game_flag) {
			if (_removedChoices == null) {
				if (_coins > _COIN_REMOVE) {
					_remove_button.setVisibility(View.INVISIBLE);
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
					DatabasHandler db = new DatabasHandler(
							getApplicationContext());
					db.minusCoins(_COIN_REMOVE);
					db.setRemovedChoices(_levelId, _removedChoices);
					db.close();
					_coins -= _COIN_REMOVE;
					_coins_view.setText(String.valueOf(_coins));
				} else
					Toast.makeText(getApplicationContext(),
							"You don`t have enough coins.", Toast.LENGTH_LONG)
							.show();
			} else {
				Toast.makeText(getApplicationContext(),
						"You can`t use it twice.", Toast.LENGTH_LONG)
						.show();
			}
		} else
			Toast.makeText(getApplicationContext(), "Really needs help??",
					Toast.LENGTH_LONG).show();
	}

	public void onFreezClick(View v) {
		if (game_flag) {
			if (_used_freez == 0) {
				if (currentTime > 0) {
					if (_coins > _COIN_FREEZ) {
						counter.cancel();
						time_bar.setBackgroundColor(Color.GREEN);

						DatabasHandler db = new DatabasHandler(
								getApplicationContext());
						db.minusCoins(_COIN_FREEZ);
						db.usedFreez(_levelId, currentTime);
						db.close();
						_used_freez = (int) Math.min(currentTime, _TOTAL_TIME);
						_coins -= _COIN_FREEZ;
						_coins_view.setText(String.valueOf(_coins));
					} else
						Toast.makeText(getApplicationContext(),
								"You don`t have enough coins.",
								Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(getApplicationContext(),
							"You are out of time.", Toast.LENGTH_LONG).show();
			} else
				Toast.makeText(getApplicationContext(),
						"You already freezed time.", Toast.LENGTH_LONG).show();
		} else
			Toast.makeText(getApplicationContext(), "Really needs help??",
					Toast.LENGTH_LONG).show();
	}

	public void onHintClick(View v) {
		if (game_flag) {
			if (_used_hint == 0) {
				if (_coins > _COIN_HELP) {
					DatabasHandler db = new DatabasHandler(
							getApplicationContext());
					db.minusCoins(_COIN_HELP);
					db.usedHint(_levelId);
					db.close();
					_coins -= _COIN_HELP;
					_used_hint = 1;
					_coins_view.setText(String.valueOf(_coins));
					Toast.makeText(getApplicationContext(), _hint_toshow,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(getApplicationContext(),
							"You don`t have enough coins.", Toast.LENGTH_LONG)
							.show();
			} else
				Toast.makeText(getApplicationContext(), _hint_toshow,
						Toast.LENGTH_LONG).show();
		} else
			Toast.makeText(getApplicationContext(), "Really needs help??",
					Toast.LENGTH_LONG).show();
	}

	public void gotoNextAction() {
		if (__levelData.size() > __position + 1) {
			Intent i = new Intent(QuestionActivity.this, QuestionActivity.class);
			i.putExtra(
					getResources().getString(R.string.KEY_ID),
					__levelData.get(__position + 1).get(
							getResources().getString(R.string.KEY_ID)));
			i.putExtra("position", __position + 1);
			i.putExtra("test", __levelData);
			startActivity(i);
			finish();
		} else
			finish();
	}

	public void backAction() {
		finish();
	}
}