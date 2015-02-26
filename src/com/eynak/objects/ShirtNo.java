package com.eynak.objects;

public class ShirtNo {

	private int _id;
	private int _relatedId;
	private int _diff;

	public ShirtNo(int id, int relatedId, int diff) {
		this._id = id;
		this._relatedId = relatedId;
		this._diff = diff;
	}

	public int get_id() {
		return _id;
	}

	public int get_relatedId() {
		return _relatedId;
	}

	public int get_diff() {
		return _diff;
	}

	public void set_id(int id) {
		this._id = id;
	}

	public void set_relatedId(int relatedId) {
		this._relatedId = relatedId;
	}

	public void set_diff(int diff) {
		this._diff = diff;
	}

}
