package com.glass.footballquize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.glass.objects.Logo;
import com.glass.objects.Manager;
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

	protected static final int TITLE_TEXT_SIZE = 22;

	protected ImageView mOriginalImageView;
	protected ImageView mModifyImageView;
	protected Bitmap mFilterBitmap;

	private int[] mColors;

	boolean[] filters;

	int _relatedId;
	int _type;
	int _levelId;
	String _falseAnswers;

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

	RelativeLayout time_holder;
	RelativeLayout time_bar;
	int time_bar_width;

	CountDownTimer counter;
	long currentTime;

	boolean game_flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		_relatedId = intent.getIntExtra(
				getResources().getString(R.string.KEY_RELATED), 0);
		_type = intent.getIntExtra(getResources().getString(R.string.KEY_TYPE),
				0);
		_levelId = intent.getIntExtra(
				getResources().getString(R.string.KEY_ID), 0);

		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_falseAnswers = db.getFalseAnswers(_levelId);
		db.close();

		if (_type == getResources().getInteger(R.integer.TYPE_LOGO)) {
			setContentView(R.layout.activity_question);
			setLogo();
		} else if (_type == getResources().getInteger(R.integer.TYPE_MANAGER)) {
			setContentView(R.layout.activity_question_pic);
			setManager();
		} else if (_type == getResources().getInteger(R.integer.TYPE_PLAYER)) {
			setContentView(R.layout.activity_question);
			setPlayer();
		} else if (_type == getResources().getInteger(R.integer.TYPE_QUESTION)) {
			setContentView(R.layout.activity_question);
			setQuestion();
		} else if (_type == getResources().getInteger(R.integer.TYPE_SHIRT)) {
			setContentView(R.layout.activity_question);
			setShirt();
		} else if (_type == getResources().getInteger(R.integer.TYPE_STADIUM)) {
			setContentView(R.layout.activity_question);
			setStadium();
		}

		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_longAnimTime);

		time_bar = (RelativeLayout) findViewById(R.id.q_tima_bar);
		time_holder = (RelativeLayout) findViewById(R.id.q_header);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		time_bar_width = metrics.widthPixels;

		Time now = new Time();
		long time = now.toMillis(true);
		db = new DatabasHandler(getApplicationContext());
		db.setStartTime(_levelId, time);
		db.close();
	}

	private void setStadium() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_stadium = db.getStadium(_relatedId);
		if (_stadium == null)
			finish();
		_ch1 = (Button) findViewById(R.id.q_c_1);
		_ch2 = (Button) findViewById(R.id.q_c_2);
		_ch3 = (Button) findViewById(R.id.q_c_3);
		_ch4 = (Button) findViewById(R.id.q_c_4);
		_ch5 = (Button) findViewById(R.id.q_c_5);
		_ch6 = (Button) findViewById(R.id.q_c_6);
		_ch7 = (Button) findViewById(R.id.q_c_7);
		_ch8 = (Button) findViewById(R.id.q_c_8);
		List<String> tmp = new ArrayList<String>();
		tmp.add(_stadium.get_ch1());
		tmp.add(_stadium.get_ch2());
		tmp.add(_stadium.get_ch3());
		tmp.add(_stadium.get_ch4());
		tmp.add(_stadium.get_ch5());
		tmp.add(_stadium.get_ch6());
		tmp.add(_stadium.get_ch7());
		tmp.add(_stadium.get_answer());
		Collections.shuffle(tmp);
		_ch1.setText(tmp.get(0));
		_ch2.setText(tmp.get(1));
		_ch3.setText(tmp.get(2));
		_ch4.setText(tmp.get(3));
		_ch5.setText(tmp.get(4));
		_ch6.setText(tmp.get(5));
		_ch7.setText(tmp.get(6));
		_ch8.setText(tmp.get(7));
		_ch1.setTag(true);
		_ch2.setTag(true);
		_ch3.setTag(true);
		_ch4.setTag(true);
		_ch5.setTag(true);
		_ch6.setTag(true);
		_ch7.setTag(true);
		_ch8.setTag(true);
		mModifyImageView = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Stadium/" + _stadium.get_imagePath()));
			mModifyImageView.setImageBitmap(bitmap);
			mModifyImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					zoomImageFromThumb(mModifyImageView, bitmap);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// finish();
		}

		runTimer(20400);
	}

	private void setShirt() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_shirt = db.getShirt(_relatedId);
		_logo = db.getLogo(_shirt.get_logoId());
		if (_shirt == null)
			finish();
		_ch1 = (Button) findViewById(R.id.q_c_1);
		_ch2 = (Button) findViewById(R.id.q_c_2);
		_ch3 = (Button) findViewById(R.id.q_c_3);
		_ch4 = (Button) findViewById(R.id.q_c_4);
		_ch5 = (Button) findViewById(R.id.q_c_5);
		_ch6 = (Button) findViewById(R.id.q_c_6);
		_ch7 = (Button) findViewById(R.id.q_c_7);
		_ch8 = (Button) findViewById(R.id.q_c_8);
		List<String> tmp = new ArrayList<String>();
		tmp.add(_logo.get_ch1());
		tmp.add(_logo.get_ch2());
		tmp.add(_logo.get_ch3());
		tmp.add(_logo.get_ch4());
		tmp.add(_logo.get_ch5());
		tmp.add(_logo.get_ch6());
		tmp.add(_logo.get_ch7());
		tmp.add(_logo.get_nameFa());
		Collections.shuffle(tmp);
		_ch1.setText(tmp.get(0));
		_ch2.setText(tmp.get(1));
		_ch3.setText(tmp.get(2));
		_ch4.setText(tmp.get(3));
		_ch5.setText(tmp.get(4));
		_ch6.setText(tmp.get(5));
		_ch7.setText(tmp.get(6));
		_ch8.setText(tmp.get(7));
		_ch1.setTag(true);
		_ch2.setTag(true);
		_ch3.setTag(true);
		_ch4.setTag(true);
		_ch5.setTag(true);
		_ch6.setTag(true);
		_ch7.setTag(true);
		_ch8.setTag(true);
		mModifyImageView = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Shirt/" + _shirt.get_imagePath()));
			mModifyImageView.setImageBitmap(bitmap);
			mModifyImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					zoomImageFromThumb(mModifyImageView, bitmap);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// finish();
		}

		runTimer(20400);
	}

	private void setQuestion() {
		// TODO Auto-generated method stub

	}

	private void setPlayer() {
		// TODO Auto-generated method stub

	}

	private void setManager() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_manager = db.getManager(_relatedId);
		if (_manager == null)
			finish();
		_ch1 = (Button) findViewById(R.id.q_c_1);
		_ch2 = (Button) findViewById(R.id.q_c_2);
		_ch3 = (Button) findViewById(R.id.q_c_3);
		_ch4 = (Button) findViewById(R.id.q_c_4);
		_ch5 = (Button) findViewById(R.id.q_c_5);
		_ch6 = (Button) findViewById(R.id.q_c_6);
		_ch7 = (Button) findViewById(R.id.q_c_7);
		_ch8 = (Button) findViewById(R.id.q_c_8);
		List<String> tmp = new ArrayList<String>();
		tmp.add(_manager.get_ch1());
		tmp.add(_manager.get_ch2());
		tmp.add(_manager.get_ch3());
		tmp.add(_manager.get_ch4());
		tmp.add(_manager.get_ch5());
		tmp.add(_manager.get_ch6());
		tmp.add(_manager.get_ch7());
		tmp.add(_manager.get_nameFa());
		Collections.shuffle(tmp);
		_ch1.setText(tmp.get(0));
		_ch2.setText(tmp.get(1));
		_ch3.setText(tmp.get(2));
		_ch4.setText(tmp.get(3));
		_ch5.setText(tmp.get(4));
		_ch6.setText(tmp.get(5));
		_ch7.setText(tmp.get(6));
		_ch8.setText(tmp.get(7));
		_ch1.setTag(true);
		_ch2.setTag(true);
		_ch3.setTag(true);
		_ch4.setTag(true);
		_ch5.setTag(true);
		_ch6.setTag(true);
		_ch7.setTag(true);
		_ch8.setTag(true);
		mModifyImageView = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Manager/" + _manager.get_imagePath()));
			mModifyImageView.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// finish();
		}

		runTimer(20400);
	}

	public void setLogo() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		_logo = db.getLogo(_relatedId);
		if (_logo == null)
			finish();
		_ch1 = (Button) findViewById(R.id.q_c_1);
		_ch2 = (Button) findViewById(R.id.q_c_2);
		_ch3 = (Button) findViewById(R.id.q_c_3);
		_ch4 = (Button) findViewById(R.id.q_c_4);
		_ch5 = (Button) findViewById(R.id.q_c_5);
		_ch6 = (Button) findViewById(R.id.q_c_6);
		_ch7 = (Button) findViewById(R.id.q_c_7);
		_ch8 = (Button) findViewById(R.id.q_c_8);
		List<String> tmp = new ArrayList<String>();
		tmp.add(_logo.get_ch1());
		tmp.add(_logo.get_ch2());
		tmp.add(_logo.get_ch3());
		tmp.add(_logo.get_ch4());
		tmp.add(_logo.get_ch5());
		tmp.add(_logo.get_ch6());
		tmp.add(_logo.get_ch7());
		tmp.add(_logo.get_nameFa());
		Collections.shuffle(tmp);
		_ch1.setText(tmp.get(0));
		_ch2.setText(tmp.get(1));
		_ch3.setText(tmp.get(2));
		_ch4.setText(tmp.get(3));
		_ch5.setText(tmp.get(4));
		_ch6.setText(tmp.get(5));
		_ch7.setText(tmp.get(6));
		_ch8.setText(tmp.get(7));
		_ch1.setTag(true);
		_ch2.setTag(true);
		_ch3.setTag(true);
		_ch4.setTag(true);
		_ch5.setTag(true);
		_ch6.setTag(true);
		_ch7.setTag(true);
		_ch8.setTag(true);
		mModifyImageView = (ImageView) findViewById(R.id.q_main_image);
		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(
					"Logo/" + _logo.get_imagePath()));
			mModifyImageView.setImageBitmap(bitmap);
			mModifyImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					zoomImageFromThumb(mModifyImageView, bitmap);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// finish();
		}
		if (_falseAnswers != null && _falseAnswers.length() > 0) {
			String[] parts = _falseAnswers.split(",");
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

		runTimer(20400);
	}

	public void runTimer(long time) {
		counter = new CountDownTimer(time, 10) {

			public void onTick(long milli) {

				LayoutParams l = (LayoutParams) time_bar.getLayoutParams();
				// currentTime = (int) ((double) milli / (double) 1000);
				currentTime = milli;
				l.width = (Math
						.min((int) (((double) milli / (double) 20000) * time_bar_width),
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
			if (_type == getResources().getInteger(R.integer.TYPE_LOGO)) {
				if (select.equals(_logo.get_nameFa())) {
					counter.cancel();
					game_flag = false;
					v.setBackgroundColor(Color.GREEN);
					SuccessDialog fr = new SuccessDialog();
					fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
					fr.setScore((int) Math.round((double) currentTime
							/ (double) 100));
					fr.show(getSupportFragmentManager(), "Hello");
					DatabasHandler db = new DatabasHandler(
							getApplicationContext());
					db.setCorrectAnswer(_levelId);
					db.close();
				} else {
					v.setBackgroundColor(Color.RED);
					counter.cancel();
					runTimer(Math.max(currentTime - 2000, 0));
					DatabasHandler db = new DatabasHandler(
							getApplicationContext());
					if (_falseAnswers == null)
						_falseAnswers = select;
					else
						_falseAnswers = _falseAnswers + "," + select;
					db.setFalseAnswer(_levelId, _falseAnswers);
					db.close();
				}
			} else if (_type == getResources().getInteger(
					R.integer.TYPE_MANAGER)) {
				if (select.equals(_manager.get_nameFa())) {
					counter.cancel();
					game_flag = false;
					v.setBackgroundColor(Color.GREEN);
					SuccessDialog fr = new SuccessDialog();
					fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
					fr.setScore((int) Math.round((double) currentTime
							/ (double) 100));
					fr.show(getSupportFragmentManager(), "Hello");
				} else {
					v.setBackgroundColor(Color.RED);
					counter.cancel();
					runTimer(Math.max(currentTime - 2000, 0));
				}
			} else if (_type == getResources()
					.getInteger(R.integer.TYPE_PLAYER)) {
			} else if (_type == getResources().getInteger(
					R.integer.TYPE_QUESTION)) {
			} else if (_type == getResources().getInteger(R.integer.TYPE_SHIRT)) {
				if (select.equals(_logo.get_nameFa())) {
					counter.cancel();
					game_flag = false;
					v.setBackgroundColor(Color.GREEN);
					SuccessDialog fr = new SuccessDialog();
					fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
					fr.setScore((int) Math.round((double) currentTime
							/ (double) 100));
					fr.show(getSupportFragmentManager(), "Hello");
				} else {
					v.setBackgroundColor(Color.RED);
					counter.cancel();
					runTimer(Math.max(currentTime - 2000, 0));
				}
			} else if (_type == getResources().getInteger(
					R.integer.TYPE_STADIUM)) {
				if (select.equals(_stadium.get_answer())) {
					counter.cancel();
					game_flag = false;
					v.setBackgroundColor(Color.GREEN);
					SuccessDialog fr = new SuccessDialog();
					fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
					fr.setScore((int) Math.round((double) currentTime
							/ (double) 100));
					fr.show(getSupportFragmentManager(), "Hello");
				} else {
					v.setBackgroundColor(Color.RED);
					counter.cancel();
					runTimer(Math.max(currentTime - 2000, 0));
				}
			}
		}
	}

	public void sssss() {
		filters = new boolean[6];

		// mOriginalImageView = (ImageView)
		// findViewById(R.id.q_main_image_null);
		mModifyImageView = (ImageView) findViewById(R.id.q_main_image);

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
		mModifyImageView.setWillNotDraw(true);

		if (mFilterBitmap != null) {
			mFilterBitmap.recycle();
			mFilterBitmap = null;
		}

		mFilterBitmap = Bitmap.createBitmap(colors, 0, width, width, height,
				Bitmap.Config.ARGB_8888);
		mModifyImageView.setImageBitmap(mFilterBitmap);

		mModifyImageView.setWillNotDraw(false);
		mModifyImageView.postInvalidate();
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
}