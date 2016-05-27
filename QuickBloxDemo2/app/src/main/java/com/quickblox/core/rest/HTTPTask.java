package com.quickblox.core.rest;

import android.os.Build.VERSION;
import android.util.Pair;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.helper.Lo;
import com.quickblox.core.io.ByteStreams;
import com.quickblox.core.io.IOUtils;
import com.quickblox.core.rest.IHttpResponse;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class HTTPTask implements IHttpResponse {

   private String charset = "UTF-8";
   protected int contentLength;
   private int statusCode;
   private Map requestHeaders;
   protected QBProgressCallback progressCallback;
   protected HttpURLConnection connection;
   private InputStream inputStream;
   private String formBody;
   private File uploadFile;
   private String uploadFileName;
   private Map formData;
   private static final int MAX_RETRIES = 3;
   private int retryCount = 0;


   public HTTPTask(URL url, Map headers) throws IOException {
      this.initConnection(url, headers);
   }

   private void initConnection(URL url, Map headers) throws IOException {
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setInstanceFollowRedirects(true);
      this.connection = connection;
      this.requestHeaders = headers;
      if(headers != null) {
         this.addHeaders(headers);
      }

      this.setupMethod();
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public HttpURLConnection getConnection() {
      return this.connection;
   }

   public Map getResponseHeaders() {
      HashMap header = new HashMap();
      if(this.connection != null) {
         Map map = this.connection.getHeaderFields();
         Iterator i$ = map.entrySet().iterator();

         while(i$.hasNext()) {
            Entry entry = (Entry)i$.next();
            StringBuilder listString = new StringBuilder();
            if(entry.getKey() != null) {
               Iterator i$1 = ((List)entry.getValue()).iterator();

               while(i$1.hasNext()) {
                  String s = (String)i$1.next();
                  listString.append(s);
               }

               header.put(entry.getKey(), listString.toString());
            }
         }
      }

      return header;
   }

   public String getContentType() {
      return this.connection != null?this.connection.getHeaderField("Content-Type"):null;
   }

   public InputStream getInputStream() {
      return this.inputStream;
   }

   public long getContentLength() {
      return (long)this.contentLength;
   }

   public String getFormBody() {
      return this.formBody;
   }

   protected abstract void setupMethod() throws ProtocolException;

   private void addHeaders(Map headers) {
      Iterator i$ = headers.keySet().iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         this.connection.setRequestProperty(key, (String)headers.get(key));
      }

   }

   public void setFormBody(Map formData, List pairParameters) {
      if(formData == null) {
         this.formBody = null;
      } else {
         this.formData = formData;
         StringBuilder sb = new StringBuilder();
         int i = 0;
         Iterator i$ = formData.entrySet().iterator();

         while(i$.hasNext()) {
            Entry item = (Entry)i$.next();

            try {
               sb.append(URLEncoder.encode((String)item.getKey(), this.charset));
               sb.append("=");
               sb.append(URLEncoder.encode(item.getValue().toString(), this.charset));
            } catch (UnsupportedEncodingException var11) {
               Lo.g(var11);
            }

            if(i++ != formData.size() - 1) {
               sb.append("&");
            }
         }

         i = 0;
         if(pairParameters != null) {
            for(i$ = pairParameters.iterator(); i$.hasNext(); ++i) {
               Pair var12 = (Pair)i$.next();
               String key = var12.first.toString();
               String value = var12.second.toString();
               if(value != null) {
                  if(i == 0) {
                     if(sb.length() > 0) {
                        sb.append("&");
                     }
                  } else {
                     sb.append("&");
                  }

                  try {
                     sb.append(URLEncoder.encode(key, this.charset));
                     sb.append("=");
                     sb.append(URLEncoder.encode(value, this.charset));
                  } catch (UnsupportedEncodingException var10) {
                     Lo.g(var10);
                  }
               }
            }
         }

         this.formBody = sb.toString();
      }
   }

   public void setUploadFile(File file, String fileName) {
      this.uploadFile = file;
      this.uploadFileName = fileName;
   }

   public void setProgressCallback(QBProgressCallback callback) {
      this.progressCallback = callback;
   }

   protected OutputStream generateOutputStreamForUploadDownloadRequest() throws IOException {
      return this.connection.getOutputStream();
   }

   protected InputStream generateInputStreamForUploadDownloadRequest() throws IOException {
      return this.connection.getInputStream();
   }

   public void executeTask() throws IOException {
      try {
         this.tryToFixNetHttpRequest();
         String boundary = Long.toHexString(System.currentTimeMillis());
         OutputStream out = null;
         if(this.uploadFile != null) {
            this.connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            ByteArrayOutputStream redirect = new ByteArrayOutputStream();
            this.writeMultipart(boundary, redirect, false);
            byte[] newUrl = redirect.toByteArray();
            this.contentLength = newUrl.length;
            this.contentLength = (int)((long)this.contentLength + this.uploadFile.length());
            this.connection.setFixedLengthStreamingMode(this.contentLength);
            out = this.generateOutputStreamForUploadDownloadRequest();
            this.writeMultipart(boundary, out, true);
         } else if(this.formBody != null) {
            this.connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + this.charset);
            this.connection.setFixedLengthStreamingMode(this.formBody.length());
            out = this.connection.getOutputStream();
            this.writeFormData(out);
         }

         IOUtils.closeOutputStreams(new OutputStream[]{out});

         try {
            this.statusCode = this.connection.getResponseCode();
         } catch (EOFException var9) {
            Lo.e("EOFException, retrying the request, retry count: " + this.retryCount);
            if(this.retryCount >= 3) {
               throw var9;
            }

            ++this.retryCount;
            this.retry(this.connection.getURL());
         } catch (IOException var10) {
            this.statusCode = this.connection.getResponseCode();
         }

         boolean redirect1 = false;
         if(this.statusCode != 200 && (this.statusCode == 302 || this.statusCode == 301 || this.statusCode == 303)) {
            redirect1 = true;
         }

         if(redirect1) {
            String newUrl1 = this.connection.getHeaderField("Location");
            this.retry(new URL(newUrl1));
         } else {
            if(this.statusCode >= 400 || this.statusCode == -1) {
               this.inputStream = this.connection.getErrorStream();
               return;
            }

            this.contentLength = this.connection.getContentLength();
            this.inputStream = this.generateInputStreamForUploadDownloadRequest();
         }

      } finally {
         ;
      }
   }

   private void retry(URL url) throws IOException {
      this.initConnection(url, this.requestHeaders);
      this.executeTask();
   }

   private void tryToFixNetHttpRequest() {
      if(VERSION.SDK_INT >= 14) {
         this.connection.setRequestProperty("Connection", "close");
      }

   }

   private void writeMultiForm(String boundary, BufferedWriter writer) throws IOException {
      Iterator i$ = this.formData.entrySet().iterator();

      while(i$.hasNext()) {
         Entry entry = (Entry)i$.next();
         writer.write("--" + boundary);
         writer.write("\r\n");
         writer.write("Content-Disposition: form-data; name=\"" + (String)entry.getKey() + "\"");
         writer.write("\r\n");
         writer.write("Content-Type: text/plain; charset=" + this.charset);
         writer.write("\r\n");
         writer.write("\r\n");
         writer.write(entry.getValue().toString());
         writer.write("\r\n");
         writer.flush();
      }

   }

   private void writeBinaryForm(String boundary, BufferedWriter writer) throws IOException {
      writer.write("--" + boundary);
      writer.write("\r\n");
      writer.write("Content-Disposition: form-data; name=\"" + this.uploadFileName + "\"; filename=\"" + this.uploadFile.getName() + "\"");
      writer.write("\r\n");
      writer.write("Content-Type: " + URLConnection.guessContentTypeFromName(this.uploadFile.getName()));
      writer.write("\r\n");
      writer.write("Content-Transfer-Encoding: binary");
      writer.write("\r\n");
      writer.write("\r\n");
      writer.flush();
   }

   private void endMultiForm(String boundary, BufferedWriter writer) throws IOException {
      writer.write("\r\n");
      writer.flush();
      writer.write("--" + boundary + "--");
      writer.write("\r\n");
      writer.flush();
   }

   private void writeMultipart(String boundary, OutputStream output, boolean writeContent) throws IOException {
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new OutputStreamWriter(output, Charset.forName(this.charset)), 8192);
         if(this.formBody != null) {
            this.writeMultiForm(boundary, writer);
         }

         this.writeBinaryForm(boundary, writer);
         if(writeContent) {
            FileInputStream input = null;

            try {
               input = new FileInputStream(this.uploadFile);
               ByteStreams.copy(input, output);
               output.flush();
            } catch (IOException var15) {
               var15.printStackTrace();
            } finally {
               IOUtils.closeInputStreams(new InputStream[]{input});
            }
         }

         this.endMultiForm(boundary, writer);
      } finally {
         if(writer != null) {
            writer.close();
         }

      }

   }

   private void writeFormData(OutputStream output) throws IOException {
      output.write(this.formBody.getBytes(this.charset));
      output.flush();
   }
}
