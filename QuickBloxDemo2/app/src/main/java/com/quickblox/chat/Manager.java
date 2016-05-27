package com.quickblox.chat;

import java.lang.ref.WeakReference;
import org.jivesoftware.smack.XMPPConnection;

public abstract class Manager {

   final WeakReference weakConnection;


   public Manager(XMPPConnection connection) {
      this.weakConnection = new WeakReference(connection);
   }

   protected final XMPPConnection connection() {
      return (XMPPConnection)this.weakConnection.get();
   }
}
