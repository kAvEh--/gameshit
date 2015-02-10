package com.glass.footballquize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.glass.adapters.GridLevelAdapter;
import com.glass.objects.Manager;
import com.glass.objects.Question;
import com.glass.utils.DatabasHandler;

public class LevelActivity extends Activity {

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

		_points_view = (TextView) findViewById(R.id.question_header_points);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_title_view = (TextView) findViewById(R.id.level_title);

		_title_view.setText("Level " + _level);

		_grid = (GridView) findViewById(R.id.level_grid);
		initialize();

//		DatabasHandler db = new DatabasHandler(getApplicationContext());
//		System.err.println("hello ----------------");
//		for (int j = 0; j < 150; j++) {
//			Question l = db.getQuestion(j + 1);
////			try {
////				getAssets().open("Manager/" + l.get_imagePath());
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				// e.printStackTrace();
////				System.err.println(l.get_id() + "---" + l.get_imagePath());
////			}
//			String[] tt = new String[8];
//			tt[0] = l.get_answer();
//			tt[1] = l.get_ch1();
//			tt[2] = l.get_ch2();
//			tt[3] = l.get_ch3();
//			tt[4] = l.get_ch4();
//			tt[5] = l.get_ch5();
//			tt[6] = l.get_ch6();
//			tt[7] = l.get_ch7();
//			for (int k = 0; k < 8; k++) {
//				for (int k2 = 0; k2 < k; k2++) {
//					if (tt[k].equals(tt[k2])) {
//						System.err.println(l.get_id() + "-----" + tt[k] + "---" + tt[k2]);
//					}
//				}
//			}
//		}
	}

	private void initialize() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		ArrayList<HashMap<String, Integer>> data = db.getLevelData(_level);
		db.close();
		adapter = new GridLevelAdapter(LevelActivity.this, data);
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

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}

}