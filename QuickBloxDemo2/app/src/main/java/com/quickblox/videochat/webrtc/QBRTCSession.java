//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.videochat.webrtc;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.videochat.webrtc.LooperExecutor;
import com.quickblox.videochat.webrtc.PeerFactoryManager;
import com.quickblox.videochat.webrtc.QBMediaStreamManager;
import com.quickblox.videochat.webrtc.QBMediaStreamManagerCallback;
import com.quickblox.videochat.webrtc.QBPeerChannel;
import com.quickblox.videochat.webrtc.QBPeerChannelCallback;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import com.quickblox.videochat.webrtc.QBRTCUtils;
import com.quickblox.videochat.webrtc.QBSignalChannel;
import com.quickblox.videochat.webrtc.RTCSignallingMessageProcessor;
import com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType;
import com.quickblox.videochat.webrtc.QBRTCTypes.QBRTCConnectionState;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBSignalCMD;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBSignalField;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionConnectionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSignalingCallback;
import com.quickblox.videochat.webrtc.exception.QBRTCException;
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException;
import com.quickblox.videochat.webrtc.util.Logger;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.VideoTrack;
import org.webrtc.SessionDescription.Type;
import org.webrtc.VideoCapturerAndroid.CameraErrorHandler;

public final class QBRTCSession implements QBPeerChannelCallback, QBMediaStreamManagerCallback {
    private static final String CLASS_TAG = QBRTCSession.class.getSimpleName();
    private static final Logger LOGGER = Logger.getInstance("RTCClient");
    private final CameraErrorHandler cameraErrorHandler;
    private final RTCSignallingMessageProcessor xmppRTCMessageProcessor;
    private LooperExecutor executor;
    private QBRTCSessionDescription sessionDescription;
    private volatile QBMediaStreamManager mediaStreamManager;
    private final Map<Integer, QBPeerChannel> channels;
    private final Set<QBPeerChannel> activeChannels;
    private QBSignalChannel signalChannel;
    private Set<QBRTCClientSessionCallbacks> chatCallbackList;
    private Set<QBRTCClientVideoTracksCallbacks> videoTracksCallbacksList;
    private Set<QBRTCSessionConnectionCallbacks> connectionCallbacksList;
    private Set<QBRTCSignalingCallback> signalingCallbackSet;
    private boolean audioEnabled;
    private boolean videoEnabled;
    private QBRTCSession.QBRTCSessionState state;
    private boolean isNeedClose;
    private PeerFactoryManager factoryManager;
    private QBRTCClient client;
    private QBRTCSession.SessionWaitingTimers sessionWaitingTimers;
    private QBRTCSession.SessionOptions sessionOptions = new QBRTCSession.SessionOptions();
    private boolean isDestroyed;

    QBRTCSession(QBRTCClient client, QBRTCSessionDescription sessionDescription, Set<QBRTCClientSessionCallbacks> chatCallbackList, CameraErrorHandler cameraErrorHandler, RTCSignallingMessageProcessor xmppRTCMessageProcessor) {
        this.client = client;
        this.factoryManager = client.getPeerFactoryManager();
        LOGGER.d(CLASS_TAG, "Create new session");
        this.channels = new ConcurrentHashMap();
        this.activeChannels = new HashSet();
        this.sessionDescription = sessionDescription;
        this.signalChannel = new QBSignalChannel(this);
        this.chatCallbackList = chatCallbackList;
        this.videoTracksCallbacksList = new CopyOnWriteArraySet();
        this.connectionCallbacksList = new CopyOnWriteArraySet();
        this.signalingCallbackSet = new CopyOnWriteArraySet();
        this.cameraErrorHandler = cameraErrorHandler;
        this.xmppRTCMessageProcessor = xmppRTCMessageProcessor;
        this.initExecutor(client.getExecutor());
        this.initSessionState(sessionDescription);
        this.initSignallingWithOpponents(sessionDescription.getOpponents());
        this.makeChannelsWithOpponents(sessionDescription.getOpponents());
        this.startWaitingAcceptOrRejectTimer();
    }

    private void startWaitingAcceptOrRejectTimer() {
        boolean isInitiator = this.getCallerID().equals(QBRTCUtils.getCurrentChatUser());
        LOGGER.d(CLASS_TAG, "isInitiator=" + isInitiator);
        this.sessionWaitingTimers = new QBRTCSession.SessionWaitingTimers();
        if(!isInitiator) {
            this.sessionWaitingTimers.startWaitForUserActionsTask();
        }

    }

    public void setOptions(QBRTCSession.SessionOptions sessionOptions) {
        if(sessionOptions != null) {
            this.sessionOptions = sessionOptions;
        }
    }

    public void addSessionCallbacksListener(QBRTCSessionConnectionCallbacks callback) {
        if(callback != null) {
            this.connectionCallbacksList.add(callback);
        }

    }

    public void addSignalingCallback(QBRTCSignalingCallback callback) {
        if(callback != null) {
            this.signalingCallbackSet.add(callback);
        }

    }

