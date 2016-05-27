package com.quickblox.videochat.webrtc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import com.quickblox.videochat.webrtc.R;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

public class RTCGLVideoView extends GLSurfaceView {

   private static final String TAG = RTCGLVideoView.class.getSimpleName();
   private static final int NUMBER_COORDINATES = 4;
   private final int[] remoteCoords = new int[]{0, 0, 100, 100};
   private final int[] localCoords = new int[]{0, 0, 100, 100};
   private VideoRenderer.Callbacks mainRendererCallback;
   private VideoRenderer.Callbacks localRendererCallback;
   private boolean mainMirror;
   private boolean secondMirror;


   public RTCGLVideoView(Context context) {
      super(context);
      Log.i(TAG, "ctor");
      this.init((TypedArray)null);
   }

   public RTCGLVideoView(Context c, AttributeSet attr) {
      super(c, attr);
      Log.i(TAG, "ctor with attrs");
      TypedArray a = c.getTheme().obtainStyledAttributes(attr, R.styleable.RTCGlView, 0, 0);
      this.init(a);
   }

   public VideoRenderer.Callbacks obtainVideoRenderer(RTCGLVideoView.RendererSurface rendererSurface) {
      Log.i(TAG, "obtainVideoRenderer");
      return RTCGLVideoView.RendererSurface.MAIN.equals(rendererSurface)?this.obtainMainVideoRenderer():this.obtainSecondVideoRenderer();
   }

   private VideoRenderer.Callbacks obtainMainVideoRenderer() {
      Log.i(TAG, "obtainMainVideoRenderer");
      if(this.mainRendererCallback == null) {
         this.mainRendererCallback = this.initRenderer(this.mainMirror, this.remoteCoords);
      }

      return this.mainRendererCallback;
   }

   private VideoRenderer.Callbacks obtainSecondVideoRenderer() {
      Log.i(TAG, "obtainSecondVideoRenderer");
      if(this.localRendererCallback == null) {
         this.localRendererCallback = this.initRenderer(this.secondMirror, this.localCoords);
      }

      return this.localRendererCallback;
   }

   public void updateRenderer(RTCGLVideoView.RendererSurface rendererSurface, RTCGLVideoView.RendererConfig config) {
      boolean mainRenderer = RTCGLVideoView.RendererSurface.MAIN.equals(rendererSurface);
      VideoRenderer.Callbacks callbacks = mainRenderer?this.mainRendererCallback:this.localRendererCallback;
      if(config.coordinates != null) {
         this.setViewCoordinates(mainRenderer?this.remoteCoords:this.localCoords, config.coordinates);
      }

      this.setRendererMirror(config.mirror, rendererSurface);
      int[] viewCoordinates = mainRenderer?this.remoteCoords:this.localCoords;
      VideoRendererGui.update(callbacks, viewCoordinates[0], viewCoordinates[1], viewCoordinates[2], viewCoordinates[3], VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, mainRenderer?this.mainMirror:this.secondMirror);
   }

   public void release() {
      if(this.localRendererCallback != null) {
         VideoRendererGui.remove(this.localRendererCallback);
      }

      if(this.mainRendererCallback != null) {
         VideoRendererGui.remove(this.mainRendererCallback);
      }

   }

   private void setRendererMirror(boolean mirror, RTCGLVideoView.RendererSurface type) {
      Log.i(TAG, "setRendererMirror type=" + type + ", value= " + mirror);
      if(RTCGLVideoView.RendererSurface.MAIN.equals(type)) {
         this.mainMirror = mirror;
      } else {
         this.secondMirror = mirror;
      }

   }

   private VideoRenderer.Callbacks initRenderer(boolean mirror, int[] viewCoordinates) {
      return VideoRendererGui.createGuiRenderer(viewCoordinates[0], viewCoordinates[1], viewCoordinates[2], viewCoordinates[3], VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, mirror);
   }

   private void init(TypedArray typedArray) {
      VideoRendererGui.setView(this, (Runnable)null);
      if(typedArray != null) {
         this.setValuefromResources(typedArray);
         typedArray.recycle();
      }

      this.obtainMainVideoRenderer();
   }

   private void setValuefromResources(TypedArray typedArray) {
      Log.i(TAG, "setValuefromResources");
      this.setRendererMirror(typedArray.getBoolean(R.styleable.RTCGlView_mainMirror, false), RTCGLVideoView.RendererSurface.MAIN);
      this.setRendererMirror(typedArray.getBoolean(R.styleable.RTCGlView_secondMirror, false), RTCGLVideoView.RendererSurface.SECOND);
      int remoteValuesId = typedArray.getResourceId(R.styleable.RTCGlView_mainCoords, 0);
      if(remoteValuesId != 0) {
         int[] localValuesId = this.getResources().getIntArray(remoteValuesId);
         this.setViewCoordinates(this.remoteCoords, localValuesId);
      }

      int localValuesId1 = typedArray.getResourceId(R.styleable.RTCGlView_secondCoords, 0);
      if(localValuesId1 != 0) {
         int[] values = this.getResources().getIntArray(localValuesId1);
         this.setViewCoordinates(this.localCoords, values);
      }

   }

   private void setViewCoordinates(int[] coordinates, int[] resources) {
      if(resources.length >= 4) {
         System.arraycopy(resources, 0, coordinates, 0, 4);
      }

   }


   public static class RendererConfig {

      public int[] coordinates;
      public boolean mirror;


   }

   public static enum RendererSurface {

      MAIN("MAIN", 0),
      SECOND("SECOND", 1);
      // $FF: synthetic field
      private static final RTCGLVideoView.RendererSurface[] $VALUES = new RTCGLVideoView.RendererSurface[]{MAIN, SECOND};


      private RendererSurface(String var1, int var2) {}

   }
}
