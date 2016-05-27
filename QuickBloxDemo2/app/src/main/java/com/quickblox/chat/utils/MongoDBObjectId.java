package com.quickblox.chat.utils;

import android.os.Process;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MongoDBObjectId {

   final int _time = (int)(System.currentTimeMillis() / 1000L);
   final int _machine;
   final int _inc;
   boolean _new;
   private static AtomicInteger _nextInc = new AtomicInteger((new Random()).nextInt());
   private static final int _genmachine;


   public static MongoDBObjectId get() {
      return new MongoDBObjectId();
   }

   public MongoDBObjectId() {
      this._machine = _genmachine;
      this._inc = _nextInc.getAndIncrement();
      this._new = true;
   }

   public String toHexString() {
      StringBuilder buf = new StringBuilder(24);
      byte[] arr$ = this.toByteArray();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         byte b = arr$[i$];
         buf.append(String.format("%02x", new Object[]{Integer.valueOf(b & 255)}));
      }

      return buf.toString();
   }

   public static boolean isValid(String s) {
      if(s == null) {
         return false;
      } else {
         int len = s.length();
         if(len != 24) {
            return false;
         } else {
            for(int i = 0; i < len; ++i) {
               char c = s.charAt(i);
               if((c < 48 || c > 57) && (c < 97 || c > 102) && (c < 65 || c > 70)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public String toStringMongod() {
      byte[] b = this.toByteArray();
      StringBuilder buf = new StringBuilder(24);

      for(int i = 0; i < b.length; ++i) {
         int x = b[i] & 255;
         String s = Integer.toHexString(x);
         if(s.length() == 1) {
            buf.append("0");
         }

         buf.append(s);
      }

      return buf.toString();
   }

   public byte[] toByteArray() {
      byte[] b = new byte[12];
      ByteBuffer bb = ByteBuffer.wrap(b);
      bb.putInt(this._time);
      bb.putInt(this._machine);
      bb.putInt(this._inc);
      return b;
   }

   public String toString() {
      return this.toStringMongod();
   }

   static {
      try {
         int e;
         try {
            StringBuilder processPiece = new StringBuilder();
            Enumeration processId = NetworkInterface.getNetworkInterfaces();

            while(processId.hasMoreElements()) {
               NetworkInterface loader = (NetworkInterface)processId.nextElement();
               processPiece.append(loader.toString());
            }

            e = processPiece.toString().hashCode() << 16;
         } catch (Throwable var7) {
            e = (new Random()).nextInt() << 16;
         }

         int processId1 = (new Random()).nextInt();

         try {
            processId1 = Process.myPid();
         } catch (Throwable var6) {
            ;
         }

         ClassLoader loader1 = MongoDBObjectId.class.getClassLoader();
         int loaderId = loader1 != null?System.identityHashCode(loader1):0;
         StringBuilder sb = new StringBuilder();
         sb.append(Integer.toHexString(processId1));
         sb.append(Integer.toHexString(loaderId));
         int processPiece1 = sb.toString().hashCode() & '\uffff';
         _genmachine = e | processPiece1;
      } catch (Exception var8) {
         throw new RuntimeException(var8);
      }
   }
}
