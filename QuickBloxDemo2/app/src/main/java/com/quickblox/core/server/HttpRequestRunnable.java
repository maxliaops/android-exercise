package com.quickblox.core.server;

import android.os.Handler;
import android.os.Message;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.rest.HTTPTask;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.server.HttpRequestTask;
import com.quickblox.core.server.RestRequestCallback;
import java.util.UUID;

public class HttpRequestRunnable implements Runnable {

   private final int EMPTY_MSG = 0;
   private QBProgressCallback progressCallback;
   private UUID uuid;
   private RestResponse restResponse;
   private Handler requestHandler;
   private RestRequestCallback requestCallback;
   private HTTPTask restTask;
   private boolean isDownloadRequest;


   public HttpRequestRunnable(RestRequestCallback restRequestCallback, UUID uuid, HTTPTask restTask, QBProgressCallback progressCallback, boolean isDownloadRequest) {
      this.uuid = uuid;
      this.restTask = restTask;
      this.requestCallback = restRequestCallback;
      this.progressCallback = progressCallback;
      this.isDownloadRequest = isDownloadRequest;
      this.requestHandler = new Handler() {
         public void handleMessage(Message msg) {
            if(HttpRequestRunnable.this.requestCallback != null) {
               HttpRequestRunnable.this.requestCallback.completedWithResponse(HttpRequestRunnable.this.restResponse);
            }

         }
      };
   }

   public void run() {
      this.restResponse = HttpRequestTask.executeRest(this.restTask, this.uuid, this.isDownloadRequest, this.progressCallback);
      this.requestHandler.sendEmptyMessage(0);
   }
}