    public void removeSignalingCallback(QBRTCSignalingCallback callback) {
        if(callback != null) {
            this.signalingCallbackSet.remove(callback);
        }

    }

    public void removeSessionCallbacksListener(QBRTCSessionConnectionCallbacks callback) {
        this.connectionCallbacksList.remove(callback);
    }

    public void addVideoTrackCallbacksListener(QBRTCClientVideoTracksCallbacks callback) {
        if(callback != null) {
            this.videoTracksCallbacksList.add(callback);
            LOGGER.d(CLASS_TAG, " ADD VideoTrackCallbacksListener " + callback);
        } else {
            LOGGER.e(CLASS_TAG, "Try to add null VideoTrackCallbacksListener");
        }

    }

    public void removeVideoTrackCallbacksListener(QBRTCClientVideoTracksCallbacks callback) {
        this.videoTracksCallbacksList.remove(callback);
        LOGGER.d(CLASS_TAG, " REMOVE VideoTrackCallbacksListener " + callback);
    }

    private void initSessionState(QBRTCSessionDescription sessionDescription) {
        this.setState(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_NEW);
    }

    private void initExecutor(LooperExecutor executor) {
        if(executor != null) {
            this.executor = executor;
        }

    }

    private void makeChannelsWithOpponents(List<Integer> opponents) {
        if(opponents != null) {
            Iterator i$ = opponents.iterator();

            while(i$.hasNext()) {
                Integer opponentID = (Integer)i$.next();
                if(!opponentID.equals(QBRTCUtils.getCurrentChatUser())) {
                    this.makeAndAddNewChannelForOpponent(opponentID);
                } else {
                    this.makeAndAddNewChannelForOpponent(this.getCallerID());
                }
            }
        }

    }

    private void makeAndAddNewChannelForOpponent(Integer opponentID) {
        if(!this.channels.containsKey(opponentID)) {
            QBPeerChannel newChannel = new QBPeerChannel(this.factoryManager, this, opponentID, this.getConferenceType());
            this.channels.put(opponentID, newChannel);
            this.activeChannels.add(newChannel);
            LOGGER.d(CLASS_TAG, "Make new channel for oppoennt:" + opponentID + newChannel.toString());
        } else {
            LOGGER.d(CLASS_TAG, "Channel with this opponent " + opponentID + " already exists");
        }

    }

    private synchronized void checkAllChannelsClosed(QBPeerChannel peerChannel) {
        LOGGER.d(CLASS_TAG, "Check is session need close");
        LOGGER.d(CLASS_TAG, "removing peer channel " + peerChannel);
        this.activeChannels.remove(peerChannel);
        this.stopWaitingUserTimer();
        if(this.isNeedToClose()) {
            LOGGER.d(CLASS_TAG, "Session isNeedToClose true");
            this.closeSession();
        } else {
            LOGGER.d(CLASS_TAG, "Session isNeedToClose false");
        }

    }

    private boolean isNeedToClose() {
        return this.activeChannels.size() == 0;
    }

    private void closeSession() {
        LOGGER.d(CLASS_TAG, "closeSession");
        if(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_CLOSE == this.getState()) {
            LOGGER.e(CLASS_TAG, "Session has already been closed");
        } else {
            this.setState(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_CLOSE);
            if(this.mediaStreamManager != null) {
                this.mediaStreamManager.setClosed();
            }

            this.executor.execute(new Runnable() {
                public void run() {
                    Iterator i$ = QBRTCSession.this.chatCallbackList.iterator();

                    QBRTCClientSessionCallbacks callback;
                    while(i$.hasNext()) {
                        callback = (QBRTCClientSessionCallbacks)i$.next();
                        callback.onSessionStartClose(QBRTCSession.this);
                    }

                    QBRTCSession.this.connectionCallbacksList.clear();
                    if(QBRTCSession.this.mediaStreamManager != null) {
                        QBRTCSession.this.mediaStreamManager.close();
                        QBRTCSession.this.mediaStreamManager = null;
                    }

                    QBRTCSession.this.sessionWaitingTimers.shutDown();
                    QBRTCSession.LOGGER.d(QBRTCSession.CLASS_TAG, "Notify sesions callbacks in count of:" + QBRTCSession.this.chatCallbackList.size());
                    i$ = QBRTCSession.this.chatCallbackList.iterator();

                    while(i$.hasNext()) {
                        callback = (QBRTCClientSessionCallbacks)i$.next();
                        callback.onSessionClosed(QBRTCSession.this);
                    }

                }
            });
        }
    }

    public void procRejectCallFromOpponent(Integer opponentID, Map<String, String> userInfo) {
        LOGGER.d(CLASS_TAG, "Process reject from " + opponentID);
        if(!this.isDestroyed()) {
            QBPeerChannel channel = (QBPeerChannel)this.channels.get(opponentID);
            if(channel != null) {
                channel.reject();
            }

            Iterator i$ = this.chatCallbackList.iterator();

            while(i$.hasNext()) {
                QBRTCClientSessionCallbacks callback = (QBRTCClientSessionCallbacks)i$.next();
                callback.onCallRejectByUser(this, opponentID, userInfo);
            }

        }
    }

