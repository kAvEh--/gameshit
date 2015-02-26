package com.eynak.footballquize;

import util.IabHelper;
import util.IabResult;
import util.Inventory;
import util.Purchase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ShopActivity extends Activity {

	// Debug tag, for logging
	static final String TAG = "";

	// SKUs for our products: the premium upgrade (non-consumable)
	static final String SKU_100_coins = "buy_100_coins";

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
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");
			if (result.isFailure()) {
				Log.d(TAG, "Failed to query inventory: " + result);
				return;
			} else {
				Log.d(TAG, "Query inventory was successful.");
				// does the user have the premium upgrade?
				mIsPremium = inventory.hasPurchase(SKU_100_coins);

				// update UI accordingly

				Log.d(TAG, "User is "
						+ (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
			}

			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				Log.d(TAG, "Error purchasing: " + result);
				return;
			} else if (purchase.getSku().equals(SKU_100_coins)) {
				// give user access to premium content and update the UI
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}
}
