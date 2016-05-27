package com.quickblox.videochat.webrtc;

import com.quickblox.videochat.webrtc.QBRTCMediaConfig;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.util.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.webrtc.MediaConstraints;
import org.webrtc.SessionDescription;

public final class RTCMediaUtils {

   private static final String CLASS_TAG = RTCMediaUtils.class.getSimpleName();
   private static final Logger LOGGER = Logger.getInstance("RTCClient");
   private static final String MAX_VIDEO_WIDTH_CONSTRAINT = "maxWidth";
   private static final String MIN_VIDEO_WIDTH_CONSTRAINT = "minWidth";
   private static final String MAX_VIDEO_HEIGHT_CONSTRAINT = "maxHeight";
   private static final String MIN_VIDEO_HEIGHT_CONSTRAINT = "minHeight";
   private static final String MAX_VIDEO_FPS_CONSTRAINT = "maxFrameRate";
   private static final String MIN_VIDEO_FPS_CONSTRAINT = "minFrameRate";
   private static final int MAX_VIDEO_WIDTH = 1280;
   private static final int MAX_VIDEO_HEIGHT = 1280;
   private static final String VIDEO_CODEC_PARAM_START_BITRATE = "x-google-start-bitrate";
   private static final String AUDIO_CODEC_PARAM_BITRATE = "maxaveragebitrate";
   private static final int MAX_VIDEO_FPS = 30;


   public static MediaConstraints createMediaConstraints(QBRTCMediaConfig sessionParameters, QBRTCTypes.QBConferenceType conferenceType) {
      MediaConstraints mediaConstraints = new MediaConstraints();
      int videoWidth = QBRTCMediaConfig.getVideoWidth();
      int videoHeight = QBRTCMediaConfig.getVideoHeight();
      if(QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(conferenceType)) {
         if(videoWidth > 0 && videoHeight > 0) {
            videoWidth = Math.min(videoWidth, 1280);
            videoHeight = Math.min(videoHeight, 1280);
            LOGGER.w(CLASS_TAG, "Set constraints for video:" + videoWidth + ":" + videoHeight);
            mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("minWidth", Integer.toString(videoWidth)));
            mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxWidth", Integer.toString(videoWidth)));
            mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("minHeight", Integer.toString(videoHeight)));
            mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxHeight", Integer.toString(videoHeight)));
         }

