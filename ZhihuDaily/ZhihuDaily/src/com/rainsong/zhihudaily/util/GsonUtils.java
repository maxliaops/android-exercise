package com.rainsong.zhihudaily.util;

import java.util.ArrayList;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.rainsong.zhihudaily.entity.NewsListEntity;
import com.rainsong.zhihudaily.entity.NewsListEntity.NewsEntity;

public class GsonUtils {
    public static ArrayList<NewsEntity> getNewsList(String content) {

        if (TextUtils.isEmpty(content))
            return null;

        Gson gson = new Gson();

        try {
            NewsListEntity newsListEntity = gson.fromJson(content,
                    NewsListEntity.class);
            return newsListEntity != null ? newsListEntity.stories : null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析一个字符串，得到BaseEntity对象
     * 
     * @param content
     * @param clazz
     * @return
     */
    public static Object getEntity(String content, Class<?> clazz) {

        if (TextUtils.isEmpty(content))
            return null;

        Gson gson = new Gson();

        try {
            Object baseEntity = (Object) gson.fromJson(content, clazz);
            return baseEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
