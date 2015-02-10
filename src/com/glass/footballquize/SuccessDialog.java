package com.glass.footballquize;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SuccessDialog extends DialogFragment {
	
	int _score = 0;
	int _coins = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.dialog_success,
				container, false);
		TextView title = (TextView) rootView.findViewById(R.id.dialog_success_main);
		title.setText("Success !!!");
		
		TextView score = (TextView) rootView.findViewById(R.id.dialog_success_score);
		score.setText("You get " + _score + " balls...");
		
		TextView coins = (TextView) rootView.findViewById(R.id.dialog_success_coins);
		coins.setText("You get " + _coins + " cups...");
		
		Button next = (Button) rootView.findViewById(R.id.dialog_success_next);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((QuestionActivity) getActivity()).gotoNextAction();
			}
		});
		
		Button back = (Button) rootView.findViewById(R.id.dialog_success_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((QuestionActivity) getActivity()).backAction();
			}
		});
		
		return rootView;
	}
	
	public void setScore(int s) {
		this._score = s;
	}
	
	public void setCoins(int c) {
		this._coins = c;
	}
}
