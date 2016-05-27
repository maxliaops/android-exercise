//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.videochat.webrtc;

import com.quickblox.videochat.webrtc.LooperExecutor;
import com.quickblox.videochat.webrtc.PeerFactoryManager;
import com.quickblox.videochat.webrtc.QBPeerChannelCallback;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCMediaConfig;
import com.quickblox.videochat.webrtc.QBRTCUtils;
import com.quickblox.videochat.webrtc.RTCMediaUtils;
import com.quickblox.videochat.webrtc.LooperExecutor.ExecuteCondition;
import com.quickblox.videochat.webrtc.LooperExecutor.ExecutorLifecycleListener;
import com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType;
import com.quickblox.videochat.webrtc.QBRTCTypes.QBRTCConnectionState;
import com.quickblox.videochat.webrtc.exception.QBRTCException;
import com.quickblox.videochat.webrtc.util.Logger;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.StatsObserver;
import org.webrtc.StatsReport;
import org.webrtc.VideoTrack;
import org.webrtc.PeerConnection.IceConnectionState;
import org.webrtc.PeerConnection.IceGatheringState;
import org.webrtc.PeerConnection.Observer;
import org.webrtc.PeerConnection.SignalingState;
import org.webrtc.SessionDescription.Type;

public final class QBPeerChannel implements ExecuteCondition, ExecutorLifecycleListener {
    private static final String CLASS_TAG = QBPeerChannel.class.getSimpleName();
    private static final Logger LOGGER = Logger.getInstance("RTCClient");
    private LooperExecutor executor;
    private final QBPeerChannel.PeerChannelLifeCycleTimers peerChannelTimers;
    private QBConferenceType conferenceType;
    private QBPeerChannelCallback pcEventsCallback;
    private Integer userID;
    private MediaConstraints peerConstraints;
    private QBRTCConnectionState state;
    private List<IceCandidate> iceCandidates;
    private MediaStream localStream;
    private MediaStream remoteStream;
    private Observer pcObserver = new QBPeerChannel.PCObserver();
    private SdpObserver sdpObserver = new QBPeerChannel.SDPObserver();
    private SessionDescription remoteSDP;
    private SessionDescription localSdp;
    private final Timer statsTimer = new Timer();
    private boolean isError;
    private QBRTCConnectionState disconnectReason;
    private QBRTCVideoTrack remoteVideoTrack;
    private long answerTime;
    private volatile PeerConnection peerConnection;
    private volatile boolean isCloseStarted;
    private PeerFactoryManager peerFactoryManager;

    QBPeerChannel(PeerFactoryManager peerFactoryManager, QBPeerChannelCallback callback, Integer userID, QBConferenceType conferenceType) {
        this.peerFactoryManager = peerFactoryManager;
        this.pcEventsCallback = callback;
        this.userID = userID;
        this.peerConstraints = RTCMediaUtils.createConferenceConstraints(conferenceType);
        this.state = QBRTCConnectionState.QB_RTC_CONNECTION_NEW;
        this.iceCandidates = new LinkedList();
        this.conferenceType = conferenceType;
        this.disconnectReason = QBRTCConnectionState.QB_RTC_CONNECTION_UNKNOWN;
        this.peerChannelTimers = new QBPeerChannel.PeerChannelLifeCycleTimers();
        this.peerChannelTimers.initSchedledTasks();
        this.initExecutor(new LooperExecutor(QBPeerChannel.class));
    }

    public synchronized QBRTCConnectionState getState() {
        return this.state;
    }

    public QBRTCConnectionState getDisconnectReason() {
        return this.disconnectReason;
    }

    public QBRTCVideoTrack getRemoteVideoTrack() {
        return this.remoteVideoTrack;
    }

    private void initExecutor(LooperExecutor executor) {
        this.executor = executor;
        this.executor.requestStart();
        this.executor.addExecutorLifecycleListener(this);
        this.executor.setExecutionCondition(this);
    }

