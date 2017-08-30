package com.demo.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_BOOK = "create table Book (id integer primary key autoincrement, author text, price real, pages integer, name text)";
    private static final String CREATE_CATEGORY = "create table Category (id integer primary key autoincrement, category_name text, category_code, integer)";
    private static final String DROP_BOOK = "drop table if exists Book";
    private static final String DROP_CATEGORY = "drop table if exists Category";

    MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_BOOK);
        db.execSQL(DROP_CATEGORY);
        onCreate(db);
    }
}
