package com.quickblox.core.helper;

import android.util.Pair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ToStringHelper {

   public static final String SEPARATOR = "\n";
   private static final String ARROW = "=";
   public static final String COMMA_SEPARATOR = ",";


   public static String toString(List l) {
      if(l == null) {
         return "null";
      } else {
         StringBuilder sb = new StringBuilder("(");
         String sep = "";

         for(Iterator i$ = l.iterator(); i$.hasNext(); sep = "\n") {
            Object object = i$.next();
            sb.append(sep).append(object.toString());
         }

         return sb.append(")").toString();
      }
   }

   public static String toString(List l, String separator, String leftDelim, String rightDelim) {
      if(l == null) {
         return "null";
      } else {
         StringBuilder sb = new StringBuilder(leftDelim);
         String sep = "";

         for(Iterator i$ = l.iterator(); i$.hasNext(); sep = separator) {
            Object object = i$.next();
            sb.append(sep).append(object.toString());
         }

         return sb.append(rightDelim).toString();
      }
   }

   public static String toString(List l, String separator) {
      return toString(l, separator, "", "");
   }

   public static String toString(Map m, String prefix) {
      return toString(m, prefix, "\n");
   }

   public static String toString(Map m, String prefix, String separator) {
      return toStringBuilder(m, prefix, separator).toString();
   }

   public static StringBuilder toStringBuilder(Map m, String prefix, String separator) {
      StringBuilder sb = new StringBuilder();
      if(m == null) {
         sb.append("null");
         return sb;
      } else {
         String sep = "";

         for(Iterator i$ = m.keySet().iterator(); i$.hasNext(); sep = separator) {
            Object object = i$.next();
            Object value = m.get(object);
            sb.append(sep).append(prefix).append(object.toString()).append("=").append(value.toString());
         }

         return sb;
      }
   }

   public static String toStringPairList(List pairList, String prefix) {
      return toStringPairList(pairList, prefix, "\n");
   }

   public static String toStringPairList(List pairList, String prefix, String separator) {
      if(pairList == null) {
         return "null";
      } else {
         StringBuilder sb = new StringBuilder();
         String sep = "";

         for(Iterator i$ = pairList.iterator(); i$.hasNext(); sep = separator) {
            Pair item = (Pair)i$.next();
            sb.append(sep).append(prefix).append(String.valueOf(item.first)).append("=").append(String.valueOf(item.second));
         }

         return sb.toString();
      }
   }

   public static String toParamString(String[] keys, Object[] values, String separator) {
      if(keys == null) {
         return "null";
      } else {
         HashMap stringObjectMap = new HashMap();

         for(int i = 0; i < keys.length && i < values.length; ++i) {
            Object value = values[i];
            if(value != null) {
               stringObjectMap.put(keys[i], value);
            }
         }

         return toString(stringObjectMap, "", separator);
      }
   }

   public static String toString(Set s) {
      if(s == null) {
         return "null";
      } else {
         StringBuilder sb = new StringBuilder("{");
         String sep = "";

         for(Iterator i$ = s.iterator(); i$.hasNext(); sep = "\n") {
            Object object = i$.next();
            sb.append(sep).append(object.toString());
         }

         return sb.append("}").toString();
      }
   }

   public static String arrayToString(Object ... values) {
      StringBuilder arrayString = new StringBuilder();
      String delimiter = "";
      if(values != null && values.length > 0) {
         Object[] arr$ = values;
         int len$ = values.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object obj = arr$[i$];
            arrayString.append(delimiter);
            arrayString.append(obj);
            delimiter = ",";
         }
      }

      return arrayString.toString();
   }
}
