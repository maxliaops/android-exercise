package com.quickblox.videochat.webrtc;

import com.quickblox.videochat.webrtc.QBPeerChannel;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.exception.QBRTCException;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import java.util.List;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;

interface QBPeerChannelCallback {

   void onChannelConnectionDisconnected(QBPeerChannel var1);

   void onChannelConnectionClosed(QBPeerChannel var1);

   void onChannelConnectionConnecting(QBPeerChannel var1);

   void onChannelConnectionConnected(QBPeerChannel var1);

   void onError(QBPeerChannel var1, QBRTCException var2);

   void onRemoteVideoTrackReceive(QBPeerChannel var1, QBRTCVideoTrack var2);

   MediaStream onLocalStreamNeedAdd(QBPeerChannel var1) throws QBRTCException;

   void onSessionDescriptionSend(QBPeerChannel var1, SessionDescription var2);

   void onHangUpSend(QBPeerChannel var1);

   void onIceCandidatesSend(QBPeerChannel var1, List<IceCandidate> var2);

   void onChannelNotAnswer(QBPeerChannel var1);

   void onChannelConnectionFailed(QBPeerChannel var1);

   void onPeerConnectionStatsReady(StatsReport[] var1);

   QBRTCTypes.QBConferenceType conferenceTypeForChannel(QBPeerChannel var1);
}
