package com.quickblox.core.server;

import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.interfaces.QBCancelable;
import com.quickblox.core.rest.HTTPTask;
import com.quickblox.core.rest.IHttpResponse;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.server.HttpExecutor;
import com.quickblox.core.server.RestRequestCallback;
import com.quickblox.core.server.ThreadPoolIntentService;
import java.io.IOException;
import java.util.UUID;

public class HttpRequestTask implements QBCancelable {

   private ThreadPoolIntentService httpRequestAsyncTask;
   private QBProgressCallback progressCallback;


   public void executeAsyncRest(RestRequestCallback callback, HTTPTask restTask, UUID uuid, boolean isDownloadRequest) {
      this.httpRequestAsyncTask = new ThreadPoolIntentService();
      this.httpRequestAsyncTask.executeRest(callback, uuid, restTask, this.progressCallback, isDownloadRequest);
   }

   public RestResponse executeSyncRest(HTTPTask restTask, UUID uuid, boolean isDownloadRequest) {
      return executeRest(restTask, uuid, isDownloadRequest, this.progressCallback);
   }

   public static RestResponse executeRest(HTTPTask restTask, UUID uuid, boolean isDownloadRequest, QBProgressCallback progressCallback) {
      IHttpResponse httpResponse = null;
      IOException ioException = null;

      try {
         httpResponse = HttpExecutor.executeRest(restTask, progressCallback);
      } catch (IOException var7) {
         var7.printStackTrace();
         ioException = var7;
      }

      RestResponse restResponse = new RestResponse(httpResponse, uuid, ioException);
      restResponse.setDownloadFileResponse(isDownloadRequest);
      if(httpResponse != null) {
         restResponse.setInputStream(httpResponse.getInputStream());
         restResponse.setContentLength(httpResponse.getContentLength());
      }

      return restResponse;
   }

   public void cancel() {
      this.httpRequestAsyncTask.cancel();
   }

   public void setProgressCallback(QBProgressCallback progressCallback) {
      this.progressCallback = progressCallback;
   }
}
