package com.quickblox.videochat.webrtc.callbacks;

import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.exception.QBRTCException;

public interface QBRTCSessionConnectionCallbacks {

   void onStartConnectToUser(QBRTCSession var1, Integer var2);

   void onConnectedToUser(QBRTCSession var1, Integer var2);

   void onConnectionClosedForUser(QBRTCSession var1, Integer var2);

   void onDisconnectedFromUser(QBRTCSession var1, Integer var2);

   void onDisconnectedTimeoutFromUser(QBRTCSession var1, Integer var2);

   void onConnectionFailedWithUser(QBRTCSession var1, Integer var2);

   void onError(QBRTCSession var1, QBRTCException var2);
}
