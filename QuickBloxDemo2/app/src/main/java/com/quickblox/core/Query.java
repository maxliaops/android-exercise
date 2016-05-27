package com.quickblox.core;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.quickblox.auth.QBAuth;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.RestMethod;
import com.quickblox.core.account.model.QBAccountSettings;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Lo;
import com.quickblox.core.interfaces.QBCancelable;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.parser.QBResponseParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.server.BaseService;
import com.quickblox.core.server.Perform;
import com.quickblox.core.server.RestRequestCallback;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query implements Perform, RestRequestCallback, QBCancelable {

   private QBSettings qbSettings = QBSettings.getInstance();
   private Object originalObject;
   private RestRequest restRequest;
   protected boolean isCancel = false;
   protected QBEntityCallback entityCallback;
   private boolean shouldCheckAccountSynchronizing = true;
   protected QBResponseParser qbResponseParser;
   private Bundle bundleResult = new Bundle();
   protected RestRequestCallback versionCallback;


   public Object getOriginalObject() {
      return this.originalObject;
   }

   public void setOriginalObject(Object originalObject) {
      this.originalObject = originalObject;
   }

   public void setParser(QBResponseParser parser) {
      this.qbResponseParser = parser;
   }

   public QBResponseParser getParser() {
      return this.qbResponseParser;
   }

   protected BaseService getService() {
      try {
         return BaseService.getBaseService();
      } catch (BaseServiceException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   protected boolean isDownloadQuery() {
      return false;
   }

   protected void performInBgAsyncWithDelegate(QBEntityCallback callback) {
      this.entityCallback = callback;
      this.restRequest = new RestRequest();
      this.setupRequest(this.restRequest);
      Lo.g(this.restRequest);
      this.restRequest.asyncRequestWithCallback(this);
   }

   protected void setupRequest(RestRequest request) {
      this.setBody(request);
      this.setParams(request);
      this.setHeaders(request);
      this.setMethod(request);
      this.setUrl(request);
      this.setFiles(request);
      this.setShouldRedirect(request);
      this.setApiVersion(request);
      this.setFrameworkVersion(request);
      this.setAuthentication(request);
      request.setIsDownloadFileRequest(this.isDownloadQuery());
   }

   private RestRequest makeRetrieveAccountSettingsRequest() {
      StringBuilder urlBuilder = new StringBuilder("https://api.quickblox.com/account_settings.json");
      HashMap headers = new HashMap();
      headers.put("QB-Account-Key", QBSettings.getInstance().getAccountKey());
      RestRequest accountSettingsRequest = RestRequest.create(urlBuilder.toString(), headers, (Map)null, RestMethod.GET);
      return accountSettingsRequest;
   }

   private void setApiVersion(RestRequest request) {
      String version = this.qbSettings.getApiVersion();
      if(version != null) {
         request.getHeaders().put("QuickBlox-REST-API-Version", version);
      }

   }

   private void setFrameworkVersion(RestRequest request) {
      String version = this.qbSettings.getVersionName();
      String versionValue = String.format("%s %s", new Object[]{"Android", version});
      request.getHeaders().put("QB-SDK", versionValue);
   }

   protected void setAuthentication(RestRequest request) {
      try {
         String e = BaseService.getBaseService().getToken();
         if(e != null) {
            request.getHeaders().put("QB-Token", e);
         }
      } catch (BaseServiceException var3) {
         var3.printStackTrace();
      }

   }

   protected String getUrl() {
      throw new UnsupportedOperationException();
   }

   protected void setUrl(RestRequest request) {
      URL url = null;

      try {
         url = new URL(this.getUrl());
      } catch (MalformedURLException var4) {
         var4.printStackTrace();
      }

      request.setUrl(url);
   }

   protected String buildQueryUrl(Object ... specificParts) {
      String specificPart = TextUtils.join("/", specificParts);
      StringBuilder urlBuilder = new StringBuilder(this.qbSettings.getApiEndpoint() + "/");
      urlBuilder.append(specificPart).append(".json");
      return urlBuilder.toString();
   }

   protected void setBody(RestRequest request) {}

   protected void setParams(RestRequest request) {}

   protected void setHeaders(RestRequest request) {}

   protected void setMethod(RestRequest request) {}

   protected void setFiles(RestRequest request) {}

   protected void setShouldRedirect(RestRequest request) {}

   protected void doneSuccess() {}

   public Query performAsyncWithCallback(final QBEntityCallback entityCallbackArg) {
      if(this.IsShouldUpdateAccountSettings()) {
         RestRequest request = this.makeRetrieveAccountSettingsRequest();
         Lo.g(request);
         request.asyncRequestWithCallback(new RestRequestCallback() {
            public void completedWithResponse(RestResponse response) {
               if(!Query.this.isCancel) {
                  try {
                     Query.this.parseAccountRequest(response);
                  } catch (QBResponseException var3) {
                     if(entityCallbackArg != null) {
                        entityCallbackArg.onError(var3);
                     }

                     return;
                  }

                  Query.this.performInBgAsyncWithDelegate(entityCallbackArg);
               }
            }
         });
      } else {
         this.performInBgAsyncWithDelegate(entityCallbackArg);
      }

      this.versionCallback = new Query.VersionEntityCallback(null);
      return this;
   }

   public Object perform(Bundle bundle) throws QBResponseException {
      if(this.IsShouldUpdateAccountSettings()) {
         RestRequest restResponse = this.makeRetrieveAccountSettingsRequest();
         Lo.g(restResponse);
         this.parseAccountRequest(restResponse.syncRequest());
      }

      this.restRequest = new RestRequest();
      this.setupRequest(this.restRequest);
      Lo.g(this.restRequest);
      RestResponse restResponse1 = this.restRequest.syncRequest();
      if(restResponse1.getStatusCode() > 0) {
         Lo.g(restResponse1);
      }

      this.extractTokenExpirationDate(restResponse1);

      try {
         Object e = this.qbResponseParser.parse(restResponse1, bundle);
         this.doneSuccess();
         return e;
      } catch (QBResponseException var4) {
         throw var4;
      }
   }

   public void completedWithResponse(RestResponse restResponse) {
      if(restResponse.getStatusCode() > 0) {
         Lo.g(restResponse);
      }

      this.versionCallback.completedWithResponse(restResponse);
   }

   public void cancel() {
      this.isCancel = true;
      this.restRequest.cancel();
      if(this.qbResponseParser != null) {
         this.qbResponseParser.cancel();
      }

   }

   protected void putValue(Map parametersMap, String key, Object value) {
      if(value != null) {
         parametersMap.put(key, String.valueOf(value));
      }

   }

   protected void putIdenticalValue(List parameters, String key, Object value) {
      if(value != null) {
         parameters.add(new Pair(key, String.valueOf(value)));
      }

   }

   protected void setShouldCheckAccountSynchronizing(boolean shouldCheckAccountSynchronizing) {
      this.shouldCheckAccountSynchronizing = shouldCheckAccountSynchronizing;
   }

   private void parseAccountRequest(RestResponse restResponse) throws QBResponseException {
      if(restResponse.getStatusCode() > 0) {
         Lo.g(restResponse);
      }

      QBJsonParser settingsJsonParser = new QBJsonParser((JsonQuery)null);
      settingsJsonParser.setDeserializer(QBAccountSettings.class);

      try {
         QBAccountSettings e = (QBAccountSettings)settingsJsonParser.parse(restResponse, (Bundle)null);
         QBSettings.AutoUpdateMode updateMode = this.qbSettings.getUpdateMode();
         if(updateMode != null) {
            updateMode.updateSettings(e, this.qbSettings);
         }

         Lo.g("Retrieved custom endpoints. ApiEndpoint: " + e.getApiEndpoint() + ", ChatEndpoint: " + e.getChatEndpoint());
      } catch (QBResponseException var5) {
         Lo.g("Synchronizing account settings failed");
         throw new QBResponseException("\nSomething wrong with your Account Key. Please check it in QuickBlox Admin Panel (https://admin.quickblox.com/account), Settings tab.");
      }
   }

   private boolean IsShouldUpdateAccountSettings() {
      QBSettings.AutoUpdateMode updateMode = this.qbSettings.getUpdateMode();
      return this.shouldCheckAccountSynchronizing && updateMode != null?updateMode.isUpdatePeriodExpired():false;
   }

   private void extractTokenExpirationDate(RestResponse response) {
      Date expirationDate = null;
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      if(response != null) {
         Map headers = response.getHeaders();
         String tokenExpired;
         if(headers != null && (tokenExpired = (String)headers.get("QB-Token-ExpirationDate")) != null) {
            try {
               expirationDate = simpleDateFormat.parse(tokenExpired);
            } catch (ParseException var8) {
               var8.printStackTrace();
            }

            if(expirationDate != null) {
               try {
                  QBAuth.getBaseService().setTokenExpirationDate(expirationDate);
               } catch (BaseServiceException var7) {
                  var7.printStackTrace();
               }
            }
         }
      }

   }

   public Bundle getBundle() {
      return this.bundleResult;
   }

   protected void notifyErrors(QBResponseException exception) {
      if(this.entityCallback != null) {
         this.entityCallback.onError(exception);
      }

   }

   protected void notifySuccess(Object result, Bundle resultBundle) {
      this.doneSuccess();
      if(this.entityCallback != null) {
         this.entityCallback.onSuccess(result, resultBundle);
      }

   }

   public boolean isShouldCheckAccountSynchronizing() {
      return this.shouldCheckAccountSynchronizing;
   }

   private class VersionEntityCallback implements RestRequestCallback {

      private VersionEntityCallback() {}

      public void completedWithResponse(RestResponse response) {
         if(!Query.this.isCancel) {
            Query.this.extractTokenExpirationDate(response);
            QBResponseException responseException = null;
            Object result = null;
            Bundle bundle = Query.this.getBundle();
            if(Query.this.qbResponseParser != null) {
               try {
                  result = Query.this.qbResponseParser.parse(response, bundle);
               } catch (QBResponseException var6) {
                  responseException = var6;
               }
            } else {
               responseException = this.createDefaultException();
            }

            if(!Query.this.isCancel) {
               if(responseException == null) {
                  Query.this.notifySuccess(result, bundle);
               } else {
                  Query.this.notifyErrors(responseException);
               }

            }
         }
      }

      private QBResponseException createDefaultException() {
         ArrayList customErrors = new ArrayList();
         customErrors.add("Response parser was not specified");
         QBResponseException responseException = new QBResponseException(customErrors);
         return responseException;
      }

      // $FF: synthetic method
      VersionEntityCallback(Object x1) {
         this();
      }
   }
}
