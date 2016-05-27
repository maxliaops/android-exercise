package com.quickblox.videochat.webrtc.exception;


public class QBRTCException extends Exception {

   public QBRTCException(QBRTCException.QBRTCExceptionType exceptionType) {
      super(exceptionType.getCause());
   }

   public static class QBRTCExceptionsCause {

      public static final String WRONG_OPPONENT_COUNT = "Wrong opponent count";
      public static final String CONNECTION_PROBLEM_CHECK_CONNECTION_OR_RELOGIN = "Connection problem with ChatService. Check connection or repeat login in chat";
      public static final String CAMS_ABSENT = "No device for video input was found";
      public static final String CAMERA_DENIED = "Camera permission wasn\'t granted for application!";
      public static final String CHANNEL_IS_NULL = "Channel is null";
      public static final String CHANNEL_IS_NOT_NEW = "Channels isn\'t new";
      public static final String MISSING_CONTEXT = "Context can\'t be null";
      public static final String CLIENT_WAS_NOT_PREPARED_TO_PROCESS_CALLS = "Method prepareToProcessCalls(Context context) was not called on QBRTCClient instance";
      public static final String MISSING_CALLER_ID = "Caller Id was not set properly in signaling message";
      public static final String MISSING_CONFERENCE_TYPE = "Conference type was not set properly in signaling message";
      public static final String MISSING_OPPONENTS = "Field \'Opponents\' was not set properly in signaling message";
      public static final String MISSING_SESSION_DESCRIPTION = "Session description was not set properly in signaling message";
      public static final String MISSING_ICE_CANDIDATES = "Field \'Ice candidates\' was not set properly in signaling message";


   }

   public static enum QBRTCExceptionType {

      VIDEO_CAPTURE_EXCEPTION("VIDEO_CAPTURE_EXCEPTION", 0, "No device for video input was found");
      private final String cause;
      // $FF: synthetic field
      private static final QBRTCException.QBRTCExceptionType[] $VALUES = new QBRTCException.QBRTCExceptionType[]{VIDEO_CAPTURE_EXCEPTION};


      private QBRTCExceptionType(String var1, int var2, String qbrtcExeptionsCause) {
         this.cause = qbrtcExeptionsCause;
      }

      public String getCause() {
         return this.cause;
      }

   }
}
