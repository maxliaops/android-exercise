package com.quickblox.videochat.webrtc.callbacks;

import com.quickblox.videochat.webrtc.QBRTCSession;
import java.util.Map;

public interface QBRTCClientSessionCallbacks {

   void onReceiveNewSession(QBRTCSession var1);

   void onUserNotAnswer(QBRTCSession var1, Integer var2);

   void onCallRejectByUser(QBRTCSession var1, Integer var2, Map<String, String> var3);

   void onCallAcceptByUser(QBRTCSession var1, Integer var2, Map<String, String> var3);

   void onReceiveHangUpFromUser(QBRTCSession var1, Integer var2, Map<String, String> var3);

   void onUserNoActions(QBRTCSession var1, Integer var2);

   void onSessionClosed(QBRTCSession var1);

   void onSessionStartClose(QBRTCSession var1);
}
