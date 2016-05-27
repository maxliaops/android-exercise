//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.videochat.webrtc;

import android.text.TextUtils;
import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingListener;
import com.quickblox.chat.model.QBChatMessageExtension;
import com.quickblox.core.helper.CollectionsUtil;
import com.quickblox.core.helper.Decorators;
import com.quickblox.core.helper.Lo;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.IceCandidateParser;
import com.quickblox.videochat.webrtc.OpponentsParser;
import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import com.quickblox.videochat.webrtc.UserInfoParser;
import com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBCandidate;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBSignalCMD;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBSignalField;
import com.quickblox.videochat.webrtc.callbacks.RTCSignallingMessageProcessorCallback;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import org.jivesoftware.smack.packet.Message;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;
import org.webrtc.SessionDescription.Type;

public final class RTCSignallingMessageProcessor implements QBVideoChatSignalingListener {
    LinkedBlockingDeque<RTCSignallingMessageProcessorCallback> webRTCSmackCallbacks = new LinkedBlockingDeque();

    public RTCSignallingMessageProcessor() {
    }

    public void addSignalingListener(RTCSignallingMessageProcessorCallback webRTCSmackCallback) {
        if(webRTCSmackCallback != null && !this.webRTCSmackCallbacks.contains(webRTCSmackCallback)) {
            this.webRTCSmackCallbacks.add(webRTCSmackCallback);
        }

    }

    public void removeSignalingListener(RTCSignallingMessageProcessorCallback webRTCSmackCallback) {
        this.webRTCSmackCallbacks.remove(webRTCSmackCallback);
    }

    public void processSignalMessage(QBSignaling signaling, Message message) {
        Lo.g("processSignalMessage..");
        int fromUserId = JIDHelper.INSTANCE.parseUserId(message.getFrom());
        if(fromUserId == -1) {
            Lo.g("processSignalMessage. fromUserId wasn\'t defined in message.");
        } else {
            QBUser fromUser = new QBUser(Integer.valueOf(fromUserId));
            QBChatMessageExtension extension = (QBChatMessageExtension)message.getExtension("extraParams", "jabber:client");
            if(extension != null) {
                String destinationModule = extension.getProperty(QBSignalField.MODULE_IDENTIFIER.getValue());
                if("WebRTCVideoChat".equals(destinationModule)) {
                    String signalKey = QBSignalField.SIGNALING_TYPE.getValue();
                    String signalValue = extension.getProperty(signalKey);
                    String sessionIdKey = QBSignalField.SESSION_ID.getValue();
                    String sessionIdValue = extension.getProperty(sessionIdKey);
                    if(signalValue != null && sessionIdValue != null) {
                        Lo.g("Receive Video signal \' " + signalValue + "\', from User: " + fromUser.getId() + ", sessionId: " + sessionIdValue);
                    } else {
                        Lo.g("Receive Video signal, from User: " + fromUser.getId());
                    }

                    this.processVideoAudioChatMessage(fromUser, extension);
                }

            }
        }
    }

    private void processVideoAudioChatMessage(QBUser fromUser, QBChatMessageExtension msgExtension) {
        String sessionID = this.retrieveDataByPropertyID(msgExtension, QBSignalField.SESSION_ID.getValue());
        Map userInfo = this.getUserInfo(msgExtension);
        QBSignalCMD signalingType = this.getSignallingType(msgExtension);
        if(signalingType != null) {
            QBRTCSessionDescription sessionDescription = this.createSessionDescription(msgExtension, sessionID);
            if(sessionDescription != null) {
                sessionDescription.setUserInfo(userInfo);
                this.processCommand(fromUser, msgExtension, signalingType, sessionDescription);
            }
        }
    }

    private void processCommand(QBUser fromUser, QBChatMessageExtension msgExtension, QBSignalCMD signalingType, QBRTCSessionDescription sessionDescription) {
        switch(signalingType.ordinal()) {
        case 1:
            SessionDescription sdp = new SessionDescription(Type.OFFER, msgExtension.getProperty(QBSignalField.SDP.getValue()));
            if(Decorators.logIfNull(sdp, "Session description was not set properly in signaling message")) {
                return;
            }

            this.notifyOnCall(sessionDescription, fromUser, sdp);
            break;
        case 2:
            SessionDescription sdpAccept = new SessionDescription(Type.ANSWER, msgExtension.getProperty(QBSignalField.SDP.getValue()));
            if(Decorators.logIfNull(sdpAccept, "Session description was not set properly in signaling message")) {
                return;
            }

            this.notifyAcceptCall(sessionDescription, fromUser, sdpAccept);
            break;
        case 3:
            this.notifyRejectCall(fromUser, sessionDescription);
            break;
        case 4:
            try {
                int iceCandidates1 = Integer.parseInt(msgExtension.getProperty(QBCandidate.SDP_MLINE_INDEX.getValue()));
                String sdpMid = msgExtension.getProperty(QBCandidate.SDP_MID.getValue());
                String sdpCandidate = msgExtension.getProperty(QBCandidate.CANDIDATE_DESC.getValue());
                IceCandidate candidate = new IceCandidate(sdpMid, iceCandidates1, sdpCandidate);
                LinkedList candidates = new LinkedList();
                candidates.add(candidate);
                this.notifyCandidates(sessionDescription, fromUser, candidates);
            } catch (NumberFormatException var12) {
                Decorators.logIfNull((Object)null, "Field \'Ice candidates\' was not set properly in signaling message");
            }
            break;
        case 5:
            List iceCandidates = (List)msgExtension.getComplexProperty(QBSignalField.CANDIDATES.getValue());
            if(CollectionsUtil.isEmpty(iceCandidates)) {
                Decorators.logIfNull((Object)null, "Field \'Ice candidates\' was not set properly in signaling message");
                return;
            }

            this.notifyCandidates(sessionDescription, fromUser, iceCandidates);
            break;
        case 6:
            this.notifyStopCall(fromUser, sessionDescription);
        case 7:
        case 8:
        case 9:
        }

    }

