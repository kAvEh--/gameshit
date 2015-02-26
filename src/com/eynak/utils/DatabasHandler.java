package com.eynak.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eynak.footballquize.R;
import com.eynak.objects.Logo;
import com.eynak.objects.Manager;
import com.eynak.objects.Player;
import com.eynak.objects.Question;
import com.eynak.objects.Shirt;
import com.eynak.objects.Stadium;
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
	private static final String TABLE_PACKAGE_INFO = "packageInfo";
	private static final String TABLE_USER = "user";
	private static final String TABLE_ACHIEVEMENTS = "Achievements";

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
	private static final String KEY_IS_SENT = "isSent";
	private static final String KEY_LEVEL_COMPLETED = "levelCompleted";
	private static final String KEY_FIRST_SHOT = "firstShot";
	private static final String KEY_START_TIME = "startTime";
	private static final String KEY_REMOVED_CHOICES = "removedChoices";
	private static final String KEY_LAST_P_UNLOCK = "lastPackageUnlock";
	private static final String KEY_API_KEY = "api_key";
	private static final String KEY_NAME = "name";
	private static final String KEY_PHONE_NUM = "phoneNum";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_SKIP_NUM = "skipNum";
	private static final String KEY_FREEZE_NUM = "freezeNum";
	private static final String KEY_REMOVE_NUM = "removeNum";
	private static final String KEY_HINT_NUM = "hintNum";
	private static final String KEY_CORRECT_IN_RAW = "CorrectInRaw";
	private static final String KEY_TITLE = "title";
	private static final String KEY_IS_DONE = "isDone";
	private static final String KEY_FREE_HINT = "freeHint";
	private static final String KEY_FREE_REMOVE = "freeRemove";
	private static final String KEY_FREE_FREEZE = "freeFreeze";
	private static final String KEY_FREE_SKIP = "freeSkip";

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
			ret.put(KEY_LAST_P_UNLOCK,
					cursor.getInt(cursor.getColumnIndex(KEY_LAST_P_UNLOCK)));
			ret.put(KEY_CORRECT_IN_RAW,
					cursor.getInt(cursor.getColumnIndex(KEY_CORRECT_IN_RAW)));
			ret.put(KEY_FREE_HINT,
					cursor.getInt(cursor.getColumnIndex(KEY_FREE_HINT)));
			ret.put(KEY_FREE_REMOVE,
					cursor.getInt(cursor.getColumnIndex(KEY_FREE_REMOVE)));
			ret.put(KEY_FREE_FREEZE,
					cursor.getInt(cursor.getColumnIndex(KEY_FREE_FREEZE)));
			ret.put(KEY_FREE_SKIP,
					cursor.getInt(cursor.getColumnIndex(KEY_FREE_SKIP)));
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

	public Player getPlayer(int id) {
		String selectQuery = "SELECT * FROM " + TABLE_PLAYER + " WHERE "
				+ KEY_ID + " = " + id + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Player ret = null;
		if (cursor.moveToFirst()) {
			ret = new Player(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
					cursor.getString(cursor.getColumnIndex(KEY_NAME_EN)),
					cursor.getString(cursor.getColumnIndex(KEY_NAME_FA)),
					cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)),
					cursor.getString(cursor.getColumnIndex(KEY_NATIONALITY)),
					cursor.getInt(cursor.getColumnIndex(KEY_PLAY_NUM)),
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

	public void setRemovedChoices(int packageId, int levelId, String choices) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_REMOVED_CHOICES
				+ " = '" + choices + "' WHERE " + KEY_ID + " = " + levelId
				+ ";";
		db.execSQL(sql);
		sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_REMOVE_NUM + " = "
				+ KEY_REMOVE_NUM + " + 1 WHERE " + KEY_ID + " = " + packageId
				+ ";";
		db.execSQL(sql);
		db.close();
	}

	public void setCorrectAnswer(int packageId, int levelId, boolean flag) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_STATE + " = "
				+ myContex.getResources().getInteger(R.integer.STATE_CORRECT)
				+ " WHERE " + KEY_ID + " = " + levelId + ";";
		db.execSQL(sql);
		sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_LEVEL_COMPLETED
				+ " = " + KEY_LEVEL_COMPLETED + " + 1 " + " WHERE " + KEY_ID
				+ " = " + packageId + ";";
		db.execSQL(sql);
		if (flag) {
			sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_FIRST_SHOT
					+ " = " + KEY_FIRST_SHOT + " + 1 " + " WHERE " + KEY_ID
					+ " = " + packageId + ";";
			db.execSQL(sql);
			sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_CORRECT_IN_RAW
					+ " = " + KEY_CORRECT_IN_RAW + " + 1;";
			db.execSQL(sql);
		} else {
			sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_CORRECT_IN_RAW
					+ " = 0;";
			db.execSQL(sql);
		}
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
			ret.put(KEY_REMOVED_CHOICES, cursor.getString(cursor
					.getColumnIndex(KEY_REMOVED_CHOICES)));
		}
		cursor.close();
		db.close();
		return ret;
	}

	public void addScore(int _score) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_POINT + " = "
				+ KEY_POINT + " + " + _score + ";";
		db.execSQL(sql);
		db.close();
	}
	
	public void addScoreLevel(int _score, int level) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_POINT + " = "
				+ _score + " WHERE " + KEY_ID + " = " + level + ";";
		db.execSQL(sql);
		db.close();
	}

	public void updatePackageInfoData(int packageId, int score, int coins) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_COINS
				+ " = " + KEY_COINS + " + " + coins + ", " + KEY_POINT + " = "
				+ KEY_POINT + " + " + score + " WHERE " + KEY_ID + " = "
				+ packageId + ";";
		db.execSQL(sql);
		db.close();
	}

	public void addCoins(int _coins) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_COINS + " = "
				+ KEY_COINS + " + " + _coins + ";";
		db.execSQL(sql);
		db.close();
	}

	public void usedFreez(int packageId, int levelId, long time) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_USED_FREEZ
				+ " = " + time + " WHERE " + KEY_ID + " = " + levelId + ";";
		db.execSQL(sql);
		sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_FREEZE_NUM + " = "
				+ KEY_FREEZE_NUM + " + 1 WHERE " + KEY_ID + " = " + packageId
				+ ";";
		db.execSQL(sql);
		db.close();
	}

	public void usedSkip(int packageId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_SKIP_NUM
				+ " = " + KEY_SKIP_NUM + " + 1" + " WHERE " + KEY_ID + " = "
				+ packageId + ";";
		db.execSQL(sql);
		db.close();
	}

	public void usedHint(int packageId, int levelId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEVELS + " SET " + KEY_USED_HINTS
				+ " = 1 WHERE " + KEY_ID + " = " + levelId + ";";
		db.execSQL(sql);
		sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_HINT_NUM + " = "
				+ KEY_HINT_NUM + " + 1" + " WHERE " + KEY_ID + " = "
				+ packageId + ";";
		db.execSQL(sql);
		db.close();
	}

	public HashMap<String, String> getUser() {
		String selectQuery = "SELECT * FROM " + TABLE_USER + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		HashMap<String, String> ret = new HashMap<String, String>();
		if (cursor.moveToFirst()) {
			ret.put(KEY_ID, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_ID))));
			ret.put(KEY_API_KEY,
					cursor.getString(cursor.getColumnIndex(KEY_API_KEY)));
			ret.put(KEY_NAME, cursor.getString(cursor
					.getColumnIndex(KEY_NAME)));
			ret.put(KEY_PHONE_NUM, cursor.getString(cursor
					.getColumnIndex(KEY_PHONE_NUM)));
			ret.put(KEY_EMAIL, cursor.getString(cursor
					.getColumnIndex(KEY_EMAIL)));
			ret.put(KEY_IS_SENT, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_IS_SENT))));
		}
		cursor.close();
		db.close();
		return ret;
	}

	public void setUser(int id, String api_key) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_USER + " SET " + KEY_ID + " = " + id
				+ ", " + KEY_API_KEY + " = '" + api_key + "';";
		db.execSQL(sql);
		db.close();
	}

	public void setUserData(String name, String phone, String email) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_USER + " SET " + KEY_NAME + " = '"
				+ name + "', " + KEY_PHONE_NUM + " = '" + phone + "', "
				+ KEY_EMAIL + " = '" + email + "';";
		db.execSQL(sql);
		db.close();
	}

	public void setUserIsNoSent() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_USER + " SET " + KEY_IS_SENT + " = 1;";
		db.execSQL(sql);
		db.close();
	}

	public void setUserIsSent() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_USER + " SET " + KEY_IS_SENT + " = 2;";
		db.execSQL(sql);
		db.close();
	}

	public void setPackageIsFinish(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_IS_SENT
				+ " = " + 1 + " WHERE " + KEY_ID + " = " + id + ";";
		db.execSQL(sql);
		db.close();
	}

	public void setPackageIsSent(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_PACKAGE_INFO + " SET " + KEY_IS_SENT
				+ " = " + 2 + " WHERE " + KEY_ID + " = " + id + ";";
		db.execSQL(sql);
		db.close();
	}

	public ArrayList<HashMap<String, String>> getUnSentPackages() {
		String selectQuery = "SELECT * FROM " + TABLE_PACKAGE_INFO + " WHERE "
				+ KEY_IS_SENT + " = " + 1 + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hash;
		if (cursor.moveToFirst()) {
			do {
				hash = new HashMap<String, String>();
				hash.put(KEY_ID, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_ID))));
				hash.put(KEY_POINT, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_POINT))));
				hash.put(KEY_COINS, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_COINS))));
				hash.put(KEY_FIRST_SHOT, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_FIRST_SHOT))));
				hash.put(KEY_SKIP_NUM, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_SKIP_NUM))));
				hash.put(KEY_FREEZE_NUM, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_FREEZE_NUM))));
				hash.put(KEY_REMOVE_NUM,
						cursor.getString(cursor.getColumnIndex(KEY_REMOVE_NUM)));
				hash.put(KEY_HINT_NUM,
						cursor.getString(cursor.getColumnIndex(KEY_HINT_NUM)));
				ret.add(hash);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return ret;
	}

	public ArrayList<HashMap<String, String>> getUnSentAchievements() {
		String selectQuery = "SELECT * FROM " + TABLE_ACHIEVEMENTS + " WHERE "
				+ KEY_IS_SENT + " = " + 1 + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hash;
		if (cursor.moveToFirst()) {
			do {
				hash = new HashMap<String, String>();
				hash.put(KEY_ID, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_ID))));
				hash.put(KEY_TITLE, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_TITLE))));
				hash.put(KEY_BODY, String.valueOf(cursor.getInt(cursor
						.getColumnIndex(KEY_BODY))));
				ret.add(hash);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return ret;
	}

	public HashMap<String, String> getPackageInfo(int packageId) {
		String selectQuery = "SELECT * FROM " + TABLE_PACKAGE_INFO + " WHERE "
				+ KEY_ID + " = " + packageId + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		HashMap<String, String> hash = new HashMap<String, String>();
		if (cursor.moveToFirst()) {
			hash = new HashMap<String, String>();
			hash.put(KEY_ID, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_ID))));
			hash.put(KEY_LEVEL_COMPLETED, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_LEVEL_COMPLETED))));
			hash.put(KEY_POINT, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_POINT))));
			hash.put(KEY_COINS, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_COINS))));
			hash.put(KEY_FIRST_SHOT, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_FIRST_SHOT))));
			hash.put(KEY_SKIP_NUM, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_SKIP_NUM))));
			hash.put(KEY_FREEZE_NUM, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_FREEZE_NUM))));
			hash.put(KEY_REMOVE_NUM,
					cursor.getString(cursor.getColumnIndex(KEY_REMOVE_NUM)));
			hash.put(KEY_HINT_NUM,
					cursor.getString(cursor.getColumnIndex(KEY_HINT_NUM)));
		}
		cursor.close();
		db.close();
		return hash;
	}

	public HashMap<String, String> checkAchievements(int id) {
		String selectQuery = "SELECT * FROM " + TABLE_ACHIEVEMENTS + " WHERE "
				+ KEY_ID + " = " + id + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		HashMap<String, String> hash = new HashMap<String, String>();
		if (cursor.moveToFirst()) {
			hash.put(KEY_TITLE,
					cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
			hash.put(KEY_BODY,
					cursor.getString(cursor.getColumnIndex(KEY_BODY)));
			hash.put(KEY_IS_DONE, String.valueOf(cursor.getInt(cursor
					.getColumnIndex(KEY_IS_DONE))));
		}
		cursor.close();
		db.close();
		return hash;
	}

	public void setAchievement(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_ACHIEVEMENTS + " SET " + KEY_IS_DONE
				+ " = 1, " + KEY_IS_SENT + " = 1 WHERE " + KEY_ID + " = " + id
				+ ";";
		db.execSQL(sql);
		db.close();
	}

	public void setAchievementIsSent(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_ACHIEVEMENTS + " SET " + KEY_IS_SENT
				+ " = " + 2 + " WHERE " + KEY_ID + " = " + id + ";";
		db.execSQL(sql);
		db.close();
	}

	public void addFreeHint() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_FREE_HINT
				+ " = " + KEY_FREE_HINT + " + 1;";
		db.execSQL(sql);
		db.close();
	}

	public void addFreeRemove() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_FREE_REMOVE
				+ " = " + KEY_FREE_REMOVE + " + 1;";
		db.execSQL(sql);
		db.close();
	}

	public void addFreeFreeze() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_FREE_FREEZE
				+ " = " + KEY_FREE_FREEZE + " + 1;";
		db.execSQL(sql);
		db.close();
	}

	public void addFreeSkip() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_GAME_DATA + " SET " + KEY_FREE_SKIP
				+ " = " + KEY_FREE_SKIP + " + 1;";
		db.execSQL(sql);
		db.close();
	}
	
	public ArrayList<HashMap<String, Integer>> getLevelStat(int l) {
		String selectQuery = "SELECT * FROM " + TABLE_LEVELS + " WHERE "
				+ KEY_LEVEL + " = " + l + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<HashMap<String, Integer>> ret = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer> hash;
		if (cursor.moveToFirst()) {
			do {
				hash = new HashMap<String, Integer>();
				hash.put(KEY_TYPE,
						cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
				hash.put(KEY_POINT,
						cursor.getInt(cursor.getColumnIndex(KEY_POINT)));
				ret.add(hash);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return ret;
	}
	
	public ArrayList<HashMap<String, Integer>> getAllLevel() {
		String selectQuery = "SELECT * FROM " + TABLE_LEVELS +  ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<HashMap<String, Integer>> ret = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer> hash;
		if (cursor.moveToFirst()) {
			do {
				hash = new HashMap<String, Integer>();
				hash.put(KEY_TYPE,
						cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
				hash.put(KEY_POINT,
						cursor.getInt(cursor.getColumnIndex(KEY_POINT)));
				ret.add(hash);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return ret;
	}
}