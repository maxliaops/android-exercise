package com.quickblox.auth;

import android.os.Bundle;
import com.quickblox.auth.model.QBSession;
import com.quickblox.auth.query.QueryCreateSession;
import com.quickblox.auth.query.QueryDeleteSession;
import com.quickblox.auth.query.QueryGetSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBRequestCanceler;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.model.QBUser;

public class QBAuth extends BaseService {

   public static QBRequestCanceler createSession(QBEntityCallback callback) {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession();
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBSession createSession() throws QBResponseException {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession();
      return (QBSession)query.perform((Bundle)null);
   }

   public static QBRequestCanceler createSession(QBUser user, QBEntityCallback callback) {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession(user);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBRequestCanceler createSession(String userLogin, String userPassword, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setLogin(userLogin);
      user.setPassword(userPassword);
      return createSession(user, callback);
   }

   public static QBSession createSession(QBUser user) throws QBResponseException {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession(user);
      return (QBSession)query.perform((Bundle)null);
   }

   public static QBSession createSession(String userLogin, String userPassword) throws QBResponseException {
      QBUser user = new QBUser();
      user.setLogin(userLogin);
      user.setPassword(userPassword);
      return createSession(user);
   }

   public static QBRequestCanceler createSessionByEmail(String userEmail, String userPassword, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setEmail(userEmail);
      user.setPassword(userPassword);
      return createSessionByEmail(user, callback);
   }

   public static QBRequestCanceler createSessionByEmail(QBUser user, QBEntityCallback callback) {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession(user);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBSession createSessionByEmail(QBUser user) throws QBResponseException {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession(user);
      return (QBSession)query.perform((Bundle)null);
   }

   public static QBSession createSessionByEmail(String userEmail, String userPassword) throws QBResponseException {
      QBUser user = new QBUser();
      user.setEmail(userEmail);
      user.setPassword(userPassword);
      return createSessionByEmail(user);
   }

   public static QBRequestCanceler createSessionUsingSocialProvider(String socialProvider, String accessToken, String accessTokenSecret, QBEntityCallback callback) {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession(socialProvider, accessToken, accessTokenSecret);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBSession createSessionUsingSocialProvider(String socialProvider, String accessToken, String accessTokenSecret) throws QBResponseException {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession(socialProvider, accessToken, accessTokenSecret);
      return (QBSession)query.perform((Bundle)null);
   }

   public static QBRequestCanceler createSessionUsingTwitterDigits(String xAuthServiceProvider, String xVerifyCredentialsAuthorization, QBEntityCallback callback) {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession(xAuthServiceProvider, xVerifyCredentialsAuthorization);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBSession createSessionUsingTwitterDigits(String xAuthServiceProvider, String xVerifyCredentialsAuthorization) throws QBResponseException {
      createBaseService();
      QueryCreateSession query = new QueryCreateSession(xAuthServiceProvider, xVerifyCredentialsAuthorization);
      return (QBSession)query.perform((Bundle)null);
   }

   public static QBRequestCanceler deleteSession(QBEntityCallback callback) {
      QueryDeleteSession query = new QueryDeleteSession();
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static void deleteSession() throws QBResponseException {
      QueryDeleteSession query = new QueryDeleteSession();
      query.perform((Bundle)null);
   }

   public static QBRequestCanceler getSession(QBEntityCallback callback) {
      QueryGetSession query = new QueryGetSession();
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBSession getSession() throws QBResponseException {
      QueryGetSession query = new QueryGetSession();
      return (QBSession)query.perform((Bundle)null);
   }
}
