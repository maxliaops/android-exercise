package com.quickblox.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

   private static final int DEFAULT_BUFFER_SIZE = 4096;


   public static int copy(InputStream input, OutputStream output) throws IOException {
      long count = copyLarge(input, output);
      return count > 2147483647L?-1:(int)count;
   }

   public static long copyLarge(InputStream input, OutputStream output) throws IOException {
      byte[] buffer = new byte[4096];
      long count = 0L;

      int n1;
      for(boolean n = false; -1 != (n1 = input.read(buffer)); count += (long)n1) {
         output.write(buffer, 0, n1);
      }

      return count;
   }

   public static void closeInputStreams(InputStream ... args) {
      InputStream[] arr$ = args;
      int len$ = args.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         InputStream is = arr$[i$];

         try {
            if(is != null) {
               is.close();
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

   }

   public static void closeOutputStreams(OutputStream ... args) {
      OutputStream[] arr$ = args;
      int len$ = args.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         OutputStream outputStream = arr$[i$];

         try {
            if(outputStream != null) {
               outputStream.close();
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

   }
}
