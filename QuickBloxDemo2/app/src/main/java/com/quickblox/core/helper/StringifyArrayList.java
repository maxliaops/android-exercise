//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.core.helper;

import android.text.TextUtils;
import com.quickblox.core.helper.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class StringifyArrayList<T> extends ArrayList<T> {
    public StringifyArrayList() {
    }

    public StringifyArrayList(Collection<? extends T> collection) {
        super(collection);
    }

    public void add(T... items) {
        this.addAll(Arrays.asList(items));
    }

    public String getItemsAsString() {
        return TextUtils.join(",", this);
    }

    public String getItemsAsStringOrNull() {
        String str = "";
        if(this.size() > 0) {
            str = TextUtils.join(",", this);
        }

        if(StringUtils.isEmpty(str)) {
            str = null;
        }

        return str;
    }
}
