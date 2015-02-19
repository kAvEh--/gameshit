package com.glass.footballquize;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.glass.utils.DatabasHandler;

public class UnlockDialog extends DialogFragment {

	int _level = 0;
	int _coins = 0;

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		setRetainInstance(true);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View rootView = inflater.inflate(R.layout.dialog_unlock, null);
		TextView title = (TextView) rootView
				.findViewById(R.id.dialog_unlock_title);
		title.setText("Unlock Level " + _level);

		TextView score = (TextView) rootView
				.findViewById(R.id.dialog_unlock_body);
		if (_coins >= getActivity().getResources().getInteger(
				R.integer.COINS_TO_UNLOCK_LEVEL))
			score.setText("Do you wanna unlock it?...");
		else
			score.setText("You need "
					+ getActivity().getResources().getInteger(
							R.integer.COINS_TO_UNLOCK_LEVEL)
					+ " cups to unlock....");

		if (_coins >= getActivity().getResources().getInteger(
				R.integer.COINS_TO_UNLOCK_LEVEL)) {
			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			builder.setView(rootView)
					// Add action buttons
					.setPositiveButton("Unlock",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									DatabasHandler db = new DatabasHandler(
											getActivity());
									db.setLastUnlock(_level);
									db.addCoins(-getActivity()
											.getResources()
											.getInteger(
													R.integer.COINS_TO_UNLOCK_LEVEL));
									db.close();
									((MainActivity) getActivity())
											.updateunlock();
									UnlockDialog.this.getDialog().cancel();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									UnlockDialog.this.getDialog().cancel();
								}
							});
		} else {
			builder.setView(rootView)
			// Add action buttons
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									UnlockDialog.this.getDialog().cancel();
								}
							});
		}
		return builder.create();
	}

	public void setLevel(int s) {
		this._level = s;
	}

	public void setCoins(int c) {
		this._coins = c;
	}
}