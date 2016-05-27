package com.quickblox.core.server;

import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.HTTPTask;
import com.quickblox.core.rest.IHttpResponse;
import java.io.IOException;

public class HttpExecutor {

   public static IHttpResponse executeRest(HTTPTask restTask, QBProgressCallback progressCallback) throws IOException {
      if(restTask.getConnection().getRequestMethod().equals(RestMethod.GET.toString()) && progressCallback != null) {
         restTask.setProgressCallback(progressCallback);
      }

      restTask.executeTask();
      return restTask;
   }
}
