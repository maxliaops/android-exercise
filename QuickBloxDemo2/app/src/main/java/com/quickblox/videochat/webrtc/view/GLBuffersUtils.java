package com.quickblox.videochat.webrtc.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

class GLBuffersUtils {

   public static FloatBuffer makeDirectNativeFloatBuffer(float[] array) {
      FloatBuffer buffer = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      buffer.put(array);
      buffer.flip();
      return buffer;
   }

   public static ByteBuffer makeNativeByteBufferWithValue(int width, int height, byte value) {
      byte[] array = new byte[width * height];
      Arrays.fill(array, value);
      ByteBuffer buffer = ByteBuffer.allocateDirect(width * height).order(ByteOrder.nativeOrder());
      buffer.put(array);
      buffer.flip();
      return buffer;
   }

   public static ByteBuffer makeNativeByteBufferFromArray(byte[] array, int width, int height) {
      ByteBuffer buffer = ByteBuffer.allocateDirect(width * height).order(ByteOrder.nativeOrder());
      buffer.put(array);
      buffer.flip();
      return buffer;
   }

   public static ByteBuffer[] makePlanesBufferWithValues(int width, int height, byte[] yuv) {
      ByteBuffer[] buffer = new ByteBuffer[3];

      for(int i = 0; i < 3; ++i) {
         int w = i == 0?width:height / 2;
         buffer[i] = makeNativeByteBufferWithValue(w, height, yuv[i]);
      }

      return buffer;
   }
}
