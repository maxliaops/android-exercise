package com.quickblox.videochat.webrtc.view;

import android.graphics.Bitmap;

class GraphicsConverter {

   public static final float PI = 3.1415927F;
   public static final float DEG_TO_RAD = 0.017453292F;
   public static final float RAD_TO_DEG = 57.295776F;


   public static byte[] RGBtoYUV(byte r, byte g, byte b) {
      byte y = (byte)((int)((double)r * 0.299D + (double)g * 0.587D + (double)b * 0.114D));
      byte u = (byte)((int)((double)r * -0.168736D + (double)g * -0.331264D + (double)b * 0.5D + 128.0D));
      byte v = (byte)((int)((double)r * 0.5D + (double)g * -0.418688D + (double)b * -0.081312D + 128.0D));
      return new byte[]{y, u, v};
   }

   public static float[] rotateVertices(float[] vertices, int rotation, float rotationCenterX, float rotationCenterY) {
      if(rotation == 0) {
         return vertices;
      } else {
         float rotationRad = degToRad((float)rotation);
         float sinRotationRad = (float)Math.sin((double)rotationRad);
         float cosRotationInRad = (float)Math.cos((double)rotationRad);

         for(int i = vertices.length - 2; i >= 0; i -= 2) {
            float pX = vertices[i];
            float pY = vertices[i + 1];
            vertices[i] = rotationCenterX + (cosRotationInRad * (pX - rotationCenterX) - sinRotationRad * (pY - rotationCenterY));
            vertices[i + 1] = rotationCenterY + sinRotationRad * (pX - rotationCenterX) + cosRotationInRad * (pY - rotationCenterY);
         }

         return vertices;
      }
   }

   public static float degToRad(float pDegree) {
      return 0.017453292F * pDegree;
   }

   public static byte[] BitmapToYUVPlane(Bitmap bitmap, int inputWidth, int inputHeight) {
      int[] argb = new int[inputWidth * inputHeight];
      bitmap.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);
      byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];
      encodeYUV420SP(yuv, argb, inputWidth, inputHeight);
      bitmap.recycle();
      return yuv;
   }

   private static void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
      int frameSize = width * height;
      int yIndex = 0;
      int uvIndex = frameSize;
      int index = 0;

      for(int j = 0; j < height; ++j) {
         for(int i = 0; i < width; ++i) {
            int a = (argb[index] & -16777216) >> 24;
            int R = (argb[index] & 16711680) >> 16;
            int G = (argb[index] & '\uff00') >> 8;
            int B = (argb[index] & 255) >> 0;
            int Y = (66 * R + 129 * G + 25 * B + 128 >> 8) + 16;
            int U = (-38 * R - 74 * G + 112 * B + 128 >> 8) + 128;
            int V = (112 * R - 94 * G - 18 * B + 128 >> 8) + 128;
            yuv420sp[yIndex++] = (byte)(Y < 0?0:(Y > 255?255:Y));
            if(j % 2 == 0 && index % 2 == 0) {
               yuv420sp[uvIndex++] = (byte)(V < 0?0:(V > 255?255:V));
               yuv420sp[uvIndex++] = (byte)(U < 0?0:(U > 255?255:U));
            }

            ++index;
         }
      }

   }

   private static byte[] encodeYUV420SP(int[] argb, int width, int height) {
      int frameSize = width * height;
      byte[] yuv420sp = new byte[3];
      int yIndex = 0;
      int uvIndex = frameSize;
      int index = 0;

      for(int j = 0; j < height; ++j) {
         for(int i = 0; i < width; ++i) {
            int a = (argb[index] & -16777216) >> 24;
            int R = (argb[index] & 16711680) >> 16;
            int G = (argb[index] & '\uff00') >> 8;
            int B = (argb[index] & 255) >> 0;
            int Y = (66 * R + 129 * G + 25 * B + 128 >> 8) + 16;
            int U = (-38 * R - 74 * G + 112 * B + 128 >> 8) + 128;
            int V = (112 * R - 94 * G - 18 * B + 128 >> 8) + 128;
            yuv420sp[yIndex++] = (byte)(Y < 0?0:(Y > 255?255:Y));
            if(j % 2 == 0 && index % 2 == 0) {
               yuv420sp[uvIndex++] = (byte)(V < 0?0:(V > 255?255:V));
               yuv420sp[uvIndex++] = (byte)(U < 0?0:(U > 255?255:U));
            }

            ++index;
         }
      }

      return null;
   }
}
