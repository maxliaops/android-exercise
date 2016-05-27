package com.quickblox.videochat.webrtc;

import com.quickblox.videochat.webrtc.QBMediaStreamManager;
import org.webrtc.VideoTrack;

interface QBMediaStreamManagerCallback {

   void setAudioCategoryError(Exception var1);

   void onReceiveLocalVideoTrack(VideoTrack var1);

   void onMediaStreamChange(QBMediaStreamManager var1);
}
