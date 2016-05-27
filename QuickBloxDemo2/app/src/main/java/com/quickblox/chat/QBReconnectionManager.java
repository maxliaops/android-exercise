package com.quickblox.chat;

import com.quickblox.chat.QBChatService;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.StreamError;

public class QBReconnectionManager extends AbstractConnectionListener {

   private static final Logger LOGGER = Logger.getLogger(QBReconnectionManager.class.getName());
   boolean done = false;
   private Thread reconnectionThread;
   private int randomBase = (new Random()).nextInt(11) + 5;


   private boolean isReconnectionAllowed() {
      QBChatService chatService = QBChatService.getInstance();
      return !this.done && !chatService.isConnected() && chatService.isReconnectionAllowed();
   }

   protected synchronized void reconnect() {
      if(this.isReconnectionAllowed()) {
         if(this.reconnectionThread != null && this.reconnectionThread.isAlive()) {
            return;
         }

         this.reconnectionThread = new Thread() {

            private int attempts = 0;

            private int timeDelay() {
               ++this.attempts;
               return this.attempts > 13?QBReconnectionManager.this.randomBase * 6 * 5:(this.attempts > 7?QBReconnectionManager.this.randomBase * 6:QBReconnectionManager.this.randomBase);
            }
            public void run() {
               while(QBReconnectionManager.this.isReconnectionAllowed()) {
                  int remainingSeconds = this.timeDelay();

                  while(QBReconnectionManager.this.isReconnectionAllowed() && remainingSeconds > 0) {
                     try {
                        Thread.sleep(1000L);
                        --remainingSeconds;
                        QBReconnectionManager.this.notifyAttemptToReconnectIn(remainingSeconds);
                     } catch (InterruptedException var4) {
                        var4.printStackTrace();
                        QBReconnectionManager.this.notifyReconnectionFailed(var4);
                     }
                  }

                  try {
                     if(QBReconnectionManager.this.isReconnectionAllowed()) {
                        QBChatService.getInstance().connect();
                     }
                  } catch (Exception var3) {
                     QBReconnectionManager.this.notifyReconnectionFailed(var3);
                  }
               }

            }
         };
         this.reconnectionThread.setName("Smack Reconnection Manager");
         this.reconnectionThread.setDaemon(true);
         this.reconnectionThread.start();
      }

   }

   protected void notifyReconnectionFailed(Exception exception) {
      if(this.isReconnectionAllowed()) {
         Iterator i$ = QBChatService.getInstance().getConnectionListeners().iterator();

         while(i$.hasNext()) {
            ConnectionListener listener = (ConnectionListener)i$.next();
            listener.reconnectionFailed(exception);
         }
      }

   }

   protected void notifyAttemptToReconnectIn(int seconds) {
      if(this.isReconnectionAllowed()) {
         Iterator i$ = QBChatService.getInstance().getConnectionListeners().iterator();

         while(i$.hasNext()) {
            ConnectionListener listener = (ConnectionListener)i$.next();
            listener.reconnectingIn(seconds);
         }
      }

   }

   public void connectionClosed() {
      this.done = true;
   }

   public void connectionClosedOnError(Exception e) {
      this.done = false;
      if(e instanceof XMPPException.StreamErrorException) {
         XMPPException.StreamErrorException xmppEx = (XMPPException.StreamErrorException)e;
         StreamError error = xmppEx.getStreamError();
         String reason = error.getConditionText();
         if("conflict".equals(reason)) {
            return;
         }
      }

      if(this.isReconnectionAllowed()) {
         this.reconnect();
      }

   }

}
