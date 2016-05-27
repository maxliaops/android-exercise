package com.quickblox.videochat.webrtc.callbacks;

import android.util.Log;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

public class QBRTCClientVideoTracksCallbacksImpl implements QBRTCClientVideoTracksCallbacks {

   private final String TAG = this.getClass().getSimpleName();


   public void onLocalVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack) {
      this.log("onLocalVideoTrackReceive");
   }

   public void onRemoteVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack, Integer userID) {
      this.log("onRemoteVideoTrackReceive");
   }

   private void log(String message) {
      Log.d(this.TAG, message);
   }
}
