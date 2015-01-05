package com.glass.footballquize;

import java.util.ArrayList;
import java.util.HashMap;

import com.glass.adapters.GridLevelAdapter;
import com.glass.utils.DatabasHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

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
		GridLevelAdapter adapter = new GridLevelAdapter(LevelActivity.this, data);
		_grid.setAdapter(adapter);
		_grid.setDrawSelectorOnTop(true);
	}

}
