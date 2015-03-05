package com.eynak.footballquize;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eynak.utils.DatabasHandler;

public class UnlockDialog extends DialogFragment {

	int _level = 0;
	int _coins = 0;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_unlock, null);

		ImageButton button = (ImageButton) rootView
				.findViewById(R.id.lock_button);

		TextView body_2 = (TextView) rootView.findViewById(R.id.lock_body_2);
		TextView body_3 = (TextView) rootView.findViewById(R.id.lock_body_3);

		if (_level == 31) {
			if (_coins >= getActivity().getResources().getInteger(
					R.integer.COINS_TO_UNLOCK_LEVEL_FINALE)) {
				button.setImageResource(R.drawable.coin_100);
				body_2.setText(getActivity().getResources().getString(
						R.string.lock_body_unlock));
			} else {
				button.setImageResource(R.drawable.unlock_shop_button);
				body_2.setText((getActivity().getResources().getInteger(
						R.integer.COINS_TO_UNLOCK_LEVEL_FINALE) - _coins)
						+ " "
						+ getActivity().getResources().getString(
								R.string.lock_body_shop));
				body_3.setText(getActivity().getResources().getString(
								R.string.lock_body_shop_2));
			}
		} else if (_level == 30) {
			if (_coins >= getActivity().getResources().getInteger(
					R.integer.COINS_TO_UNLOCK_LEVEL_30)) {
				button.setImageResource(R.drawable.unlock_button_30);
				body_2.setText(getActivity().getResources().getString(
						R.string.lock_body_unlock));
			} else {
				button.setImageResource(R.drawable.unlock_shop_button);
				body_2.setText((getActivity().getResources().getInteger(
						R.integer.COINS_TO_UNLOCK_LEVEL_30) - _coins)
						+ " "
						+ getActivity().getResources().getString(
								R.string.lock_body_shop));
				body_3.setText(getActivity().getResources().getString(
								R.string.lock_body_shop_2));
			}
		} else {
			if (_coins >= getActivity().getResources().getInteger(
					R.integer.COINS_TO_UNLOCK_LEVEL)) {
				button.setImageResource(R.drawable.unlock_button);
				body_2.setText(getActivity().getResources().getString(
						R.string.lock_body_unlock));
			} else {
				button.setImageResource(R.drawable.unlock_shop_button);
				body_2.setText((getActivity().getResources().getInteger(
						R.integer.COINS_TO_UNLOCK_LEVEL) - _coins)
						+ " "
						+ getActivity().getResources().getString(
								R.string.lock_body_shop));
				body_3.setText(getActivity().getResources().getString(
						R.string.lock_body_shop_2));
			}
		}

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_level == 31) {
					if (_coins >= getActivity().getResources().getInteger(
							R.integer.COINS_TO_UNLOCK_LEVEL_FINALE)) {
						DatabasHandler db = new DatabasHandler(getActivity());
						db.addCoins(-getActivity().getResources().getInteger(
								R.integer.COINS_TO_UNLOCK_LEVEL_FINALE));
						db.close();
						((FinaleLevelActivity) getActivity()).gotoLevel();
						UnlockDialog.this.getDialog().cancel();
					} else {
						((FinaleLevelActivity) getActivity()).onShopClick(null);
					}
				} else if (_level == 30) {
					if (_coins >= getActivity().getResources().getInteger(
							R.integer.COINS_TO_UNLOCK_LEVEL_30)) {
						DatabasHandler db = new DatabasHandler(getActivity());
						db.setLastUnlock(_level);
						db.addCoins(-getActivity().getResources().getInteger(
								R.integer.COINS_TO_UNLOCK_LEVEL_30));
						db.close();
						((MainActivity) getActivity()).updateunlock();
						UnlockDialog.this.getDialog().cancel();
					} else {
						((MainActivity) getActivity()).onShopClick(null);
					}
				} else {
					if (_coins >= getActivity().getResources().getInteger(
							R.integer.COINS_TO_UNLOCK_LEVEL)) {
						DatabasHandler db = new DatabasHandler(getActivity());
						db.setLastUnlock(_level);
						db.addCoins(-getActivity().getResources().getInteger(
								R.integer.COINS_TO_UNLOCK_LEVEL));
						db.close();
						((MainActivity) getActivity()).updateunlock();
						UnlockDialog.this.getDialog().cancel();
					} else {
						((MainActivity) getActivity()).onShopClick(null);
					}
				}
			}
		});
		return rootView;
	}

	public void setLevel(int s) {
		this._level = s;
	}

	public void setCoins(int c) {
		this._coins = c;
	}
}