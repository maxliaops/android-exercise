package com.quickblox.core.rest;

import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.HTTPTask;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class HTTPPutTask extends HTTPTask {

   public HTTPPutTask(URL url, Map headers) throws IOException {
      super(url, headers);
   }

   protected void setupMethod() throws ProtocolException {
      this.connection.setRequestMethod(RestMethod.PUT.toString());
      this.connection.setDoOutput(true);
   }
}
