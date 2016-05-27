package com.quickblox.videochat.webrtc;

import android.content.Context;
import com.quickblox.videochat.webrtc.LooperExecutor;
import com.quickblox.videochat.webrtc.QBRTCMediaConfig;
import com.quickblox.videochat.webrtc.util.Logger;
import org.webrtc.PeerConnectionFactory;

public class PeerFactoryManager {

   private static final String CLASS_TAG = PeerFactoryManager.class.getSimpleName();
   private static final Logger LOGGER = Logger.getInstance("RTCClient");
   private static final String FIELD_TRIAL_AUTOMATIC_RESIZE = "WebRTC-MediaCodecVideoEncoder-AutomaticResize/Enabled/";
   private static final String FIELD_TRIAL_VP9 = "WebRTC-SupportVP9/Enabled/";
   private final Object lock;
   private LooperExecutor executor;
   private Context context;
   private PeerConnectionFactory peerConnectionFactory;


   public PeerFactoryManager(LooperExecutor executor, Context context) {
      this.lock = new Object();
      this.executor = executor;
      this.context = context;
      executor.requestStart();
   }

   public PeerFactoryManager(Context context) {
      this(new LooperExecutor(PeerFactoryManager.class), context);
   }

   LooperExecutor getExecutor() {
      return this.executor;
   }

   PeerConnectionFactory getPeerConnectionFactory() {
      Object var1 = this.lock;
      synchronized(this.lock) {
         while(this.peerConnectionFactory == null) {
            try {
               this.lock.wait();
            } catch (InterruptedException var4) {
               LOGGER.e(CLASS_TAG, "Waiting peerFactory failed");
            }
         }
      }

      return this.peerConnectionFactory;
   }

   public void createFactory() {
      this.executor.execute(new Runnable() {
         public void run() {
            PeerFactoryManager.this.initPeerConnectionFactory(PeerFactoryManager.this.context);
         }
      });
   }

   public void dispose() {
      this.executor.execute(new Runnable() {
         public void run() {
            PeerFactoryManager.LOGGER.d(PeerFactoryManager.CLASS_TAG, "start dispose Peer factory");
            if(PeerFactoryManager.this.peerConnectionFactory != null) {
               PeerFactoryManager.this.peerConnectionFactory.dispose();
               PeerFactoryManager.this.peerConnectionFactory = null;
            }

            PeerFactoryManager.LOGGER.d(PeerFactoryManager.CLASS_TAG, "dispose Peer factory done");
            PeerFactoryManager.this.executor.requestStop();
            PeerFactoryManager.this.executor = null;
         }
      });
   }

   private void initPeerConnectionFactory(Context context) {
      Object var2 = this.lock;
      synchronized(this.lock) {
         if(this.peerConnectionFactory == null) {
            this.initializeFactoryFieldTrials();
            if(!PeerConnectionFactory.initializeAndroidGlobals(context, true, true, QBRTCMediaConfig.isVideoHWAcceleration(), (Object)null)) {
               throw new IllegalStateException("Session can\'t be initialized. Factory wasn\'t created");
            }

            this.peerConnectionFactory = new PeerConnectionFactory();
            LOGGER.d(CLASS_TAG, "Peer connection factory initiated from thread" + Thread.currentThread().getId());
         } else {
            LOGGER.d(CLASS_TAG, "Peer connection factory has been already initiated ");
         }

         this.lock.notifyAll();
      }
   }

   private void initializeFactoryFieldTrials() {
      String field_trials = "WebRTC-MediaCodecVideoEncoder-AutomaticResize/Enabled/";
      QBRTCMediaConfig.VideoCodec videoCodec = QBRTCMediaConfig.getVideoCodec();
      if(QBRTCMediaConfig.VideoCodec.VP9.equals(videoCodec)) {
         field_trials = field_trials + "WebRTC-SupportVP9/Enabled/";
         LOGGER.d(CLASS_TAG, "initializing Factory with WebRTC-SupportVP9/Enabled/");
      }

      PeerConnectionFactory.initializeFieldTrials(field_trials);
   }

}
