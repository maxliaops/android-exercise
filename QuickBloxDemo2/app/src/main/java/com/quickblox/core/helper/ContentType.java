package com.quickblox.core.helper;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import com.quickblox.core.helper.MimeUtils;
import java.io.File;
import java.util.regex.Pattern;

public class ContentType {

   public static String getContentType(File file) {
      if(file != null) {
         String filePath = file.getAbsolutePath();
         String extension = getFileExtension(filePath);
         if(!TextUtils.isEmpty(extension)) {
            return getContentType(extension);
         }
      }

      return null;
   }

   private static String getFileExtension(String filePath) {
      int filenamePos = filePath.lastIndexOf(47);
      String filename = 0 <= filenamePos?filePath.substring(filenamePos + 1):filePath;
      if(!TextUtils.isEmpty(filename) && Pattern.matches("[a-zA-Z_0-9 \\.\\-\\(\\)\\%]+", filename)) {
         int dotPos = filename.lastIndexOf(46);
         if(0 <= dotPos) {
            return filename.substring(dotPos + 1);
         }
      }

      return "";
   }

   public static String getContentType(String extension) {
      MimeTypeMap mime = MimeTypeMap.getSingleton();
      String type = mime.getMimeTypeFromExtension(extension);
      if(type == null) {
         type = MimeUtils.guessMimeTypeFromExtension(extension);
         if(type == null) {
            type = "unknown";
         }
      }

      return type;
   }
}
