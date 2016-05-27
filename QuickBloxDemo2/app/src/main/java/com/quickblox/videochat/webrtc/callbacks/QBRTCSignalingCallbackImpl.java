package com.quickblox.videochat.webrtc.callbacks;

import android.util.Log;
import com.quickblox.videochat.webrtc.QBSignalingSpec;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSignalingCallback;
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException;

public class QBRTCSignalingCallbackImpl implements QBRTCSignalingCallback {

   private final String TAG = this.getClass().getSimpleName();


   public void onSuccessSendingPacket(QBSignalingSpec.QBSignalCMD packetType, Integer opponentId) {
      this.log("onSuccessSendingPacket");
   }

   public void onErrorSendingPacket(QBSignalingSpec.QBSignalCMD packetType, Integer opponentId, QBRTCSignalException e) {
      this.log("onErrorSendingPacket");
   }

   private void log(String message) {
      Log.d(this.TAG, message);
   }
}