    private void createConnection() {
        LOGGER.d(CLASS_TAG, "createConnection for opponent " + this.userID);
        this.executor.execute(new Runnable() {
            public void run() {
                if(!QBPeerChannel.this.isDestroyed()) {
                    PeerConnectionFactory peerConnectionFactory = QBPeerChannel.this.peerFactoryManager.getPeerConnectionFactory();
                    QBPeerChannel.this.peerConnection = peerConnectionFactory.createPeerConnection(QBRTCUtils.getIceServersList(), QBPeerChannel.this.peerConstraints, QBPeerChannel.this.pcObserver);

                    try {
                        QBPeerChannel.this.setLocalMediaStream();
                    } catch (QBRTCException var3) {
                        QBPeerChannel.this.pcEventsCallback.onError(QBPeerChannel.this, var3);
                    }
                }

            }
        });
    }

    private void setLocalMediaStream() throws QBRTCException {
        this.localStream = this.pcEventsCallback.onLocalStreamNeedAdd(this);
        this.peerConnection.addStream(this.localStream);
    }

    void createOffer() {
        LOGGER.d(CLASS_TAG, "createOffer for opponent " + this.userID);
        this.executor.execute(new Runnable() {
            public void run() {
                if(QBPeerChannel.this.isPeerConnectionValid()) {
                    QBPeerChannel.this.peerConnection.createOffer(QBPeerChannel.this.sdpObserver, QBPeerChannel.this.peerConstraints);
                }

            }
        });
    }

    void createAnswer() {
        LOGGER.d(CLASS_TAG, "createAnswer for opponent " + this.userID);
        this.executor.execute(new Runnable() {
            public void run() {
                if(QBPeerChannel.this.isPeerConnectionValid()) {
                    QBPeerChannel.this.peerConnection.createAnswer(QBPeerChannel.this.sdpObserver, QBPeerChannel.this.peerConstraints);
                }

            }
        });
    }

    void startAsAnswer() {
        LOGGER.d(CLASS_TAG, "startAsAnswer for opponent: " + this.userID);
        if(!this.checkDestroyed()) {
            this.setState(QBRTCConnectionState.QB_RTC_CONNECTION_CONNECTING);
            this.peerChannelTimers.stopWaitTimer();
            this.conferenceType = this.pcEventsCallback.conferenceTypeForChannel(this);
            this.createConnection();
            this.setRemoteSDPToConnection(this.remoteSDP);
        }
    }

    void startAsOffer() {
        LOGGER.d(CLASS_TAG, "startAsOffer for opponent: " + this.userID);
        if(!this.checkDestroyed()) {
            this.setState(QBRTCConnectionState.QB_RTC_CONNECTION_CONNECTING);
            this.createConnection();
            this.createOffer();
        }
    }

    void startWaitOffer() {
        LOGGER.d(CLASS_TAG, "startWaitOffer for opponent: " + this.userID);
        if(!this.checkDestroyed()) {
            this.setState(QBRTCConnectionState.QB_RTC_CONNECTION_PENDING);
            this.peerChannelTimers.startWaitTask();
        }
    }

    void enableStatsEvents(boolean enable, int periodMs) {
        if(enable) {
            this.statsTimer.schedule(new TimerTask() {
                public void run() {
                    QBPeerChannel.this.executor.execute(new Runnable() {
                        public void run() {
                            QBPeerChannel.this.getStats();
                        }
                    });
                }
            }, 0L, (long)periodMs);
        } else {
            this.statsTimer.cancel();
        }

    }

    private void getStats() {
        if(this.peerConnection != null && !this.isError) {
            boolean success = this.peerConnection.getStats(new StatsObserver() {
                public void onComplete(StatsReport[] reports) {
                    QBPeerChannel.this.pcEventsCallback.onPeerConnectionStatsReady(reports);
                }
            }, (MediaStreamTrack)null);
            if(!success) {
                LOGGER.e(CLASS_TAG, "getStats() returns false!");
            }

        }
    }

    void setRemoteSDPToConnection(SessionDescription remoteSessionDP) {
        LOGGER.d(CLASS_TAG, "setRemoteSDPToConnection");
        if(remoteSessionDP != null) {
            this.peerChannelTimers.stopDialingTimer();
            this.remoteSDP = this.prepareDescription(remoteSessionDP);
            this.executor.execute(new Runnable() {
                public void run() {
                    if(QBPeerChannel.this.isPeerConnectionValid() && QBPeerChannel.this.peerConnection.getRemoteDescription() == null) {
                        QBPeerChannel.this.peerConnection.setRemoteDescription(QBPeerChannel.this.sdpObserver, QBPeerChannel.this.remoteSDP);
                    }

                }
            });
        }
    }

