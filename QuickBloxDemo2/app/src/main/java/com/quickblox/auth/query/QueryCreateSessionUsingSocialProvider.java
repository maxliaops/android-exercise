package com.quickblox.auth.query;

import com.quickblox.auth.model.QBProvider;
import com.quickblox.core.QBSettings;
import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.SignHelper;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import java.security.SignatureException;
import java.util.Map;
import java.util.Random;

public class QueryCreateSessionUsingSocialProvider extends JsonQuery {

   String provider;
   String scope = "friends_status";


   public QueryCreateSessionUsingSocialProvider(String provider, String[] scope) {
      this.provider = provider;
   }

   protected void setParams(RestRequest request) {
      Map parameters = request.getParameters();
      String appId = QBSettings.getInstance().getApplicationId();
      String authKey = QBSettings.getInstance().getAuthorizationKey();
      int nonce = (new Random()).nextInt();
      long timestamp = System.currentTimeMillis() / 1000L;
      this.putValue(parameters, "application_id", appId);
      this.putValue(parameters, "auth_key", authKey);
      this.putValue(parameters, "nonce", Integer.valueOf(nonce));
      if(this.provider != null) {
         this.putValue(parameters, "provider", this.provider);
         if(this.provider.equals(QBProvider.FACEBOOK)) {
            this.putValue(parameters, "scope", this.scope);
         }
      }

      this.putValue(parameters, "timestamp", Long.valueOf(timestamp));
   }

   private void signRequest(RestRequest request) {
      String paramsString = request.getParamsOnlyStringNotEncoded();
      String authSecret = QBSettings.getInstance().getAuthorizationSecret();

      try {
         String e = SignHelper.calculateHMAC_SHA(paramsString, authSecret);
         request.getParameters().put("signature", e);
      } catch (SignatureException var5) {
         var5.printStackTrace();
      }

   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"session"});
   }

   protected void setupRequest(RestRequest request) {
      super.setupRequest(request);
      this.signRequest(request);
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.POST);
   }
}
