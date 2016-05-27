package com.quickblox.core.result;

import com.quickblox.core.Query;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Lo;
import com.quickblox.core.interfaces.QBErrorParser;
import com.quickblox.core.parser.QBJsonErrorParser;
import com.quickblox.core.rest.RestResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestResult {

   private static Map errorMsgs = new HashMap();
   protected RestResponse response;
   private List errors = new ArrayList();
   private Query query;
   private QBErrorParser errorParser;


   public RestResult(RestResponse response) {
      this.response = response;
   }

   public RestResult() {
      this.errors = new ArrayList();
      this.errorParser = new QBJsonErrorParser();
   }

   public void setErrorParser(QBErrorParser errorParser) {
      this.errorParser = errorParser;
   }

   protected RestResponse getResponse() {
      return this.response;
   }

   public void setResponse(RestResponse response) {
      this.response = response;
      if(response != null) {
         if(response.getIOException() != null) {
            this.errors.add("Connection failed. Please check your internet connection.");
         } else {
            this.checkServerError(response.getStatusCode());
         }

         this.processResponse();
      } else {
         this.errors.add("Connection closed due to timeout. Please check your internet connection.");
      }

   }

   protected Query getQuery() {
      return this.query;
   }

   public void setQuery(Query query) {
      this.query = query;
   }

   protected void processResponse() {
      String responseBody = this.getRawBody();
      if(!this.isEmpty() && this.foundError()) {
         this.parseErrors(responseBody);
      }

   }

   private void parseErrors(String body) {
      if(this.errorParser != null) {
         try {
            List e = this.errorParser.parseError(body);
            if(e != null) {
               this.errors.addAll(e);
            }
         } catch (QBResponseException var3) {
            Lo.g("Problem has occurred during parsing errors");
         }
      }

   }

   private boolean isXml() {
      return !this.isEmpty()?this.getRawBody().startsWith("<?xml"):false;
   }

   protected boolean isEmpty() {
      return this.getRawBody() != null?this.getRawBody().trim().length() == 0:true;
   }

   private void checkServerError(int status) {
      String errorMsg = (String)errorMsgs.get(Integer.valueOf(status));
      if(errorMsg != null) {
         this.errors.add(errorMsg);
      }

   }

   public boolean isArray() {
      return !this.isEmpty()?this.getRawBody().charAt(0) == 91:false;
   }

   boolean isObject() {
      return !this.isEmpty()?this.getRawBody().charAt(0) == 123:false;
   }

   protected boolean foundError() {
      if(this.response == null) {
         return false;
      } else {
         int statusCode = this.getStatusCode();
         return statusCode != 200 && statusCode != 201 && statusCode != 202;
      }
   }

   protected void extractEntity() {}

   public String getRawBody() {
      return this.response != null?this.response.getRawBody():"";
   }

   public int getStatusCode() {
      return this.response != null?this.response.getStatusCode():0;
   }

   public List getErrors() {
      return this.errors;
   }

   public void setErrors(List errors) {
      this.errors = errors;
   }

   public String toString() {
      return "RestResult{isEmpty=" + this.isEmpty() + ", foundError=" + this.foundError() + ", statusCode=" + this.getStatusCode() + ", onError=" + this.getStatusCode() + '}';
   }

   static {
      errorMsgs.put(Integer.valueOf(404), "Entity you are looking for was not found.");
      errorMsgs.put(Integer.valueOf(500), "We\'re sorry, but something went wrong. We\'ve been notified about this issue and we\'ll take a look at it shortly.");
   }
}
