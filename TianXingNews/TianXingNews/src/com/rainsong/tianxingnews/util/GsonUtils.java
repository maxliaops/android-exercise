package com.rainsong.tianxingnews.util;

import com.google.gson.Gson;

import android.text.TextUtils;

public class GsonUtils {

    /**
     * 解析一个字符串，得到Object对象
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
