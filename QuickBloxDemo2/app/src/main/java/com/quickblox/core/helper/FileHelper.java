package com.quickblox.core.helper;

import android.os.Environment;
import com.quickblox.core.io.ByteStreams;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {

   private static String SD_CARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();


   public static File getDirectory(String rootFolder) {
      File filesRoot = new File(SD_CARD_ROOT + "/" + rootFolder);
      if(!filesRoot.exists()) {
         boolean result = filesRoot.mkdir();
      }

      return filesRoot;
   }

   public static File getFileFromAsset(InputStream inputStream, String fileName, String assetsDirectory) {
      File filesRoot = getDirectory(assetsDirectory);

      try {
         File e = new File(filesRoot, fileName);
         FileOutputStream out = new FileOutputStream(e);
         copyFile(inputStream, out);
         out.close();
         return e;
      } catch (IOException var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public static void copyFile(InputStream is, OutputStream outputStream) {
      byte[] content = new byte[1024];
      BufferedInputStream bufferedInputStream = new BufferedInputStream(is);

      int e;
      try {
         while((e = bufferedInputStream.read(content)) != -1) {
            outputStream.write(content, 0, e);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public static File getFileInputStream(InputStream inputStream, String fileName, String rootFolder) {
      File filesRoot = getDirectory(rootFolder);

      try {
         File e = new File(filesRoot, fileName);
         byte[] content = ByteStreams.toByteArray(inputStream);
         FileOutputStream out = new FileOutputStream(e);
         out.write(content);
         out.close();
         return e;
      } catch (IOException var7) {
         var7.printStackTrace();
         return null;
      }
   }

}
