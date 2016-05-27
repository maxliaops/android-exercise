package com.quickblox.videochat.webrtc;

import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBSettings;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBSignalingSpec;
import com.quickblox.videochat.webrtc.util.Logger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.webrtc.SessionDescription;

class QBSignalChannel {

   private static final String ANDROID = "android";
   private static final String CLASS_TAG = QBSignalChannel.class.getSimpleName();
   private static final Logger LOGGER = Logger.getInstance("RTCClient");
   private Map messageParameters = new HashMap();
   private QBRTCSession session;


   public QBSignalChannel(QBRTCSession session) {
      this.session = session;
   }

   public QBChatMessage sendCallRequestMessage(SessionDescription sdp, Integer opponentID, Map customParameters) {
      LOGGER.d(CLASS_TAG, "start assemble CallRequestMessage");
      this.messageParameters.clear();
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SIGNALING_TYPE.getValue(), QBSignalingSpec.QBSignalCMD.CALL.getValue());
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SDP.getValue(), sdp.description);
      return this.xmppMessageWithParameters(opponentID, customParameters);
   }

   public QBChatMessage sendAcceptCallMessage(SessionDescription sdp, Integer opponentID, Map customParameters) {
      LOGGER.d(CLASS_TAG, "start assemble AcceptCallMessage");
      this.messageParameters.clear();
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SIGNALING_TYPE.getValue(), QBSignalingSpec.QBSignalCMD.ACCEPT_CALL.getValue());
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SDP.getValue(), sdp.description);
      return this.xmppMessageWithParameters(opponentID, customParameters);
   }

   public QBChatMessage sendRejectCallToOpponent(Integer opponentID, Map customParameters) {
      LOGGER.d(CLASS_TAG, "start assemble RejectCallToOpponent");
      this.messageParameters.clear();
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SIGNALING_TYPE.getValue(), QBSignalingSpec.QBSignalCMD.REJECT_CALL.getValue());
      return this.xmppMessageWithParameters(opponentID, customParameters);
   }

   public QBChatMessage sendHandUpCallWithStatus(Integer opponentID, Map customParameters) {
      LOGGER.d(CLASS_TAG, "start assemble HandUpCallWithStatus");
      this.messageParameters.clear();
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SIGNALING_TYPE.getValue(), QBSignalingSpec.QBSignalCMD.HANG_UP.getValue());
      return this.xmppMessageWithParameters(opponentID, customParameters);
   }

   public QBChatMessage sendIceCandidates(Integer opponentID, List iceCandidates, Map customParameters) {
      LOGGER.d(CLASS_TAG, "start assemble IceCandidates");
      this.messageParameters.clear();
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SIGNALING_TYPE.getValue(), QBSignalingSpec.QBSignalCMD.CANDITATES.getValue());
      this.messageParameters.put(QBSignalingSpec.QBSignalField.CANDIDATES.getValue(), iceCandidates);
      return this.xmppMessageWithParameters(opponentID, customParameters);
   }

   public QBChatMessage updateParams(Integer opponentID, Map customParameters) {
      LOGGER.d(CLASS_TAG, "start assemble AddUser");
      this.messageParameters.clear();
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SIGNALING_TYPE.getValue(), QBSignalingSpec.QBSignalCMD.UPDATE.getValue());
      return this.xmppMessageWithParameters(opponentID, customParameters);
   }

   public QBChatMessage sendAddUser(Integer opponentID, Map customParameters) {
      LOGGER.d(CLASS_TAG, "start assemble AddUser");
      this.messageParameters.clear();
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SIGNALING_TYPE.getValue(), QBSignalingSpec.QBSignalCMD.ADD_USER.getValue());
      return this.xmppMessageWithParameters(opponentID, customParameters);
   }

   public QBChatMessage sendRemoveUser(Integer opponentID, Map customParameters) {
      LOGGER.d(CLASS_TAG, "start assemble RemoveUser");
      this.messageParameters.clear();
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SIGNALING_TYPE.getValue(), QBSignalingSpec.QBSignalCMD.REMOVE_USER.getValue());
      return this.xmppMessageWithParameters(opponentID, customParameters);
   }

   public QBChatMessage videoChatMessageWithOpponentID(Integer opponentID) {
      String id = UUID.randomUUID().toString();
      String to = opponentID.toString();
      Message smackMessage = new Message();
      smackMessage.setStanzaId(id);
      smackMessage.setType(Type.headline);
      smackMessage.setTo(to);
      QBChatMessage message = new QBChatMessage(smackMessage);
      if(this.messageParameters != null) {
         Iterator i$ = this.messageParameters.keySet().iterator();

         while(i$.hasNext()) {
            String key = (String)i$.next();
            if(this.messageParameters.get(key) instanceof String) {
               message.setProperty(key, (String)this.messageParameters.get(key));
            } else {
               message.setComplexProperty(key, this.messageParameters.get(key));
            }
         }
      }

      return message;
   }

   public QBChatMessage xmppMessageWithParameters(Integer opponentID, Map customParameters) {
      this.messageParameters.put(QBSignalingSpec.QBSignalField.SESSION_ID.getValue(), this.session.getSessionID());
      this.messageParameters.put(QBSignalingSpec.QBSignalField.MODULE_IDENTIFIER.getValue(), "WebRTCVideoChat");
      this.messageParameters.put(QBSignalingSpec.QBSignalField.CALL_TYPE.getValue(), String.valueOf(this.session.getConferenceType().getValue()));
      this.messageParameters.put(QBSignalingSpec.QBSignalField.CALLER.getValue(), this.session.getCallerID().toString());
      this.messageParameters.put(QBSignalingSpec.QBSignalField.OPPONENTS.getValue(), this.session.getOpponents());
      this.messageParameters.put(QBSignalingSpec.QBSignalField.PLATFORM.getValue(), "android");
      this.messageParameters.put(QBSignalingSpec.QBSignalField.VERSION_SDK.getValue(), QBSettings.getInstance().getVersionName());
      LOGGER.d(CLASS_TAG, "assembled message [session: " + this.session.getSessionID() + "]");
      if(customParameters != null && customParameters.size() != 0) {
         this.messageParameters.put(QBSignalingSpec.QBSignalField.USER_INFO.getValue(), customParameters);
         QBChatMessage message = this.videoChatMessageWithOpponentID(opponentID);
         return message;
      } else {
         return this.videoChatMessageWithOpponentID(opponentID);
      }
   }

}
