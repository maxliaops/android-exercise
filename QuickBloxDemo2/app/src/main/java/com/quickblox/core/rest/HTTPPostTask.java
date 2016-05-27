package com.quickblox.core.rest;

import com.quickblox.core.RestMethod;
import com.quickblox.core.request.ProgressHttpEntityWrapper;
import com.quickblox.core.rest.HTTPTask;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class HTTPPostTask extends HTTPTask {

   public HTTPPostTask(URL url, Map headers) throws IOException {
      super(url, headers);
   }

   protected void setupMethod() throws ProtocolException {
      this.connection.setRequestMethod(RestMethod.POST.toString());
      this.connection.setDoOutput(true);
   }

   protected OutputStream generateOutputStreamForUploadDownloadRequest() throws IOException {
      return this.progressCallback != null?(new ProgressHttpEntityWrapper(this.connection, this.progressCallback, this.contentLength)).getOutputStream():this.connection.getOutputStream();
   }
}
