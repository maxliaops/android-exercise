package com.quickblox.videochat.webrtc;

import android.content.Context;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.videochat.webrtc.LooperExecutor;
import com.quickblox.videochat.webrtc.PeerFactoryManager;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.QBRTCUtils;
import com.quickblox.videochat.webrtc.RTCSignallingMessageProcessor;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacksImpl;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionConnectionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.RTCSignallingMessageProcessorCallback;
import com.quickblox.videochat.webrtc.util.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturerAndroid;

public final class QBRTCClient implements RTCSignallingMessageProcessorCallback {

   public static final String TAG = "RTCClient";
   private static volatile QBRTCClient INSTANCE;
   private static final Logger LOGGER = Logger.getInstance("RTCClient");
   private static final String CLASS_TAG = "";
   private Context context;
   private LooperExecutor executor;
   private PeerFactoryManager peerFactoryManager;
   private final Map sessions;
   private RTCSignallingMessageProcessor xmppRTCMessageProcessor;
   private Set videoTracksCallbacksList;
   private Set sessionConnectionCallbacksList;
   private Set sessionCallbacksList;
   private VideoCapturerAndroid.CameraErrorHandler cameraErrorHendler;
   List signallingList = new ArrayList();


   private QBRTCClient(Context context) {
      if(context == null) {
         throw new IllegalArgumentException("Context can\'t be null");
      } else {
         this.context = context.getApplicationContext();
         this.sessions = Collections.synchronizedMap(new HashMap());
         this.sessionConnectionCallbacksList = new CopyOnWriteArraySet();
         this.videoTracksCallbacksList = new CopyOnWriteArraySet();
         this.sessionCallbacksList = new CopyOnWriteArraySet();
         this.xmppRTCMessageProcessor = new RTCSignallingMessageProcessor();
         this.xmppRTCMessageProcessor.addSignalingListener(this);
         this.sessionCallbacksList.add(new QBRTCClient.SessionClosedListener());
         this.initTaskExecutor();
      }
   }

   private void initTaskExecutor() {
      LOGGER.d("", "init Task Executor");
      this.executor = new LooperExecutor(this.getClass());
      this.executor.requestStart();
   }

   public static QBRTCClient getInstance(Context context) {
      if(INSTANCE == null) {
         Class var1 = QBRTCClient.class;
         synchronized(QBRTCClient.class) {
            if(INSTANCE == null) {
               LOGGER.d("", "Create QBRTCClient INSTANCE");
               INSTANCE = new QBRTCClient(context);
            }
         }
      }

      return INSTANCE;
   }

   @Deprecated
   public static void init(Context context) {
      getInstance(context);
   }

   @Deprecated
   public static boolean isInitiated() {
      return INSTANCE != null;
   }

   public QBRTCSession createNewSessionWithOpponents(List userIds, QBRTCTypes.QBConferenceType qbConferenceType) throws IllegalStateException {
      LOGGER.d("", "Call createNewSessionWithOpponents" + userIds.toString() + "conference type=" + qbConferenceType);
      if(userIds.size() > 0) {
         QBRTCSessionDescription sessionDescription = new QBRTCSessionDescription(QBRTCUtils.getCurrentChatUser(), userIds, qbConferenceType);
         QBRTCSession session = new QBRTCSession(this, sessionDescription, this.sessionCallbacksList, this.cameraErrorHendler, this.xmppRTCMessageProcessor);
         this.sessions.put(session.getSessionID(), session);
         return session;
      } else {
         throw new IllegalStateException("Wrong opponent count");
      }
   }

   private QBRTCSession createSessionWithDescription(QBRTCSessionDescription sessionDescription) {
      LOGGER.d("", "createSessionWithDescription" + sessionDescription);
      QBRTCSession session = new QBRTCSession(this, sessionDescription, this.sessionCallbacksList, this.cameraErrorHendler, this.xmppRTCMessageProcessor);
      this.sessions.put(sessionDescription.getSessionId(), session);
      return session;
   }

   public void addSignaling(QBWebRTCSignaling signaling) {
      if(signaling != null) {
         LOGGER.d("", "New signalling was added for participant" + signaling.getParticipant());
         this.signallingList.add(signaling);
         signaling.addMessageListener(this.xmppRTCMessageProcessor);
      } else {
         LOGGER.e("", "Try to add null QBWebRTCSignaling");
      }

   }

