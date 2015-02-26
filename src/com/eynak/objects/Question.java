package com.eynak.objects;

public class Question {

	private int _id;
	private String _body;
	private String _answer;
	private String _ch1;
	private String _ch2;
	private String _ch3;
	private String _ch4;
	private String _ch5;
	private String _ch6;
	private String _ch7;

	public Question(int id, String body, String answer, String ch1,
			String ch2, String ch3, String ch4, String ch5, String ch6, String ch7) {
		this._id = id;
		this._body = body;
		this._answer = answer;
		this._ch1 = ch1;
		this._ch2 = ch2;
		this._ch3 = ch3;
		this._ch4 = ch4;
		this._ch5 = ch5;
		this._ch6 = ch6;
		this._ch7 = ch7;
	}

	public int get_id() {
		return _id;
	}

	public String get_body() {
		return _body;
	}

	public String get_answer() {
		return _answer;
	}

	public String get_ch1() {
		return _ch1;
	}

	public String get_ch2() {
		return _ch2;
	}

	public String get_ch3() {
		return _ch3;
	}

	public String get_ch4() {
		return _ch4;
	}

	public String get_ch5() {
		return _ch5;
	}
	
	public String get_ch6() {
		return _ch6;
	}
	
	public String get_ch7() {
		return _ch7;
	}

	public void set_id(int id) {
		this._id = id;
	}

	public void set_body(String body) {
		this._body = body;
	}

	public void set_answer(String answer) {
		this._answer = answer;
	}

	public void set_ch1(String ch1) {
		this._ch1 = ch1;
	}

	public void set_ch2(String ch2) {
		this._ch2 = ch2;
	}

	public void set_ch3(String ch3) {
		this._ch3 = ch3;
	}

	public void set_ch4(String ch4) {
		this._ch4 = ch4;
	}

	public void set_ch5(String ch5) {
		this._ch5 = ch5;
	}
	
	public void set_ch6(String ch6) {
		this._ch6 = ch6;
	}
	
	public void set_ch7(String ch7) {
		this._ch7 = ch7;
	}

}