package com.rainsong.toutiao.util;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by maxliaops on 17-1-12.
 */

public class GsonUtils {
    /**
     * 解析一个字符串，得到BaseEntity对象O
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
