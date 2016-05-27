package com.quickblox.videochat.webrtc;

import java.util.HashMap;
import java.util.Map;

public class QBSignalingSpec {

   public static final String WEB_RTC_VIDEOCHAT = "WebRTCVideoChat";



   public static enum QBReason {

      MANUALLY("MANUALLY", 0, "K_STOP_VIDEOCHAT_CALL_STATUS_MANUALLY"),
      BAD_CONNECTION("BAD_CONNECTION", 1, "K_STOP_VIDEO_CHAT_CALL_STATUS_BAD_CONNECTION"),
      CANCEL("CANCEL", 2, "K_STOP_VIDEO_CHAT_CALL_STATUS_CANCEL"),
      OPONENT_DID_NOT_ANSWER("OPONENT_DID_NOT_ANSWER", 3, "K_STOP_VIDEO_CHAT_CALL_STATUS_OPPONENT_DID_NOT_ANSWER");
      private final String value;
      // $FF: synthetic field
      private static final QBSignalingSpec.QBReason[] $VALUES = new QBSignalingSpec.QBReason[]{MANUALLY, BAD_CONNECTION, CANCEL, OPONENT_DID_NOT_ANSWER};


      private QBReason(String var1, int var2, String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }

   }

   public static enum QBCandidate {

      SDP_MLINE_INDEX("SDP_MLINE_INDEX", 0, "sdpMLineIndex"),
      SDP_MID("SDP_MID", 1, "sdpMid"),
      CANDIDATE_DESC("CANDIDATE_DESC", 2, "candidate");
      private final String value;
      // $FF: synthetic field
      private static final QBSignalingSpec.QBCandidate[] $VALUES = new QBSignalingSpec.QBCandidate[]{SDP_MLINE_INDEX, SDP_MID, CANDIDATE_DESC};


      private QBCandidate(String var1, int var2, String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }

   }

   public static enum QBSignalField {

      MODULE_IDENTIFIER("MODULE_IDENTIFIER", 0, "moduleIdentifier"),
      MESSAGE_TYPE("MESSAGE_TYPE", 1, "type"),
      FROM("FROM", 2, "from"),
      TO("TO", 3, "to"),
      SIGNALING_TYPE("SIGNALING_TYPE", 4, "signalType"),
      SESSION_ID("SESSION_ID", 5, "sessionID"),
      CALL_TYPE("CALL_TYPE", 6, "callType"),
      SDP("SDP", 7, "sdp"),
      SDP_TYPE("SDP_TYPE", 8, "sdpType"),
      PLATFORM("PLATFORM", 9, "platform"),
      DEVICE_ORIENTATION("DEVICE_ORIENTATION", 10, "device_orientation"),
      STATUS("STATUS", 11, "status"),
      CANDIDATE("CANDIDATE", 12, "iceCandidate"),
      CANDIDATES("CANDIDATES", 13, "iceCandidates"),
      OPPONENTS("OPPONENTS", 14, "opponentsIDs"),
      OPPONENT("OPPONENT", 15, "opponentID"),
      CALLER("CALLER", 16, "callerID"),
      USER_INFO("USER_INFO", 17, "userInfo"),
      VERSION_SDK("VERSION_SDK", 18, "version_sdk");
      private final String value;
      // $FF: synthetic field
      private static final QBSignalingSpec.QBSignalField[] $VALUES = new QBSignalingSpec.QBSignalField[]{MODULE_IDENTIFIER, MESSAGE_TYPE, FROM, TO, SIGNALING_TYPE, SESSION_ID, CALL_TYPE, SDP, SDP_TYPE, PLATFORM, DEVICE_ORIENTATION, STATUS, CANDIDATE, CANDIDATES, OPPONENTS, OPPONENT, CALLER, USER_INFO, VERSION_SDK};


      private QBSignalField(String var1, int var2, String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }

   }

   public static enum QBSignalCMD {

      CALL("CALL", 0, "call"),
      ACCEPT_CALL("ACCEPT_CALL", 1, "accept"),
      REJECT_CALL("REJECT_CALL", 2, "reject"),
      HANG_UP("HANG_UP", 3, "hangUp"),
      CANDITATES("CANDITATES", 4, "iceCandidates"),
      CANDITATE("CANDITATE", 5, "iceCandidate"),
      ADD_USER("ADD_USER", 6, "addUser"),
      REMOVE_USER("REMOVE_USER", 7, "removeUser"),
      UPDATE("UPDATE", 8, "update");
      private static final Map commands = new HashMap();
      private final String value;
      // $FF: synthetic field
      private static final QBSignalingSpec.QBSignalCMD[] $VALUES = new QBSignalingSpec.QBSignalCMD[]{CALL, ACCEPT_CALL, REJECT_CALL, HANG_UP, CANDITATES, CANDITATE, ADD_USER, REMOVE_USER, UPDATE};


      private QBSignalCMD(String var1, int var2, String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }

      public static QBSignalingSpec.QBSignalCMD getTypeByRawValue(String signalingRawType) {
         QBSignalingSpec.QBSignalCMD result = null;
         if(signalingRawType.equals(CALL.getValue())) {
            result = CALL;
         } else if(signalingRawType.equals(ACCEPT_CALL.getValue())) {
            result = ACCEPT_CALL;
         } else if(signalingRawType.equals(REJECT_CALL.getValue())) {
            result = REJECT_CALL;
         } else if(signalingRawType.equals(HANG_UP.getValue())) {
            result = HANG_UP;
         } else if(signalingRawType.equals(CANDITATES.getValue())) {
            result = CANDITATES;
         } else if(signalingRawType.equals(CANDITATE.getValue())) {
            result = CANDITATE;
         } else if(signalingRawType.equals(ADD_USER.getValue())) {
            result = ADD_USER;
         } else if(signalingRawType.equals(REMOVE_USER.getValue())) {
            result = REMOVE_USER;
         } else if(signalingRawType.equals(UPDATE.getValue())) {
            result = UPDATE;
         }

         return result;
      }

   }
}
