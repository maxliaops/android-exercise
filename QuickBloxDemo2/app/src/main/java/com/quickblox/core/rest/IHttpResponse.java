package com.quickblox.core.rest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

public interface IHttpResponse {

   HttpURLConnection getConnection();

   int getStatusCode();

   Map getResponseHeaders();

   String getContentType();

   long getContentLength();

   InputStream getInputStream();
}
