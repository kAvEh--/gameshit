package com.glass.objects;

public class Logo {

	private int _id;
	private String _nameFa;
	private String _nameEn;
	private String _hint;
	private String _imagePath;
	private int _diff;
	private String _ch1;
	private String _ch2;
	private String _ch3;
	private String _ch4;
	private String _ch5;
	private String _ch6;
	private String _ch7;

	public Logo(int id, String nameFa, String nameEn, String hint,
			String imagePath, int diff, String ch1, String ch2, String ch3,
			String ch4, String ch5, String ch6, String ch7) {
		this._id = id;
		this._nameFa = nameFa;
		this._nameEn = nameEn;
		this._hint = hint;
		this._imagePath = imagePath;
		this._diff = diff;
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

	public String get_nameFa() {
		return _nameFa;
	}

	public String get_nameEn() {
		return _nameEn;
	}

	public String get_hint() {
		return _hint;
	}

	public String get_imagePath() {
		return _imagePath;
	}

	public int get_diff() {
		return _diff;
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

	public void set_nameFa(String nameFa) {
		this._nameFa = nameFa;
	}

	public void set_nameEn(String nameEn) {
		this._nameEn = nameEn;
	}

	public void set_hint(String hint) {
		this._hint = hint;
	}

	public void set_imagePath(String imagePath) {
		this._imagePath = imagePath;
	}

	public void set_diff(int diff) {
		this._diff = diff;
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
