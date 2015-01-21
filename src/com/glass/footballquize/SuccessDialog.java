package com.glass.footballquize;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SuccessDialog extends DialogFragment {
	
	int _score = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.dialog_success,
				container, false);
		TextView title = (TextView) rootView.findViewById(R.id.dialog_success_main);
		title.setText("Success !!!");
		
		TextView score = (TextView) rootView.findViewById(R.id.dialog_success_score);
		score.setText(_score + "...");
		
		return rootView;
	}
	
	public void setScore(int s) {
		this._score = s;
	}

}