    public void procHangUpOpponent(Integer opponentID, final Map<String, String> userInfo) {
        LOGGER.d(CLASS_TAG, "Process hang up from " + opponentID);
        if(opponentID.equals(this.getCallerID()) && (this.getState().ordinal() < QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE.ordinal() || this.shouldLeaveIfInitiatorHangUp())) {
            this.hangUp(this.getUserInfo());
        } else if(!this.isDestroyed()) {
            final QBPeerChannel channel = (QBPeerChannel)this.channels.get(opponentID);
            if(channel != null) {
                this.executor.execute(new Runnable() {
                    public void run() {
                        channel.procHungUp();
                        QBRTCSession.LOGGER.d(QBRTCSession.CLASS_TAG, "Notify users about hangUp session in count of " + QBRTCSession.this.chatCallbackList.size());
                        Iterator i$ = QBRTCSession.this.chatCallbackList.iterator();

                        while(i$.hasNext()) {
                            QBRTCClientSessionCallbacks callback = (QBRTCClientSessionCallbacks)i$.next();
                            callback.onReceiveHangUpFromUser(QBRTCSession.this, channel.getUserID(), userInfo);
                        }

                    }
                });
            }

        }
    }

    private boolean shouldLeaveIfInitiatorHangUp() {
        return this.sessionOptions.leaveSessionIfInitiatorHangUp;
    }

    void procRemoteAnswerSDP(SessionDescription sdp, Integer opponentID, Map<String, String> userInfo) {
        LOGGER.d(CLASS_TAG, "Process accept from " + opponentID);
        QBPeerChannel channel = (QBPeerChannel)this.channels.get(opponentID);
        if(channel != null) {
            if(QBRTCConnectionState.QB_RTC_CONNECTION_UNKNOWN != channel.getDisconnectReason()) {
                this.signalChannel.sendHandUpCallWithStatus(opponentID, (Map)null);
            } else {
                channel.setRemoteSDPToConnection(sdp);
                Iterator i$ = this.chatCallbackList.iterator();

                while(i$.hasNext()) {
                    QBRTCClientSessionCallbacks callback = (QBRTCClientSessionCallbacks)i$.next();
                    callback.onCallAcceptByUser(this, channel.getUserID(), userInfo);
                }
            }
        }

    }

    void procRemoteOfferSDP(SessionDescription sdp, Integer opponentID, Set<QBRTCClientSessionCallbacks> sessionCallbacksList) {
        LOGGER.d(CLASS_TAG, "procRemoteOfferSDP from " + opponentID);
        if(!this.checkAlreadyClosedChannel(opponentID)) {
            if(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_NEW == this.getState()) {
                this.setState(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_GATHERING);
                Iterator channel = sessionCallbacksList.iterator();

                while(channel.hasNext()) {
                    QBRTCClientSessionCallbacks chatCallback = (QBRTCClientSessionCallbacks)channel.next();
                    chatCallback.onReceiveNewSession(this);
                }
            }

            QBPeerChannel channel1 = (QBPeerChannel)this.channels.get(opponentID);
            if(channel1 != null) {
                channel1.setRemoteSessionDescription(sdp);
                if(this.isActive() && channel1.getState() == QBRTCConnectionState.QB_RTC_CONNECTION_PENDING) {
                    channel1.startAsAnswer();
                }
            } else {
                LOGGER.e(CLASS_TAG, "Chanel wasn\'t accepted till now");
            }

        }
    }

    public void procSetIceCandidates(List<IceCandidate> candidates, Integer opponentID) {
        LOGGER.d(CLASS_TAG, "process ice candidates from " + opponentID);
        QBPeerChannel channel = (QBPeerChannel)this.channels.get(opponentID);
        if(channel != null) {
            channel.setRemoteIceCandidates(candidates);
        }

    }

    private boolean checkAlreadyClosedChannel(Integer opponent) {
        QBPeerChannel peerChannel = (QBPeerChannel)this.channels.get(opponent);
        if(peerChannel == null) {
            return false;
        } else {
            QBRTCConnectionState disconnectReason = peerChannel.getDisconnectReason();
            boolean result = disconnectReason != QBRTCConnectionState.QB_RTC_CONNECTION_UNKNOWN;
            if(result) {
                QBChatMessage notifyingMessage = null;
                QBSignalCMD signalingCommand = null;
                if(disconnectReason == QBRTCConnectionState.QB_RTC_CONNECTION_HANG_UP) {
                    LOGGER.d(CLASS_TAG, "Channel for opponent:" + opponent + " already hung up");
                    notifyingMessage = this.signalChannel.sendHandUpCallWithStatus(opponent, (Map)null);
                    signalingCommand = QBSignalCMD.HANG_UP;
                } else if(disconnectReason == QBRTCConnectionState.QB_RTC_CONNECTION_REJECT) {
                    LOGGER.d(CLASS_TAG, "Channel for opponent:" + opponent + " already rejected");
                    notifyingMessage = this.signalChannel.sendRejectCallToOpponent(opponent, (Map)null);
                    signalingCommand = QBSignalCMD.REJECT_CALL;
                }

                if(notifyingMessage != null) {
                    this.sendMessage(signalingCommand, notifyingMessage, opponent);
                }
            }

            return result;
        }
    }

