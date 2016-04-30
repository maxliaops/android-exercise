package com.rainsong.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "zhihudaily.db";
    public static final int DB_VERSION = 1;

    public static final String NEWS_TABLE_NAME = "news_list";
    public static final String NEWS_COLUMN_ID = "_id";
    public static final String NEWS_COLUMN_TYPE = "type";
    public static final String NEWS_COLUMN_KEY = "key";
    public static final String NEWS_COLUMN_CONTENT = "content";

    private static final String NEWS_TABLE_CREATE = "CREATE TABLE "
            + NEWS_TABLE_NAME + "(" + NEWS_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NEWS_COLUMN_TYPE
            + " INTEGER NOT NULL, " + NEWS_COLUMN_KEY
            + " CHAR(256) UNIQUE NOT NULL, " + NEWS_COLUMN_CONTENT
            + " TEXT NOT NULL);";

    private volatile static DatabaseHelper mDBHelper;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mDBHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (mDBHelper == null) {
                    mDBHelper = new DatabaseHelper(context);
                }
            }
        }

        return mDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NEWS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE_NAME);
        onCreate(db);
    }

}
