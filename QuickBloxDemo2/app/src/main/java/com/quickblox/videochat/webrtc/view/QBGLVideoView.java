package com.quickblox.videochat.webrtc.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import com.quickblox.core.helper.Lo;
import com.quickblox.videochat.webrtc.view.FramePool;
import com.quickblox.videochat.webrtc.view.GLBuffersUtils;
import com.quickblox.videochat.webrtc.view.GraphicsConverter;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import com.quickblox.videochat.webrtc.view.VideoCallBacks;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.EnumMap;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.webrtc.VideoRenderer;

@Deprecated
public class QBGLVideoView extends GLSurfaceView implements Renderer {

   private static final VideoRenderer.I420Frame backgroundFrame;
   private static final int DEFAULT_YUV_FRAME_WIDTH = 640;
   private static final int DEFAULT_YUV_FRAME_HEIGHT = 480;
   private static final int DEFAULT_YUV_FRAME_ROTATION_DEGREE = QBGLVideoView.ORIENTATION_MODE.portrait.degreeRotation;
   private static final String VERTEX_SHADER_STRING = "varying vec2 interp_tc;\n\nattribute vec4 in_pos;\nattribute vec2 in_tc;\n\nvoid main() {\n  gl_Position = in_pos;\n  interp_tc = in_tc;\n}\n";
   private static final String FRAGMENT_SHADER_STRING = "precision mediump float;\nvarying vec2 interp_tc;\n\nuniform sampler2D y_tex;\nuniform sampler2D u_tex;\nuniform sampler2D v_tex;\n\nvoid main() {\n  float y = texture2D(y_tex, interp_tc).r;\n  float u = texture2D(u_tex, interp_tc).r - .5;\n  float v = texture2D(v_tex, interp_tc).r - .5;\n  gl_FragColor = vec4(y + 1.403 * v,                       y - 0.344 * u - 0.714 * v,                       y + 1.77 * u, 1);\n}\n";
   private static final float[] videoViewVertices = new float[]{-1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, -1.0F};
   private static final FloatBuffer textureCoords = GLBuffersUtils.makeDirectNativeFloatBuffer(new float[]{0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F});
   private QBGLVideoView.VIDEO_MODE mode;
   private int[][] yuvTextures;
   private int posLocation;
   private long lastFPSLogTime;
   private long numFramesSinceLastLog;
   private FramePool framePool;
   private EnumMap framesToRender;
   private int videoViewOrientation;
   private FloatBuffer currentVideoViewVertices;
   private Bitmap backgroundBitmap;
   private QBRTCVideoTrack videoTrack;
   private int curentRemoteRotation;
   private int curentLocalRotation;


   public QBGLVideoView(Context c) {
      super(c);
      this.mode = QBGLVideoView.VIDEO_MODE.NOT_ACTIVE;
      this.yuvTextures = new int[][]{{-1, -1, -1}, {-1, -1, -1}};
      this.posLocation = -1;
      this.lastFPSLogTime = System.nanoTime();
      this.numFramesSinceLastLog = 0L;
      this.framePool = new FramePool();
      this.framesToRender = new EnumMap(QBGLVideoView.Endpoint.class);
      this.videoViewOrientation = 1;
      this.currentVideoViewVertices = GLBuffersUtils.makeDirectNativeFloatBuffer(videoViewVertices);
      this.curentRemoteRotation = -1;
      this.curentLocalRotation = -1;
      this.init();
   }

   public QBGLVideoView(Context c, AttributeSet attr) {
      super(c, attr);
      this.mode = QBGLVideoView.VIDEO_MODE.NOT_ACTIVE;
      this.yuvTextures = new int[][]{{-1, -1, -1}, {-1, -1, -1}};
      this.posLocation = -1;
      this.lastFPSLogTime = System.nanoTime();
      this.numFramesSinceLastLog = 0L;
      this.framePool = new FramePool();
      this.framesToRender = new EnumMap(QBGLVideoView.Endpoint.class);
      this.videoViewOrientation = 1;
      this.currentVideoViewVertices = GLBuffersUtils.makeDirectNativeFloatBuffer(videoViewVertices);
      this.curentRemoteRotation = -1;
      this.curentLocalRotation = -1;
      this.init();
   }

