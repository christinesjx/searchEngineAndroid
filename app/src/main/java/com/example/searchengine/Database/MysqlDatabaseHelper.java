package com.example.searchengine.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MysqlDatabaseHelper extends SQLiteOpenHelper{


    public static final String SMARTWATCH_DATABASE = "smartwatch";
    public static final String ACTIVFIT_TABLE = "activfit";
    public static final String ACTIVITY_TABLE = "activity";
    public static final String BATTERY_TABLE = "battery";
    public static final String BLUETOOTH_TABLE = "bluetooth";
    public static final String HEARTRATE_TABLE = "heartrate";

    public static final String ACTIVFIT_COL_1 = "start_time";
    public static final String ACTIVFIT_COL_2 = "end_time";
    public static final String ACTIVFIT_COL_3 = "timestamp";
    public static final String ACTIVFIT_COL_4 = "activity";
    public static final String ACTIVFIT_COL_5 = "duration";






    public MysqlDatabaseHelper(@Nullable Context context) {
        super(context, SMARTWATCH_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ACTIVFIT_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,start_time TEXT,end_time TEXT,activity TEXT,duration TEXT)");
        db.execSQL("create table " + ACTIVITY_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,MARKS INTEGER)");
        db.execSQL("create table " + BATTERY_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,MARKS INTEGER)");
        db.execSQL("create table " + BLUETOOTH_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,MARKS INTEGER)");
        db.execSQL("create table " + HEARTRATE_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,MARKS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ACTIVFIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ACTIVITY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BATTERY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BLUETOOTH_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+HEARTRATE_TABLE);
        onCreate(db);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ACTIVFIT_TABLE,null);
        return res;
    }

    public boolean insertDataACTIVFIT(String startTime,String endTime,String timestamp, String activity, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACTIVFIT_COL_1,startTime);
        contentValues.put(ACTIVFIT_COL_2,endTime);
        contentValues.put(ACTIVFIT_COL_3,timestamp);
        contentValues.put(ACTIVFIT_COL_4,activity);
        contentValues.put(ACTIVFIT_COL_5,duration);
        long result = db.insert(ACTIVFIT_TABLE,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


}
