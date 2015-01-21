package com.glass.objects;

public class Manager {
	private int _id;
	private String _nameEn;
	private String _nameFa;
	private String _imagePath;
	private String _team;
	private String _year;
	private String _ch1;
	private String _ch2;
	private String _ch3;
	private String _ch4;
	private String _ch5;
	private String _ch6;
	private String _ch7;

	public Manager(int _id, String _nameEn, String _nameFa, String _imagePath,
			String team, String year, String _ch1, String _ch2, String _ch3,
			String _ch4, String _ch5, String _ch6, String _ch7) {
		this._id = _id;
		this._nameEn = _nameEn;
		this._nameFa = _nameFa;
		this._imagePath = _imagePath;
		this._team = team;
		this._year = year;
		this._ch1 = _ch1;
		this._ch2 = _ch2;
		this._ch3 = _ch3;
		this._ch4 = _ch4;
		this._ch5 = _ch5;
		this._ch6 = _ch6;
		this._ch7 = _ch7;
	}

	public int get_id() {
		return _id;
	}

	public String get_nameEn() {
		return _nameEn;
	}

	public String get_nameFa() {
		return _nameFa;
	}

	public String get_imagePath() {
		return _imagePath;
	}

	public String get_team() {
		return _team;
	}

	public String get_year() {
		return _year;
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

	public void set_id(int _id) {
		this._id = _id;
	}

	public void set_nameEn(String _nameEn) {
		this._nameEn = _nameEn;
	}

	public void set_nameFa(String _nameFa) {
		this._nameFa = _nameFa;
	}

	public void set_imagePath(String _imagePath) {
		this._imagePath = _imagePath;
	}

	public void set_team(String team) {
		this._team = team;
	}

	public void set_year(String year) {
		this._team = year;
	}

	public void set_ch1(String _ch1) {
		this._ch1 = _ch1;
	}

	public void set_ch2(String _ch2) {
		this._ch2 = _ch2;
	}

	public void set_ch3(String _ch3) {
		this._ch3 = _ch3;
	}

	public void set_ch4(String _ch4) {
		this._ch4 = _ch4;
	}

	public void set_ch5(String _ch5) {
		this._ch5 = _ch5;
	}

	public void set_ch6(String _ch6) {
		this._ch6 = _ch6;
	}

	public void set_ch7(String _ch7) {
		this._ch7 = _ch7;
	}
}