   public void setBackgroundBitmap(Bitmap backgroundBitmap) {
      if(backgroundBitmap != null && this.backgroundBitmap != backgroundBitmap) {
         this.backgroundBitmap = backgroundBitmap;
         GLES20.glBindTexture(3553, this.yuvTextures[0][0]);
         GLUtils.texImage2D(3553, 0, backgroundBitmap, 0);
         GLES20.glTexParameterf(3553, 10241, 9729.0F);
         GLES20.glTexParameterf(3553, 10240, 9729.0F);
         GLES20.glTexParameterf(3553, 10242, 33071.0F);
         GLES20.glTexParameterf(3553, 10243, 33071.0F);
         this.requestRender();
      }
   }

   public void setVideoViewOrientation(int orientation) {
      Lo.g("remote orientation=" + orientation);
      if(this.videoViewOrientation != orientation) {
         this.videoViewOrientation = orientation;
         float[] vertices = new float[videoViewVertices.length];
         System.arraycopy(videoViewVertices, 0, vertices, 0, videoViewVertices.length);
         this.currentVideoViewVertices = this.rebuildVertices(this.currentVideoViewVertices, vertices, this.videoViewOrientation, 0.0F, 0.0F);
         this.queueEvent(new Runnable() {
            public void run() {
               QBGLVideoView.this.requestRender();
            }
         });
      }
   }

   private QBGLVideoView.ORIENTATION_MODE defineOrientation(int orientation) {
      QBGLVideoView.ORIENTATION_MODE orientationMode = QBGLVideoView.ORIENTATION_MODE.landscape_left;
      if(0 == orientation) {
         orientationMode = QBGLVideoView.ORIENTATION_MODE.landscape_left;
      } else if(1 == orientation) {
         orientationMode = QBGLVideoView.ORIENTATION_MODE.portrait;
      } else if(2 == orientation) {
         orientationMode = QBGLVideoView.ORIENTATION_MODE.landscape_right;
      } else if(3 == orientation) {
         orientationMode = QBGLVideoView.ORIENTATION_MODE.portrait_upside_down;
      }

      return orientationMode;
   }

   public void setVideoMode(QBGLVideoView.VIDEO_MODE videoMode) {
      if(!this.mode.equals(videoMode)) {
         this.mode = videoMode;
         this.queueEvent(new Runnable() {
            public void run() {
               VideoRenderer.I420Frame replacingframe = null;
               boolean clearTexture = true;
               if(QBGLVideoView.VIDEO_MODE.NOT_ACTIVE.equals(QBGLVideoView.this.mode)) {
                  replacingframe = QBGLVideoView.backgroundFrame;
                  clearTexture = false;
               }

               QBGLVideoView.this.fillViewTexturesWithFrame(replacingframe, 640, 480, clearTexture);
               QBGLVideoView.this.requestRender();
            }
         });
      }
   }

   public void queueFrame(final QBGLVideoView.Endpoint stream, VideoRenderer.I420Frame frame) {
      showAbortMessage(FramePool.validateDimensions(frame), "Frame too large!");
      VideoRenderer.I420Frame frameCopy = this.framePool.takeFrame(frame).copyFrom(frame);
      EnumMap var5 = this.framesToRender;
      boolean needToScheduleRender;
      synchronized(this.framesToRender) {
         needToScheduleRender = this.framesToRender.isEmpty();
         VideoRenderer.I420Frame frameToDrop = (VideoRenderer.I420Frame)this.framesToRender.put(stream, frameCopy);
         if(frameToDrop != null) {
            this.framePool.returnFrame(frameToDrop);
         }
      }

      if(needToScheduleRender) {
         this.queueEvent(new Runnable() {
            public void run() {
               QBGLVideoView.this.updateFrames(stream);
            }
         });
      }

   }

   public void setSize(QBGLVideoView.Endpoint stream, int width, int height) {
      this.mode = QBGLVideoView.VIDEO_MODE.ACTIVE;
      int[] textures = this.yuvTextures[0];
      this.bindTexture((VideoRenderer.I420Frame)null, width, height, textures, true);
   }

   public void onSurfaceChanged(GL10 unused, int width, int height) {
      GLES20.glViewport(0, 0, width, height);
      checkNoGLES2Error();
   }

   public void onDrawFrame(GL10 unused) {
      GLES20.glClear(16384);
      this.drawRectangle(this.yuvTextures[0], this.currentVideoViewVertices);
      ++this.numFramesSinceLastLog;
      long now = System.nanoTime();
      if(this.lastFPSLogTime == -1L || (double)(now - this.lastFPSLogTime) > 1.0E9D) {
         double fps = (double)this.numFramesSinceLastLog / ((double)(now - this.lastFPSLogTime) / 1.0E9D);
         this.lastFPSLogTime = now;
         this.numFramesSinceLastLog = 1L;
      }

      checkNoGLES2Error();
   }

