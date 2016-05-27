package com.quickblox.videochat.webrtc;


public final class QBRTCMediaConfig {

   private static int videoWidth = 0;
   private static int videoHeight = 0;
   private static int videoFps = 0;
   private static int videoStartBitrate = 0;
   private static boolean videoHWAcceleration = false;
   private static int audioStartBitrate = 0;
   private static QBRTCMediaConfig.AudioCodec audioCodec = QBRTCMediaConfig.AudioCodec.ISAC;
   private static QBRTCMediaConfig.VideoCodec videoCodec = null;


   public static void setAudioCodec(QBRTCMediaConfig.AudioCodec audioCodec) {
      audioCodec = audioCodec;
   }

   public static void setVideoCodec(QBRTCMediaConfig.VideoCodec videoCodec) {
      videoCodec = videoCodec;
   }

   public static QBRTCMediaConfig.AudioCodec getAudioCodec() {
      return audioCodec;
   }

   public static QBRTCMediaConfig.VideoCodec getVideoCodec() {
      return videoCodec;
   }

   public static int getVideoWidth() {
      return videoWidth;
   }

   public static void setVideoWidth(int videoWidth) {
      videoWidth = videoWidth;
   }

   public static int getVideoHeight() {
      return videoHeight;
   }

   public static void setVideoHeight(int videoHeight) {
      videoHeight = videoHeight;
   }

   public static int getVideoFps() {
      return videoFps;
   }

   public static void setVideoFps(int videoFps) {
      videoFps = videoFps;
   }

   public static int getVideoStartBitrate() {
      return videoStartBitrate;
   }

   public static void setVideoStartBitrate(int videoStartBitrate) {
      videoStartBitrate = videoStartBitrate;
   }

   public static int getAudioStartBitrate() {
      return audioStartBitrate;
   }

   public static void setAudioStartBitrate(int audioStartBitrate) {
      audioStartBitrate = audioStartBitrate;
   }

   public static boolean isVideoHWAcceleration() {
      return videoHWAcceleration;
   }

   public static void setVideoHWAcceleration(boolean videoHWAcceleration) {
      videoHWAcceleration = videoHWAcceleration;
   }


   public static enum AudioCodec {

      OPUS("OPUS", 0, "opus"),
      ISAC("ISAC", 1, "ISAC");
      private String description;
      // $FF: synthetic field
      private static final QBRTCMediaConfig.AudioCodec[] $VALUES = new QBRTCMediaConfig.AudioCodec[]{OPUS, ISAC};


      private AudioCodec(String var1, int var2, String description) {
         this.description = description;
      }

      public String getDescription() {
         return this.description;
      }

   }

   public static enum VideoQuality {

      HD_VIDEO("HD_VIDEO", 0, 1280, 720),
      VGA_VIDEO("VGA_VIDEO", 1, 640, 480),
      QBGA_VIDEO("QBGA_VIDEO", 2, 320, 240);
      public final int width;
      public final int height;
      // $FF: synthetic field
      private static final QBRTCMediaConfig.VideoQuality[] $VALUES = new QBRTCMediaConfig.VideoQuality[]{HD_VIDEO, VGA_VIDEO, QBGA_VIDEO};


      private VideoQuality(String var1, int var2, int width, int height) {
         this.width = width;
         this.height = height;
      }

   }

   public static enum VideoCodec {

      VP8("VP8", 0, "VP8"),
      VP9("VP9", 1, "VP9"),
      H264("H264", 2, "H264");
      private final String description;
      // $FF: synthetic field
      private static final QBRTCMediaConfig.VideoCodec[] $VALUES = new QBRTCMediaConfig.VideoCodec[]{VP8, VP9, H264};


      private VideoCodec(String var1, int var2, String description) {
         this.description = description;
      }

      public String getDescription() {
         return this.description;
      }

   }
}
