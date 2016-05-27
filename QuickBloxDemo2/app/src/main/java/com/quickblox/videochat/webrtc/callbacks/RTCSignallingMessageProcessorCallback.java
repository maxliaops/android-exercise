package com.quickblox.videochat.webrtc.callbacks;

import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import java.util.List;
import org.webrtc.SessionDescription;

@Deprecated
public interface RTCSignallingMessageProcessorCallback {

   void onReceiveCallFromUser(Integer var1, QBRTCSessionDescription var2, SessionDescription var3);

   void onReceiveAcceptFromUser(Integer var1, QBRTCSessionDescription var2, SessionDescription var3);

   void onReceiveRejectFromUser(Integer var1, QBRTCSessionDescription var2);

   void onReceiveIceCandidatesFromUser(List var1, Integer var2, QBRTCSessionDescription var3);

   void onReceiveUserHungUpCall(Integer var1, QBRTCSessionDescription var2);

   void onAddUserNeed(Integer var1, QBRTCSessionDescription var2);
}
