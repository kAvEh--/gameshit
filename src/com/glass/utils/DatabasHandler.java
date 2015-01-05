package com.glass.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.glass.footballquize.R;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabasHandler extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "FootballQuize.db";
	private static final int DATABASE_VERSION = 1;

	// Location table name
	private static final String TABLE_LOGO = "Logo";
	private static final String TABLE_SHIRT = "Shirt";
	private static final String TABLE_PLAYER = "Player";
	private static final String TABLE_QUESTION = "Question";
	private static final String TABLE_SHIRT_NUMBER = "ShirtNo";
	private static final String TABLE_LEVELS = "Levels";
	private static final String TABLE_GAME_DATA = "GameData";

	// Items Fields
	private static final String KEY_ID = "Id";
	private static final String KEY_NAME_FA = "nameFa";
	private static final String KEY_NAME_EN = "nameEn";
	private static final String KEY_IMAGE_PATH = "imagePath";
	private static final String KEY_HINT = "hint";
	private static final String KEY_DIFFICULTY = "diff";
	private static final String KEY_CHOICE_1 = "ch1";
	private static final String KEY_CHOICE_2 = "ch2";
	private static final String KEY_CHOICE_3 = "ch3";
	private static final String KEY_CHOICE_4 = "ch4";
	private static final String KEY_CHOICE_5 = "ch5";
	private static final String KEY_CHOICE_6 = "ch6";
	private static final String KEY_CHOICE_7 = "ch7";
	private static final String KEY_LOGO_ID = "logoId";
	private static final String KEY_BODY = "body";
	private static final String KEY_ANSWER = "answer";
	private static final String KEY_RELATED = "relatedId";
	private static final String KEY_FALSE_ANSWERS = "falseAnswers";
	private static final String KEY_USED_HINTS = "usedHints";
	private static final String KEY_IS_GENERATED = "isGenerated";
	private static final String KEY_COINS = "coins";
	private static final String KEY_TYPE = "Type";
	private static final String KEY_LEVEL = "level";
	private static final String KEY_STATE = "state";

	private Context myContex;

	public DatabasHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		myContex = context;
	}

	public void generateDatabase() {
		String selectQuery = "SELECT * FROM " + TABLE_LOGO + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		int level = 1;
		int counter = 0;
		if (cursor.moveToFirst()) {
			do {
				if (counter < 15) {
					ContentValues values = new ContentValues();
					values.put(
							KEY_TYPE,
							myContex.getResources().getInteger(
									R.integer.TYPE_LOGO));
					values.put(KEY_RELATED,
							cursor.getInt(cursor.getColumnIndex(KEY_ID)));
					values.put(KEY_LEVEL, level);
					values.put(
							KEY_STATE,
							myContex.getResources().getInteger(
									R.integer.STATE_NONE));

					db.insert(TABLE_LEVELS, null, values);
					counter++;
				} else {
					counter = 0;
					level++;
					ContentValues values = new ContentValues();
					values.put(
							KEY_TYPE,
							myContex.getResources().getInteger(
									R.integer.TYPE_LOGO));
					values.put(KEY_RELATED,
							cursor.getInt(cursor.getColumnIndex(KEY_ID)));
					values.put(KEY_LEVEL, level);
					values.put(
							KEY_STATE,
							myContex.getResources().getInteger(
									R.integer.STATE_NONE));

					db.insert(TABLE_LEVELS, null, values);
					counter++;
				}
			} while (cursor.moveToNext());
		}
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_IS_GENERATED
				+ " = "
				+ myContex.getResources().getInteger(R.integer.IS_GENERATED)
				+ ";";
		db.execSQL(sql);
		cursor.close();
		db.close();
	}

	public HashMap<String, Integer> getGameData() {
		String selectQuery = "SELECT * FROM " + TABLE_GAME_DATA + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		if (cursor.moveToFirst()) {
			ret.put(KEY_IS_GENERATED,
					cursor.getInt(cursor.getColumnIndex(KEY_IS_GENERATED)));
			ret.put(KEY_COINS, cursor.getInt(cursor.getColumnIndex(KEY_COINS)));
		}
		cursor.close();
		db.close();

		return ret;
	}

	public ArrayList<HashMap<String, Integer>> getLevelData(int level) {
		String selectQuery = "SELECT * FROM " + TABLE_LEVELS + " WHERE "
				+ KEY_LEVEL + " = " + level + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<HashMap<String, Integer>> ret = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer> hash;
		if (cursor.moveToFirst()) {
			do {
				hash = new HashMap<String, Integer>();
				hash.put(KEY_TYPE,
						cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
				hash.put(KEY_RELATED,
						cursor.getInt(cursor.getColumnIndex(KEY_RELATED)));
				hash.put(KEY_LEVEL,
						cursor.getInt(cursor.getColumnIndex(KEY_LEVEL)));
				hash.put(KEY_STATE,
						cursor.getInt(cursor.getColumnIndex(KEY_STATE)));
				ret.add(hash);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return ret;
	}
}