    private QBMediaStreamManager obtainMediaStreamManager() {
        if(this.mediaStreamManager == null) {
            this.mediaStreamManager = new QBMediaStreamManager(this.factoryManager, this.client.getContext(), this);
        }

        return this.mediaStreamManager;
    }

    public String getSessionID() {
        return this.sessionDescription.getSessionId();
    }

    public Integer getCallerID() {
        return this.sessionDescription.getCallerID();
    }

    public List<Integer> getOpponents() {
        return this.sessionDescription.getOpponents();
    }

    public QBConferenceType getConferenceType() {
        return this.sessionDescription.getConferenceType();
    }

    synchronized void setState(QBRTCSession.QBRTCSessionState state) {
        if(state != QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_UNKNOWN) {
            this.state = state;
            this.onStateChanged(this.state);
        }

    }

    private void onStateChanged(QBRTCSession.QBRTCSessionState state) {
        if(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE == state) {
            this.factoryManager.createFactory();
        }

    }

    public synchronized QBRTCSession.QBRTCSessionState getState() {
        return this.state;
    }

    public Map<String, String> getUserInfo() {
        return this.sessionDescription.getUserInfo();
    }

    public QBPeerChannel getPeerChannel(Integer userId) {
        return (QBPeerChannel)this.channels.get(userId);
    }

    public QBRTCSessionDescription getSessionDescription() {
        return this.sessionDescription;
    }

    public String toString() {
        return "QBRTCSession{sessionDescription=" + this.sessionDescription + ", MediaStreamManager=" + (this.mediaStreamManager != null?this.mediaStreamManager:"null") + ", channels=" + this.channels + ", signalChannel=" + this.signalChannel + ", chatCallbackList=" + this.chatCallbackList + ", audioEnabled=" + this.audioEnabled + ", videoEnabled=" + this.videoEnabled + ", state=" + this.state + ", isNeedClose=" + this.isNeedClose + '}';
    }

    public boolean equals(Object session) {
        return session instanceof QBRTCSession?(this == session?true:this.getSessionID().equals(((QBRTCSession)session).getSessionID())):false;
    }

    public int hashCode() {
        int result = this.sessionDescription.hashCode();
        result = 31 * result + this.mediaStreamManager.hashCode();
        result = 31 * result + this.channels.hashCode();
        result = 31 * result + this.signalChannel.hashCode();
        result = 31 * result + this.chatCallbackList.hashCode();
        result = 31 * result + (this.audioEnabled?1:0);
        result = 31 * result + (this.videoEnabled?1:0);
        result = 31 * result + this.state.hashCode();
        return result;
    }

    private void sendMessage(QBSignalCMD packet, QBChatMessage chatMessage, Integer opponentId) {
        QBWebRTCSignaling usrSignalling = QBChatService.getInstance().getVideoChatWebRTCSignalingManager().getSignaling(opponentId.intValue());
        if(usrSignalling != null) {
            try {
                LOGGER.d(CLASS_TAG, (String)chatMessage.getProperties().get(QBSignalField.SIGNALING_TYPE.getValue()) + " message is sending to opponent" + opponentId);
                usrSignalling.sendMessage(chatMessage);
                this.notifySignalingCallbacks(opponentId, packet, (NotConnectedException)null);
            } catch (NotConnectedException var6) {
                LOGGER.d(CLASS_TAG, "A Problem occurred while sending message: " + (var6.getMessage() != null?var6.getMessage():""));
                this.notifySignalingCallbacks(opponentId, packet, var6);
            }
        } else {
            LOGGER.d(CLASS_TAG, "There is no signalling exists for this user");
        }

    }

    private void notifySignalingCallbacks(Integer opponentId, QBSignalCMD packet, NotConnectedException e) {
        Iterator i$ = this.signalingCallbackSet.iterator();

        while(i$.hasNext()) {
            QBRTCSignalingCallback signalingCallback = (QBRTCSignalingCallback)i$.next();
            if(e != null) {
                signalingCallback.onErrorSendingPacket(packet, opponentId, new QBRTCSignalException(e));
            } else {
                signalingCallback.onSuccessSendingPacket(packet, opponentId);
            }
        }

    }

