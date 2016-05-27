package com.quickblox.videochat.webrtc.callbacks;

import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

public interface QBRTCClientVideoTracksCallbacks {

   void onLocalVideoTrackReceive(QBRTCSession var1, QBRTCVideoTrack var2);

   void onRemoteVideoTrackReceive(QBRTCSession var1, QBRTCVideoTrack var2, Integer var3);
}