         int videoFps = QBRTCMediaConfig.getVideoFps();
         if(videoFps > 0) {
            videoFps = Math.min(videoFps, 30);
            LOGGER.w(CLASS_TAG, "Set constraints for fps:" + videoFps);
            mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("minFrameRate", Integer.toString(videoFps)));
            mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxFrameRate", Integer.toString(videoFps)));
         }
      }

      return mediaConstraints;
   }

   public static MediaConstraints createConferenceConstraints(QBRTCTypes.QBConferenceType conferenceType) {
      MediaConstraints sdpMediaConstraints = new MediaConstraints();
      sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
      if(QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(conferenceType)) {
         sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
      } else {
         sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"));
      }

      sdpMediaConstraints.optional.add(new MediaConstraints.KeyValuePair("RtpDataChannels", "true"));
      sdpMediaConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
      return sdpMediaConstraints;
   }

   private static String preferCodec(String sdpDescription, String codec, boolean isAudio) {
      String[] lines = sdpDescription.split("\r\n");
      int mLineIndex = -1;
      String codecRtpMap = null;
      String regex = "^a=rtpmap:(\\d+) " + codec + "(/\\d+)+[\r]?$";
      Pattern codecPattern = Pattern.compile(regex);
      String mediaDescription = "m=video ";
      if(isAudio) {
         mediaDescription = "m=audio ";
      }

      for(int origMLineParts = 0; origMLineParts < lines.length && (mLineIndex == -1 || codecRtpMap == null); ++origMLineParts) {
         if(lines[origMLineParts].startsWith(mediaDescription)) {
            mLineIndex = origMLineParts;
         } else {
            Matcher newSdpDescription = codecPattern.matcher(lines[origMLineParts]);
            if(newSdpDescription.matches()) {
               codecRtpMap = newSdpDescription.group(1);
            }
         }
      }

      if(mLineIndex == -1) {
         LOGGER.w(CLASS_TAG, "No " + mediaDescription + " line, so can\'t prefer " + codec);
         return sdpDescription;
      } else if(codecRtpMap == null) {
         LOGGER.w(CLASS_TAG, "No rtpmap for " + codec);
         return sdpDescription;
      } else {
         LOGGER.d(CLASS_TAG, "Found " + codec + " rtpmap " + codecRtpMap + ", prefer at " + lines[mLineIndex]);
         String[] var16 = lines[mLineIndex].split(" ");
         StringBuilder var18;
         if(var16.length > 3) {
            var18 = new StringBuilder();
            byte arr$ = 0;
            int var17 = arr$ + 1;
            var18.append(var16[arr$]).append(" ");
            var18.append(var16[var17++]).append(" ");
            var18.append(var16[var17++]).append(" ");
            var18.append(codecRtpMap);

            for(; var17 < var16.length; ++var17) {
               if(!var16[var17].equals(codecRtpMap)) {
                  var18.append(" ").append(var16[var17]);
               }
            }

            lines[mLineIndex] = var18.toString();
            LOGGER.d(CLASS_TAG, "Change media description: " + lines[mLineIndex]);
         } else {
            LOGGER.e(CLASS_TAG, "Wrong SDP media description format: " + lines[mLineIndex]);
         }

         var18 = new StringBuilder();
         String[] var19 = lines;
         int len$ = lines.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String line = var19[i$];
            var18.append(line).append("\r\n");
         }

         return var18.toString();
      }
   }

   private static String setStartBitrate(String codec, boolean isVideoCodec, String sdpDescription, int bitrateKbps) {
      String[] lines = sdpDescription.split("\r\n");
      int rtpmapLineIndex = -1;
      boolean sdpFormatUpdated = false;
      String codecRtpMap = null;
      String regex = "^a=rtpmap:(\\d+) " + codec + "(/\\d+)+[\r]?$";
      Pattern codecPattern = Pattern.compile(regex);

      int newSdpDescription;
      Matcher i;
      for(newSdpDescription = 0; newSdpDescription < lines.length; ++newSdpDescription) {
         i = codecPattern.matcher(lines[newSdpDescription]);
         if(i.matches()) {
            codecRtpMap = i.group(1);
            rtpmapLineIndex = newSdpDescription;
            break;
         }
      }

      if(codecRtpMap == null) {
         LOGGER.w(CLASS_TAG, "No rtpmap for " + codec + " codec");
         return sdpDescription;
      } else {
         LOGGER.d(CLASS_TAG, "Found " + codec + " rtpmap " + codecRtpMap + " at " + lines[rtpmapLineIndex]);
         regex = "^a=fmtp:" + codecRtpMap + " \\w+=\\d+.*[\r]?$";
         codecPattern = Pattern.compile(regex);

         for(newSdpDescription = 0; newSdpDescription < lines.length; ++newSdpDescription) {
            i = codecPattern.matcher(lines[newSdpDescription]);
            if(i.matches()) {
               LOGGER.d(CLASS_TAG, "Found " + codec + " " + lines[newSdpDescription]);
               if(isVideoCodec) {
                  lines[newSdpDescription] = lines[newSdpDescription] + "; x-google-start-bitrate=" + bitrateKbps;
               } else {
                  lines[newSdpDescription] = lines[newSdpDescription] + "; maxaveragebitrate=" + bitrateKbps * 1000;
               }

               LOGGER.d(CLASS_TAG, "Update remote SDP line: " + lines[newSdpDescription]);
               sdpFormatUpdated = true;
               break;
            }
         }

         StringBuilder var13 = new StringBuilder();

         for(int var14 = 0; var14 < lines.length; ++var14) {
            var13.append(lines[var14]).append("\r\n");
            if(!sdpFormatUpdated && var14 == rtpmapLineIndex) {
               String bitrateSet;
               if(isVideoCodec) {
                  bitrateSet = "a=fmtp:" + codecRtpMap + " " + "x-google-start-bitrate" + "=" + bitrateKbps;
               } else {
                  bitrateSet = "a=fmtp:" + codecRtpMap + " " + "maxaveragebitrate" + "=" + bitrateKbps * 1000;
               }

               LOGGER.d(CLASS_TAG, "Add remote SDP line: " + bitrateSet);
               var13.append(bitrateSet).append("\r\n");
            }
         }

         return var13.toString();
      }
   }

   public static String generateRemoteDescription(SessionDescription sdp, QBRTCMediaConfig parameters, QBRTCTypes.QBConferenceType conferenceType) {
      String sdpDescription = sdp.description;
      QBRTCMediaConfig.AudioCodec audioCodec = QBRTCMediaConfig.getAudioCodec();
      QBRTCMediaConfig.VideoCodec videoCodec = QBRTCMediaConfig.getVideoCodec();
      if(audioCodec != null) {
         LOGGER.d(CLASS_TAG, "generateRemoteDescription:  audioCodec=" + audioCodec.getDescription());
         sdpDescription = preferCodec(sdpDescription, audioCodec.getDescription(), true);
      }

      boolean isVideoConference = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(conferenceType);
      if(isVideoConference && videoCodec != null) {
         LOGGER.d(CLASS_TAG, "generateRemoteDescription:  videoCodec=" + videoCodec.getDescription());
         sdpDescription = preferCodec(sdpDescription, videoCodec.getDescription(), false);
      }

      int videoStartBitrate = QBRTCMediaConfig.getVideoStartBitrate();
      if(isVideoConference && videoStartBitrate > 0) {
         sdpDescription = setStartBitrate(QBRTCMediaConfig.VideoCodec.VP8.getDescription(), true, sdpDescription, videoStartBitrate);
         sdpDescription = setStartBitrate(QBRTCMediaConfig.VideoCodec.VP9.getDescription(), true, sdpDescription, videoStartBitrate);
         sdpDescription = setStartBitrate(QBRTCMediaConfig.VideoCodec.H264.getDescription(), true, sdpDescription, videoStartBitrate);
      }

      int audioStartBitrate = QBRTCMediaConfig.getAudioStartBitrate();
      if(audioStartBitrate > 0) {
         sdpDescription = setStartBitrate(QBRTCMediaConfig.AudioCodec.OPUS.getDescription(), false, sdpDescription, audioStartBitrate);
      }

      return sdpDescription;
   }

   public static String generateLocalDescription(SessionDescription sdp, QBRTCMediaConfig parameters, QBRTCTypes.QBConferenceType conferenceType) {
      String sdpDescription = sdp.description;
      QBRTCMediaConfig.AudioCodec audioCodec = QBRTCMediaConfig.getAudioCodec();
      QBRTCMediaConfig.VideoCodec videoCodec = QBRTCMediaConfig.getVideoCodec();
      if(audioCodec != null) {
         LOGGER.d(CLASS_TAG, "generateLocalDescription:  audioCodec=" + audioCodec.getDescription());
         sdpDescription = preferCodec(sdpDescription, audioCodec.getDescription(), true);
      }

      if(QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(conferenceType) && videoCodec != null) {
         LOGGER.d(CLASS_TAG, "generateLocalDescription:  videoCodec=" + videoCodec.getDescription());
         sdpDescription = preferCodec(sdpDescription, videoCodec.getDescription(), false);
      }

      return sdpDescription;
   }

}
