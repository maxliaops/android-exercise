package com.quickblox.users.helper;

import android.text.TextUtils;
import android.util.Log;
import com.quickblox.users.model.QBUser;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomDataObjectParserHelper {

   public static String parseCustomDataObjectToString(Object customDataObject) {
      String customData = "{";
      int capacity = customDataObject.getClass().getDeclaredFields().length;

      for(int i = 0; i < capacity; ++i) {
         Field field = customDataObject.getClass().getDeclaredFields()[i];
         Method[] arr$ = customDataObject.getClass().getMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            if((method.getName().startsWith("get") || method.getName().startsWith("is")) && method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
               try {
                  if(i == capacity - 1) {
                     customData = customData + field.getName() + ": " + getValueForStringType(field, method.invoke(customDataObject, new Object[0])) + "}";
                  } else {
                     customData = customData + field.getName() + ": " + getValueForStringType(field, method.invoke(customDataObject, new Object[0])) + ", ";
                  }
               } catch (IllegalAccessException var10) {
                  Log.e(QBUser.class.getSimpleName(), customDataObject.getClass().getName() + " must have public getters-methods for each field of this class!");
               } catch (InvocationTargetException var11) {
                  Log.e(QBUser.class.getSimpleName(), customDataObject.getClass().getName() + " must have public getters-methods for each field of this class!");
               }
            }
         }
      }

      return customData;
   }

   private static boolean isStringType(Field field) {
      return field.getType().equals(String.class);
   }

   private static Object getValueForStringType(Field field, Object object) {
      return isStringType(field) && object != null?"\"" + object + "\"":object;
   }

   public static Object parseStringToObject(Class customDataClass, String string) {
      Object newObject = null;

      try {
         newObject = customDataClass.newInstance();
      } catch (InstantiationException var15) {
         var15.printStackTrace();
      } catch (IllegalAccessException var16) {
         var16.printStackTrace();
      }

      if(TextUtils.isEmpty(string)) {
         return newObject;
      } else {
         JSONObject jsonObj = null;

         try {
            jsonObj = new JSONObject(string);
         } catch (JSONException var14) {
            var14.printStackTrace();
         }

         Field[] arr$ = customDataClass.getDeclaredFields();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];

            for(int j = 0; j < customDataClass.getMethods().length; ++j) {
               Method method = customDataClass.getMethods()[j];
               if(method.getName().startsWith("set") && method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                  try {
                     if(newObject != null && jsonObj != null) {
                        Object e = getValue(jsonObj.getString(field.getName()), newObject.getClass().getMethods()[j].getParameterTypes()[0]);
                        newObject.getClass().getMethods()[j].invoke(newObject, new Object[]{e});
                     }
                  } catch (IllegalAccessException var11) {
                     Log.e(QBUser.class.getSimpleName(), customDataClass.getName() + " must have public getters-methods for each field of this class!");
                  } catch (InvocationTargetException var12) {
                     Log.e(QBUser.class.getSimpleName(), customDataClass.getName() + " must have public getters-methods for each field of this class!");
                  } catch (JSONException var13) {
                     var13.printStackTrace();
                  }
               }
            }
         }

         return newObject;
      }
   }

   private static Object getValue(String key, Object value) {
      Object object = null;
      if(value.equals(Boolean.TYPE)) {
         object = Boolean.valueOf(Boolean.parseBoolean(key));
      } else if(value.equals(Integer.TYPE)) {
         object = Integer.valueOf(Integer.parseInt(key));
      } else if(value.equals(Float.TYPE)) {
         object = Float.valueOf(Float.parseFloat(key));
      } else if(value.equals(Long.TYPE)) {
         object = Long.valueOf(Long.parseLong(key));
      } else if(value.equals(String.class)) {
         object = key;
      }

      return object;
   }
}
