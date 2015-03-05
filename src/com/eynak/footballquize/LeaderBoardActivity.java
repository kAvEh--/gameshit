package com.eynak.footballquize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eynak.adapters.LeadersAdapter;
import com.eynak.utils.AppController;
import com.eynak.utils.DatabasHandler;

public class LeaderBoardActivity extends Activity {

	ListView leaderBoard;
	HashMap<String, String> userData;

	TextView _points_view;
	TextView _coins_view;
	
	ProgressBar pb;

	int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_leader_board);

		Typeface face = Typeface.createFromAsset(getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");
		
		_points_view = (TextView) findViewById(R.id.question_header_points);
		_points_view.setTypeface(face);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_coins_view.setTypeface(face);
		
		pb = (ProgressBar) findViewById(R.id.lb_pb);
		
		pb.setVisibility(View.VISIBLE);

		updateCoins();

		DatabasHandler db = new DatabasHandler(getApplicationContext());

		leaderBoard = (ListView) findViewById(R.id.lv_leader_board);
		userData = db.getUser();
		db.close();

		userId = Integer.parseInt(userData.get(getResources().getString(
				R.string.KEY_ID)));
		String apiKey = userData.get(getResources().getString(
				R.string.KEY_API_KEY));
		getOnlineLeaderBoard(apiKey);

		super.onCreate(savedInstanceState);
	}

	private void getOnlineLeaderBoard(final String apiKey) {
		// TODO Auto-generated method stub
		String url = "http://eynakgroup.ir/fq/v1/index.php/getleaders";
		String tag_string_req = "string_req";
		StringRequest strReq = new StringRequest(Method.GET, url,
				new Response.Listener<String>() {

					public void onResponse(String response) {
						ArrayList<String[]> leaders = new ArrayList<String[]>();
						try {
							JSONObject serverResult = new JSONObject(response);
							JSONArray array = serverResult
									.getJSONArray("leaders");
							for (int i = 0; i < array.length(); i++) {
								System.out.println(array.get(i));
								JSONObject temp = new JSONObject(array.get(i)
										.toString());
								String[] tempArray = { temp.getString("_id"),
										temp.getString("_name"),
										temp.getString("_score") };
								leaders.add(tempArray);

							}
							writeLeaders(leaders);

							// return leaders;
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					public void onErrorResponse(VolleyError error) {
						// TODO
						System.out.println("Some Error in updateUserLog");
						System.out.println(error);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Authorization", apiKey);
				return headers;
			}

		};
		// RequestFuture<StringRequest> future = RequestFuture.newFuture();
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	public void updateCoins() {
		HashMap<String, Integer> gameData;
		DatabasHandler db = new DatabasHandler(getApplicationContext());
		gameData = db.getGameData();
		db.close();

		int _points = gameData
				.get(getResources().getString(R.string.KEY_POINT));
		int _coins = gameData.get(getResources().getString(R.string.KEY_COINS));

		_points_view.setText(String.valueOf(_points));
		_coins_view.setText(String.valueOf(_coins));
	}

	protected void writeLeaders(ArrayList<String[]> leaders) {
		pb.setVisibility(View.GONE);
		LeadersAdapter la = new LeadersAdapter(leaders, getApplicationContext(), userId);
		leaderBoard.setAdapter(la);
	}
}
