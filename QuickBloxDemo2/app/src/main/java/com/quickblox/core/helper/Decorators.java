package com.quickblox.core.helper;

import com.quickblox.core.helper.Lo;

public class Decorators {

   public static Object requireNonNull(Object obj, String message) {
      if(obj == null) {
         throw new NullPointerException(message);
      } else {
         return obj;
      }
   }

   public static Object requireNonNullInRuntime(Object obj, String message) {
      if(obj == null) {
         throw new RuntimeException(message);
      } else {
         return obj;
      }
   }

   public static boolean logIfNull(Object obj, String message) {
      if(obj == null) {
         Lo.e(message);
         return true;
      } else {
         return false;
      }
   }
}
