//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.core;

import android.os.Bundle;
import com.quickblox.core.exception.QBResponseException;

public interface QBEntityCallback<T> {
    void onSuccess(T var1, Bundle var2);

    void onError(QBResponseException var1);
}