   public void onSurfaceCreated(GL10 unused, EGLConfig config) {
      int program = GLES20.glCreateProgram();
      addShaderTo('\u8b31', "varying vec2 interp_tc;\n\nattribute vec4 in_pos;\nattribute vec2 in_tc;\n\nvoid main() {\n  gl_Position = in_pos;\n  interp_tc = in_tc;\n}\n", program);
      addShaderTo('\u8b30', "precision mediump float;\nvarying vec2 interp_tc;\n\nuniform sampler2D y_tex;\nuniform sampler2D u_tex;\nuniform sampler2D v_tex;\n\nvoid main() {\n  float y = texture2D(y_tex, interp_tc).r;\n  float u = texture2D(u_tex, interp_tc).r - .5;\n  float v = texture2D(v_tex, interp_tc).r - .5;\n  gl_FragColor = vec4(y + 1.403 * v,                       y - 0.344 * u - 0.714 * v,                       y + 1.77 * u, 1);\n}\n", program);
      GLES20.glLinkProgram(program);
      int[] result = new int[]{0};
      GLES20.glGetProgramiv(program, '\u8b82', result, 0);
      showAbortMessage(result[0] == 1, GLES20.glGetProgramInfoLog(program));
      GLES20.glUseProgram(program);
      GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "y_tex"), 0);
      GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "u_tex"), 1);
      GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "v_tex"), 2);
      this.posLocation = GLES20.glGetAttribLocation(program, "in_pos");
      int tcLocation = GLES20.glGetAttribLocation(program, "in_tc");
      GLES20.glEnableVertexAttribArray(tcLocation);
      GLES20.glVertexAttribPointer(tcLocation, 2, 5126, false, 0, textureCoords);
      this.initTextures();
      this.fillDefaultTextures();
      GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
      checkNoGLES2Error();
   }

   private static void addShaderTo(int type, String source, int program) {
      int[] result = new int[]{0};
      int shader = GLES20.glCreateShader(type);
      GLES20.glShaderSource(shader, source);
      GLES20.glCompileShader(shader);
      GLES20.glGetShaderiv(shader, '\u8b81', result, 0);
      showAbortMessage(result[0] == 1, GLES20.glGetShaderInfoLog(shader) + ", source: " + source);
      GLES20.glAttachShader(program, shader);
      GLES20.glDeleteShader(shader);
      checkNoGLES2Error();
   }

   private static void showAbortMessage(boolean condition, String msg) {
      if(!condition) {
         if(msg != null) {
            Lo.g("QBGLVideoView.abort: ", new Object[]{msg});
         } else {
            Lo.g("QBGLVideoView.abort");
         }
      }

   }

   private static void checkNoGLES2Error() {
      int error = GLES20.glGetError();
      showAbortMessage(error == 0, "GLES20 error: " + error);
   }

   private FloatBuffer rebuildVertices(FloatBuffer vertices, float[] originalVertices, int rotation, float centerX, float centerY) {
      if(vertices != null) {
         vertices.clear();
      }

      vertices = GLBuffersUtils.makeDirectNativeFloatBuffer(GraphicsConverter.rotateVertices(originalVertices, rotation, centerX, centerY));
      return vertices;
   }

   private void init() {
      this.setPreserveEGLContextOnPause(true);
      this.setEGLContextClientVersion(2);
      this.setRenderer(this);
      this.setRenderMode(0);
   }

   private void updateFrames(QBGLVideoView.Endpoint stream) {
      VideoRenderer.I420Frame frame = null;
      EnumMap var3 = this.framesToRender;
      synchronized(this.framesToRender) {
         frame = (VideoRenderer.I420Frame)this.framesToRender.remove(stream);
      }

      if(frame != null) {
         this.bindTexture(frame, frame.width, frame.height, this.yuvTextures[0], false);
         this.framePool.returnFrame(frame);
      }

      showAbortMessage(frame != null, "Nothing to render!");
      this.requestRender();
   }

   private void initTextures() {
      int[][] arr$ = this.yuvTextures;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int[] textures = arr$[i$];
         GLES20.glGenTextures(3, textures, 0);
      }

   }

   private void bindTexture(VideoRenderer.I420Frame frame, int width, int height, int[] textures, boolean clearing) {
      for(int i = 0; i < 3; ++i) {
         ByteBuffer plane = clearing?null:frame.yuvPlanes[i];
         GLES20.glActiveTexture('\u84c0' + i);
         GLES20.glBindTexture(3553, textures[i]);
         int w = i == 0?width:width / 2;
         int h = i == 0?height:height / 2;
         if(frame != null) {
            showAbortMessage(w == frame.yuvStrides[i], frame.yuvStrides[i] + "!=" + w);
         }

         GLES20.glTexImage2D(3553, 0, 6409, w, h, 0, 6409, 5121, plane);
         GLES20.glTexParameterf(3553, 10241, 9729.0F);
         GLES20.glTexParameterf(3553, 10240, 9729.0F);
         GLES20.glTexParameterf(3553, 10242, 33071.0F);
         GLES20.glTexParameterf(3553, 10243, 33071.0F);
      }

      checkNoGLES2Error();
   }

   private void fillDefaultTextures() {
      this.bindTexture(backgroundFrame, 640, 480, this.yuvTextures[0], false);
   }

   private void fillViewTexturesWithFrame(VideoRenderer.I420Frame frame, int width, int height, boolean clear) {
      this.bindTexture(frame, width, height, this.yuvTextures[0], clear);
   }

   private void drawRectangle(int[] textures, FloatBuffer vertices) {
      for(int i = 0; i < 3; ++i) {
         GLES20.glActiveTexture('\u84c0' + i);
         GLES20.glBindTexture(3553, textures[i]);
      }

      GLES20.glVertexAttribPointer(this.posLocation, 2, 5126, false, 0, vertices);
      GLES20.glEnableVertexAttribArray(this.posLocation);
      GLES20.glDrawArrays(5, 0, 4);
      checkNoGLES2Error();
   }

   private void logFrame(VideoRenderer.I420Frame frame) {
      Lo.g(" new frame -> ");
      Lo.g(" width -> " + frame.width + ", height - >" + frame.height);
      Lo.g("frame.yuvStrides -> " + Arrays.toString(frame.yuvStrides));
      Lo.g("frame buffer length -> " + frame.yuvPlanes.length);

      for(int i = 0; i < frame.yuvPlanes.length; ++i) {
         ByteBuffer yuvPlane = frame.yuvPlanes[i];
         Lo.g("frame buffer  [" + i + "] lnegth->" + yuvPlane.array().length);
         Lo.g("frame buffer  [" + i + "] ->" + Arrays.toString(yuvPlane.array()));
      }

   }

   public void setVideoTrack(QBRTCVideoTrack videoTrack, QBGLVideoView.Endpoint endpoint) {
      if(videoTrack != null) {
         if(this.videoTrack != null && this.videoTrack != videoTrack) {
            videoTrack.removeRenderer(videoTrack.getRenderer());
         }

         videoTrack.addRenderer(new VideoRenderer(new VideoCallBacks(this, endpoint)));
         this.videoTrack = videoTrack;
      }

   }

   static {
      byte r = 0;
      byte g = 0;
      byte b = 0;
      byte[] yuv = GraphicsConverter.RGBtoYUV(r, g, b);
      ByteBuffer[] planes = GLBuffersUtils.makePlanesBufferWithValues(640, 480, yuv);
      backgroundFrame = new VideoRenderer.I420Frame(640, 480, DEFAULT_YUV_FRAME_ROTATION_DEGREE, new int[]{640, 320, 320}, planes);
   }

   public static enum Endpoint {

      LOCAL("LOCAL", 0),
      REMOTE("REMOTE", 1);
      // $FF: synthetic field
      private static final QBGLVideoView.Endpoint[] $VALUES = new QBGLVideoView.Endpoint[]{LOCAL, REMOTE};


      private Endpoint(String var1, int var2) {}

   }

   public static enum ORIENTATION_MODE {

      portrait("portrait", 0, 90),
      portrait_upside_down("portrait_upside_down", 1, 270),
      landscape_left("landscape_left", 2, 0),
      landscape_right("landscape_right", 3, 180);
      private int degreeRotation = 0;
      // $FF: synthetic field
      private static final QBGLVideoView.ORIENTATION_MODE[] $VALUES = new QBGLVideoView.ORIENTATION_MODE[]{portrait, portrait_upside_down, landscape_left, landscape_right};


      private ORIENTATION_MODE(String var1, int var2, int degree) {
         this.degreeRotation = degree;
      }

      public int getDegreeRotation() {
         return this.degreeRotation;
      }

   }

   public static enum VIDEO_MODE {

      ACTIVE("ACTIVE", 0),
      NOT_ACTIVE("NOT_ACTIVE", 1);
      // $FF: synthetic field
      private static final QBGLVideoView.VIDEO_MODE[] $VALUES = new QBGLVideoView.VIDEO_MODE[]{ACTIVE, NOT_ACTIVE};


      private VIDEO_MODE(String var1, int var2) {}

   }
}
