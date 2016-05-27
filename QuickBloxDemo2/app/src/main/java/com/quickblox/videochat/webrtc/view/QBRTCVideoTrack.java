package com.quickblox.videochat.webrtc.view;

import org.webrtc.VideoRenderer;
import org.webrtc.VideoTrack;

public class QBRTCVideoTrack {

   private Integer userID;
   private VideoTrack rtcVideoTrack;
   private boolean isRemote;
   private static final String TAG = "WebRTCChat.QBRTCVideoTrack";
   private VideoRenderer renderer;


   public QBRTCVideoTrack(VideoTrack rtcVideoTrack, boolean isRemote) {
      this.rtcVideoTrack = rtcVideoTrack;
      this.isRemote = isRemote;
   }

   public QBRTCVideoTrack(VideoTrack rtcVideoTrack, Integer userID, boolean isRemote) {
      this.rtcVideoTrack = rtcVideoTrack;
      this.userID = userID;
      this.isRemote = isRemote;
   }

   public void addRenderer(VideoRenderer renderer) {
      this.renderer = renderer;
      this.rtcVideoTrack.addRenderer(renderer);
   }

   public void removeRenderer(VideoRenderer renderer) {
      this.rtcVideoTrack.removeRenderer(renderer);
      this.renderer = null;
   }

   public void cleanUp() {
      this.removeRenderer(this.renderer);
   }

   public void dealloc() {
      this.rtcVideoTrack = null;
   }

   public VideoRenderer getRenderer() {
      return this.renderer;
   }

   public boolean isEnabled() {
      return this.rtcVideoTrack.enabled();
   }
}
