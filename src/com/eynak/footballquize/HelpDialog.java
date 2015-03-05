package com.eynak.footballquize;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HelpDialog extends DialogFragment {

	int _type = 0;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_help, null);

		ImageView mainIV = (ImageView) rootView.findViewById(R.id.help_main);
		if (_type == 1) {
			mainIV.setImageResource(R.drawable.help_1);
		} else if (_type == 2) {
			mainIV.setImageResource(R.drawable.help_2);
		} else if (_type == 3) {
			mainIV.setImageResource(R.drawable.help_3);
		} else if (_type == 4) {
			mainIV.setImageResource(R.drawable.help_4);
		} else {
			mainIV.setImageResource(R.drawable.help_5);
		}
		ImageView close = (ImageView) rootView.findViewById(R.id.help_close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HelpDialog.this.dismiss();
			}
		});
		return rootView;
	}

	public void setType(int t) {
		this._type = t;
	}
}