package com.rainsong.tiantiannews.entity;

import android.os.Parcel;
import android.os.Parcelable;

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

//            @Override
//            public int describeContents() {
//                return 0;
//            }
//
//            @Override
//            public void writeToParcel(Parcel dest, int flags) {
//                dest.writeString(uniquekey);
//                dest.writeString(title);
//                dest.writeString(date);
//                dest.writeString(category);
//                dest.writeString(author_name);
//                dest.writeString(url);
//            }
//
//            public static final Creator<NewsEntity> CREATOR = new Creator<NewsEntity>() {
//
//                @SuppressWarnings("unchecked")
//                // Override
//                public NewsEntity createFromParcel(Parcel source) {
//                    NewsEntity newsEntity = new NewsEntity();
//                    newsEntity.uniquekey = source.readString();
//                    newsEntity.title = source.readString();
//                    newsEntity.date = source.readString();
//                    newsEntity.category = source.readString();
//                    newsEntity.author_name = source.readString();
//                    newsEntity.url = source.readString();
//                    return newsEntity;
//                }
//                // Override
//                public NewsEntity[] newArray(int size) {
//                    return new NewsEntity[size];
//                }
//
//            };
        }

        public List<NewsEntity> getData() {
            return this.data;
        }

    }
}
