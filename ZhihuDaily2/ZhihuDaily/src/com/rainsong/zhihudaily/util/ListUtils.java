package com.rainsong.zhihudaily.util;

import java.util.List;

public class ListUtils {
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }
}
