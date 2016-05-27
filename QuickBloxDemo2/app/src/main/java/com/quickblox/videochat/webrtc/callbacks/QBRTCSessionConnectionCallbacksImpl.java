package com.quickblox.videochat.webrtc.callbacks;

import android.util.Log;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionConnectionCallbacks;
import com.quickblox.videochat.webrtc.exception.QBRTCException;

public class QBRTCSessionConnectionCallbacksImpl implements QBRTCSessionConnectionCallbacks {

   private final String TAG = this.getClass().getSimpleName();


   public void onStartConnectToUser(QBRTCSession session, Integer userID) {
      this.log("onStartConnectToUser");
   }

   public void onConnectedToUser(QBRTCSession session, Integer userID) {
      this.log("onConnectedToUser");
   }

   public void onConnectionClosedForUser(QBRTCSession session, Integer userID) {}

   public void onDisconnectedFromUser(QBRTCSession session, Integer userID) {
      this.log("onDisconnectedFromUser");
   }

   public void onDisconnectedTimeoutFromUser(QBRTCSession session, Integer userID) {
      this.log("onDisconnectedTimeoutFromUser");
   }

   public void onConnectionFailedWithUser(QBRTCSession session, Integer userID) {
      this.log("onConnectionFaildWithUser");
   }

   public void onError(QBRTCSession session, QBRTCException exeption) {
      this.log("onError");
   }

   private void log(String message) {
      Log.d(this.TAG, message);
   }
}
