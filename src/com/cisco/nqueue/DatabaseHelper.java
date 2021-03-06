package com.cisco.nqueue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 3;
	public static final String DATABASE_NAME = "check_in_records.db";
	public static final String RESTAURANT_ID = "restaurant_id";
	public static final String RESTAURANT_NAME = "restaurant_name";
	public static final String CLIENT_ID = "client_id";
	public static final String RANK = "rank";
	
	public static final String CHECK_IN_TABLE = "check_in_records";
	
	//sql for create restaurant_id and client_id 
	private static final String CREATE_TABLE =" CREATE TABLE "+CHECK_IN_TABLE+
			                                   " ("+ RESTAURANT_ID +" int NOT NULL ,"+
			                                         CLIENT_ID+" int NOT NULL ,"+
			                                         RANK+" int NOT NULL ,"+
			                                         RESTAURANT_NAME+" text NOT NULL"+
			                                   ");";
	
	DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
	}		          
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
		db.execSQL("DROP TABLE IF EXISTS " + CHECK_IN_TABLE);
	    onCreate(db);
		
	}
	
	
} 