package com.quickblox.videochat.webrtc;

import java.util.List;

public final class QBRTCConfig {

   private static long dialingTimeInterval = 5L;
   private static long answerTimeInterval = 60L;
   private static Integer maxOpponentsCount = Integer.valueOf(10);
   private static Integer disconnectTime = Integer.valueOf(10);
   private static List iceServerList = null;
   private static boolean debugEnabled = true;


   public static void setDialingTimeInterval(long dialingTimeInterval) {
      dialingTimeInterval = dialingTimeInterval;
   }

   public static void setAnswerTimeInterval(long answerTimeInterval) {
      answerTimeInterval = answerTimeInterval;
   }

   public static void setMaxOpponentsCount(Integer maxOpponentsCount) {
      maxOpponentsCount = maxOpponentsCount;
   }

   public static void setDisconnectTime(Integer disconnectTime) {
      disconnectTime = disconnectTime;
   }

   public static void setIceServerList(List iceServerList) {
      iceServerList = iceServerList;
   }

   public static long getDialingTimeInterval() {
      return dialingTimeInterval;
   }

   public static long getAnswerTimeInterval() {
      return answerTimeInterval;
   }

   public static Integer getMaxOpponentsCount() {
      return maxOpponentsCount;
   }

   public static Integer getDisconnectTime() {
      return disconnectTime;
   }

   public static List getIceServerList() {
      return iceServerList;
   }

   public static void setDebugEnabled(boolean debugEnabled) {
      debugEnabled = debugEnabled;
   }

   public static boolean isDebugEnabled() {
      return debugEnabled;
   }

}
