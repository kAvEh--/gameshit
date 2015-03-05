package com.eynak.footballquize;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.eynak.adapters.GridLevelAdapter;
import com.eynak.utils.DatabasHandler;

public class LevelActivity extends FragmentActivity {

	int _level;
	GridView _grid;
	GridLevelAdapter adapter;

	TextView _points_view;
	TextView _coins_view;
	TextView _title_view;

	private int _points;
	private int _coins;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);

		Intent i = getIntent();
		_level = i.getIntExtra("level", 1);
		
		if (_level == 1) {
			HelpDialog fr = new HelpDialog();
			fr.setStyle(DialogFragment.STYLE_NO_TITLE,
					R.style.MyDialog);
			fr.setType(2);
			fr.show(getSupportFragmentManager(), "Hello");
		} else if (_level == 30) {
			HelpDialog fr = new HelpDialog();
			fr.setStyle(DialogFragment.STYLE_NO_TITLE,
					R.style.MyDialog);
			fr.setType(3);
			fr.show(getSupportFragmentManager(), "Hello");
		}

		Typeface face = Typeface.createFromAsset(getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");
		
		_points_view = (TextView) findViewById(R.id.question_header_points);
		_points_view.setTypeface(face);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_coins_view.setTypeface(face);
		_title_view = (TextView) findViewById(R.id.level_title);
		_title_view.setTypeface(face);

		_title_view.setText("مرحله " + _level);

		_grid = (GridView) findViewById(R.id.level_grid);
		initialize();
	}

	private void initialize() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		ArrayList<HashMap<String, Integer>> data = db.getLevelData(_level);
		db.close();
		adapter = new GridLevelAdapter(LevelActivity.this, data, _level);
		_grid.setAdapter(adapter);
		// _grid.setDrawSelectorOnTop(true);

		HashMap<String, Integer> gameData;
		db = new DatabasHandler(getApplicationContext());
		gameData = db.getGameData();
		db.close();

		_points = gameData.get(getResources().getString(R.string.KEY_POINT));
		_coins = gameData.get(getResources().getString(R.string.KEY_COINS));

		_points_view.setText(String.valueOf(_points));
		_coins_view.setText(String.valueOf(_coins));
	}
	
	public void onCoinClick(View v) {
		Intent intent = new Intent(LevelActivity.this, ShopActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}

}