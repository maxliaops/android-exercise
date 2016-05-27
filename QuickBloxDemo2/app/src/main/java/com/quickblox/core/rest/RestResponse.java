package com.quickblox.core.rest;

import com.quickblox.core.helper.ToStringHelper;
import com.quickblox.core.io.ByteStreams;
import com.quickblox.core.rest.IHttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class RestResponse {

   private UUID uuid;
   private String responseBodyString;
   private byte[] content;
   private InputStream inputStream;
   private long contentLength;
   private IOException ioException;
   private boolean isDownloadFileResponse;
   private IHttpResponse httpResponse;


   public RestResponse(IHttpResponse httpResponse, UUID uuid, IOException ioException) {
      this.uuid = uuid;
      this.httpResponse = httpResponse;
      this.ioException = ioException;
   }

   public int getStatusCode() {
      return this.httpResponse != null?this.httpResponse.getStatusCode():0;
   }

   public Map getHeaders() {
      return this.httpResponse != null?this.httpResponse.getResponseHeaders():null;
   }

   public byte[] getContent() {
      return this.content;
   }

   public void setInputStream(InputStream inputStream) {
      try {
         if(!this.isDownloadFileResponse) {
            this.content = ByteStreams.toByteArray(inputStream);
         } else {
            this.inputStream = inputStream;
         }
      } catch (IOException var3) {
         this.ioException = var3;
      }

   }

   public InputStream getInputStream() {
      return this.inputStream;
   }

   public long getContentLength() {
      return this.contentLength;
   }

   public void setContentLength(long contentLength) {
      this.contentLength = contentLength;
   }

   public String getRawBody() {
      if(this.responseBodyString == null) {
         if(this.getContent() != null) {
            this.responseBodyString = new String(this.getContent());
         } else {
            this.responseBodyString = "";
         }
      }

      return this.responseBodyString;
   }

   public String getContentType() {
      return this.httpResponse != null?this.httpResponse.getContentType():null;
   }

   public IOException getIOException() {
      return this.ioException;
   }

   public void setDownloadFileResponse(boolean downloadFileResponse) {
      this.isDownloadFileResponse = downloadFileResponse;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("*********************************************************\n");
      sb.append("*** RESPONSE *** %s ***\nSTATUS : %s ");
      sb.append("\nHEADERS\n%s\nBODY\n    \'%s\'\n\n");
      String tab = "    ";
      return String.format(sb.toString(), new Object[]{String.valueOf(this.uuid), Integer.valueOf(this.getStatusCode()), ToStringHelper.toString(this.getHeaders(), tab), this.getRawBody()});
   }
}
