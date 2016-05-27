package com.quickblox.videochat.webrtc.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

   public static Object newInstance(Class clazz) {
      try {
         Object e = clazz.newInstance();
         return e;
      } catch (InstantiationException var2) {
         var2.printStackTrace();
      } catch (IllegalAccessException var3) {
         var3.printStackTrace();
      }

      return null;
   }

   public static void setClassField(Class aClass, String fieldName, Object value) {
      setObjectField(aClass, (Object)null, fieldName, value);
   }

   public static void setObjectField(Class aClass, Object instance, String fieldName, Object value) {
      try {
         Field e = aClass.getDeclaredField(fieldName);
         if(e != null) {
            e.setAccessible(true);
            e.set(instance, value);
         }
      } catch (NoSuchFieldException var5) {
         var5.printStackTrace();
      } catch (IllegalAccessException var6) {
         var6.printStackTrace();
      }

   }
}
