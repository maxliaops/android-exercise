package com.quickblox.core.helper;

import android.os.Bundle;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

public class CommonUtils {

   public static void notifyEntityCallbackOnSuccess(Object result, Bundle bundle, QBEntityCallback entityCallback) {
      if(entityCallback != null) {
         entityCallback.onSuccess(result, bundle);
      }

   }

   public static void notifyEntityCallbackOnError(QBResponseException exc, QBEntityCallback entityCallback) {
      if(entityCallback != null && exc != null) {
         entityCallback.onError(exc);
      }

   }
}