    private SessionDescription prepareDescription(SessionDescription sdp) {
        String description = RTCMediaUtils.generateRemoteDescription(sdp, new QBRTCMediaConfig(), this.conferenceType);
        return new SessionDescription(sdp.type, description);
    }

    private boolean checkDestroyed() {
        if(this.isDestroyed()) {
            LOGGER.d(CLASS_TAG, "Peer channel for user=" + this.userID + " have already been destroyed");
            return true;
        } else {
            return false;
        }
    }

    private synchronized boolean isDestroyed() {
        return this.disconnectReason != QBRTCConnectionState.QB_RTC_CONNECTION_UNKNOWN;
    }

    void reject() {
        LOGGER.d(CLASS_TAG, "Call Reject to opponent " + this.userID);
        this.close(QBRTCConnectionState.QB_RTC_CONNECTION_REJECT);
    }

    void hangUp() {
        LOGGER.d(CLASS_TAG, "Call hangUp to opponent " + this.userID);
        this.close(QBRTCConnectionState.QB_RTC_CONNECTION_HANG_UP);
    }

    void procHungUp() {
        LOGGER.d(CLASS_TAG, "Call procHungUp to opponent " + this.userID);
        this.close(QBRTCConnectionState.QB_RTC_CONNECTION_HANG_UP);
    }

    void close(QBRTCConnectionState disconnectReason) {
        LOGGER.d(CLASS_TAG, "close");
        if(!this.checkDestroyed()) {
            this.disconnectReason = disconnectReason;
            this.peerChannelTimers.stopAllTascks();
            this.executor.execute(new Runnable() {
                public void run() {
                    QBPeerChannel.this.isCloseStarted = true;
                    QBPeerChannel.LOGGER.d(QBPeerChannel.CLASS_TAG, "Closing peer connection start.");
                    if(QBPeerChannel.this.peerConnection != null) {
                        if(QBPeerChannel.this.localStream != null) {
                            QBPeerChannel.this.peerConnection.removeStream(QBPeerChannel.this.localStream);
                        }

                        QBPeerChannel.this.peerConnection.dispose();
                        QBPeerChannel.this.peerConnection = null;
                    }

                    QBPeerChannel.this.setState(QBRTCConnectionState.QB_RTC_CONNECTION_CLOSED);
                    QBPeerChannel.this.pcEventsCallback.onChannelConnectionClosed(QBPeerChannel.this);
                    QBPeerChannel.this.executor.requestStop();
                }
            });
        }
    }

    public void onExecutorStop() {
        LOGGER.d(CLASS_TAG, "onExecutorStop.PeerChannel succesfully stoped");
        this.executor.removeExecutorLifecycleListener(this);
    }

    private void onError(QBRTCException exception) {
        LOGGER.d(CLASS_TAG, "Call onError");
        this.close(QBRTCConnectionState.QB_RTC_CONNECTION_ERROR);
        this.pcEventsCallback.onError(this, exception);
    }

    private void noOffer() {
        LOGGER.d(CLASS_TAG, "noOffer for opponent " + this.userID);
        this.peerChannelTimers.stopDisconnectTimer();
        this.pcEventsCallback.onHangUpSend(this);
        this.close(QBRTCConnectionState.QB_RTC_CONNECTION_NOT_OFFER);
    }

    private void dialing() {
        LOGGER.d(CLASS_TAG, "dialing for opponent " + this.userID);
        this.answerTime += QBRTCConfig.getDialingTimeInterval();
        if(this.answerTime >= QBRTCConfig.getAnswerTimeInterval()) {
            this.peerChannelTimers.stopDisconnectTimer();
            this.pcEventsCallback.onChannelNotAnswer(this);
            this.pcEventsCallback.onHangUpSend(this);
            this.close(QBRTCConnectionState.QB_RTC_CONNECTION_NOT_ANSWER);
        } else {
            this.pcEventsCallback.onSessionDescriptionSend(this, this.peerConnection.getLocalDescription());
        }

    }

    void channelDisconnect() {
        LOGGER.d(CLASS_TAG, "channelDisconnect for opponent " + this.userID);
        this.close(QBRTCConnectionState.QB_RTC_CONNECTION_DISCONNECT_TIMEOUT);
    }

