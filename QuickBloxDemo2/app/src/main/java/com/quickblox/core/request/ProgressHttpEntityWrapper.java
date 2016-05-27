package com.quickblox.core.request;

import com.quickblox.core.QBProgressCallback;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ProgressHttpEntityWrapper {

   private final QBProgressCallback progressCallback;
   private HttpURLConnection conn;
   private int totalLength;


   public ProgressHttpEntityWrapper(HttpURLConnection conn, QBProgressCallback progressCallback, int totalLength) {
      this.progressCallback = progressCallback;
      this.conn = conn;
      this.totalLength = totalLength;
   }

   public InputStream getInputStream() throws IOException {
      return new ProgressHttpEntityWrapper.ProgressFilterInputStream(this.conn.getInputStream(), this.progressCallback, (long)this.totalLength);
   }

   public OutputStream getOutputStream() throws IOException {
      return new ProgressHttpEntityWrapper.ProgressFilterOutputStream(this.conn.getOutputStream(), this.progressCallback, (long)this.totalLength);
   }

   static class ProgressFilterInputStream extends FilterInputStream {

      private QBProgressCallback progressCallback;
      private long received;
      private long totalBytes;
      private int previousProgress = -1;


      ProgressFilterInputStream(InputStream in, QBProgressCallback progressCallback, long totalBytes) {
         super(in);
         this.progressCallback = progressCallback;
         this.received = 0L;
         this.totalBytes = totalBytes;
      }

      public int read(byte[] b, int off, int len) throws IOException {
         int result = this.in.read(b, off, len);
         if(result == -1) {
            return result;
         } else {
            this.received += (long)len;
            int currentProgress = (int)this.getCurrentProgress();
            if(currentProgress > this.previousProgress) {
               this.progressCallback.onProgressUpdate(currentProgress);
            }

            this.previousProgress = currentProgress;
            return result;
         }
      }

      public int read(byte[] buffer) throws IOException {
         int result = this.in.read(buffer);
         if(result == -1) {
            return result;
         } else {
            this.received += (long)result;
            int currentProgress = (int)this.getCurrentProgress();
            if(currentProgress > this.previousProgress) {
               this.progressCallback.onProgressUpdate(currentProgress);
            }

            this.previousProgress = currentProgress;
            return result;
         }
      }

      private float getCurrentProgress() {
         return (float)this.received / (float)this.totalBytes * 100.0F;
      }
   }

   static class ProgressFilterOutputStream extends FilterOutputStream {

      private QBProgressCallback progressCallback;
      private long transferred;
      private long totalBytes;
      private int previousProgress = -1;


      ProgressFilterOutputStream(OutputStream out, QBProgressCallback progressCallback, long totalBytes) {
         super(out);
         this.progressCallback = progressCallback;
         this.transferred = 0L;
         this.totalBytes = totalBytes;
      }

      public void write(byte[] b, int off, int len) throws IOException {
         this.out.write(b, off, len);
         this.transferred += (long)len;
         int currentProgress = this.getCurrentProgress();
         if(currentProgress > this.previousProgress) {
            this.progressCallback.onProgressUpdate(currentProgress);
         }

         this.previousProgress = currentProgress;
      }

      public void write(int b) throws IOException {
         this.out.write(b);
         ++this.transferred;
         int currentProgress = this.getCurrentProgress();
         if(currentProgress > this.previousProgress) {
            this.progressCallback.onProgressUpdate(currentProgress);
         }

         this.previousProgress = currentProgress;
      }

      private int getCurrentProgress() {
         return (int)(this.transferred * 100L / this.totalBytes);
      }
   }
}
