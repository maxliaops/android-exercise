package com.quickblox.videochat.webrtc.view;

import com.quickblox.videochat.webrtc.view.QBGLVideoView;
import org.webrtc.VideoRenderer;

public class VideoCallBacks implements VideoRenderer.Callbacks {

   private final QBGLVideoView view;
   private final QBGLVideoView.Endpoint stream;


   public VideoCallBacks(QBGLVideoView view, QBGLVideoView.Endpoint stream) {
      this.view = view;
      this.stream = stream;
   }

   public boolean canApplyRotation() {
      return false;
   }

   public void renderFrame(VideoRenderer.I420Frame frame) {
      this.view.queueFrame(this.stream, frame);
   }
}