    public void startCall(final Map<String, String> userInfo) {
        LOGGER.d(CLASS_TAG, "startCall");
        this.executor.execute(new Runnable() {
            public void run() {
                if(QBRTCSession.this.isDestroyed()) {
                    QBRTCSession.LOGGER.d(QBRTCSession.CLASS_TAG, "session destroyed");
                } else {
                    QBRTCSession.this.sessionDescription.setUserInfo(userInfo);
                    QBRTCSession.this.setState(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE);
                    QBRTCSession.this.stopWaitingUserTimer();
                    Iterator i$ = QBRTCSession.this.getOpponents().iterator();

                    while(i$.hasNext()) {
                        Integer opponentID = (Integer)i$.next();
                        QBPeerChannel channel = (QBPeerChannel)QBRTCSession.this.channels.get(opponentID);
                        if(channel == null) {
                            return;
                        }

                        if(channel.getState() != QBRTCConnectionState.QB_RTC_CONNECTION_NEW) {
                            return;
                        }

                        channel.startAsOffer();
                    }

                }
            }
        });
    }

    public void acceptCall(final Map<String, String> userInfo) {
        LOGGER.d(CLASS_TAG, "acceptCall");
        this.executor.execute(new Runnable() {
            public void run() {
                if(!QBRTCSession.this.isDestroyed() && QBRTCSession.this.getState().ordinal() != QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE.ordinal()) {
                    QBRTCSession.this.stopWaitingUserTimer();
                    QBRTCSession.this.sessionDescription.setUserInfo(userInfo);
                    QBRTCSession.this.setState(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE);
                    Iterator i$ = QBRTCSession.this.channels.entrySet().iterator();

                    while(i$.hasNext()) {
                        Entry entry = (Entry)i$.next();
                        QBPeerChannel channel = (QBPeerChannel)entry.getValue();
                        Integer opponentID = (Integer)entry.getKey();
                        if(channel != null && channel.getState() == QBRTCConnectionState.QB_RTC_CONNECTION_NEW) {
                            if(opponentID.equals(QBRTCSession.this.getCallerID())) {
                                channel.startAsAnswer();
                            } else if(opponentID.intValue() < QBRTCUtils.getCurrentChatUser().intValue()) {
                                channel.startAsOffer();
                            } else if(channel.getRemoteSDP() != null) {
                                channel.startAsAnswer();
                            } else {
                                channel.startWaitOffer();
                            }
                        }
                    }

                }
            }
        });
    }

    private MediaConstraints defineMediaConstraints() {
        return null;
    }

    public void rejectCall(final Map<String, String> userInfo) {
        LOGGER.d(CLASS_TAG, "rejectCall");
        if(!this.isDestroyed()) {
            this.setState(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_REJECTED);
            this.executor.execute(new Runnable() {
                public void run() {
                    QBRTCSession.this.stopWaitingUserTimer();
                    QBRTCSession.LOGGER.d(QBRTCSession.CLASS_TAG, "Set session state rajected");
                    QBRTCSession.this.callRejectOnChannels(userInfo);
                }
            });
        }
    }

    public void setMessageListenerQBWebRTCSignaling(QBWebRTCSignaling mRTCSignaling) {
        mRTCSignaling.addMessageListener(this.xmppRTCMessageProcessor);
    }

    public QBMediaStreamManager getMediaStreamManager() {
        return this.mediaStreamManager;
    }

    /** @deprecated */
    @Deprecated
    public boolean getAudioEnability() {
        return this.obtainMediaStreamManager().isAudioEnabled();
    }

    /** @deprecated */
    @Deprecated
    public void setAudioEnabled(boolean audioEnability) {
        if(this.isActive()) {
            this.obtainMediaStreamManager().setAudioEnabled(audioEnability);
        }

    }

    /** @deprecated */
    @Deprecated
    public boolean getVideoEnability() {
        return this.obtainMediaStreamManager().isVideoEnabled();
    }

    /** @deprecated */
    @Deprecated
    public void setVideoEnabled(boolean videoEnability) {
        if(this.isActive()) {
            this.obtainMediaStreamManager().setVideoEnabled(videoEnability);
        }

    }

    /** @deprecated */
    @Deprecated
    public void switchCapturePosition(Runnable runnable) {
        if(this.isActive()) {
            this.obtainMediaStreamManager().switchCameraInput(runnable);
        }

    }

    /** @deprecated */
    @Deprecated
    public boolean switchAudioOutput() {
        return this.isActive()?this.obtainMediaStreamManager().switchAudioOutput():false;
    }

    /** @deprecated */
    @Deprecated
    public QBRTCConnectionState connectionStateForUser(Integer userID) {
        if(userID != null) {
            QBPeerChannel channel = (QBPeerChannel)this.channels.get(userID);
            return channel.getState();
        } else {
            return QBRTCConnectionState.QB_RTC_CONNECTION_UNKNOWN;
        }
    }

    private void callRejectOnChannels(Map<String, String> userInfo) {
        Iterator i$ = this.channels.entrySet().iterator();

        while(i$.hasNext()) {
            Entry entry = (Entry)i$.next();
            QBChatMessage rejectCallMessage = this.signalChannel.sendRejectCallToOpponent(this.getCallerID(), userInfo);
            this.sendMessage(QBSignalCMD.REJECT_CALL, rejectCallMessage, (Integer)entry.getKey());
            ((QBPeerChannel)entry.getValue()).reject();
        }

    }

