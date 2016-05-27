package com.quickblox.core.rest;

import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.HTTPTask;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

class HTTPDeleteTask extends HTTPTask {

   public HTTPDeleteTask(URL url, Map headers) throws IOException {
      super(url, headers);
   }

   protected void setupMethod() throws ProtocolException {
      this.connection.setRequestMethod(RestMethod.DELETE.toString());
   }
}
