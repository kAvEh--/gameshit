package com.eynak.footballquize;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eynak.utils.DatabasHandler;
import com.eynak.utils.SendDatatoServer;

public class UserDialog extends DialogFragment {

	EditText _name;
	EditText _phone;
	EditText _email;

	TextView _error;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_user, null);

		_name = (EditText) rootView.findViewById(R.id.user_name);
		_phone = (EditText) rootView.findViewById(R.id.user_phone);
		_email = (EditText) rootView.findViewById(R.id.user_email);
		_error = (TextView) rootView.findViewById(R.id.user_error_text);
		
		ImageButton _button = (ImageButton) rootView.findViewById(R.id.user_ok_button);
		_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onOKClick();
			}
		});

		DatabasHandler db = new DatabasHandler(getActivity());
		HashMap<String, String> userData = db.getUser();
		db.close();
		if (userData.get(getActivity().getResources().getString(
				R.string.KEY_NAME)) != null) {
			_name.setText(userData.get(getActivity().getResources().getString(
					R.string.KEY_NAME)));
		}
		if (userData.get(getActivity().getResources().getString(
				R.string.KEY_PHONE_NUM)) != null) {
			_phone.setText(userData.get(getActivity().getResources().getString(
					R.string.KEY_PHONE_NUM)));
		}
		if (userData.get(getActivity().getResources().getString(
				R.string.KEY_EMAIL)) != null) {
			_email.setText(userData.get(getActivity().getResources().getString(
					R.string.KEY_EMAIL)));
		}
		return rootView;
	}

	public void onOKClick() {
		if (_name.getText().toString() == null
				|| _name.getText().toString().length() < 1) {
			_error.setText(getActivity().getResources().getString(
					R.string.NAME_IS_REQUIRED));
			return;
		}
		if ((_phone.getText().toString() == null || _phone.getText().toString()
				.length() < 1)
				&& (_email.getText().toString() == null || _email.getText()
						.toString().length() < 1)) {
			_error.setText(getActivity().getResources().getString(
					R.string.PHONE_IS_REQUIRED));
			return;
		}
		DatabasHandler db = new DatabasHandler(getActivity());
		db.setUserData(_name.getText().toString(), _phone.getText().toString(),
				_email.getText().toString());
		db.setUserIsNoSent();
		db.close();
		SendDatatoServer tmp = new SendDatatoServer(getActivity());
		tmp.checkUser();
		UserDialog.this.dismiss();
	}
}