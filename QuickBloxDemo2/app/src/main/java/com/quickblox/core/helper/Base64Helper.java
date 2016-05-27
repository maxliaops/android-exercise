package com.quickblox.core.helper;

import android.util.Base64;
import java.io.UnsupportedEncodingException;

public class Base64Helper {

   public static String encode(String string) {
      return encode(string, "UTF-8");
   }

   public static String encode(String string, String charsetName) {
      byte[] pushTestBytes;
      try {
         pushTestBytes = string.getBytes(charsetName);
      } catch (UnsupportedEncodingException var4) {
         var4.printStackTrace();
         throw new RuntimeException("Unsupported charset: " + charsetName);
      }

      return Base64.encodeToString(pushTestBytes, 2);
   }
}
