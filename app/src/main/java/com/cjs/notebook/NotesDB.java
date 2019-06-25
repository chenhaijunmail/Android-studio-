package com.cjs.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDB extends SQLiteOpenHelper {

	public static final String TABLE_NAME_NOTES = "note";
	public static final String COLUMN_NAME_ID = "_id";
	public static final String COLUMN_NAME_NOTE_CONTENT = "content";
	public static final String COLUMN_NAME_NOTE_DATE = "date";

	public NotesDB(Context context) {
		super(context, "note.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE note (" +
                "    _id     INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    content TEXT," +
                "    date    TEXT" +
                ")";
		Log.d("SQL1", sql);
		db.execSQL(sql);
//		String sql1="insert into "+TABLE_NAME_NOTES+"values("+"1,"+"'写作业',"+"'晚上要写作业的干活'"+")";
//		Log.d("SQL1", sql1);
//		db.execSQL(sql1);
		ContentValues values=new ContentValues();
		values.put("id",1);
		values.put("content","写作业");
		values.put("date", "2013-1-2");
		db.insert("note", null, values);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
