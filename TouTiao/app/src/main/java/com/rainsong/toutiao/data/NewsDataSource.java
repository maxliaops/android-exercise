package com.rainsong.toutiao.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.rainsong.toutiao.bean.NewsListBean.ResultBean.DataBean;
import com.rainsong.toutiao.data.NewsContract.NewsEntry;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by maxliaops on 17-2-13.
 */

public class NewsDataSource {
    private static NewsDataSource INSTANCE;

    private NewsDbHelper mDbHelper;

    // Prevent direct instantiation.
    private NewsDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new NewsDbHelper(context);
    }

    public static NewsDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NewsDataSource(context);
        }
        return INSTANCE;
    }

    public void saveNews(@NonNull DataBean newsBean) {
        checkNotNull(newsBean);
        if(!isNewsExist(newsBean.getUniquekey())) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(NewsEntry.COLUMN_NAME_UNIQUE_KEY, newsBean.getUniquekey());
            values.put(NewsEntry.COLUMN_NAME_TITLE, newsBean.getTitle());
            values.put(NewsEntry.COLUMN_NAME_DATE, newsBean.getDate());
            values.put(NewsEntry.COLUMN_NAME_CATEGORY, newsBean.getCategory());
            values.put(NewsEntry.COLUMN_NAME_AUTHOR_NAME, newsBean.getAuthorName());
            values.put(NewsEntry.COLUMN_NAME_URL, newsBean.getUrl());
            values.put(NewsEntry.COLUMN_NAME_THUMBNAIL_PIC_S, newsBean.getThumbnail_pic_s());
            values.put(NewsEntry.COLUMN_NAME_THUMBNAIL_PIC_S02, newsBean.getThumbnail_pic_s02());
            values.put(NewsEntry.COLUMN_NAME_THUMBNAIL_PIC_S03, newsBean.getThumbnail_pic_s03());

            db.insert(NewsEntry.TABLE_NAME, null, values);

            db.close();
        }
    }

    private boolean isNewsExist(String uniquekey) {
        boolean result = false;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                NewsEntry.COLUMN_NAME_UNIQUE_KEY,
                NewsEntry.COLUMN_NAME_TITLE,
        };

        String selection = NewsEntry.COLUMN_NAME_UNIQUE_KEY + " == ?";
        String[] selectionArgs = {uniquekey};

        Cursor cursor = db.query(NewsEntry.TABLE_NAME, projection,
                selection, selectionArgs,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            result = true;

        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return result;
    }

}
