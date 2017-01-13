package com.rainsong.tiantiannews.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxliaops on 17-1-12.
 */

public class NewsListEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public String reason;
    public int error_code;
    public Result result;

    public static class Result implements Serializable {
        private static final long serialVersionUID = 1L;

        public String stat;
        public List<NewsEntity> data;

        public static class NewsEntity implements Serializable {
            private static final long serialVersionUID = 1L;

            public String uniquekey;
            public String title;
            public String date;
            public String category;
            public String author_name;
            public String url;
            public String thumbnail_pic_s;
            public String thumbnail_pic_s02;
            public String thumbnail_pic_s03;
        }

        public List<NewsEntity> getData() {
            return this.data;
        }

    }
}
