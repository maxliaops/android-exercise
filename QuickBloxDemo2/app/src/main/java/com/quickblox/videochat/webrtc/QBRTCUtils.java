package com.quickblox.videochat.webrtc;

import android.util.Log;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.webrtc.PeerConnection;

class QBRTCUtils {

   private static final String TAG = "RTCClient.PeerConUtils";


   public static List getIceServersList() {
      if(QBRTCConfig.getIceServerList() == null) {
         LinkedList iceServersList = new LinkedList();
         iceServersList.add(new PeerConnection.IceServer("stun:turn.quickblox.com", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
         iceServersList.add(new PeerConnection.IceServer("turn:turn.quickblox.com:3478?transport=udp", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
         iceServersList.add(new PeerConnection.IceServer("turn:turn.quickblox.com:3478?transport=tcp", "quickblox", "baccb97ba2d92d71e26eb9886da5f1e0"));
         return iceServersList;
      } else {
         return QBRTCConfig.getIceServerList();
      }
   }

   public static Integer getCurrentChatUser() {
      QBUser user = QBChatService.getInstance().getUser();
      return Integer.valueOf(user != null?user.getId().intValue():-1);
   }

   public static String preferISAC(String sdpDescription) {
      String[] lines = sdpDescription.split("\r\n");
      int mLineIndex = -1;
      String isac16kRtpMap = null;
      Pattern isac16kPattern = Pattern.compile("^a=rtpmap:(\\d+) ISAC/16000[\r]?$");

      for(int origMLineParts = 0; origMLineParts < lines.length && (mLineIndex == -1 || isac16kRtpMap == null); ++origMLineParts) {
         if(lines[origMLineParts].startsWith("m=audio ")) {
            mLineIndex = origMLineParts;
         } else {
            Matcher newMLine = isac16kPattern.matcher(lines[origMLineParts]);
            if(newMLine.matches()) {
               isac16kRtpMap = newMLine.group(1);
            }
         }
      }

      if(mLineIndex == -1) {
         Log.d("RTCClient.PeerConUtils", "No m=audio line, so can\'t prefer iSAC");
         return sdpDescription;
      } else if(isac16kRtpMap == null) {
         Log.d("RTCClient.PeerConUtils", "No ISAC/16000 line, so can\'t prefer iSAC");
         return sdpDescription;
      } else {
         String[] var14 = lines[mLineIndex].split(" ");
         StringBuilder var15 = new StringBuilder();
         byte origPartIndex = 0;
         int var16 = origPartIndex + 1;
         var15.append(var14[origPartIndex]).append(" ");
         var15.append(var14[var16++]).append(" ");
         var15.append(var14[var16++]).append(" ");
         var15.append(isac16kRtpMap);

         for(; var16 < var14.length; ++var16) {
            if(!var14[var16].equals(isac16kRtpMap)) {
               var15.append(" ").append(var14[var16]);
            }
         }

         lines[mLineIndex] = var15.toString();
         StringBuilder newSdpDescription = new StringBuilder();
         String[] arr$ = lines;
         int len$ = lines.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String line = arr$[i$];
            newSdpDescription.append(line).append("\r\n");
         }

         return newSdpDescription.toString();
      }
   }

   public static void abortUnless(boolean condition, String msg) {
      if(!condition) {
         reportError(msg);
      }

   }

   public static void reportError(String errorMessage) {
      Log.e("RTCClient.PeerConUtils", "Peerconnection ERROR: " + errorMessage);
   }
}
