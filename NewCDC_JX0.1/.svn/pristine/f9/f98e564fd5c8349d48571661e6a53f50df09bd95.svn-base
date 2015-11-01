package com.newcdc.chat.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int VERSION = 2;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public void onCreate(SQLiteDatabase db) {
		// 创建数据库sql语句
		// 成色列表
		String sql1 = "create table friend(userId integer primary key ,userName varchar(50),userIcon varchar(100),userState Boolean)";
		String sql2 = "create table message(msgId integer primary key autoincrement,msgType Boolean,msgContent varchar(100),msgTime varchar(100),msgStatus Boolean,userId integer, foreign key(userId) references friend(userId))";
		// 执行创建数据库表操作
		db.execSQL(sql1);
		db.execSQL(sql2);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
