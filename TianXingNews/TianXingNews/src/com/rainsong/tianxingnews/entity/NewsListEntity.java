package com.rainsong.tianxingnews.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsListEntity implements Serializable {

    private static final long serialVersionUID = -8745847207188927701L;

    public String code;
    public String msg;
    public ArrayList<NewsEntity> newslist;

    public static class NewsEntity implements Serializable {

        private static final long serialVersionUID = 1L;

        public String ctime;
        public String title;
        public String description;
        public String picUrl;
        public String url;
    }
}