    private boolean isPeerConnectionValid() {
        return this.peerConnection != null && !this.isDestroyed();
    }

    void setRemoteSessionDescription(SessionDescription remoteSDP) {
        this.remoteSDP = remoteSDP;
    }

    SessionDescription getRemoteSDP() {
        return this.remoteSDP;
    }

    synchronized void setState(QBRTCConnectionState state) {
        this.state = state;
    }

    void setRemoteIceCandidates(final List<IceCandidate> candidates) {
        LOGGER.d(CLASS_TAG, "Set iceCandidates in count of: " + candidates.size());
        this.executor.execute(new Runnable() {
            public void run() {
                if(QBPeerChannel.this.isPeerConnectionValid()) {
                    Iterator i$ = candidates.iterator();

                    while(i$.hasNext()) {
                        IceCandidate candidate = (IceCandidate)i$.next();
                        QBPeerChannel.this.peerConnection.addIceCandidate(candidate);
                    }
                }

            }
        });
    }

    void setLocalMediaStream(MediaStream localMediaStream) {
        this.localStream = localMediaStream;
    }

    public Integer getUserID() {
        return this.userID;
    }

    public boolean isExecutionAllow() {
        return !this.isCloseStarted;
    }

    private void clearIceCandidates() {
        this.iceCandidates.clear();
    }

    class PeerChannelLifeCycleTimers {
        private final String TAG;
        private ScheduledExecutorService scheduledTasksService;
        private Runnable waitOfferTask;
        private Runnable dialingTask;
        private Runnable disconnectionTask;
        private ScheduledFuture<?> futureWaitTask;
        private ScheduledFuture<?> futureDisconnectTask;
        private ScheduledFuture<?> futureDialingTask;

        PeerChannelLifeCycleTimers() {
            this.TAG = QBPeerChannel.CLASS_TAG + "." + QBPeerChannel.PeerChannelLifeCycleTimers.class.getSimpleName();
        }

        public void initSchedledTasks() {
            this.scheduledTasksService = Executors.newScheduledThreadPool(3);
            this.waitOfferTask = new Runnable() {
                public void run() {
                    QBPeerChannel.this.noOffer();
                }
            };
            this.dialingTask = new Runnable() {
                public void run() {
                    QBPeerChannel.this.dialing();
                }
            };
            this.disconnectionTask = new Runnable() {
                public void run() {
                    QBPeerChannel.this.channelDisconnect();
                }
            };
        }

        private void startWaitTask() {
            if(!this.scheduledTasksService.isShutdown()) {
                this.futureWaitTask = this.scheduledTasksService.schedule(this.waitOfferTask, QBRTCConfig.getAnswerTimeInterval(), TimeUnit.SECONDS);
            }

        }

        private void startDisconnectTask() {
            if(!this.scheduledTasksService.isShutdown()) {
                this.futureDisconnectTask = this.scheduledTasksService.schedule(this.disconnectionTask, (long)QBRTCConfig.getDisconnectTime().intValue(), TimeUnit.SECONDS);
            }

        }

        private void startDialingTimer() {
            if(!this.scheduledTasksService.isShutdown()) {
                this.futureDialingTask = this.scheduledTasksService.scheduleAtFixedRate(this.dialingTask, 0L, QBRTCConfig.getDialingTimeInterval(), TimeUnit.SECONDS);
            }

        }

        private void stopAllTascks() {
            this.stopDialingTimer();
            this.stopWaitTimer();
            this.stopDisconnectTimer();
            this.scheduledTasksService.shutdownNow();
        }

        private void stopDialingTimer() {
            QBPeerChannel.LOGGER.d(this.TAG, "Stop DialingTimer");
            if(this.futureDialingTask != null) {
                this.futureDialingTask.cancel(true);
                this.futureDialingTask = null;
            }

        }

        private void stopDisconnectTimer() {
            QBPeerChannel.LOGGER.d(this.TAG, "Stop DisconnectTimer");
            if(this.futureDisconnectTask != null) {
                this.futureDisconnectTask.cancel(true);
                this.futureDisconnectTask = null;
            }

        }

        private void stopWaitTimer() {
            QBPeerChannel.LOGGER.d(this.TAG, "Stop WaitTimer");
            if(this.futureWaitTask != null) {
                this.futureWaitTask.cancel(true);
                this.futureWaitTask = null;
            }

        }
    }

