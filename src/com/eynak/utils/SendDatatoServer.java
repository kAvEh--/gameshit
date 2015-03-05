package com.eynak.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.provider.Settings.Secure;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eynak.footballquize.R;

public class SendDatatoServer {

	public static final String URL_JSON_OBJECT = "http://eynakgroup.ir/fq/v1/index.php/register";
	public static final String URL_PACKAGE_DATA = "http://eynakgroup.ir/fq/v1/index.php/packageData";
	public static final String URL_ACHIEVEMENTS_DATA = "http://eynakgroup.ir/fq/v1/index.php/achievements";
	public static final String URL_USER_DATA = "http://eynakgroup.ir/fq/v1/index.php/userData";
	public static final String URL_FINALE_DATA = "http://eynakgroup.ir/fq/v1/index.php/sendFinaleScore";
	private String tag_json_obj = "jobj_req";

	private String _api_key;

	private Activity _myActivity;

	public SendDatatoServer(Activity a) {
		this._myActivity = a;
	}

	public void checkUser() {
		DatabasHandler db = new DatabasHandler(_myActivity);
		HashMap<String, String> userData = db.getUser();
		db.close();
		if (userData.get(_myActivity.getResources().getString(
				R.string.KEY_API_KEY)) == null
				|| Integer.parseInt(userData.get(_myActivity.getResources()
						.getString(R.string.KEY_ID))) < 1) {
			sendUserData();
		} else {
			_api_key = userData.get(_myActivity.getResources().getString(
					R.string.KEY_API_KEY));
			checkUnSent();
		}
	}

	/**
	 * Making json object request
	 * */
	public void sendUserData() {
		// showProgressDialog();
		StringRequest jsonObjReq = new StringRequest(Method.POST,
				URL_JSON_OBJECT, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// Log.d(TAG, response.toString());
						try {
							System.out.println(response);
							JSONObject json = new JSONObject(response);
							if (!Boolean.parseBoolean(json.get("error")
									.toString())) {
								_api_key = json.get("api_key").toString();
								DatabasHandler db = new DatabasHandler(
										_myActivity.getApplicationContext());
								db.setUser(Integer.parseInt(json.get("user_id")
										.toString()), json.get("api_key")
										.toString());
								db.close();
								checkUnSent();
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
				// headers.put("Content-Type",
				// "application/x-www-form-urlencoded");
				return headers;
			}

			protected Map<String, String> getParams() {
				String android_id = Secure.getString(_myActivity
						.getApplicationContext().getContentResolver(),
						Secure.ANDROID_ID);
				Map<String, String> params = new HashMap<String, String>();
				params.put("userKey", android_id);

				return params;
			}

		};

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

		// Cancelling request
		// ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
	}

	public void checkUnSent() {
		DatabasHandler db = new DatabasHandler(
				_myActivity.getApplicationContext());
		HashMap<String, String> userData = db.getUser();
		if (Integer.parseInt(userData.get(_myActivity.getResources().getString(R.string.KEY_IS_SENT))) == 1) {
			sendUserData(userData);
		}
		ArrayList<HashMap<String, String>> data = db.getUnSentPackages();
		HashMap<String, String> tmp;
		for (int i = 0; i < data.size(); i++) {
			tmp = new HashMap<String, String>();
			tmp = data.get(i);
			sendData(tmp);
		}
		data = db.getUnSentAchievements();
		db.close();
		for (int i = 0; i < data.size(); i++) {
			tmp = new HashMap<String, String>();
			tmp = data.get(i);
			sendAchievData(tmp);
		}
	}

	private void sendUserData(final HashMap<String, String> userData) {
		StringRequest jsonObjReq = new StringRequest(Method.POST,
				URL_USER_DATA, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// Log.d(TAG, response.toString());
						System.out.println(response);
						try {
							JSONObject json = new JSONObject(response);
							if (!Boolean.parseBoolean(json.get("error")
									.toString())) {
								DatabasHandler db = new DatabasHandler(
										_myActivity.getApplicationContext());
								db.setUserIsSent();
								db.close();
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
				params.put(
						"name",
						userData.get(_myActivity.getResources().getString(
								R.string.KEY_NAME)));
				params.put(
						"phone",
						userData.get(_myActivity.getResources().getString(
								R.string.KEY_PHONE_NUM)));
				params.put(
						"email",
						userData.get(_myActivity.getResources().getString(
								R.string.KEY_EMAIL)));

				return params;
			}

		};

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
	}

	private void sendAchievData(final HashMap<String, String> data) {
		StringRequest jsonObjReq = new StringRequest(Method.POST,
				URL_ACHIEVEMENTS_DATA, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// Log.d(TAG, response.toString());
						System.out.println(response);
						try {
							JSONObject json = new JSONObject(response);
							if (!Boolean.parseBoolean(json.get("error")
									.toString())) {
								DatabasHandler db = new DatabasHandler(
										_myActivity.getApplicationContext());
								db.setAchievementIsSent(Integer.parseInt(data
										.get(_myActivity.getResources()
												.getString(R.string.KEY_ID))));
								db.close();
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
				params.put(
						"achievement_id",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_ID)));

				return params;
			}

		};

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
	}

	public void sendData(final HashMap<String, String> data) {
		StringRequest jsonObjReq = new StringRequest(Method.POST,
				URL_PACKAGE_DATA, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// Log.d(TAG, response.toString());
						System.out.println(response);
						try {
							JSONObject json = new JSONObject(response);
							if (!Boolean.parseBoolean(json.get("error")
									.toString())) {
								DatabasHandler db = new DatabasHandler(
										_myActivity.getApplicationContext());
								db.setPackageIsSent(Integer.parseInt(data
										.get(_myActivity.getResources()
												.getString(R.string.KEY_ID))));
								db.close();
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
				params.put(
						"packageNum",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_ID)));
				params.put(
						"point",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_POINT)));
				params.put(
						"coins",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_COINS)));
				params.put(
						"firstShot",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_FIRST_SHOT)));
				params.put(
						"skipNum",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_SKIP_NUM)));
				params.put(
						"freezNum",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_FREEZE_NUM)));
				params.put(
						"removeNum",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_REMOVE_NUM)));
				params.put(
						"hintNum",
						data.get(_myActivity.getResources().getString(
								R.string.KEY_HINT_NUM)));

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
