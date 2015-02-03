package com.glass.footballquize;

import com.glass.utils.DatabasHandler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UnlockDialog extends DialogFragment {

	int _level = 0;
	int _score = 0;
	int[] _unlocks;

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		setRetainInstance(true);
		_unlocks = getActivity().getResources().getIntArray(
				R.array.scores_unlock);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View rootView = inflater.inflate(R.layout.dialog_unlock, null);
		TextView title = (TextView) rootView
				.findViewById(R.id.dialog_unlock_title);
		title.setText("Unlock Level " + _level);

		TextView score = (TextView) rootView
				.findViewById(R.id.dialog_unlock_body);
		System.out.println(_unlocks[_level]);
		System.out.println(_unlocks[_level + 1]);
		System.out.println(_unlocks[_level + 2]);
		if (_score >= _unlocks[_level])
			score.setText("Do you wanna unlock it?...");
		else
			score.setText("You need " + _unlocks[_level] + " score to unlock....");

		if (_score >= _unlocks[_level]) {
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
									db.close();
									((MainActivity) getActivity())
											.updateunlock(_level);
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

	public void setScore(int s) {
		this._score = s;
	}
}