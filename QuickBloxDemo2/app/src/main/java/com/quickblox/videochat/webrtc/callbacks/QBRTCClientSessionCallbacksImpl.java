package com.quickblox.videochat.webrtc.callbacks;

import android.util.Log;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import java.util.Map;

public class QBRTCClientSessionCallbacksImpl implements QBRTCClientSessionCallbacks {

   private final String TAG = this.getClass().getSimpleName();


   public void onReceiveNewSession(QBRTCSession session) {
      this.log("onReceiveNewSession");
   }

   public void onCallRejectByUser(QBRTCSession session, Integer userID, Map userInfo) {
      this.log("onCallRejectByUser");
   }

   public void onCallAcceptByUser(QBRTCSession session, Integer userID, Map userInfo) {
      this.log("onCallAcceptByUser");
   }

   public void onUserNotAnswer(QBRTCSession session, Integer userID) {
      this.log("onUserNotAnswer");
   }

   public void onUserNoActions(QBRTCSession session, Integer userID) {
      this.log("onUserNoActions");
   }

   public void onSessionClosed(QBRTCSession session) {
      this.log("onSessionClosed");
   }

   public void onSessionStartClose(QBRTCSession session) {
      this.log("onSessionStartClose");
   }

   public void onReceiveHangUpFromUser(QBRTCSession session, Integer userID, Map userInfo) {
      this.log("onReceiveHangUpFromUser");
   }

   private void log(String message) {
      Log.d(this.TAG, message);
   }
}
