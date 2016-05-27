package com.quickblox.core.helper;

import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignHelper {

   private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";


   public static String calculateHMAC_SHA(String data, String key) throws SignatureException {
      String result = null;

      try {
         SecretKeySpec e = new SecretKeySpec(key.getBytes(), "HmacSHA1");
         Mac mac = Mac.getInstance("HmacSHA1");
         mac.init(e);
         byte[] digest = mac.doFinal(data.getBytes());
         StringBuilder sb = new StringBuilder(digest.length * 2);
         byte[] arr$ = digest;
         int len$ = digest.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte b = arr$[i$];
            String s = Integer.toHexString(255 & b);
            if(s.length() == 1) {
               sb.append('0');
            }

            sb.append(s);
         }

         result = sb.toString();
         return result;
      } catch (Exception var12) {
         throw new SignatureException("Failed to generate HMAC : " + var12.getMessage());
      }
   }
}
