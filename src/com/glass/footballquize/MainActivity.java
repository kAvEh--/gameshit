package com.glass.footballquize;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.glass.utils.DatabasHandler;
import com.glass.utils.SendDatatoServer;

public class MainActivity extends FragmentActivity {

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_LEVELS = 30;
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

	ImageView _ball_image;

	private int _points;
	private int _coins;

	private int _last_level;

	DisplayMetrics metrics;
	float image_margin_px;

	private String _api_key;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_points_view = (TextView) findViewById(R.id.question_header_points);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_ball_image = (ImageView) findViewById(R.id.main_ball);

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
	}

	private void initialize() {
		HashMap<String, Integer> gameData;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		gameData = db.getGameData();
		db.close();

		_points = gameData.get(getResources().getString(R.string.KEY_POINT));
		_coins = gameData.get(getResources().getString(R.string.KEY_COINS));
		_last_level = gameData.get(getResources().getString(
				R.string.KEY_LAST_P_UNLOCK));

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
			// int pageWidth = view.getWidth();
			// int pageHeight = view.getHeight();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(MIN_ALPHA);
				view.setScaleY(MIN_SCALE);
				params.setMargins((int) (metrics.widthPixels / 30 * mPager
						.getCurrentItem()), (int) dp_10_px, 0, (int) dp_15_px);
				params.addRule(RelativeLayout.BELOW, R.id.pager_container);
				// _ball_image.setLayoutParams(params);

			} else if (position <= 1) { // [-1,1]
				// Modify the default slide transition to shrink the page as
				// well
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				// float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				// float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				// if (position < 0) {
				// // view.setTranslationX(horzMargin - vertMargin / 3);
				// } else {
				// // view.setTranslationX(-horzMargin + vertMargin / 3);
				// }

				// Scale the page down (between MIN_SCALE and 1)
				// view.setScaleX(scaleFactor);
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
				_ball_image.setLayoutParams(params);
				// tt.setTranslationX((position) * (pageWidth / 4));
				//
				// jj.setTranslationX((position) * (pageWidth / 1));

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(MIN_ALPHA);
				view.setScaleY(MIN_SCALE);
				// params.setMargins(
				// (int) (metrics.widthPixels / 30 * mPager.getCurrentItem() +
				// dp_15_px),
				// (int) dp_10_px, 0, (int) dp_15_px);
				// params.addRule(RelativeLayout.BELOW, R.id.pager_container);
				// _ball_image.setLayoutParams(params);
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

			ImageView lockIcon = (ImageView) view
					.findViewById(R.id.level_main_image);
			TextView tv = (TextView) view.findViewById(R.id.level_title);
			tv.setTextSize(50);
			tv.setText("Level " + (position + 1));

			view.setTag(position);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int stage = (Integer) v.getTag();
					if (stage < _last_level) {
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
						Toast.makeText(getApplicationContext(),
								"You don`t have access to this level.",
								Toast.LENGTH_LONG).show();
					}
				}
			});
			if (position < _last_level) {
				lockIcon.setVisibility(View.INVISIBLE);
			} else {
				lockIcon.setVisibility(View.VISIBLE);
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

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}

	public void updateunlock() {
		initialize();
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(_last_level - 1);
	}

}