package com.quickblox.core.task;

import com.quickblox.core.QBRequestCanceler;

public abstract class QueriesTaskAbs {

   protected QBRequestCanceler canceler;


   public void setCanceler(QBRequestCanceler canceler) {
      this.canceler = canceler;
   }

   public abstract QBRequestCanceler performTask();
}
