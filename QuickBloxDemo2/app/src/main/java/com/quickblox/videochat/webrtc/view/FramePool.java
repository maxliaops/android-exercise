package com.quickblox.videochat.webrtc.view;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import org.webrtc.VideoRenderer;

class FramePool {

   private final HashMap availableFrames = new HashMap();
   private static final long MAX_DIMENSION = 4096L;


   public VideoRenderer.I420Frame takeFrame(VideoRenderer.I420Frame source) {
      long desc = summarizeFrameDimensions(source);
      VideoRenderer.I420Frame dst = null;
      HashMap var5 = this.availableFrames;
      synchronized(this.availableFrames) {
         LinkedList frames = (LinkedList)this.availableFrames.get(Long.valueOf(desc));
         if(frames == null) {
            frames = new LinkedList();
            this.availableFrames.put(Long.valueOf(desc), frames);
         }

         if(!frames.isEmpty()) {
            dst = (VideoRenderer.I420Frame)frames.pop();
         } else {
            dst = new VideoRenderer.I420Frame(source.width, source.height, source.rotationDegree, source.yuvStrides, (ByteBuffer[])null);
         }

         return dst;
      }
   }

   public void returnFrame(VideoRenderer.I420Frame frame) {
      long desc = summarizeFrameDimensions(frame);
      HashMap var4 = this.availableFrames;
      synchronized(this.availableFrames) {
         LinkedList frames = (LinkedList)this.availableFrames.get(Long.valueOf(desc));
         if(frames == null) {
            throw new IllegalArgumentException("Unexpected frame dimensions");
         } else {
            frames.add(frame);
         }
      }
   }

   public static boolean validateDimensions(VideoRenderer.I420Frame frame) {
      return (long)frame.width < 4096L && (long)frame.height < 4096L && (long)frame.yuvStrides[0] < 4096L && (long)frame.yuvStrides[1] < 4096L && (long)frame.yuvStrides[2] < 4096L;
   }

   private static long summarizeFrameDimensions(VideoRenderer.I420Frame frame) {
      long ret = (long)frame.width;
      ret = ret * 4096L + (long)frame.height;
      ret = ret * 4096L + (long)frame.yuvStrides[0];
      ret = ret * 4096L + (long)frame.yuvStrides[1];
      ret = ret * 4096L + (long)frame.yuvStrides[2];
      return ret;
   }
}