    public void hangUp(final Map<String, String> userInfo) {
        LOGGER.d(CLASS_TAG, "hangUp");
        if(!this.isDestroyed()) {
            final boolean shouldNotifyParticipants = this.isActive();
            this.setState(QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_HANG_UP);
            this.stopWaitingUserTimer();
            this.executor.execute(new Runnable() {
                public void run() {
                    QBRTCSession.this.callHangUpOnChannels(userInfo, shouldNotifyParticipants);
                }
            });
        }
    }

    private void callHangUpOnChannels(Map<String, String> userInfo, boolean shouldNotifyParticipants) {
        LOGGER.d(CLASS_TAG, "callHangUpOnChannels");
        if(this.channels.size() != 0) {
            QBPeerChannel channel;
            for(Iterator i$ = this.channels.keySet().iterator(); i$.hasNext(); channel.hangUp()) {
                Integer opponentID = (Integer)i$.next();
                channel = (QBPeerChannel)this.channels.get(opponentID);
                if(shouldNotifyParticipants) {
                    QBChatMessage hangUpMessage = this.signalChannel.sendHandUpCallWithStatus(opponentID, userInfo);
                    this.sendMessage(QBSignalCMD.HANG_UP, hangUpMessage, opponentID);
                }
            }

        }
    }

    private void sendHangUpOnChannel(int opponentID, Map<String, String> userInfo) {
        QBChatMessage hangUpMessage = this.signalChannel.sendHandUpCallWithStatus(Integer.valueOf(opponentID), userInfo);
        this.sendMessage(QBSignalCMD.HANG_UP, hangUpMessage, Integer.valueOf(opponentID));
    }

    private synchronized boolean isDestroyed() {
        return this.state.ordinal() > QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE.ordinal();
    }

    private boolean isActive() {
        return this.getState().ordinal() == QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_ACTIVE.ordinal();
    }

    private void initSignallingWithOpponents(List<Integer> opponentIDs) {
        Iterator i$ = opponentIDs.iterator();

        while(i$.hasNext()) {
            Integer opponentId = (Integer)i$.next();
            if(!opponentId.equals(QBRTCUtils.getCurrentChatUser())) {
                QBWebRTCSignaling signaling = QBChatService.getInstance().getVideoChatWebRTCSignalingManager().getSignaling(opponentId.intValue());
                if(signaling == null) {
                    signaling = QBChatService.getInstance().getVideoChatWebRTCSignalingManager().createSignaling(opponentId.intValue(), (QBVideoChatSignalingListener)null);
                }

                this.client.addSignaling(signaling);
            }
        }

    }

    private void stopWaitingUserTimer() {
        this.sessionWaitingTimers.stopWaitForUserActionsTimer();
    }

    private void startWaitingUserTimer() {
        this.sessionWaitingTimers.stopWaitForUserActionsTimer();
    }

    public void setAudioCategoryError(Exception error) {
        LOGGER.d(CLASS_TAG, "setAudioCategoryError. " + error);
    }

    public void onReceiveLocalVideoTrack(VideoTrack videoTrack) {
        LOGGER.d(CLASS_TAG, "onReceiveLocalVideoTrack=" + videoTrack.id());
        this.executor.execute(new Runnable() {
            public void run() {
                VideoTrack incomeVideoTrack = QBRTCSession.this.obtainMediaStreamManager().getLocalVideoTrack();
                if(incomeVideoTrack != null) {
                    QBRTCVideoTrack localVideoTrack = new QBRTCVideoTrack(incomeVideoTrack, QBRTCUtils.getCurrentChatUser(), false);
                    QBRTCSession.this.obtainMediaStreamManager().getVideoTracks().put(QBRTCUtils.getCurrentChatUser(), localVideoTrack);
                    Iterator i$ = QBRTCSession.this.videoTracksCallbacksList.iterator();

                    while(i$.hasNext()) {
                        QBRTCClientVideoTracksCallbacks callback = (QBRTCClientVideoTracksCallbacks)i$.next();
                        callback.onLocalVideoTrackReceive(QBRTCSession.this, localVideoTrack);
                    }
                } else {
                    QBRTCSession.LOGGER.e(QBRTCSession.CLASS_TAG, "obtainMediaStreamManager().getLocalVideoTrack() return NULL");
                }

            }
        });
    }

