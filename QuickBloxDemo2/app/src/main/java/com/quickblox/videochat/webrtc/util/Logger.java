package com.quickblox.videochat.webrtc.util;

import android.util.Log;
import com.quickblox.videochat.webrtc.QBRTCConfig;

public class Logger {

   private static Logger INSTANCE = null;
   private final String prefixTag;


   public Logger(String prefixTag) {
      this.prefixTag = prefixTag;
   }

   public static Logger getInstance(String prefixName) {
      Class var1 = Logger.class;
      synchronized(Logger.class) {
         if(INSTANCE == null) {
            INSTANCE = new Logger(prefixName);
         }
      }

      return INSTANCE;
   }

   public void v(String tag, String msg) {
      if(QBRTCConfig.isDebugEnabled()) {
         Log.v(this.prefixTag + "." + tag, msg);
      }

   }

   public void d(String tag, String msg) {
      if(QBRTCConfig.isDebugEnabled()) {
         Log.d(this.prefixTag + "." + tag, msg);
      }

   }

   public void e(String tag, String msg) {
      if(QBRTCConfig.isDebugEnabled()) {
         Log.e(this.prefixTag + "." + tag, msg);
      }

   }

   public void w(String tag, String msg) {
      if(QBRTCConfig.isDebugEnabled()) {
         Log.w(this.prefixTag + "." + tag, msg);
      }

   }

}