   private Map getCustomParameters() {
      return new HashMap();
   }

   public Context getContext() {
      return this.context;
   }

   public void setCameraErrorHendler(VideoCapturerAndroid.CameraErrorHandler cameraErrorHendler) {
      this.cameraErrorHendler = cameraErrorHendler;
   }

   public QBRTCSession getSession(String sessionId) {
      return (QBRTCSession)this.sessions.get(sessionId);
   }

   public void prepareToProcessCalls() {
      this.xmppRTCMessageProcessor.addSignalingListener(this);
   }

   @Deprecated
   public void prepareToProcessCalls(Context context) {
      this.prepareToProcessCalls();
   }

   public void addSessionCallbacksListener(QBRTCClientSessionCallbacks callback) {
      if(callback != null) {
         this.sessionCallbacksList.add(callback);
         LOGGER.d("", " Added session CALLBACK listener" + callback);
      } else {
         LOGGER.e("", "Try to add null SessionCallbacksListener");
      }

   }

   public void removeSessionsCallbacksListener(QBRTCClientSessionCallbacks callback) {
      this.sessionCallbacksList.remove(callback);
      LOGGER.d("", " REMOVE SessionsCallbacksListene " + callback);
   }

   @Deprecated
   public void addVideoTrackCallbacksListener(QBRTCClientVideoTracksCallbacks callback) {
      if(callback != null) {
         this.videoTracksCallbacksList.add(callback);
         LOGGER.d("", " ADD VideoTrackCallbacksListener " + callback);
      } else {
         LOGGER.e("", "Try to add null VideoTrackCallbacksListener");
      }

   }

   @Deprecated
   public void removeVideoTrackCallbacksListener(QBRTCClientVideoTracksCallbacks callback) {
      this.videoTracksCallbacksList.remove(callback);
      LOGGER.d("", " REMOVE VideoTrackCallbacksListener " + callback);
   }

   @Deprecated
   public void addConnectionCallbacksListener(QBRTCSessionConnectionCallbacks callback) {
      if(callback != null) {
         this.sessionConnectionCallbacksList.add(callback);
      }

   }

   @Deprecated
   public void removeConnectionCallbacksListener(QBRTCSessionConnectionCallbacks callback) {
      this.sessionConnectionCallbacksList.remove(callback);
   }

   public void addRTCSignallingMessageProcessorCallbackListener(RTCSignallingMessageProcessorCallback callback) {
      if(callback != null) {
         this.xmppRTCMessageProcessor.addSignalingListener(callback);
         LOGGER.d("", " ADD RTCSignallingMessageProcessorCallback " + callback);
      } else {
         LOGGER.d("", "Try to add null ConnectionCallbacksListener");
      }

   }

   public void removeRTCSignallingMessageProcessorCallbackListener(RTCSignallingMessageProcessorCallback callback) {
      this.xmppRTCMessageProcessor.removeSignalingListener(callback);
      LOGGER.d("", " REMOVE RTCSignallingMessageProcessorCallback " + callback);
   }

   public void onReceiveCallFromUser(final Integer from, final QBRTCSessionDescription sessionDescription, final SessionDescription sdp) {
      LOGGER.d("", "Call offer message received from" + from);
      this.executor.execute(new Runnable() {
         public void run() {
            QBRTCSession session = null;
            session = QBRTCClient.this.getOrCreateSession(sessionDescription);
            QBRTCClient.LOGGER.d("", "Session " + session.getSessionID() + " exists");
            session.procRemoteOfferSDP(sdp, from, QBRTCClient.this.sessionCallbacksList);
         }
      });
   }

   private QBRTCSession getOrCreateSession(QBRTCSessionDescription sessionDescription) {
      Map var2 = this.sessions;
      synchronized(this.sessions) {
         QBRTCSession session = (QBRTCSession)this.sessions.get(sessionDescription.getSessionId());
         if(session == null) {
            session = this.createSessionWithDescription(sessionDescription);
         }

         return session;
      }
   }

   private boolean isClientFree() {
      Iterator i$ = this.sessions.keySet().iterator();

      QBRTCSession.QBRTCSessionState state;
      do {
         if(!i$.hasNext()) {
            return true;
         }

         String sessionID = (String)i$.next();
         QBRTCSession session = (QBRTCSession)this.sessions.get(sessionID);
         state = session.getState();
      } while(state == null || state.ordinal() >= QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_REJECTED.ordinal());

      return false;
   }

