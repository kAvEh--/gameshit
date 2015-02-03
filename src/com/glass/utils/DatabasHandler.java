package com.glass.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.glass.footballquize.R;
import com.glass.objects.Logo;
import com.glass.objects.Manager;
import com.glass.objects.Question;
import com.glass.objects.Shirt;
import com.glass.objects.Stadium;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabasHandler extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "FootballQuize.db";
	private static final int DATABASE_VERSION = 1;

	// Location table name
	private static final String TABLE_LOGO = "Logo";
	private static final String TABLE_SHIRT = "Shirt";
	private static final String TABLE_PLAYER = "Player";
	private static final String TABLE_QUESTION = "Question";
	private static final String TABLE_STADIUM = "Stadium";
	private static final String TABLE_MANAGER = "Manager";
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
	private static final String KEY_ANSWER = "answer";
	private static final String KEY_BODY = "body";
	private static final String KEY_TEAM = "team";
	private static final String KEY_YEAR = "year";
	private static final String KEY_NATIONALITY = "nationality";
	private static final String KEY_PLAY_NUM = "playNum";
	private static final String KEY_RELATED = "relatedId";
	private static final String KEY_FALSE_ANSWERS = "falseAnswers";
	private static final String KEY_USED_HINTS = "usedHints";
	private static final String KEY_USED_FREEZ = "usedFreez";
	private static final String KEY_COINS = "Coins";
	private static final String KEY_POINT = "Point";
	private static final String KEY_TYPE = "Type";
	private static final String KEY_LEVEL = "level";
	private static final String KEY_STATE = "state";
	private static final String KEY_START_TIME = "startTime";
	private static final String KEY_LAST_P_UNLOCK = "lastPackageUnlock";

	private Context myContex;

	public DatabasHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		myContex = context;
	}

	public HashMap<String, Integer> getGameData() {
		String selectQuery = "SELECT * FROM " + TABLE_GAME_DATA + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		if (cursor.moveToFirst()) {
			ret.put(KEY_COINS, cursor.getInt(cursor.getColumnIndex(KEY_COINS)));
			ret.put(KEY_POINT, cursor.getInt(cursor.getColumnIndex(KEY_POINT)));
			ret.put(KEY_LAST_P_UNLOCK, cursor.getInt(cursor.getColumnIndex(KEY_LAST_P_UNLOCK)));
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
				hash.put(KEY_START_TIME,
						cursor.getInt(cursor.getColumnIndex(KEY_START_TIME)));
				hash.put(KEY_ID, cursor.getInt(cursor.getColumnIndex(KEY_ID)));
				ret.add(hash);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return ret;
	}

	public void setStartTime(int id, long time) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_START_TIME
				+ " = " + time + " WHERE " + KEY_ID + " = " + id + ";";
		db.execSQL(sql);
		db.close();
	}
	
	public void setLastUnlock(int _level) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_LAST_P_UNLOCK
				+ " = " + _level + ";";
		db.execSQL(sql);
		db.close();
	}

	public Logo getLogo(int id) {
		String selectQuery = "SELECT * FROM " + TABLE_LOGO + " WHERE " + KEY_ID
				+ " = " + id + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Logo ret = null;
		if (cursor.moveToFirst()) {
			ret = new Logo(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
					cursor.getString(cursor.getColumnIndex(KEY_NAME_FA)),
					cursor.getString(cursor.getColumnIndex(KEY_NAME_EN)),
					cursor.getString(cursor.getColumnIndex(KEY_HINT)),
					cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)),
					cursor.getInt(cursor.getColumnIndex(KEY_DIFFICULTY)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_1)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_2)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_3)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_4)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_5)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_6)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_7)));
		}
		cursor.close();
		db.close();
		return ret;
	}

	public Shirt getShirt(int id) {
		String selectQuery = "SELECT * FROM " + TABLE_SHIRT + " WHERE "
				+ KEY_ID + " = " + id + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Shirt ret = null;
		if (cursor.moveToFirst()) {
			ret = new Shirt(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
					cursor.getInt(cursor.getColumnIndex(KEY_LOGO_ID)),
					cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
		}
		cursor.close();
		db.close();
		return ret;
	}

	public Stadium getStadium(int id) {
		String selectQuery = "SELECT * FROM " + TABLE_STADIUM + " WHERE "
				+ KEY_ID + " = " + id + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Stadium ret = null;
		if (cursor.moveToFirst()) {
			ret = new Stadium(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
					cursor.getString(cursor.getColumnIndex(KEY_NAME_EN)),
					cursor.getString(cursor.getColumnIndex(KEY_NAME_FA)),
					cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)),
					cursor.getString(cursor.getColumnIndex(KEY_ANSWER)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_1)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_2)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_3)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_4)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_5)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_6)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_7)));
		}
		cursor.close();
		db.close();
		return ret;
	}

	public Manager getManager(int id) {
		String selectQuery = "SELECT * FROM " + TABLE_MANAGER + " WHERE "
				+ KEY_ID + " = " + id + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Manager ret = null;
		if (cursor.moveToFirst()) {
			ret = new Manager(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
					cursor.getString(cursor.getColumnIndex(KEY_NAME_EN)),
					cursor.getString(cursor.getColumnIndex(KEY_NAME_FA)),
					cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)),
					cursor.getString(cursor.getColumnIndex(KEY_TEAM)),
					cursor.getString(cursor.getColumnIndex(KEY_YEAR)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_1)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_2)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_3)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_4)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_5)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_6)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_7)));
		}
		cursor.close();
		db.close();
		return ret;
	}
	
	public Question getQuestion(int id) {
		String selectQuery = "SELECT * FROM " + TABLE_QUESTION + " WHERE "
				+ KEY_ID + " = " + id + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Question ret = null;
		if (cursor.moveToFirst()) {
			ret = new Question(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
					cursor.getString(cursor.getColumnIndex(KEY_BODY)),
					cursor.getString(cursor.getColumnIndex(KEY_ANSWER)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_1)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_2)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_3)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_4)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_5)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_6)),
					cursor.getString(cursor.getColumnIndex(KEY_CHOICE_7)));
		}
		cursor.close();
		db.close();
		return ret;
	}

	public void setFalseAnswer(int levelId, String falseAnswer) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_FALSE_ANSWERS
				+ " = '" + falseAnswer + "' WHERE " + KEY_ID + " = " + levelId
				+ ";";
		db.execSQL(sql);
		db.close();
	}

	public void setCorrectAnswer(int levelId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_STATE + " = "
				+ myContex.getResources().getInteger(R.integer.STATE_CORRECT)
				+ " WHERE " + KEY_ID + " = " + levelId + ";";
		db.execSQL(sql);
		db.close();
	}

	public HashMap<String, String> getQuestionData(int levelId) {
		String selectQuery = "SELECT * FROM " + TABLE_LEVELS + " WHERE "
				+ KEY_ID + " = " + levelId + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		HashMap<String, String> ret = new HashMap<String, String>();
		if (cursor.moveToFirst()) {
			ret.put(KEY_TYPE, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_TYPE))));
			ret.put(KEY_RELATED, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_RELATED))));
			ret.put(KEY_LEVEL, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_LEVEL))));
			ret.put(KEY_STATE, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_STATE))));
			ret.put(KEY_START_TIME, String.valueOf(cursor.getLong(cursor
					.getColumnIndex(KEY_START_TIME))));
			ret.put(KEY_STATE, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_STATE))));
			ret.put(KEY_USED_FREEZ, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_USED_FREEZ))));
			ret.put(KEY_USED_HINTS, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_USED_HINTS))));
			ret.put(KEY_FALSE_ANSWERS,
					cursor.getString(cursor.getColumnIndex(KEY_FALSE_ANSWERS)));
		}
		cursor.close();
		db.close();
		return ret;
	}

	public void minusCoins(int levelId, int _coins) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_COINS + " = "
				+ KEY_COINS + " - " + _coins + ";";
		db.execSQL(sql);
		db.close();
	}

	public void addScore(int _score) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_POINT + " = "
				+ KEY_POINT + " + " + _score + ";";
		db.execSQL(sql);
		db.close();
	}

	public void usedFreez(int levelId, long time) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_USED_FREEZ
				+ " = " + time + " WHERE " + KEY_ID + " = " + levelId + ";";
		db.execSQL(sql);
		db.close();
	}
	
	public void usedHint(int levelId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_USED_HINTS
				+ " = 1 WHERE " + KEY_ID + " = " + levelId + ";";
		db.execSQL(sql);
		db.close();
	}
}