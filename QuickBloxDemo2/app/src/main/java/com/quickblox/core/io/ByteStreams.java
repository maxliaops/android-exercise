package com.quickblox.core.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteStreams {

   private static final int BUF_SIZE = 4096;


   public static byte[] toByteArray(InputStream in) throws IOException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      copy(in, out);
      return out.toByteArray();
   }

   public static long copy(InputStream from, OutputStream to) throws IOException {
      byte[] buf = new byte[4096];
      long total = 0L;

      while(true) {
         int r = from.read(buf);
         if(r == -1) {
            return total;
         }

         to.write(buf, 0, r);
         total += (long)r;
      }
   }
}