    public void onMediaStreamChange(final QBMediaStreamManager mediaStreamManager) {
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.channels.values().iterator();

                while(i$.hasNext()) {
                    QBPeerChannel channel = (QBPeerChannel)i$.next();
                    channel.setLocalMediaStream(mediaStreamManager.getLocalMediaStream());
                }

            }
        });
    }

    public void onSessionDescriptionSend(final QBPeerChannel channel, final SessionDescription sdp) {
        LOGGER.d(CLASS_TAG, "onSessionDescriptionSend for channel opponent " + channel.getUserID());
        if(sdp != null) {
            this.executor.execute(new Runnable() {
                public void run() {
                    QBChatMessage offerMessage;
                    if(sdp.type.equals(Type.ANSWER)) {
                        offerMessage = QBRTCSession.this.signalChannel.sendAcceptCallMessage(sdp, channel.getUserID(), QBRTCSession.this.getUserInfo());
                        QBRTCSession.this.sendMessage(QBSignalCMD.ACCEPT_CALL, offerMessage, channel.getUserID());
                    } else {
                        offerMessage = QBRTCSession.this.signalChannel.sendCallRequestMessage(sdp, channel.getUserID(), QBRTCSession.this.getUserInfo());
                        QBRTCSession.this.sendMessage(QBSignalCMD.CALL, offerMessage, channel.getUserID());
                    }

                }
            });
        }

    }

    public void onHangUpSend(final QBPeerChannel channel) {
        this.executor.execute(new Runnable() {
            public void run() {
                QBRTCSession.this.sendHangUpOnChannel(channel.getUserID().intValue(), QBRTCSession.this.getUserInfo());
            }
        });
    }

    public void onIceCandidatesSend(final QBPeerChannel channel, final List<IceCandidate> candidates) {
        LOGGER.d(CLASS_TAG, "onIceCandidatesSend for channel opponent " + channel.getUserID());
        if(this.isActive()) {
            if(candidates.size() > 0) {
                this.executor.execute(new Runnable() {
                    public void run() {
                        QBChatMessage iceCandidatesMessage = QBRTCSession.this.signalChannel.sendIceCandidates(channel.getUserID(), candidates, QBRTCSession.this.getUserInfo());
                        QBRTCSession.this.sendMessage(QBSignalCMD.CANDITATES, iceCandidatesMessage, channel.getUserID());
                        QBRTCSession.LOGGER.d(QBRTCSession.CLASS_TAG, "Candidates in count of " + candidates.size() + " was sent");
                    }
                });
            }
        } else {
            LOGGER.d(CLASS_TAG, "Store candidates");
        }

    }

    public void onChannelConnectionConnected(final QBPeerChannel channel) {
        LOGGER.d(CLASS_TAG, "onChannelConnectionConnected for opponent " + channel.getUserID());
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.connectionCallbacksList.iterator();

                while(i$.hasNext()) {
                    QBRTCSessionConnectionCallbacks callback = (QBRTCSessionConnectionCallbacks)i$.next();
                    callback.onConnectedToUser(QBRTCSession.this, channel.getUserID());
                }

            }
        });
    }

    public void onChannelConnectionDisconnected(final QBPeerChannel channel) {
        LOGGER.d(CLASS_TAG, "onChannelConnectionDisconnected for opponent " + channel.getUserID());
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.connectionCallbacksList.iterator();

                while(i$.hasNext()) {
                    QBRTCSessionConnectionCallbacks callback = (QBRTCSessionConnectionCallbacks)i$.next();
                    callback.onDisconnectedFromUser(QBRTCSession.this, channel.getUserID());
                }

            }
        });
    }

    public void onError(QBPeerChannel channel, final QBRTCException e) {
        LOGGER.d(CLASS_TAG, "onError in peer channel for opponent " + channel.getUserID() + ", " + e.getMessage());
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.connectionCallbacksList.iterator();

                while(i$.hasNext()) {
                    QBRTCSessionConnectionCallbacks callback = (QBRTCSessionConnectionCallbacks)i$.next();
                    callback.onError(QBRTCSession.this, e);
                }

                QBRTCSession.this.closeChannels();
            }
        });
    }

    private void closeChannels() {
        Iterator i$ = this.channels.values().iterator();

        while(i$.hasNext()) {
            QBPeerChannel channel = (QBPeerChannel)i$.next();
            channel.hangUp();
        }

    }

    public void onChannelNotAnswer(final QBPeerChannel channel) {
        LOGGER.d(CLASS_TAG, "onChannelNotAnswer for opponent " + channel.getUserID());
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.chatCallbackList.iterator();

                while(i$.hasNext()) {
                    QBRTCClientSessionCallbacks callback = (QBRTCClientSessionCallbacks)i$.next();
                    callback.onUserNotAnswer(QBRTCSession.this, channel.getUserID());
                }

            }
        });
    }

    public void onChannelConnectionClosed(final QBPeerChannel channel) {
        LOGGER.d(CLASS_TAG, "onChannelConnectionClosed for opponent " + channel.getUserID());
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.connectionCallbacksList.iterator();

                while(i$.hasNext()) {
                    QBRTCSessionConnectionCallbacks callback = (QBRTCSessionConnectionCallbacks)i$.next();
                    callback.onConnectionClosedForUser(QBRTCSession.this, channel.getUserID());
                }

                QBRTCSession.this.checkAllChannelsClosed(channel);
            }
        });
    }

    public void onChannelConnectionFailed(final QBPeerChannel channel) {
        LOGGER.d(CLASS_TAG, "onChannelConnectionFailed for opponent " + channel.getUserID());
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.connectionCallbacksList.iterator();

                while(i$.hasNext()) {
                    QBRTCSessionConnectionCallbacks callback = (QBRTCSessionConnectionCallbacks)i$.next();
                    callback.onConnectionFailedWithUser(QBRTCSession.this, channel.getUserID());
                }

            }
        });
    }

    public void onChannelConnectionConnecting(final QBPeerChannel channel) {
        LOGGER.d(CLASS_TAG, "onChannelConnectionConnecting for opponent " + channel.getUserID());
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.connectionCallbacksList.iterator();

                while(i$.hasNext()) {
                    QBRTCSessionConnectionCallbacks callback = (QBRTCSessionConnectionCallbacks)i$.next();
                    callback.onStartConnectToUser(QBRTCSession.this, channel.getUserID());
                }

            }
        });
    }

    public MediaStream onLocalStreamNeedAdd(QBPeerChannel channel) throws QBRTCException {
        LOGGER.d(CLASS_TAG, "onLocalStreamNeedAdd for opponent " + channel.getUserID());
        return this.obtainMediaStreamManager().initLocalMediaStream(this.getConferenceType(), this.cameraErrorHandler);
    }

    public void onRemoteVideoTrackReceive(final QBPeerChannel channel, final QBRTCVideoTrack videoTrack) {
        LOGGER.d(CLASS_TAG, "onRemoteVideoTrackReceive for opponent " + channel.getUserID());
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.videoTracksCallbacksList.iterator();

                while(i$.hasNext()) {
                    QBRTCClientVideoTracksCallbacks callback = (QBRTCClientVideoTracksCallbacks)i$.next();
                    callback.onRemoteVideoTrackReceive(QBRTCSession.this, videoTrack, channel.getUserID());
                }

            }
        });
    }

    public void onPeerConnectionStatsReady(StatsReport[] reports) {
        StatsReport[] arr$ = reports;
        int len$ = reports.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            StatsReport report = arr$[i$];
            LOGGER.d(CLASS_TAG, "onRemoteVideoTrackReceive for opponent " + report.toString());
        }

    }

    public QBConferenceType conferenceTypeForChannel(QBPeerChannel channel) {
        return this.getConferenceType();
    }

    private void noUserAction() {
        LOGGER.d(CLASS_TAG, "no User Actions");
        this.stopWaitingUserTimer();
        this.executor.execute(new Runnable() {
            public void run() {
                Iterator i$ = QBRTCSession.this.chatCallbackList.iterator();

                while(i$.hasNext()) {
                    QBRTCClientSessionCallbacks sessionCallback = (QBRTCClientSessionCallbacks)i$.next();
                    sessionCallback.onUserNoActions(QBRTCSession.this, QBRTCUtils.getCurrentChatUser());
                }

            }
        });
        this.hangUp(this.getUserInfo());
    }

    public static enum QBRTCSessionState {
        QB_RTC_SESSION_UNKNOWN,
        QB_RTC_SESSION_NEW,
        QB_RTC_SESSION_GATHERING,
        QB_RTC_SESSION_ACTIVE,
        QB_RTC_SESSION_REJECTED,
        QB_RTC_SESSION_HANG_UP,
        QB_RTC_SESSION_CLOSE;

        private QBRTCSessionState() {
        }
    }

    class SessionWaitingTimers {
        private final String TAG;
        private ScheduledExecutorService scheduledTasksService;
        private Runnable waitUserActionsTask;
        private ScheduledFuture<?> futureWaitTask;

        public SessionWaitingTimers() {
            this.TAG = QBRTCSession.CLASS_TAG + "." + QBRTCSession.SessionWaitingTimers.class.getSimpleName();
            this.scheduledTasksService = Executors.newScheduledThreadPool(3);
            this.waitUserActionsTask = new Runnable() {
                public void run() {
                    QBRTCSession.this.noUserAction();
                }
            };
        }

        private void startWaitForUserActionsTask() {
            QBRTCSession.LOGGER.d(this.TAG, "startWaitForUserActionsTask for " + QBRTCConfig.getAnswerTimeInterval());
            if(!this.scheduledTasksService.isShutdown()) {
                this.futureWaitTask = this.scheduledTasksService.schedule(this.waitUserActionsTask, QBRTCConfig.getAnswerTimeInterval(), TimeUnit.SECONDS);
            }

        }

        private void shutDown() {
            this.stopWaitForUserActionsTimer();
            this.scheduledTasksService.shutdownNow();
        }

        private void stopWaitForUserActionsTimer() {
            QBRTCSession.LOGGER.d(this.TAG, "Stop WaitTimer");
            if(this.futureWaitTask != null && !this.futureWaitTask.isCancelled()) {
                this.futureWaitTask.cancel(true);
                this.futureWaitTask = null;
            }

        }
    }

    public static class SessionOptions {
        public boolean leaveSessionIfInitiatorHangUp;
        public boolean closeIfInitiatorHangUp;

        public SessionOptions() {
        }
    }
}
