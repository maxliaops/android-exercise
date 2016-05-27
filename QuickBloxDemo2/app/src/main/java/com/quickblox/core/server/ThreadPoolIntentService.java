package com.quickblox.core.server;

import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.interfaces.QBCancelable;
import com.quickblox.core.rest.HTTPTask;
import com.quickblox.core.server.HttpRequestRunnable;
import com.quickblox.core.server.QBExecutorService;
import com.quickblox.core.server.RestRequestCallback;
import java.util.UUID;
import java.util.concurrent.Future;

public class ThreadPoolIntentService implements QBCancelable {

   private Future request;


   public void executeRest(RestRequestCallback restRequestCallback, UUID uuid, HTTPTask restTask, QBProgressCallback progressCallback, boolean isDownLoadRequest) {
      this.request = QBExecutorService.getInstance().submit(new HttpRequestRunnable(restRequestCallback, uuid, restTask, progressCallback, isDownLoadRequest));
   }

   public void cancel() {
      if(!this.request.isDone()) {
         this.request.cancel(true);
      }

   }
}
