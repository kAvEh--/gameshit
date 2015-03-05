package com.eynak.footballquize;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FinaleSuccessDialog extends DialogFragment {
	
	int _score = 0;
	int _finale_score = 0;
	int _coins = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		setCancelable(false);
		View rootView = inflater.inflate(R.layout.finale_popup,
				container, false);
		
		Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");
		
		TextView score = (TextView) rootView.findViewById(R.id.dialog_success_score);
		score.setTypeface(face);
		score.setText("+ " + _score);
		
		TextView coins = (TextView) rootView.findViewById(R.id.dialog_success_coins);
		coins.setTypeface(face);
		coins.setText("+ " + _coins);
		
		TextView totalScore = (TextView) rootView.findViewById(R.id.dialog_success_total_score);
		totalScore.setTypeface(face);
		totalScore.setText(_finale_score + " امتیاز در فینال گرفته‌اید");
		
		ImageView success = (ImageView) rootView.findViewById(R.id.success_image);
		if(_score > 0) {
			success.setImageResource(R.drawable.success_ball);
		} else {
			success.setImageResource(R.drawable.failure_bg);
		}
		
		ImageButton back = (ImageButton) rootView.findViewById(R.id.dialog_success_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((FinaleQActivity) getActivity()).backAction();
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
	
	public void setFinaleScore(int s) {
		this._finale_score = s;
	}
}
