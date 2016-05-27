package com.quickblox.core;

import com.quickblox.core.Query;
import com.quickblox.core.interfaces.QBCancelable;

public class QBRequestCanceler implements QBCancelable {

   private Query query;


   public QBRequestCanceler(Query query) {
      this.query = query;
   }

   public void cancel() {
      this.query.cancel();
   }
}