   public void onReceiveAcceptFromUser(final Integer from, final QBRTCSessionDescription sessionDescription, final SessionDescription sdp) {
      LOGGER.d("", "onReceiveAcceptFromUser " + from);
      this.executor.execute(new Runnable() {
         public void run() {
            QBRTCSession session = QBRTCClient.this.getOrCreateSession(sessionDescription);
            session.procRemoteAnswerSDP(sdp, from, sessionDescription.getUserInfo());
         }
      });
   }

   public void onReceiveRejectFromUser(final Integer opponentID, final QBRTCSessionDescription sessionDescription) {
      LOGGER.d("", "Rejected call from " + opponentID);
      this.executor.execute(new Runnable() {
         public void run() {
            QBRTCSession session = QBRTCClient.this.getOrCreateSession(sessionDescription);
            session.procRejectCallFromOpponent(opponentID, sessionDescription.getUserInfo());
         }
      });
   }

   public void onReceiveIceCandidatesFromUser(final List iceCandidates, final Integer from, final QBRTCSessionDescription sessionDescription) {
      LOGGER.d("", "onReceiveIceCandidatesFromUser " + from + ", session=" + sessionDescription.getSessionId());
      this.executor.execute(new Runnable() {
         public void run() {
            QBRTCSession session = (QBRTCSession)QBRTCClient.this.sessions.get(sessionDescription.getSessionId());
            if(session != null) {
               if(session.getState().ordinal() < QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_REJECTED.ordinal()) {
                  session.procSetIceCandidates(iceCandidates, from);
               }

            }
         }
      });
   }

   public void onReceiveUserHungUpCall(final Integer opponentID, final QBRTCSessionDescription sessionDescription) {
      LOGGER.d("", "onReceiveUserHungUpCall from " + opponentID + ", sesionID=" + sessionDescription.getSessionId());
      this.executor.execute(new Runnable() {
         public void run() {
            QBRTCSession session = QBRTCClient.this.getOrCreateSession(sessionDescription);
            session.procHangUpOpponent(opponentID, sessionDescription.getUserInfo());
         }
      });
   }

   public void onAddUserNeed(Integer userToAdd, QBRTCSessionDescription sessionDescription) {}

   public void destroy() {
      LOGGER.d("", "destroy");
      this.sessionCallbacksList.clear();
      this.videoTracksCallbacksList.clear();
      Iterator i$ = this.signallingList.iterator();

      while(i$.hasNext()) {
         QBWebRTCSignaling signaling = (QBWebRTCSignaling)i$.next();
         signaling.removeMessageListener(this.xmppRTCMessageProcessor);
         signaling.close();
      }

      this.xmppRTCMessageProcessor.removeSignalingListener(this);
      this.stopExecutor();
      INSTANCE = null;
   }

   public synchronized boolean isLastActiveSession() {
      Map var1 = this.sessions;
      synchronized(this.sessions) {
         Iterator i$ = this.sessions.keySet().iterator();

         String key;
         do {
            if(!i$.hasNext()) {
               return true;
            }

            key = (String)i$.next();
         } while(((QBRTCSession)this.sessions.get(key)).getState().ordinal() >= QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_REJECTED.ordinal());

         return false;
      }
   }

   LooperExecutor getExecutor() {
      return this.executor;
   }

   synchronized PeerFactoryManager getPeerFactoryManager() {
      if(this.peerFactoryManager == null) {
         this.peerFactoryManager = new PeerFactoryManager(this.context);
      }

      return this.peerFactoryManager;
   }

   private void stopExecutor() {
      if(this.executor != null) {
         if(!this.executor.isStopped()) {
            LOGGER.d("", "Stop executor");
            this.executor.requestStop();
         }

         this.executor = null;
      }

   }


   public class SessionClosedListener extends QBRTCClientSessionCallbacksImpl {

      public void onSessionClosed(QBRTCSession session) {
         if(QBRTCClient.this.isLastActiveSession()) {
            if(QBRTCClient.this.peerFactoryManager != null) {
               QBRTCClient.this.peerFactoryManager.dispose();
               QBRTCClient.this.peerFactoryManager = null;
            }

            QBRTCClient.LOGGER.d("", "onSessionClosed");
         }

      }
   }
}
