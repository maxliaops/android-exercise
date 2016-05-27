package com.quickblox.core.helper;

import android.util.Log;
import com.quickblox.core.LogLevel;
import com.quickblox.core.QBSettings;

public class Lo {

   private static String DEFAULT_TAG = "QBASDK";
   private static final int ENTRY_MAX_LEN = 4000;


   public static void g(Object logString) {
      if(logString != null) {
         if(QBSettings.getInstance().getLogLevel() == LogLevel.DEBUG) {
            Log.d(DEFAULT_TAG, logString.toString());
         }

      }
   }

   public static void g(String logFormat, Object ... args) {
      g(String.format(logFormat, args));
   }

   public static void e(Object logString) {
      if(logString != null) {
         if(QBSettings.getInstance().getLogLevel() == LogLevel.DEBUG) {
            Log.e(DEFAULT_TAG, logString.toString());
         }

      }
   }

}
