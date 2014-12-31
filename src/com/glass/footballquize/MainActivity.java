package com.glass.footballquize;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

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

	TextView tt;
	TextView jj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tt = (TextView) findViewById(R.id.tt);
		jj = (TextView) findViewById(R.id.jj);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
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

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			LevelFragment lv = new LevelFragment();
			lv.setPage(position);
			return lv;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.85f;
		private static final float MIN_ALPHA = 0.5f;

		@SuppressLint("NewApi")
		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 1) { // [-1,1]
				// Modify the default slide transition to shrink the page as
				// well
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				if (position < 0) {
					view.setTranslationX(horzMargin - vertMargin / 2);
				} else {
					view.setTranslationX(-horzMargin + vertMargin / 2);
				}

				// Scale the page down (between MIN_SCALE and 1)
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

				// Fade the page relative to its size.
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
						/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));

				tt.setTranslationX((position) * (pageWidth / 4));

				jj.setTranslationX((position) * (pageWidth / 1));

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}
	}

	// public class kkTransformer implements ViewPager.PageTransformer {
	// private static final float MIN_SCALE = 0.85f;
	// private static final float MIN_ALPHA = 0.5f;
	//
	// @SuppressLint("NewApi")
	// public void transformPage(View view, float position) {
	// int pageWidth = view.getWidth();
	//
	// if (position < -1) { // [-Infinity,-1)
	// // This page is way off-screen to the left.
	// view.setAlpha(0);
	//
	// } else if (position <= 1) { // [-1,1]
	//
	//
	// mBlur.setTranslationX((float) (-(1 - position) * 0.5 * pageWidth));
	// mBlurLabel.setTranslationX((float) (-(1 - position) * 0.5 *
	// pageWidth));
	//
	// mDim.setTranslationX((float) (-(1 - position) * pageWidth));
	// mDimLabel.setTranslationX((float) (-(1 - position) * pageWidth));
	//
	// mCheck.setTranslationX((float) (-(1 - position) * 1.5 * pageWidth));
	// mDoneButton.setTranslationX((float) (-(1 - position) * 1.7 *
	// pageWidth));
	// // The 0.5, 1.5, 1.7 values you see here are what makes the view move
	// in a different speed.
	// // The bigger the number, the faster the view will translate.
	// // The result float is preceded by a minus because the views travel
	// in the opposite direction of the movement.
	//
	// mFirstColor.setTranslationX((position) * (pageWidth / 4));
	//
	// mSecondColor.setTranslationX((position) * (pageWidth / 1));
	//
	// mTint.setTranslationX((position) * (pageWidth / 2));
	//
	// mDesaturate.setTranslationX((position) * (pageWidth / 1));
	// // This is another way to do it
	//
	//
	// } else { // (1,+Infinity]
	// // This page is way off-screen to the right.
	// view.setAlpha(0);
	// }
	// }
	// }

	public class DepthPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.75f;

		@SuppressLint("NewApi")
		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 0) { // [-1,0]
				// Use the default slide transition when moving to the left page
				view.setAlpha(1);
				view.setTranslationX(0);
				view.setScaleX(1);
				view.setScaleY(1);

			} else if (position <= 1) { // (0,1]
				// Fade the page out.
				view.setAlpha(1 - position);

				// Counteract the default slide transition
				view.setTranslationX(pageWidth * -position);

				// Scale the page down (between MIN_SCALE and 1)
				float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
						* (1 - Math.abs(position));
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}
	}

}
