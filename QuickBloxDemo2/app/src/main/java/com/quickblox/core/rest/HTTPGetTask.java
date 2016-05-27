package com.quickblox.core.rest;

import com.quickblox.core.RestMethod;
import com.quickblox.core.request.ProgressHttpEntityWrapper;
import com.quickblox.core.rest.HTTPTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class HTTPGetTask extends HTTPTask {

   public HTTPGetTask(URL url, Map headers) throws IOException {
      super(url, headers);
   }

   protected void setupMethod() throws ProtocolException {
      this.connection.setRequestMethod(RestMethod.GET.toString());
      this.connection.setDoInput(true);
   }

   protected InputStream generateInputStreamForUploadDownloadRequest() throws IOException {
      return this.progressCallback != null?(new ProgressHttpEntityWrapper(this.connection, this.progressCallback, this.contentLength)).getInputStream():this.connection.getInputStream();
   }
}
