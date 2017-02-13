package com.rainsong.tiantiannews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by maxliaops on 17-2-13.
 */

public class NewsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "News.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + " (" +
                    NewsContract.NewsEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    NewsContract.NewsEntry.COLUMN_NAME_UNIQUE_KEY + TEXT_TYPE + COMMA_SEP +
                    NewsContract.NewsEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    NewsContract.NewsEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    NewsContract.NewsEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    NewsContract.NewsEntry.COLUMN_NAME_AUTHOR_NAME + TEXT_TYPE + COMMA_SEP +
                    NewsContract.NewsEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    NewsContract.NewsEntry.COLUMN_NAME_THUMBNAIL_PIC_S + TEXT_TYPE + COMMA_SEP +
                    NewsContract.NewsEntry.COLUMN_NAME_THUMBNAIL_PIC_S02 + TEXT_TYPE + COMMA_SEP +
                    NewsContract.NewsEntry.COLUMN_NAME_THUMBNAIL_PIC_S03 + TEXT_TYPE +
                    " )";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
