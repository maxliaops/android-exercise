package com.quickblox.core;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class URLEncodedUtils {

   public static Map parse(URI uri) throws UnsupportedEncodingException {
      LinkedHashMap parameters = new LinkedHashMap();
      String[] pairs = uri.getQuery().split("&");
      String[] arr$ = pairs;
      int len$ = pairs.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String pair = arr$[i$];
         int idx = pair.indexOf("=");
         String key = idx > 0?URLDecoder.decode(pair.substring(0, idx), "UTF-8"):pair;
         String value = idx > 0 && pair.length() > idx + 1?URLDecoder.decode(pair.substring(idx + 1), "UTF-8"):null;
         parameters.put(key, value);
      }

      return parameters;
   }
}