    private class PCObserver implements Observer {
        private String TAG_PEERCONNECTION_OBSERVER;

        private PCObserver() {
            this.TAG_PEERCONNECTION_OBSERVER = QBPeerChannel.CLASS_TAG + ".PCObserver:";
        }

        public void onSignalingChange(SignalingState signalingState) {
            this.log("onSignalingChange to " + signalingState.toString());
        }

        public void onIceConnectionChange(final IceConnectionState iceConnectionState) {
            this.log("onIceConnectionChange to " + iceConnectionState.toString());
            QBPeerChannel.this.executor.execute(new Runnable() {
                public void run() {
                    if(QBPeerChannel.this.isPeerConnectionValid()) {
                        if(iceConnectionState == IceConnectionState.CHECKING) {
                            QBPeerChannel.this.setState(QBRTCConnectionState.QB_RTC_CONNECTION_CHECKING);
                            QBPeerChannel.this.pcEventsCallback.onChannelConnectionConnecting(QBPeerChannel.this);
                        } else if(iceConnectionState == IceConnectionState.CONNECTED) {
                            QBPeerChannel.this.peerChannelTimers.stopDisconnectTimer();
                            QBPeerChannel.this.setState(QBRTCConnectionState.QB_RTC_CONNECTION_CONNECTED);
                            QBPeerChannel.this.pcEventsCallback.onChannelConnectionConnected(QBPeerChannel.this);
                        } else if(iceConnectionState == IceConnectionState.DISCONNECTED) {
                            QBPeerChannel.this.setState(QBRTCConnectionState.QB_RTC_CONNECTION_DISCONNECTED);
                            QBPeerChannel.this.pcEventsCallback.onChannelConnectionDisconnected(QBPeerChannel.this);
                            QBPeerChannel.this.peerChannelTimers.startDisconnectTask();
                        } else if(iceConnectionState == IceConnectionState.FAILED) {
                            QBPeerChannel.this.pcEventsCallback.onChannelConnectionFailed(QBPeerChannel.this);
                            PCObserver.this.loge("ICE connection failed.");
                            QBPeerChannel.this.close(QBRTCConnectionState.QB_RTC_CONNECTION_FAILED);
                        } else if(iceConnectionState == IceConnectionState.CLOSED) {
                            PCObserver.this.log("onChannelConnectionClosed called on " + QBPeerChannel.this.pcEventsCallback);
                        }

                    }
                }
            });
        }

        public void onIceGatheringChange(IceGatheringState iceGatheringState) {
            this.log("onIceGatheringChange to " + iceGatheringState.toString());
        }

        public void onIceCandidate(final IceCandidate candidate) {
            this.log("onIceCandidate: " + candidate.sdpMLineIndex + " " + candidate.sdpMid);
            QBPeerChannel.this.executor.execute(new Runnable() {
                public void run() {
                    if(QBPeerChannel.this.isPeerConnectionValid()) {
                        if(QBPeerChannel.this.iceCandidates != null) {
                            QBPeerChannel.this.iceCandidates.add(candidate);
                        } else {
                            ArrayList iceCandidate = new ArrayList();
                            iceCandidate.add(candidate);
                            QBPeerChannel.this.pcEventsCallback.onIceCandidatesSend(QBPeerChannel.this, iceCandidate);
                        }

                    }
                }
            });
        }

        public void onAddStream(final MediaStream stream) {
            this.log("onAddStream");
            QBPeerChannel.this.remoteStream = stream;
            QBPeerChannel.this.executor.execute(new Runnable() {
                public void run() {
                    if(QBPeerChannel.this.isPeerConnectionValid()) {
                        QBRTCUtils.abortUnless(QBPeerChannel.this.remoteStream.audioTracks.size() <= 1 && QBPeerChannel.this.remoteStream.videoTracks.size() <= 1, "Weird-looking stream: " + QBPeerChannel.this.remoteStream);
                        if(QBPeerChannel.this.remoteStream.videoTracks.size() == 1) {
                            PCObserver.this.log("set remote stream TO remote renderer ");
                            QBPeerChannel.this.remoteVideoTrack = new QBRTCVideoTrack((VideoTrack)stream.videoTracks.getFirst(), true);
                            QBPeerChannel.this.pcEventsCallback.onRemoteVideoTrackReceive(QBPeerChannel.this, QBPeerChannel.this.remoteVideoTrack);
                        }

                    }
                }
            });
        }

