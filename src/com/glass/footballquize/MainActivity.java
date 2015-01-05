package com.glass.footballquize;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glass.utils.DatabasHandler;

public class MainActivity extends FragmentActivity {

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 5;
	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	PagerContainer mContainer;

	TextView tt;
	TextView jj;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tt = (TextView) findViewById(R.id.tt);
		jj = (TextView) findViewById(R.id.jj);

		mContainer = (PagerContainer) findViewById(R.id.pager_container);

		ViewPager pager = mContainer.getViewPager();
		PagerAdapter adapter = new MyPagerAdapter();
		pager.setAdapter(adapter);
		pager.setPageTransformer(true, new ZoomOutPageTransformer());
		// pager.setCurrentItem(2);
		// Necessary or the pager will only have one extra page to show
		// make this at least however many pages you can see
		pager.setOffscreenPageLimit(adapter.getCount());
		// A little space between pages
		pager.setPageMargin(15);

		// If hardware acceleration is enabled, you should also remove
		// clipping on the pager for its children.
		pager.setClipChildren(false);

		initialChecks();
	}

	private void initialChecks() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		HashMap<String, Integer> data = db.getGameData();
		if (data.get(getResources().getString(R.string.KEY_IS_GENERATED)) != getResources().getInteger(R.integer.IS_GENERATED)) {
			db.generateDatabase();
		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the
			// system to handle the
			// Back button. This calls finish() on this activity and pops the
			// back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.8f;
		private static final float MIN_ALPHA = 0.5f;

		@SuppressLint("NewApi")
		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(MIN_ALPHA);
				view.setScaleY(MIN_SCALE);

			} else if (position <= 1) { // [-1,1]
				// Modify the default slide transition to shrink the page as
				// well
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				if (position < 0) {
					// view.setTranslationX(horzMargin - vertMargin / 3);
				} else {
					// view.setTranslationX(-horzMargin + vertMargin / 3);
				}

				// Scale the page down (between MIN_SCALE and 1)
				// view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

				// Fade the page relative to its size.
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
						/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));

				// tt.setTranslationX((position) * (pageWidth / 4));
				//
				// jj.setTranslationX((position) * (pageWidth / 1));

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
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
			
			TextView tv = (TextView) view.findViewById(R.id.level_title);
			tv.setTextSize(50);
			tv.setText("Level " + position);

			view.setTag(position);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int stage = (Integer) v.getTag();
					Intent i = new Intent(MainActivity.this, LevelActivity.class);
					i.putExtra("level", (stage + 1));
					startActivity(i);
				}
			});
			((ViewPager) pager).addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}
	}

}