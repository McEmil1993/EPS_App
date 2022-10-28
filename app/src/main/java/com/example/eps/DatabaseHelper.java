package com.example.eps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="eps_db";
    public static final String USERS = "users_tbl";
    public static final String SCH = "sched_tbl";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS + "(id INTEGER PRIMARY KEY AUTOINCREMENT ,us_id,  fullname TEXT,  contact TEXT,  address TEXT, username TEXT, password TEXT,filename TEXT,user_type TEXT, status INTERGER)");
        db.execSQL("CREATE TABLE " + SCH + "(id INTEGER PRIMARY KEY AUTOINCREMENT ,sc_id,  name TEXT,  date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ USERS);
        db.execSQL("DROP TABLE IF EXISTS "+ SCH);
        onCreate(db);
    }

}