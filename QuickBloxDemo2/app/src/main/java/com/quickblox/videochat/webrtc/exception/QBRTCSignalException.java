package com.quickblox.videochat.webrtc.exception;


public class QBRTCSignalException extends Exception {

   public QBRTCSignalException(Throwable wrappedThrowable) {
      super(wrappedThrowable);
   }

   public QBRTCSignalException(String message) {
      super(message);
   }

   public QBRTCSignalException(String message, Throwable wrappedThrowable) {
      super(message, wrappedThrowable);
   }
}
