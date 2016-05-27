package com.quickblox.users.query;

import com.quickblox.auth.model.QBProvider;
import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.deserializer.QBStringifyArrayListDeserializer;
import com.quickblox.users.model.QBUser;
import com.quickblox.users.model.QBUserWrap;
import java.util.Map;

public class QuerySignIn extends JsonQuery {

   private QBUser user;
   private String socialProvider;
   private String accessToken;
   private String accessTokenSecret;
   private String xAuthServiceProvider;
   private String xVerifyCredentialsAuthorization;


   private QuerySignIn() {
      this.getParser().initParser(QBUserWrap.class, StringifyArrayList.class, new QBStringifyArrayListDeserializer());
   }

   public QuerySignIn(String socialProvider, String accessToken, String accessTokenSecret) {
      this();
      this.socialProvider = socialProvider;
      this.accessToken = accessToken;
      this.accessTokenSecret = accessTokenSecret;
   }

   public QuerySignIn(QBUser user) {
      this();
      this.user = user;
      this.setOriginalObject(user);
   }

   public QuerySignIn(String xAuthServiceProvider, String xVerifyCredentialsAuthorization) {
      this();
      this.xAuthServiceProvider = xAuthServiceProvider;
      this.xVerifyCredentialsAuthorization = xVerifyCredentialsAuthorization;
      this.socialProvider = QBProvider.TWITTER_DIGITS;
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"login"});
   }

   public void setParams(RestRequest request) {
      Map parametersMap = request.getParameters();
      if(this.user != null) {
         this.putValue(parametersMap, "login", this.user.getLogin());
         this.putValue(parametersMap, "email", this.user.getEmail());
         this.putValue(parametersMap, "password", this.user.getPassword());
      } else if(this.socialProvider.equals(QBProvider.TWITTER_DIGITS)) {
         this.putValue(parametersMap, "provider", this.socialProvider);
         this.putValue(parametersMap, "twitter_digits[X-Auth-Service-Provider]", this.xAuthServiceProvider);
         this.putValue(parametersMap, "twitter_digits[X-Verify-Credentials-Authorization]", this.xVerifyCredentialsAuthorization);
      } else {
         this.putValue(parametersMap, "provider", this.socialProvider);
         this.putValue(parametersMap, "keys[token]", this.accessToken);
         if(this.socialProvider.equals(QBProvider.TWITTER)) {
            this.putValue(parametersMap, "keys[secret]", this.accessTokenSecret);
         }
      }

   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.POST);
   }

   public QBUser getUser() {
      return this.user;
   }

   public void setUser(QBUser user) {
      this.user = user;
   }
}
