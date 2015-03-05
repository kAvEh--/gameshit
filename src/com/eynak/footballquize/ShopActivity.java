package com.eynak.footballquize;

import java.util.HashMap;

import util.IabHelper;
import util.IabResult;
import util.Purchase;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eynak.utils.DatabasHandler;

public class ShopActivity extends Activity {

	// Debug tag, for logging
	static final String TAG = "";

	// SKUs for our products: the premium upgrade (non-consumable)
	static final String SKU_100_coins = "buy_100_coins";
	static final String SKU_500_coins = "buy_500_coins";
	static final String SKU_1000_coins = "buy_1000_coins";
	static final String SKU_3000_coins = "buy_3000_coins";
	static final String SKU_10000_coins = "buy_10000_coins";

	TextView _points_view;
	TextView _coins_view;

	// Does the user have the premium upgrade?
	boolean mIsPremium = false;

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 0;

	// The helper object
	IabHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);

		Typeface face = Typeface.createFromAsset(getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");
		
		_points_view = (TextView) findViewById(R.id.question_header_points);
		_points_view.setTypeface(face);
		_coins_view = (TextView) findViewById(R.id.question_header_coins);
		_coins_view.setTypeface(face);

		updateCoins();

		String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDActLMXQ0TyNXy3NCEPzF62IyAbr6HoogfGtq30npJI63Vax64tIfdwHE5GNSaDLX05BXRoTduekZcNtJFyLEErEFKhWbDizPTMTiWkyHi+4yiOG6K/Vs8tj/bLSNvkiKZX5P0YQ3P4MVN/gCvTgPKAvlJceq+RNqiCOnBe2vqsuRl8zkqEP74GtaBzxKVqRTI9eHEiUoBYKzR9dDq3051/Ij9aRgbOko0FyEIKacCAwEAAQ==";
		// You can find it in your Bazaar console, in the Dealers section.
		// It is recommended to add more security than just pasting it in your
		// source code;
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.d(TAG, "Problem setting up In-app Billing: " + result);
				}
				// Hooray, IAB is fully set up!
				// mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
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

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				Log.d(TAG, "Error purchasing: " + result);
				return;
			} else if (purchase.getSku().equals(SKU_100_coins)) {
				DatabasHandler db = new DatabasHandler(getApplicationContext());
				db.addCoins(100);
				db.close();
				updateCoins();
				return;
			} else if (purchase.getSku().equals(SKU_500_coins)) {
				DatabasHandler db = new DatabasHandler(getApplicationContext());
				db.addCoins(500);
				db.close();
				updateCoins();
				return;
			} else if (purchase.getSku().equals(SKU_1000_coins)) {
				DatabasHandler db = new DatabasHandler(getApplicationContext());
				db.addCoins(1000);
				db.close();
				updateCoins();
				return;
			} else if (purchase.getSku().equals(SKU_3000_coins)) {
				DatabasHandler db = new DatabasHandler(getApplicationContext());
				db.addCoins(3000);
				db.close();
				updateCoins();
				return;
			} else if (purchase.getSku().equals(SKU_10000_coins)) {
				DatabasHandler db = new DatabasHandler(getApplicationContext());
				db.addCoins(10000);
				db.close();
				updateCoins();
				return;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	public void on100CoinClick(View v) {
		try {
			mHelper.launchPurchaseFlow(this, SKU_100_coins, RC_REQUEST,
					mPurchaseFinishedListener, "payload-string");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void on500CoinClick(View v) {
		try {
			mHelper.launchPurchaseFlow(this, SKU_500_coins, RC_REQUEST,
					mPurchaseFinishedListener, "payload-string");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void on1000CoinClick(View v) {
		try {
			mHelper.launchPurchaseFlow(this, SKU_1000_coins, RC_REQUEST,
					mPurchaseFinishedListener, "payload-string");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void on3000CoinClick(View v) {
		try {
			mHelper.launchPurchaseFlow(this, SKU_3000_coins, RC_REQUEST,
					mPurchaseFinishedListener, "payload-string");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void on10000CoinClick(View v) {
		try {
			mHelper.launchPurchaseFlow(this, SKU_10000_coins, RC_REQUEST,
					mPurchaseFinishedListener, "payload-string");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}
}
