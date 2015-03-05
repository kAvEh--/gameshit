package com.eynak.footballquize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.eynak.adapters.FinaleLevelAdapter;
import com.eynak.utils.AppController;
import com.eynak.utils.DatabasHandler;
import com.eynak.utils.SendDatatoServer;

public class FinaleLevelActivity extends FragmentActivity {

	public static final String URL_FINALE_DATA = "http://eynakgroup.ir/fq/v1/index.php/sendFinaleScore";
	private String tag_json_obj = "jobj_req";

	GridView _grid;
	FinaleLevelAdapter adapter;

	TextView _points_view;
	TextView _coins_view;
	TextView _title_view;

	private int _points;
	private int _coins;

	int _current_level = 1;

	int current_level;
	ArrayList<HashMap<String, String>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finale_level);

		HelpDialog fr = new HelpDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.setType(4);
		fr.show(getSupportFragmentManager(), "Hello");

		Typeface face = Typeface.createFromAsset(getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");
		
		_points_view = (TextView) findViewById(R.id.question_header_points);
		_points_view.setTypeface(face);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_coins_view.setTypeface(face);
		_title_view = (TextView) findViewById(R.id.level_title);

		_title_view.setText("فینال");

		_grid = (GridView) findViewById(R.id.level_grid);
		initialize();
	}

	private void initialize() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		data = db.getFinaleData();
		int rand = db.getRandNum();
		if (rand < 1) {
			Random r = new Random();
			rand = r.nextInt(50000);
			db.setRandNum(rand);
		}
		db.close();
		Collections.shuffle(data, new Random(rand));
		adapter = new FinaleLevelAdapter(FinaleLevelActivity.this, data);
		_grid.setAdapter(adapter);

		HashMap<String, Integer> gameData;
		db = new DatabasHandler(getApplicationContext());
		gameData = db.getGameData();
		db.close();

		_points = gameData.get("finaleScore");
		_coins = gameData.get(getResources().getString(R.string.KEY_COINS));

		_points_view.setText(String.valueOf(_points));
		_coins_view.setText(String.valueOf(_coins));

		uploadData();
	}

	public void onCoinClick(View v) {
		Intent intent = new Intent(FinaleLevelActivity.this, ShopActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}

	public void onCurrentClick(int c) {
		this.current_level = c;
		UnlockDialog fr = new UnlockDialog();
		fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		fr.setLevel(31);
		fr.setCoins(_coins);
		fr.show(getSupportFragmentManager(), "Hello");
	}

	public void onShopClick(View v) {
		Intent intent = new Intent(FinaleLevelActivity.this, ShopActivity.class);
		startActivity(intent);
	}

	public void gotoLevel() {
		Intent i = new Intent(FinaleLevelActivity.this, FinaleQActivity.class);
		i.putExtra("Id", current_level);
		i.putExtra("data", data);
		startActivity(i);
	}

	public void gotoAction(int id) {
		current_level = id;
		// _grid.setSelection(_current_level);
		// _grid.requestFocusFromTouch();
		// _grid.setSelection(_current_level);
	}

	public void uploadData() {
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		HashMap<String, String> userData = db.getUser();
		db.close();

		String _api_key;
		if (userData.get(getResources().getString(R.string.KEY_API_KEY)) == null
				|| Integer.parseInt(userData.get(getResources().getString(
						R.string.KEY_ID))) < 1) {
			SendDatatoServer tmp = new SendDatatoServer(this);
			tmp.sendUserData();
		} else {
			_api_key = userData.get(getResources().getString(
					R.string.KEY_API_KEY));
			sentData(_api_key);
		}
	}

	private void sentData(String _api_key) {
		HashMap<String, String> hash;
		for (int i = 0; i < data.size(); i++) {
			hash = new HashMap<String, String>();
			hash = data.get(i);
			if (Integer.parseInt(hash.get(getResources().getString(
					R.string.KEY_IS_SENT))) == 0 && 
					Integer.parseInt(hash.get(getResources().getString(
							R.string.KEY_STATE))) != 1)
				sendIt(hash, _api_key);
		}
	}

	private void sendIt(final HashMap<String, String> hash,
			final String _api_key) {
		StringRequest jsonObjReq = new StringRequest(Method.POST,
				URL_FINALE_DATA, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// Log.d(TAG, response.toString());
						System.out.println(response);
						try {
							JSONObject json = new JSONObject(response);
							if (!Boolean.parseBoolean(json.get("error")
									.toString())) {
								DatabasHandler db = new DatabasHandler(
										getApplicationContext());
								db.setFinaleIsSent(Integer.parseInt(hash
										.get(getResources().getString(
												R.string.KEY_ID))));
								db.close();
								initialize();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// msgResponse.setText(response.toString());
						// hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// VolleyLog.d(TAG, "Error: " + error.getMessage());
						System.out.println(error.getMessage()
								+ "*****************");
						// hideProgressDialog();
					}
				}) {

			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Authorization", _api_key);
				return headers;
			}

			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("level",
						hash.get(getResources().getString(R.string.KEY_ID)));
				params.put("score",
						hash.get(getResources().getString(R.string.KEY_POINT)));
				params.put("choice",
						hash.get(getResources().getString(R.string.KEY_STATE)));
				params.put("total", String.valueOf(_points));

				return params;
			}

		};

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
	}
}