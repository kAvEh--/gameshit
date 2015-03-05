package com.eynak.footballquize;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FinishPackageDialog extends DialogFragment {

	int _score = 0;
	int _package = 1;
	int _total_score;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.dialog_finish_package,
				container, false);
		Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "font/"
				+ getResources().getString(R.string.font) + "");
		
		TextView title = (TextView) rootView
				.findViewById(R.id.dialog_success_title);
		title.setText("مرحله " + _package + " تمام شد");
		title.setTypeface(face);

		TextView score = (TextView) rootView
				.findViewById(R.id.dialog_success_score);
		score.setText(_score + "");
		score.setTypeface(face);

		TextView body = (TextView) rootView.findViewById(R.id.success_body);
		body.setTypeface(face);

		String part1 = "برای رسیدن به فینال باید از مراحل بعد به طور متوسط ";
		String part2 = " امتیاز به دست آورید";
		int _remain_score = (int) ((55555 - _total_score) / (37 - _package));
		if (_remain_score < 0)
			_remain_score = 0;
		SpannableString text = new SpannableString(part1 + _remain_score + part2);
		text.setSpan(new ForegroundColorSpan(getActivity().getResources()
				.getColor(R.color.ajori)), 14, 19,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text.setSpan(new ForegroundColorSpan(getActivity().getResources()
				.getColor(R.color.ajori)), part1.length(), part1.length()
				+ String.valueOf(_total_score).length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		body.setText(text);

		ImageButton back = (ImageButton) rootView.findViewById(R.id.dialog_success_back);
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

	public void setPackage(int p) {
		this._package = p;
	}

	public void settotalscore(int s) {
		this._total_score = s;
	}
}