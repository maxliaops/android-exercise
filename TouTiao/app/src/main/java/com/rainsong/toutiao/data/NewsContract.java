package com.rainsong.toutiao.data;

import android.provider.BaseColumns;

/**
 * Created by maxliaops on 17-2-13.
 */

public final class NewsContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public NewsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_NAME_UNIQUE_KEY = "uniquekey";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_AUTHOR_NAME = "author_name";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_THUMBNAIL_PIC_S = "thumbnail_pic_s";
        public static final String COLUMN_NAME_THUMBNAIL_PIC_S02 = "thumbnail_pic_s02";
        public static final String COLUMN_NAME_THUMBNAIL_PIC_S03 = "thumbnail_pic_s03";
    }
}
