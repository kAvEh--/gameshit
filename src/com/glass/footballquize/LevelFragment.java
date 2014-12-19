package com.glass.footballquize;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class LevelFragment extends Fragment {

	int _page = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_level, container, false);

		LinearLayout l1 = (LinearLayout) rootView.findViewById(R.id.level_1);
		LinearLayout l2 = (LinearLayout) rootView.findViewById(R.id.level_2);
		LinearLayout l3 = (LinearLayout) rootView.findViewById(R.id.level_3);
		LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT, 1);
		lp.setMargins(10, 10, 10, 10);
		TextView text;
		Button btnView;
		for (int i = 1; i <= 7; i++) {
			btnView = new Button(getActivity());
			btnView.setLayoutParams(lp);
			btnView.setTag(_page * 21 + i);
			btnView.setText("L " + (_page * 21 + i));
//			btnView.setBackgroundResource(R.drawable.aa);
			btnView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(v.getTag());
					Intent intent = new Intent(getActivity(), QuestionActivity.class);
					intent.putExtra("num", (Integer) v.getTag());
					startActivity(intent);
				}
			});
			l1.addView(btnView);
			// Second row
			btnView = new Button(getActivity());
			btnView.setLayoutParams(lp);
			btnView.setTag(_page * 21 + i + 7);
			btnView.setText("L " + (_page * 21 + i + 7));
//			btnView.setBackgroundResource(R.drawable.aa);
			btnView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(v.getTag());
					Intent intent = new Intent(getActivity(), QuestionActivity.class);
					intent.putExtra("num", (Integer) v.getTag());
					startActivity(intent);
				}
			});
			l2.addView(btnView);
			//Third row
			btnView = new Button(getActivity());
			btnView.setLayoutParams(lp);
			btnView.setTag(_page * 21 + i + 14);
			btnView.setText("L " + (_page * 21 + i + 14));
//			btnView.setBackgroundResource(R.drawable.aa);
			btnView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(v.getTag());
					Intent intent = new Intent(getActivity(), QuestionActivity.class);
					intent.putExtra("num", (Integer) v.getTag());
					startActivity(intent);
				}
			});
			l3.addView(btnView);
		}
		return rootView;
	}

	public void setPage(int page) {
		this._page = page;
	}

}