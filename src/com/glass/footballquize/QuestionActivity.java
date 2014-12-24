package com.glass.footballquize;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.glass.utils.AndroidUtils;
import com.glass.utils.DiffuseFilter;

public class QuestionActivity extends Activity {
	
	protected static final int TITLE_TEXT_SIZE = 22;

	protected ImageView mOriginalImageView;
	protected ImageView mModifyImageView;
	protected Bitmap mFilterBitmap;
	
	private int[] mColors;
	
	boolean[] filters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		Intent intent = getIntent();
		int level = intent.getIntExtra("num", 0);
		filters = new boolean[6];
		
		mOriginalImageView = (ImageView) findViewById(R.id.q_main_image_null);
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

}
