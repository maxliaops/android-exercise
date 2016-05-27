//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.core.task;

import android.os.Bundle;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBRequestCanceler;
import com.quickblox.core.Query;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.task.QueriesTaskAbs;

public abstract class QueriesTaskEntity<T> extends QueriesTaskAbs {
    protected QBEntityCallback<T> entityCallback;

    public QueriesTaskEntity(QBEntityCallback<T> callback) {
        this.canceler = new QBRequestCanceler((Query)null);
        this.entityCallback = callback;
    }

    public void completeTaskSuccess(T result, Bundle params) {
        if(this.entityCallback != null) {
            this.entityCallback.onSuccess(result, params);
        }

    }

    public void completeTaskErrors(QBResponseException exception) {
        if(this.entityCallback != null) {
            this.entityCallback.onError(exception);
        }

    }

    public QBRequestCanceler performTask() {
        return null;
    }
}
