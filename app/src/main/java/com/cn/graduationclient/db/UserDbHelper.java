package com.cn.graduationclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class UserDbHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "database";
	private static final int USER_DATABASE_VISION = 1;
	public UserDbHelper(Context context) {
		super(context, DATABASE_NAME, null, USER_DATABASE_VISION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_table = "create table "+ UserColumns.USER_TABLE_NAME+" ("+ UserColumns.UID+" verchar(11) primary key,"
				+ UserColumns.NAME+" verchar(45) not null,"
				+ UserColumns.SIGNATURE+" verchar(500) not null,"
				+ UserColumns.SEX+" verchar(2) not null,"
				+ UserColumns.BIRTHDAY+" verchar(20) not null,"
				+ UserColumns.PROFESSION+" verchar(45) not null,"
				+ UserColumns.CITY+" verchar(11) not null,"
				+ UserColumns.EMAIL+" verchar(45) not null)";
		db.execSQL(create_table);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+ UserColumns.USER_TABLE_NAME);
		onCreate(db);
	}
	
	public static final class UserColumns implements BaseColumns {
		public UserColumns() {}
		public static final String USER_TABLE_NAME = "user";
		public static final String UID = "uid";
		public static final String NAME = "name";
		public static final String SIGNATURE="signature";
		public static final String SEX="sex";
		public static final String BIRTHDAY="birthday";
		public static final String PROFESSION="profession";
		public static final String CITY="city";
		public static final String EMAIL="email";
	}  
}