        public void onRemoveStream(MediaStream stream) {
            this.log("onRemoveStream");
            QBPeerChannel.this.executor.execute(new Runnable() {
                public void run() {
                    QBPeerChannel.this.remoteStream = null;
                }
            });
        }

        public void onDataChannel(DataChannel dataChannel) {
            this.log("onDataChannel");
        }

        public void onRenegotiationNeeded() {
            this.log("onRenegotiationNeeded");
        }

        private void log(String onSignalingChange) {
            QBPeerChannel.LOGGER.d(this.TAG_PEERCONNECTION_OBSERVER, onSignalingChange);
        }

        private void loge(String onSignalingChange) {
            QBPeerChannel.LOGGER.e(this.TAG_PEERCONNECTION_OBSERVER, onSignalingChange);
        }
    }

    private class SDPObserver implements SdpObserver {
        private final String TAG_SDPOBSERVER;

        private SDPObserver() {
            this.TAG_SDPOBSERVER = QBPeerChannel.CLASS_TAG + ".SDPObserver:";
        }

        public void onCreateSuccess(SessionDescription sessionDescription) {
            this.log("SDP successfully created \n" + sessionDescription.description);
            QBRTCUtils.abortUnless(QBPeerChannel.this.localSdp == null, "multiple SDP create?!?");
            String sdpDescription = RTCMediaUtils.generateLocalDescription(sessionDescription, new QBRTCMediaConfig(), QBPeerChannel.this.conferenceType);
            final SessionDescription sdp = new SessionDescription(sessionDescription.type, sdpDescription);
            QBPeerChannel.this.localSdp = sdp;
            QBPeerChannel.this.executor.execute(new Runnable() {
                public void run() {
                    if(QBPeerChannel.this.isPeerConnectionValid()) {
                        QBPeerChannel.this.peerConnection.setLocalDescription(QBPeerChannel.this.sdpObserver, sdp);
                    }
                }
            });
        }

        public void onSetSuccess() {
            this.log("onSetSuccess");
            QBPeerChannel.this.executor.execute(new Runnable() {
                public void run() {
                    if(QBPeerChannel.this.isPeerConnectionValid()) {
                        if(QBPeerChannel.this.localSdp != null && QBPeerChannel.this.localSdp.type.equals(Type.OFFER)) {
                            if(QBPeerChannel.this.peerConnection.getRemoteDescription() == null) {
                                SDPObserver.this.log("Local SDP set successfully");
                                QBPeerChannel.this.peerChannelTimers.startDialingTimer();
                            } else {
                                SDPObserver.this.log("Remote SDP set successfully");
                                SDPObserver.this.drainIceCandidates();
                            }
                        } else if(QBPeerChannel.this.peerConnection.getLocalDescription() != null) {
                            SDPObserver.this.log("Local SDP set successfully");
                            QBPeerChannel.this.pcEventsCallback.onSessionDescriptionSend(QBPeerChannel.this, QBPeerChannel.this.localSdp);
                            SDPObserver.this.drainIceCandidates();
                        } else {
                            SDPObserver.this.log("Remote SDP set successfully");
                            QBPeerChannel.this.createAnswer();
                        }

                    }
                }
            });
        }

        public void onCreateFailure(String s) {
            this.log("onCreateFailure: " + s);
        }

        public void onSetFailure(String s) {
            this.log("onSetFailure: " + s);
        }

        private void log(String onCreateSuccess) {
            QBPeerChannel.LOGGER.d(this.TAG_SDPOBSERVER, onCreateSuccess);
        }

        private void drainIceCandidates() {
            if(QBPeerChannel.this.iceCandidates != null) {
                this.log("Add " + QBPeerChannel.this.iceCandidates.size() + " remote candidates");
                if(QBPeerChannel.this.iceCandidates.size() > 0) {
                    QBPeerChannel.this.pcEventsCallback.onIceCandidatesSend(QBPeerChannel.this, QBPeerChannel.this.iceCandidates);
                }

                QBPeerChannel.this.iceCandidates = null;
            }

        }
    }
}
