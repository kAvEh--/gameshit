package com.glass.objects;

public class Shirt {

	private int _id;
	private int _logoId;
	private String _imagePath;

	public Shirt(int id, int logoId, String imagePath) {
		this._id = id;
		this._logoId = logoId;
		this._imagePath = imagePath;
	}

	public int get_id() {
		return _id;
	}

	public int get_logoId() {
		return _logoId;
	}

	public String get_imagePath() {
		return _imagePath;
	}

	public void set_id(int id) {
		this._id = id;
	}

	public void set_logoId(int logoId) {
		this._logoId = logoId;
	}

	public void set_imagePath(String imagePath) {
		this._imagePath = imagePath;
	}

}
