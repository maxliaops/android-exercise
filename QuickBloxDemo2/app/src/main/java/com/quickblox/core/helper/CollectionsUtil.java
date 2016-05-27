package com.quickblox.core.helper;

import java.util.Collection;
import java.util.Map;

public class CollectionsUtil {

   public static boolean isEmpty(Collection collection) {
      return collection == null || collection.isEmpty();
   }

   public static boolean isEmpty(Map map) {
      return map == null || map.isEmpty();
   }
}
