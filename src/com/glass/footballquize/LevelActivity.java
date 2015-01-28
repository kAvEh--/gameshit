package com.glass.footballquize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.glass.adapters.GridLevelAdapter;
import com.glass.objects.Logo;
import com.glass.objects.Shirt;
import com.glass.utils.DatabasHandler;

public class LevelActivity extends Activity {

	int _level;
	GridView _grid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);

		Intent i = getIntent();
		_level = i.getIntExtra("level", 1);

		_grid = (GridView) findViewById(R.id.level_grid);
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		ArrayList<HashMap<String, Integer>> data = db.getLevelData(_level);
		db.close();
		GridLevelAdapter adapter = new GridLevelAdapter(LevelActivity.this,
				data);
		_grid.setAdapter(adapter);
		_grid.setDrawSelectorOnTop(true);

		db = new DatabasHandler(getApplicationContext());
		for (int j = 0; j < 120; j++) {
			Shirt l = db.getShirt(j + 1);
			try {
				getAssets().open("Shirt/" + l.get_imagePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.err.println(l.get_imagePath());
			}
		}
	}

}
