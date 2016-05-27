package com.quickblox.videochat.webrtc;

import com.quickblox.videochat.webrtc.QBRTCTypes;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class QBRTCSessionDescription implements Serializable {

   private static final long serialVersionUID = 0L;
   private String sessionID;
   private Integer callerID;
   private List opponents;
   private QBRTCTypes.QBConferenceType conferenceType;
   private Map userInfo;


   public QBRTCSessionDescription(String sessionID, Integer callerID, List opponents, QBRTCTypes.QBConferenceType conferenceType) {
      this.sessionID = sessionID;
      this.callerID = callerID;
      this.opponents = opponents;
      this.conferenceType = conferenceType;
      this.userInfo = new HashMap();
   }

   public QBRTCSessionDescription(Integer callerID, List opponents, QBRTCTypes.QBConferenceType conferenceType) {
      this(UUID.randomUUID().toString(), callerID, opponents, conferenceType);
   }

   public String getSessionId() {
      return this.sessionID;
   }

   public void setSessionID(String sessionID) {
      this.sessionID = sessionID;
   }

   public Integer getCallerID() {
      return this.callerID;
   }

   public void setCallerID(Integer callerID) {
      this.callerID = callerID;
   }

   public List getOpponents() {
      return this.opponents;
   }

   public void setOpponents(List opponents) {
      this.opponents = opponents;
   }

   public QBRTCTypes.QBConferenceType getConferenceType() {
      return this.conferenceType;
   }

   public void setConferenceType(QBRTCTypes.QBConferenceType conferenceType) {
      this.conferenceType = conferenceType;
   }

   public Map getUserInfo() {
      return this.userInfo;
   }

   public void setUserInfo(Map userInfo) {
      this.userInfo = userInfo;
   }

   public int hashCode() {
      int result = this.sessionID != null?this.sessionID.hashCode():0;
      result = 31 * result + (this.callerID != null?this.callerID.hashCode():0);
      result = 31 * result + (this.opponents != null?this.opponents.hashCode():0);
      result = 31 * result + (this.conferenceType != null?this.conferenceType.hashCode():0);
      result = 31 * result + (this.userInfo != null?this.userInfo.hashCode():0);
      return result;
   }

   public String toString() {
      return "QBRTCSessionDescription{sessionID=\'" + this.sessionID + '\'' + ", callerID=\'" + this.callerID + '\'' + ", opponents=" + this.opponents + ", conferenceType=" + this.conferenceType + ", userInfo=" + this.userInfo + '}';
   }
}
