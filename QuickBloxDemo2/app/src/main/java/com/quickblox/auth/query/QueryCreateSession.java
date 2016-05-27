package com.quickblox.auth.query;

import android.util.Log;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.auth.model.QBSessionWrap;
import com.quickblox.auth.parsers.QBSessionJsonParser;
import com.quickblox.core.QBSettings;
import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.SignHelper;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.model.QBUser;
import java.security.SignatureException;
import java.util.Map;
import java.util.Random;

public class QueryCreateSession extends JsonQuery {

   private QBUser user;
   String provider;
   String accessToken;
   String accessTokenSecret;
   String xAuthServiceProvider;
   String xVerifyCredentialsAuthorization;


   public QueryCreateSession() {
      QBSessionJsonParser qbSessionJsonParser = new QBSessionJsonParser(this);
      qbSessionJsonParser.setDeserializer(QBSessionWrap.class);
      this.setParser(qbSessionJsonParser);
   }

   public QueryCreateSession(String provider, String accessToken, String accessTokenSecret) {
      this();
      this.provider = provider;
      this.accessToken = accessToken;
      this.accessTokenSecret = accessTokenSecret;
   }

   public QueryCreateSession(QBUser user) {
      this();
      this.user = user;
   }

   public QueryCreateSession(String xAuthServiceProvider, String xVerifyCredentialsAuthorization) {
      this();
      this.xAuthServiceProvider = xAuthServiceProvider;
      this.xVerifyCredentialsAuthorization = xVerifyCredentialsAuthorization;
      this.provider = QBProvider.TWITTER_DIGITS;
   }

   private void signRequest(RestRequest request) {
      String paramsString = request.getParamsOnlyStringNotEncoded();
      Log.d("Request build: ", paramsString);
      String authSecret = QBSettings.getInstance().getAuthorizationSecret();

      try {
         String e = SignHelper.calculateHMAC_SHA(paramsString, authSecret);
         request.getParameters().put("signature", e);
      } catch (SignatureException var5) {
         var5.printStackTrace();
      }

   }

   protected void setupRequest(RestRequest request) {
      super.setupRequest(request);
      this.signRequest(request);
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.POST);
   }

   protected void setParams(RestRequest request) {
      Map parameters = request.getParameters();
      String appId = QBSettings.getInstance().getApplicationId();
      String authKey = QBSettings.getInstance().getAuthorizationKey();
      int nonce = (new Random()).nextInt();
      long timestamp = System.currentTimeMillis() / 1000L;
      this.putValue(parameters, "application_id", appId);
      this.putValue(parameters, "auth_key", authKey);
      if(this.provider != null) {
         if(this.provider.equals(QBProvider.TWITTER)) {
            this.putValue(parameters, "keys[secret]", this.accessTokenSecret);
         }

         this.putValue(parameters, "keys[token]", this.accessToken);
      }

      this.putValue(parameters, "nonce", Integer.valueOf(nonce));
      if(this.provider != null) {
         this.putValue(parameters, "provider", this.provider);
      }

      this.putValue(parameters, "timestamp", Long.valueOf(timestamp));
      if(this.provider != null && this.provider.equals(QBProvider.TWITTER_DIGITS)) {
         this.putValue(parameters, "twitter_digits[X-Auth-Service-Provider]", this.xAuthServiceProvider);
         this.putValue(parameters, "twitter_digits[X-Verify-Credentials-Authorization]", this.xVerifyCredentialsAuthorization);
      }

      if(this.user != null) {
         this.putValue(parameters, "user[email]", this.user.getEmail());
         this.putValue(parameters, "user[login]", this.user.getLogin());
         this.putValue(parameters, "user[password]", this.user.getPassword());
      }

   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"session"});
   }

   public QBUser getUser() {
      return this.user;
   }

   public void setUser(QBUser user) {
      this.user = user;
   }
}