    private QBRTCSessionDescription createSessionDescription(QBChatMessageExtension msgExtension, String sessionID) {
        Integer callerID = null;

        try {
            callerID = Integer.valueOf(this.retrieveDataByPropertyID(msgExtension, QBSignalField.CALLER.getValue()));
        } catch (NumberFormatException var7) {
            ;
        }

        if(Decorators.logIfNull(callerID, "Caller Id was not set properly in signaling message")) {
            return null;
        } else {
            int callTypeValue = this.getCallType(msgExtension);
            if(callTypeValue == 0) {
                Decorators.logIfNull((Object)null, "Conference type was not set properly in signaling message");
                return null;
            } else {
                QBConferenceType callType = callTypeValue == 1?QBConferenceType.QB_CONFERENCE_TYPE_VIDEO:QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                List opponentIDs = this.getOpponentsList(msgExtension);
                if(CollectionsUtil.isEmpty(opponentIDs)) {
                    Decorators.logIfNull((Object)null, "Field \'Opponents\' was not set properly in signaling message");
                    return null;
                } else {
                    return new QBRTCSessionDescription(sessionID, callerID, opponentIDs, callType);
                }
            }
        }
    }

    private String retrieveDataByPropertyID(QBChatMessageExtension msgExtension, String propertyName) {
        return !TextUtils.isEmpty(msgExtension.getProperty(propertyName))?msgExtension.getProperty(propertyName):"";
    }

    private Map<String, String> getUserInfo(QBChatMessageExtension msgExtension) {
        return (Map)msgExtension.getComplexProperty(QBSignalField.USER_INFO.getValue());
    }

    private List<Integer> getOpponentsList(QBChatMessageExtension msgExtension) {
        return (List)msgExtension.getComplexProperty(QBSignalField.OPPONENTS.getValue());
    }

    private QBSignalCMD getSignallingType(QBChatMessageExtension msgExtension) {
        QBSignalCMD signalingType = null;
        String signalingRawType = msgExtension.getProperty(QBSignalField.SIGNALING_TYPE.getValue());
        if(!TextUtils.isEmpty(signalingRawType)) {
            signalingType = QBSignalCMD.getTypeByRawValue(signalingRawType);
        }

        return signalingType;
    }

    private int getCallType(QBChatMessageExtension msgExtension) {
        int callType = 0;
        if(!TextUtils.isEmpty(msgExtension.getProperty(QBSignalField.CALL_TYPE.getValue()))) {
            try {
                callType = Integer.parseInt(msgExtension.getProperty(QBSignalField.CALL_TYPE.getValue()));
            } catch (NumberFormatException var4) {
                var4.printStackTrace();
            }
        }

        return callType;
    }

    private void notifyStopCall(QBUser sender, QBRTCSessionDescription sessionDescription) {
        Iterator i$ = this.webRTCSmackCallbacks.iterator();

        while(i$.hasNext()) {
            RTCSignallingMessageProcessorCallback callback = (RTCSignallingMessageProcessorCallback)i$.next();
            callback.onReceiveUserHungUpCall(sender.getId(), sessionDescription);
        }

    }

    private void notifyCandidates(QBRTCSessionDescription sessionDescription, QBUser sender, List<IceCandidate> candidates) {
        Iterator i$ = this.webRTCSmackCallbacks.iterator();

        while(i$.hasNext()) {
            RTCSignallingMessageProcessorCallback callback = (RTCSignallingMessageProcessorCallback)i$.next();
            callback.onReceiveIceCandidatesFromUser(candidates, sender.getId(), sessionDescription);
        }

    }

    private void notifyRejectCall(QBUser sender, QBRTCSessionDescription sessionDescription) {
        Iterator i$ = this.webRTCSmackCallbacks.iterator();

        while(i$.hasNext()) {
            RTCSignallingMessageProcessorCallback callback = (RTCSignallingMessageProcessorCallback)i$.next();
            callback.onReceiveRejectFromUser(sender.getId(), sessionDescription);
        }

    }

    private void notifyAcceptCall(QBRTCSessionDescription sessionDescription, QBUser sender, SessionDescription sdp) {
        Iterator i$ = this.webRTCSmackCallbacks.iterator();

        while(i$.hasNext()) {
            RTCSignallingMessageProcessorCallback callback = (RTCSignallingMessageProcessorCallback)i$.next();
            callback.onReceiveAcceptFromUser(sender.getId(), sessionDescription, sdp);
        }

    }

    private void notifyOnCall(QBRTCSessionDescription sessionDescription, QBUser sender, SessionDescription sdp) {
        Iterator i$ = this.webRTCSmackCallbacks.iterator();

        while(i$.hasNext()) {
            RTCSignallingMessageProcessorCallback callback = (RTCSignallingMessageProcessorCallback)i$.next();
            callback.onReceiveCallFromUser(sender.getId(), sessionDescription, sdp);
        }

    }

    static {
        QBChatMessageExtension.registerComplexPropertyParser(QBSignalField.USER_INFO.getValue(), new UserInfoParser());
        QBChatMessageExtension.registerComplexPropertyParser(QBSignalField.OPPONENTS.getValue(), new OpponentsParser());
        QBChatMessageExtension.registerComplexPropertyParser(QBSignalField.CANDIDATES.getValue(), new IceCandidateParser());
    }
}
