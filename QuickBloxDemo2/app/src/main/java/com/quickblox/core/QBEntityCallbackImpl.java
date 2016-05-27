package com.quickblox.core;

import android.os.Bundle;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

@Deprecated
public class QBEntityCallbackImpl implements QBEntityCallback {

   public void onSuccess(Object result, Bundle params) {}

   public void onError(QBResponseException responseException) {}
}
