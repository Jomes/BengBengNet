package com.sohu.focus.framework.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class FocusDataBaseHelper extends SQLiteOpenHelper {

	private static final String SQL_CREATE_TABLE = "CREATE TABLE ";
	// houseadvisor 6  2014年12月30日10:30:07
	// houseadvisor 7  2015-3-4
	// houseapartment 8  2015-4-9
	public static final int DB_VERSION = 8; 

	public FocusDataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public static List<String[]> tableCreationStrings(String[][] createTable) {
		ArrayList<String[]> create = new ArrayList<String[]>(createTable.length);
		for (String[] line : createTable) {
			create.add(line);
		}
		return create;
	}

	public static void createTable(SQLiteDatabase db, String table, List<String[]> columns) {
		StringBuilder create = new StringBuilder(SQL_CREATE_TABLE);
		create.append(table).append('(');
		boolean first = true;
		for (String[] column : columns) {
			if (!first) {
				create.append(',');
			}
			first = false;
			for (String val : column) {
				create.append(val).append(' ');
			}
		}
		create.append(')');
		db.beginTransaction();
		try {
			db.execSQL(create.toString());
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static void dropTable(SQLiteDatabase db, String table) {
		db.beginTransaction();
		try {
			db.execSQL("drop table if exists " + table);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}
}
