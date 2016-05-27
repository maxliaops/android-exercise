package com.quickblox.videochat.webrtc.callbacks;

import com.quickblox.videochat.webrtc.QBSignalingSpec;
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException;

public interface QBRTCSignalingCallback {

   void onSuccessSendingPacket(QBSignalingSpec.QBSignalCMD var1, Integer var2);

   void onErrorSendingPacket(QBSignalingSpec.QBSignalCMD var1, Integer var2, QBRTCSignalException var3);
}
