package com.rainsong.zhihudaily.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.rainsong.zhihudaily.entity.NewsListEntity;
import com.rainsong.zhihudaily.util.GsonUtils;

public class NewsDataSource {

    public static int NEWS_LIST = 1;

    private SQLiteDatabase mDb;

    private String[] allColumns = { DatabaseHelper.NEWS_COLUMN_ID,
            DatabaseHelper.NEWS_COLUMN_TYPE, DatabaseHelper.NEWS_COLUMN_KEY,
            DatabaseHelper.NEWS_COLUMN_CONTENT };

    public NewsDataSource(DatabaseHelper dbHelper) {
        mDb = dbHelper.getWritableDatabase();
    }

    private void insertDailyNewsList(int type, String key, String content) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NEWS_COLUMN_TYPE, type);
        values.put(DatabaseHelper.NEWS_COLUMN_KEY, key);
        values.put(DatabaseHelper.NEWS_COLUMN_CONTENT, content);

        mDb.insert(DatabaseHelper.NEWS_TABLE_NAME, null, values);
    }

    private void updateNewsList(int type, String key, String content) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NEWS_COLUMN_TYPE, type);
        values.put(DatabaseHelper.NEWS_COLUMN_KEY, key);
        values.put(DatabaseHelper.NEWS_COLUMN_CONTENT, content);
        mDb.update(DatabaseHelper.NEWS_TABLE_NAME, values,
                DatabaseHelper.NEWS_COLUMN_KEY + "='" + key + "'", null);
    }

    private boolean isContentExist(String key) {
        boolean result = false;

        Cursor cursor = mDb.query(DatabaseHelper.NEWS_TABLE_NAME, allColumns,
                DatabaseHelper.NEWS_COLUMN_KEY + " = '" + key + "'", null,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            String content = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.NEWS_COLUMN_CONTENT));
            result = !TextUtils.isEmpty(content);
        }

        cursor.close();
        return result;
    }

    public String getContent(String key) {
        Cursor cursor = mDb.query(DatabaseHelper.NEWS_TABLE_NAME, allColumns,
                DatabaseHelper.NEWS_COLUMN_KEY + " = '" + key + "'", null,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            String content = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.NEWS_COLUMN_CONTENT));
            cursor.close();
            return content;
        } else {
            return null;
        }
    }

    public void insertOrUpdateNewsList(int type, String key, String content) {
        if (TextUtils.isEmpty(content))
            return;

        if (isContentExist(key)) {
            updateNewsList(type, key, content);
        } else {
            insertDailyNewsList(type, key, content);
        }
    }

    /**
     * 获取新闻表中，日期最新那天的数据
     * 
     * @return
     */
    public NewsListEntity getLatestNews() {

        NewsListEntity newsListEntity = null;

        String orderBy = DatabaseHelper.NEWS_COLUMN_KEY + " desc";

        Cursor cursor = mDb.query(DatabaseHelper.NEWS_TABLE_NAME, allColumns,
                DatabaseHelper.NEWS_COLUMN_TYPE + "=" + NEWS_LIST, null, null,
                null, orderBy);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            String content = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.NEWS_COLUMN_CONTENT));
            newsListEntity = (NewsListEntity) GsonUtils.getEntity(content,
                    NewsListEntity.class);
        }

        cursor.close();

        return newsListEntity;
    }
}
