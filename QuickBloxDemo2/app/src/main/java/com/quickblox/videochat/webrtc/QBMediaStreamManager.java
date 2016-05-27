package com.quickblox.videochat.webrtc;

import android.content.Context;
import android.hardware.Camera;
import com.quickblox.videochat.webrtc.LooperExecutor;
import com.quickblox.videochat.webrtc.PeerFactoryManager;
import com.quickblox.videochat.webrtc.QBMediaStreamManagerCallback;
import com.quickblox.videochat.webrtc.QBRTCMediaConfig;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.RTCMediaUtils;
import com.quickblox.videochat.webrtc.exception.QBRTCException;
import com.quickblox.videochat.webrtc.util.Logger;
import java.util.HashMap;
import java.util.Map;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class QBMediaStreamManager {

   public static final String VIDEO_TRACK_ID = "ARDAMSv0";
   public static final String AUDIO_TRACK_ID = "ARDAMSa0";
   private static final String CLASS_TAG = QBMediaStreamManager.class.getSimpleName();
   private static final Logger LOGGER = Logger.getInstance(CLASS_TAG);
   private Context context;
   QBMediaStreamManagerCallback callback;
   PeerConnectionFactory peerConnectionFactory;
   MediaStream localMediaStream;
   private VideoCapturerAndroid videoCapturer;
   private VideoSource videoSource;
   private Map videoTracks;
   private int videoWidth;
   private int videoHeight;
   private int videoFps;
   private VideoTrack localVideoTrack;
   private boolean isVideoConference;
   private final Object lock = new Object();
   private PeerFactoryManager factoryManager;
   private int cameraId = -1;
   private volatile boolean closed = false;
   private int numberOfCameras;
   private VideoCapturerAndroid.CameraErrorHandler cameraErrorHandler;


   QBMediaStreamManager(PeerFactoryManager factoryManager, Context context, QBMediaStreamManagerCallback callback) {
      this.factoryManager = factoryManager;
      this.context = context;
      this.callback = callback;
      this.videoTracks = new HashMap();
   }

   public MediaStream getLocalMediaStream() {
      return this.localMediaStream;
   }

   MediaStream initLocalMediaStream(QBRTCTypes.QBConferenceType conferenceType, VideoCapturerAndroid.CameraErrorHandler cameraErrorHandler) throws QBRTCException {
      this.cameraErrorHandler = cameraErrorHandler;
      LOGGER.d(CLASS_TAG, "Init local media stream");
      Object var3 = this.lock;
      synchronized(this.lock) {
         if(this.localMediaStream == null) {
            MediaConstraints mediaConstraints = RTCMediaUtils.createMediaConstraints(new QBRTCMediaConfig(), conferenceType);
            this.peerConnectionFactory = this.factoryManager.getPeerConnectionFactory();
            this.localMediaStream = this.peerConnectionFactory.createLocalMediaStream("ARDAMS");
            if(QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO == conferenceType) {
               this.isVideoConference = true;
               this.tryInitVideo(mediaConstraints, this.localMediaStream);
            }

            AudioTrack audioTrack = this.peerConnectionFactory.createAudioTrack("ARDAMSa0", this.peerConnectionFactory.createAudioSource(mediaConstraints));
            this.localMediaStream.addTrack(audioTrack);
            if(this.localVideoTrack != null) {
               this.callback.onReceiveLocalVideoTrack(this.localVideoTrack);
            }
         }
      }

      return this.localMediaStream;
   }

   public int getCurrentCameraId() {
      return this.cameraId;
   }

   private void notifyCameraHandler(String error) {
      if(this.cameraErrorHandler != null) {
         this.cameraErrorHandler.onCameraError(error);
      }

   }

   private void tryInitVideo(MediaConstraints mediaConstraints, MediaStream mediaStream) {
      this.isVideoConference = this.isVideoConference && this.context.checkCallingOrSelfPermission("android.permission.CAMERA") == 0;
      if(!this.isVideoConference) {
         this.notifyCameraHandler("Camera permission wasn\'t granted for application!");
         LOGGER.d(CLASS_TAG, "Camera permission wasn\'t granted for application!");
      } else {
         this.numberOfCameras = VideoCapturerAndroid.getDeviceCount();
         if(this.numberOfCameras == 0) {
            LOGGER.d(CLASS_TAG, "No camera on device. Switch to audio only call.");
            this.notifyCameraHandler("No device for video input was found");
            this.isVideoConference = false;
         }

         if(this.isVideoConference) {
            VideoTrack videoTrack = this.initVideoTrack(this.cameraErrorHandler, mediaConstraints);
            if(videoTrack != null) {
               mediaStream.addTrack(videoTrack);
            }
         }

      }
   }

   private VideoTrack initVideoTrack(VideoCapturerAndroid.CameraErrorHandler cameraErrorHandler, MediaConstraints videoConstrains) {
      LOGGER.d(CLASS_TAG, "Add video stream");
      String cameraName = VideoCapturerAndroid.getNameOfFrontFacingDevice();
      this.videoCapturer = VideoCapturerAndroid.create(cameraName, cameraErrorHandler);
      if(this.videoCapturer == null) {
         cameraName = VideoCapturerAndroid.getNameOfBackFacingDevice();
         this.videoCapturer = VideoCapturerAndroid.create(cameraName, cameraErrorHandler);
      }

      if(this.videoCapturer != null) {
         this.setCamera(cameraName);
         return this.createVideoTrack(this.videoCapturer, videoConstrains);
      } else {
         return null;
      }
   }

   private void setCamera(String cameraName) {
      for(int i = 0; i < Camera.getNumberOfCameras(); ++i) {
         String existingDevice = VideoCapturerAndroid.getDeviceName(i);
         if(cameraName.equals(existingDevice)) {
            this.cameraId = i;
            break;
         }
      }

   }

   private VideoTrack createVideoTrack(VideoCapturerAndroid capturer, MediaConstraints videoConstrains) {
      this.videoSource = this.peerConnectionFactory.createVideoSource(capturer, videoConstrains);
      this.localVideoTrack = this.peerConnectionFactory.createVideoTrack("ARDAMSv0", this.videoSource);
      this.localVideoTrack.setEnabled(true);
      return this.localVideoTrack;
   }

   public void setVideoEnabled(boolean setEnability) {
      if(!this.isClosed()) {
         if(this.getLocalVideoTrack() != null) {
            this.getLocalVideoTrack().setEnabled(setEnability);
         }

      }
   }

   public void setAudioEnabled(boolean setEnability) {
      if(!this.isClosed()) {
         if(this.getLocalAudioTrack() != null) {
            this.getLocalAudioTrack().setEnabled(setEnability);
         }

      }
   }

   public boolean isVideoEnabled() {
      return this.getLocalVideoTrack() != null?this.getLocalVideoTrack().enabled():false;
   }

   public boolean isAudioEnabled() {
      return this.getLocalAudioTrack() != null?this.getLocalAudioTrack().enabled():false;
   }

   public VideoTrack getLocalVideoTrack() {
      return this.localMediaStream != null && this.localMediaStream.videoTracks.size() > 0?(VideoTrack)this.localMediaStream.videoTracks.get(0):null;
   }

   public AudioTrack getLocalAudioTrack() {
      return this.localMediaStream != null && this.localMediaStream.audioTracks.size() > 0?(AudioTrack)this.localMediaStream.audioTracks.get(0):null;
   }

   public boolean switchAudioOutput() {
      return false;
   }

   public boolean isClosed() {
      return this.closed;
   }

   private boolean switchCameraInternal(Runnable runnable) {
      boolean switchedCamera = false;
      if(this.videoCapturer != null) {
         LOGGER.d(CLASS_TAG, "Switch camera");
         switchedCamera = this.videoCapturer.switchCamera(runnable);
         if(switchedCamera) {
            this.cameraId = (this.cameraId + 1) % Camera.getNumberOfCameras();
         }
      }

      return switchedCamera;
   }

   public boolean switchCameraInput(Runnable runnable) {
      return this.isClosed()?false:this.switchCameraInternal(runnable);
   }

   void setClosed() {
      this.closed = true;
   }

   void close() {
      LooperExecutor executor = this.factoryManager.getExecutor();
      if(executor != null) {
         executor.execute(new Runnable() {
            public void run() {
               QBMediaStreamManager.this.closeInternal();
            }
         });
      }
   }

   private void closeInternal() {
      this.callback = null;
      this.setClosed();
      LOGGER.d(CLASS_TAG, "Video source start dispose");
      LOGGER.d(CLASS_TAG, "Video source is " + this.videoSource);
      if(this.videoSource != null) {
         LOGGER.d(CLASS_TAG, "Video source state is " + this.videoSource.state());
         this.videoSource.stop();
         this.videoSource = null;
         LOGGER.d(CLASS_TAG, "Video source disposed");
      }

      this.videoTracks.clear();
   }

   public Map getVideoTracks() {
      return this.videoTracks;
   }

}
