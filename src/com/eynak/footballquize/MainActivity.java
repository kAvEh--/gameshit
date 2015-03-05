package com.eynak.footballquize;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.eynak.utils.DatabasHandler;
import com.eynak.utils.SendDatatoServer;
import com.eynak.utils.ServerUtilities;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends FragmentActivity {

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_LEVELS = 31;
	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	PagerAdapter adapter;

	PagerContainer mContainer;

	TextView _points_view;
	TextView _coins_view;

	TextView _main_to_finale;
	TextView _main_to_finale_2;

	private int _points;
	private int _coins;

	private int _last_level;

	DisplayMetrics metrics;
	float image_margin_px;

	int _help_count;
	Typeface face;

	// Google project id
	static final String SENDER_ID = "367325912027";

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		face = Typeface.createFromAsset(getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");

		_points_view = (TextView) findViewById(R.id.question_header_points);
		_points_view.setTypeface(face);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_coins_view.setTypeface(face);

		_main_to_finale = (TextView) findViewById(R.id.main_finale_text);
		_main_to_finale.setTypeface(face);
		_main_to_finale_2 = (TextView) findViewById(R.id.main_finale_text_2);
		_main_to_finale_2.setTypeface(face);

		ViewPager myVP = (ViewPager) findViewById(R.id.mainViewPager);
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels * 70 / 100;
		int height = metrics.heightPixels * 30 / 100;

		FrameLayout.LayoutParams tmpParams = new FrameLayout.LayoutParams(
				width, height);
		tmpParams.gravity = Gravity.CENTER;

		myVP.setLayoutParams(tmpParams);

		initialize();

		if (_help_count < 2) {
			HelpDialog fr = new HelpDialog();
			fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
			fr.setType(1);
			fr.show(getSupportFragmentManager(), "Hello");

			DatabasHandler db = new DatabasHandler(getApplicationContext());
			db = new DatabasHandler(getApplicationContext());
			db.addShownCount();
			db.close();
		}

		mContainer = (PagerContainer) findViewById(R.id.pager_container);

		mPager = mContainer.getViewPager();
		adapter = new MyPagerAdapter();
		mPager.setAdapter(adapter);
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		// pager.setCurrentItem(2);
		// Necessary or the pager will only have one extra page to show
		// make this at least however many pages you can see
		mPager.setOffscreenPageLimit(2);
		// A little space between pages
		mPager.setPageMargin(15);

		// If hardware acceleration is enabled, you should also remove
		// clipping on the pager for its children.
		mPager.setClipChildren(false);

		// set page to last page unlocked
		mPager.setCurrentItem(_last_level - 1);

		SendDatatoServer tmp = new SendDatatoServer(this);
		tmp.checkUser();

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		// registerReceiver(mHandleMessageReceiver, new IntentFilter(
		// DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	private void initialize() {
		HashMap<String, Integer> gameData;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		gameData = db.getGameData();
		db.close();

		_help_count = gameData.get("helpShownCount");

		_points = gameData.get(getResources().getString(R.string.KEY_POINT));
		_coins = gameData.get(getResources().getString(R.string.KEY_COINS));
		_last_level = gameData.get(getResources().getString(
				R.string.KEY_LAST_P_UNLOCK));

		if (_points >= getResources().getInteger(R.integer.FINALE_SCORE)) {
			_main_to_finale.setVisibility(View.INVISIBLE);
			_main_to_finale_2.setVisibility(View.INVISIBLE);
		} else {
			_main_to_finale.setText((getResources().getInteger(
					R.integer.FINALE_SCORE) - _points)
					+ " " + getResources().getString(R.string.stat_score));
		}

		_points_view.setText(String.valueOf(_points));
		_coins_view.setText(String.valueOf(_coins));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.8f;
		private static final float MIN_ALPHA = 0.5f;

		Resources r = getResources();
		float dp_10_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				10, r.getDisplayMetrics());
		float dp_15_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				15, r.getDisplayMetrics());
		float dp_20_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				20, r.getDisplayMetrics());

		LayoutParams params = new LayoutParams((int) dp_20_px, (int) dp_20_px);

		@SuppressLint("NewApi")
		public void transformPage(View view, float position) {
			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(MIN_ALPHA);
				view.setScaleY(MIN_SCALE);
				params.setMargins((int) (metrics.widthPixels / 30 * mPager
						.getCurrentItem()), (int) dp_10_px, 0, (int) dp_15_px);
				params.addRule(RelativeLayout.BELOW, R.id.pager_container);

			} else if (position <= 1) { // [-1,1]
				// Modify the default slide transition to shrink the page as
				// well
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				view.setScaleY(scaleFactor);

				// Fade the page relative to its size.
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
						/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));

				params.setMargins(
						(int) (metrics.widthPixels
								/ 30
								* mPager.getCurrentItem()
								+ ((1 - Math.abs(position))
										* metrics.widthPixels / 60) + dp_15_px),
						(int) dp_10_px, 0, (int) dp_15_px);
				params.addRule(RelativeLayout.BELOW, R.id.pager_container);

			} else {
				view.setAlpha(MIN_ALPHA);
				view.setScaleY(MIN_SCALE);
			}
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@SuppressLint({ "NewApi", "InflateParams" })
		@Override
		public Object instantiateItem(View pager, int position) {

			View view;
			LayoutInflater inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.level_layout, null);

			ImageView bg = (ImageView) view.findViewById(R.id.package_bg);

			ImageView lockIcon = (ImageView) view
					.findViewById(R.id.level_main_lock);
			TextView tv = (TextView) view.findViewById(R.id.package_title);
			tv.setTypeface(face);
			TextView score_tv = (TextView) view
					.findViewById(R.id.package_score);
			score_tv.setTypeface(face);

			DatabasHandler db = new DatabasHandler(getApplicationContext());
			HashMap<String, String> data = db.getPackageInfo(position + 1);
			db.close();

			if (position <= 30)
				if (position < _last_level)
					score_tv.setText(data.get("Point") + " امتیاز");

			if (position == 30)
				tv.setVisibility(View.INVISIBLE);
			else
				tv.setText("مرحله " + (position + 1));

			ImageButton stat = (ImageButton) view
					.findViewById(R.id.package_stat);

			stat.setTag(position + 1);
			stat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onStatClick(v);
				}
			});

			ImageView _8x = (ImageView) view.findViewById(R.id.package_8x);

			view.setTag(position);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int stage = (Integer) v.getTag();

					if (stage == 30) {
						if (_points >= getResources().getInteger(
								R.integer.FINALE_SCORE)) {
							DatabasHandler db = new DatabasHandler(
									getApplicationContext());
							HashMap<String, String> userData = db.getUser();
							db.close();
							if (userData.get(getResources().getString(
									R.string.KEY_NAME)) != null) {
								Intent intent = new Intent(MainActivity.this,
										FinaleLevelActivity.class);
								startActivity(intent);
							} else {
								onUserClick(null);
							}
						} else {
							// TODO
						}
					} else if (stage < _last_level) {
						Intent i = new Intent(MainActivity.this,
								LevelActivity.class);
						i.putExtra("level", (stage + 1));
						startActivity(i);
					} else if (stage == _last_level) {
						UnlockDialog fr = new UnlockDialog();
						fr.setStyle(DialogFragment.STYLE_NO_TITLE,
								R.style.MyDialog);
						fr.setLevel(stage + 1);
						fr.setCoins(_coins);
						fr.show(getSupportFragmentManager(), "Hello");
					} else {
						// Toast.makeText(getApplicationContext(),
						// "You don`t have access to this level.",
						// Toast.LENGTH_LONG).show();
					}
				}
			});
			if (position == 30) {
				if (_points < getResources().getInteger(R.integer.FINALE_SCORE)) {
					bg.setImageResource(R.drawable.finale_lock_bg);
					tv.setTextColor(getResources().getColor(R.color.black));
				} else {
					bg.setImageResource(R.drawable.finale_unlock_bg);
					tv.setTextColor(getResources().getColor(R.color.ajori));
				}
				stat.setVisibility(View.INVISIBLE);
				lockIcon.setVisibility(View.INVISIBLE);
			} else if (position < _last_level) {
				bg.setImageResource(R.drawable.package_unlock_bg);
				tv.setTextColor(getResources().getColor(R.color.ajori));
				lockIcon.setVisibility(View.INVISIBLE);
				stat.setImageResource(R.drawable.ic_stat_on);
			} else {
				bg.setImageResource(R.drawable.package_lock_bg);
				tv.setTextColor(getResources().getColor(R.color.black));
				lockIcon.setVisibility(View.VISIBLE);
				stat.setImageResource(R.drawable.ic_stat_off);
			}
			if (position == 29) {
				_8x.setVisibility(View.VISIBLE);
			}
			((ViewPager) pager).addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return NUM_LEVELS;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}
	}

	public void onStatClick(View v) {
		int level = (Integer) v.getTag();
		if (level <= _last_level) {
			StatDialog fr = new StatDialog();
			fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog2);
			fr.setLevel(level);
			fr.show(getSupportFragmentManager(), "Hello");
		}
	}

	public void onShopClick(View v) {
		Intent intent = new Intent(MainActivity.this, ShopActivity.class);
		startActivity(intent);
	}

	public void onUserClick(View v) {
		UserDialog fr = new UserDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.show(getSupportFragmentManager(), "Hello");
	}

	public void onAchievementClick(View v) {
		Intent intent = new Intent(MainActivity.this, AchievementActivity.class);
		startActivity(intent);
	}

	public void onLeaderClick(View v) {
		Intent intent = new Intent(MainActivity.this, LeaderBoardActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(_last_level - 1);
		initialize();
	}

	public void updateunlock() {
		initialize();
		Intent i = new Intent(MainActivity.this, LevelActivity.class);
		i.putExtra("level", _last_level);
		startActivity(i);
	}

	public void gotoFinal(View v) {
//		Intent intent = new Intent(MainActivity.this, FinaleLevelActivity.class);
//		startActivity(intent);
	}

	public void onGiftClick(View v) {
		HelpDialog fr = new HelpDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.setType(5);
		fr.show(getSupportFragmentManager(), "Hello");
	}

}