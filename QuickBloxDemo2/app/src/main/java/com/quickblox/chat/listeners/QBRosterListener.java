package com.quickblox.chat.listeners;

import com.quickblox.chat.model.QBPresence;
import java.util.Collection;

public interface QBRosterListener {

   void entriesDeleted(Collection var1);

   void entriesAdded(Collection var1);

   void entriesUpdated(Collection var1);

   void presenceChanged(QBPresence var1);
}
