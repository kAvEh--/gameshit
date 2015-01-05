package com.glass.objects;

public class Player {

	private int _id;
	private String _imagePath;
	private String _hint;
	private int _diff;

	public Player(int id, String imagePath, String hint, int diff) {
		this._id = id;
		this._imagePath = imagePath;
		this._hint = hint;
		this._diff = diff;
	}

	public int get_id() {
		return _id;
	}

	public String get_imagePath() {
		return _imagePath;
	}

	public String get_hint() {
		return _hint;
	}

	public int get_diff() {
		return _diff;
	}

	public void set_id(int id) {
		this._id = id;
	}

	public void set_imagePath(String imagePath) {
		this._imagePath = imagePath;
	}

	public void set_hint(String hint) {
		this._hint = hint;
	}

	public void set_diff(int diff) {
		this._diff = diff;
	}

}