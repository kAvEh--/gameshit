package com.eynak.footballquize;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eynak.utils.DatabasHandler;
import com.eynak.utils.SendDatatoServer;

public class AchievementActivity extends Activity {

	ImageView _ach_1;
	ImageView _ach_2;
	ImageView _ach_3;
	ImageView _ach_4;
	ImageView _ach_5;
	ImageView _ach_6;
	ImageView _ach_7;
	ImageView _ach_8;
	ImageView _ach_9;
	ImageView _ach_10;
	ImageView _ach_11;
	ImageView _ach_12;
	ImageView _ach_13;

	TextView _points_view;
	TextView _coins_view;
	
	boolean _market_flag = false;
	
	int _coins;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievement);

		Typeface face = Typeface.createFromAsset(getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");
		
		_points_view = (TextView) findViewById(R.id.question_header_points);
		_points_view.setTypeface(face);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_coins_view.setTypeface(face);

		updateCoins();

		_ach_1 = (ImageView) findViewById(R.id.ach_1);
		_ach_2 = (ImageView) findViewById(R.id.ach_2);
		_ach_3 = (ImageView) findViewById(R.id.ach_3);
		_ach_4 = (ImageView) findViewById(R.id.ach_4);
		_ach_5 = (ImageView) findViewById(R.id.ach_5);
		_ach_6 = (ImageView) findViewById(R.id.ach_6);
		_ach_7 = (ImageView) findViewById(R.id.ach_7);
		_ach_8 = (ImageView) findViewById(R.id.ach_8);
		_ach_9 = (ImageView) findViewById(R.id.ach_9);
		_ach_10 = (ImageView) findViewById(R.id.ach_10);
		_ach_11 = (ImageView) findViewById(R.id.ach_11);
		_ach_12 = (ImageView) findViewById(R.id.ach_12);
		_ach_13 = (ImageView) findViewById(R.id.ach_13);

		HashMap<String, String> tmp;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_5Star_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_1.setImageResource(R.drawable.ach_1_on);
		} else {
			_ach_1.setImageResource(R.drawable.ach_1_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_INRAW_5_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_2.setImageResource(R.drawable.ach_2_on);
		} else {
			_ach_2.setImageResource(R.drawable.ach_2_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_INRAW_10_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_3.setImageResource(R.drawable.ach_3_on);
		} else {
			_ach_3.setImageResource(R.drawable.ach_3_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_INRAW_20_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_4.setImageResource(R.drawable.ach_4_on);
		} else {
			_ach_4.setImageResource(R.drawable.ach_4_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_INRAW_50_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_5.setImageResource(R.drawable.ach_5_on);
		} else {
			_ach_5.setImageResource(R.drawable.ach_5_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_POINTS_5000_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_6.setImageResource(R.drawable.ach_6_on);
		} else {
			_ach_6.setImageResource(R.drawable.ach_6_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_POINTS_10000_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_7.setImageResource(R.drawable.ach_7_on);
		} else {
			_ach_7.setImageResource(R.drawable.ach_7_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_POINTS_20000_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_8.setImageResource(R.drawable.ach_8_on);
		} else {
			_ach_8.setImageResource(R.drawable.ach_8_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_POINTS_40000_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_9.setImageResource(R.drawable.ach_9_on);
		} else {
			_ach_9.setImageResource(R.drawable.ach_9_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_PACkAGE_2000_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_10.setImageResource(R.drawable.ach_10_on);
		} else {
			_ach_10.setImageResource(R.drawable.ach_10_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_PACkAGE_2100_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_11.setImageResource(R.drawable.ach_11_on);
		} else {
			_ach_11.setImageResource(R.drawable.ach_11_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_PACkAGE_2200_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_12.setImageResource(R.drawable.ach_12_on);
		} else {
			_ach_12.setImageResource(R.drawable.ach_12_off);
		}
		tmp = db.checkAchievements(getResources().getInteger(
				R.integer.ACH_PACkAGE_2300_ID));
		if (Integer.parseInt(tmp.get("isDone")) == 1) {
			_ach_13.setImageResource(R.drawable.ach_13_on);
		} else {
			_ach_13.setImageResource(R.drawable.ach_13_off);
		}
	}

	public void updateCoins() {
		HashMap<String, Integer> gameData;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		gameData = db.getGameData();
		db.close();

		int _points = gameData
				.get(getResources().getString(R.string.KEY_POINT));
		_coins = gameData.get(getResources().getString(R.string.KEY_COINS));

		_points_view.setText(String.valueOf(_points));
		_coins_view.setText(String.valueOf(_coins));
	}

	public void on5StarClick(View v) {
		_market_flag = true;
		final String APP_MARKET_URL = "market://details?id=com.eynak.footballquize";
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(APP_MARKET_URL));
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if (_market_flag) {
	    	DatabasHandler db = new DatabasHandler(getApplicationContext());
	    	HashMap<String, String> data = db.checkAchievements(getResources().getInteger(
					R.integer.ACH_5Star_ID));
			if (Integer.parseInt(data.get(getResources().getString(
					R.string.KEY_IS_DONE))) == 0) {
				_ach_1.setImageResource(R.drawable.ach_1_on);
				db.setAchievement(getResources().getInteger(
						R.integer.ACH_5Star_ID));
				db.addCoins(getResources().getInteger(R.integer.ACH_5Star));
				_coins += getResources().getInteger(R.integer.ACH_5Star);
				_coins_view.setText(_coins + "");
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));

				ImageView imageTmp = (ImageView) layout.findViewById(R.id.ach_main_image);
				imageTmp.setImageResource(R.drawable.ach_1_on);

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();

				SendDatatoServer tmp = new SendDatatoServer(this);
				tmp.checkUser();
			}
			db.close();
	    }
	}
}
