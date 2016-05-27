package com.quickblox.users;

import android.os.Bundle;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBRequestCanceler;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.model.QBUser;
import com.quickblox.users.query.QueryCreateUser;
import com.quickblox.users.query.QueryDeleteUser;
import com.quickblox.users.query.QueryGetUser;
import com.quickblox.users.query.QueryGetUsers;
import com.quickblox.users.query.QueryGetUsersByParameters;
import com.quickblox.users.query.QueryResetPassword;
import com.quickblox.users.query.QuerySignIn;
import com.quickblox.users.query.QuerySignOut;
import com.quickblox.users.query.QueryUpdateUser;
import com.quickblox.users.task.TaskEntitySignUpSignIn;
import com.quickblox.users.task.TaskSyncSignupSignIn;
import java.util.ArrayList;
import java.util.Collection;

public class QBUsers extends BaseService {

   private static final String FILTER_BY_IDS = "number id in ";
   private static final String FILTER_BY_EMAILS = "string email in ";
   private static final String FILTER_BY_LOGINS = "string login in ";
   private static final String FILTER_BY_PHONES = "string phone in ";
   private static final String FILTER_BY_FB_ID = "string facebook_id in ";
   private static final String FILTER_BY_TWITTER_ID = "string twitter_id in ";
   private static final String FILTER_BY_TWITTER_DIGITS_ID = "string twitter_digits_id in ";


   public static QBRequestCanceler signIn(QBUser user, QBEntityCallback callback) {
      QuerySignIn query = new QuerySignIn(user);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBUser signIn(QBUser user) throws QBResponseException {
      QuerySignIn query = new QuerySignIn(user);
      return (QBUser)query.perform((Bundle)null);
   }

   public static QBRequestCanceler signIn(String login, String password, QBEntityCallback callback) {
      QBUser user = new QBUser(login, password);
      QuerySignIn query = new QuerySignIn(user);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBUser signIn(String login, String password) throws QBResponseException {
      QBUser user = new QBUser(login, password);
      QuerySignIn query = new QuerySignIn(user);
      return (QBUser)query.perform((Bundle)null);
   }

   public static QBRequestCanceler signInByEmail(String email, String password, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setEmail(email);
      user.setPassword(password);
      return signIn(user, callback);
   }

   public static QBUser signInByEmail(String email, String password) throws QBResponseException {
      QBUser user = new QBUser();
      user.setEmail(email);
      user.setPassword(password);
      return signIn(user);
   }

   public static QBRequestCanceler signInUsingSocialProvider(String socialProvider, String accessToken, String accessTokenSecret, QBEntityCallback callback) {
      QuerySignIn query = new QuerySignIn(socialProvider, accessToken, accessTokenSecret);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBUser signInUsingSocialProvider(String socialProvider, String accessToken, String accessTokenSecret) throws QBResponseException {
      QuerySignIn query = new QuerySignIn(socialProvider, accessToken, accessTokenSecret);
      return (QBUser)query.perform((Bundle)null);
   }

   public static QBRequestCanceler signInUsingTwitterDigits(String xAuthServiceProvider, String xVerifyCredentialsAuthorization, QBEntityCallback callback) {
      QuerySignIn query = new QuerySignIn(xAuthServiceProvider, xVerifyCredentialsAuthorization);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBUser signInUsingTwitterDigits(String xAuthServiceProvider, String xVerifyCredentialsAuthorization) throws QBResponseException {
      QuerySignIn query = new QuerySignIn(xAuthServiceProvider, xVerifyCredentialsAuthorization);
      return (QBUser)query.perform((Bundle)null);
   }

   public static QBRequestCanceler signOut(QBEntityCallback callback) {
      QuerySignOut query = new QuerySignOut();
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static void signOut() throws QBResponseException {
      QuerySignOut query = new QuerySignOut();
      query.perform((Bundle)null);
   }

   public static QBRequestCanceler signUp(QBUser user, QBEntityCallback callback) {
      QueryCreateUser query = new QueryCreateUser(user);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBUser signUp(QBUser user) throws QBResponseException {
      QueryCreateUser query = new QueryCreateUser(user);
      return (QBUser)query.perform((Bundle)null);
   }

   public static QBRequestCanceler signUpSignInTask(QBUser user, QBEntityCallback callback) {
      TaskEntitySignUpSignIn task = new TaskEntitySignUpSignIn(user, callback);
      return task.performTask();
   }

   public static QBUser signUpSignInTask(QBUser user) throws QBResponseException {
      TaskSyncSignupSignIn task = new TaskSyncSignupSignIn(user);
      return task.execute();
   }

   public static QBRequestCanceler getUsers(QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      QueryGetUsers query = new QueryGetUsers(requestBuilder);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static ArrayList getUsers(QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      QueryGetUsers query = new QueryGetUsers(requestBuilder);
      return (ArrayList)query.perform(returnedBundle);
   }

   public static QBRequestCanceler getUsersByIDs(Collection usersIDs, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      return getUsersByFilter(usersIDs, "number id in ", requestBuilder, callback);
   }

   public static ArrayList getUsersByIDs(Collection usersIDs, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      return getUsersByFilter(usersIDs, "number id in ", requestBuilder, returnedBundle);
   }

   public static QBRequestCanceler getUsersByFilter(Collection filterValue, String filter, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      QueryGetUsersByParameters query = new QueryGetUsersByParameters(filterValue, filter, requestBuilder);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static ArrayList getUsersByFilter(Collection filterValue, String filter, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      QueryGetUsersByParameters query = new QueryGetUsersByParameters(filterValue, filter, requestBuilder);
      return (ArrayList)query.perform(returnedBundle);
   }

   public static QBRequestCanceler getUsersByEmails(Collection usersEmails, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      return getUsersByFilter(usersEmails, "string email in ", requestBuilder, callback);
   }

   public static ArrayList getUsersByEmails(Collection usersEmails, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      return getUsersByFilter(usersEmails, "string email in ", requestBuilder, returnedBundle);
   }

   public static QBRequestCanceler getUsersByLogins(Collection usersLogins, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      return getUsersByFilter(usersLogins, "string login in ", requestBuilder, callback);
   }

   public static ArrayList getUsersByLogins(Collection usersLogins, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      return getUsersByFilter(usersLogins, "string login in ", requestBuilder, returnedBundle);
   }

   public static QBRequestCanceler getUsersByFacebookId(Collection usersFacebookIds, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      return getUsersByFilter(usersFacebookIds, "string facebook_id in ", requestBuilder, callback);
   }

   public static ArrayList getUsersByFacebookId(Collection usersFacebookIds, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      return getUsersByFilter(usersFacebookIds, "string facebook_id in ", requestBuilder, returnedBundle);
   }

   public static QBRequestCanceler getUsersByTwitterId(Collection usersTwitterIds, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      return getUsersByFilter(usersTwitterIds, "string twitter_id in ", requestBuilder, callback);
   }

   public static ArrayList getUsersByTwitterId(Collection usersTwitterIds, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      return getUsersByFilter(usersTwitterIds, "string twitter_id in ", requestBuilder, returnedBundle);
   }

   public static QBRequestCanceler getUsersByTwitterDigitsId(Collection usersTwitterDigitsIds, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      return getUsersByFilter(usersTwitterDigitsIds, "string twitter_digits_id in ", requestBuilder, callback);
   }

   public static ArrayList getUsersByTwitterDigitsId(Collection usersTwitterDigitsIds, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      return getUsersByFilter(usersTwitterDigitsIds, "string twitter_digits_id in ", requestBuilder, returnedBundle);
   }

   public static QBRequestCanceler getUsersByFullName(String fullName, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      QueryGetUsers query = new QueryGetUsers(fullName, requestBuilder);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static ArrayList getUsersByFullName(String fullName, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      QueryGetUsers query = new QueryGetUsers(fullName, requestBuilder);
      return (ArrayList)query.perform(returnedBundle);
   }

   public static QBRequestCanceler getUsersByTags(Collection tags, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      QueryGetUsers query = new QueryGetUsers(tags, requestBuilder);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static ArrayList getUsersByTags(Collection tags, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      QueryGetUsers query = new QueryGetUsers(tags, requestBuilder);
      return (ArrayList)query.perform(returnedBundle);
   }

   public static QBRequestCanceler getUsersByPhoneNumbers(Collection usersPhoneNumbers, QBPagedRequestBuilder requestBuilder, QBEntityCallback callback) {
      return getUsersByFilter(usersPhoneNumbers, "string phone in ", requestBuilder, callback);
   }

   public static ArrayList getUsersByPhoneNumbers(Collection usersPhoneNumbers, QBPagedRequestBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
      return getUsersByFilter(usersPhoneNumbers, "string phone in ", requestBuilder, returnedBundle);
   }

   public static QBRequestCanceler getUser(int id, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setId(id);
      QueryGetUser query = new QueryGetUser(user);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBUser getUser(int id) throws QBResponseException {
      QueryGetUser query = new QueryGetUser(new QBUser(Integer.valueOf(id)));
      return (QBUser)query.perform((Bundle)null);
   }

   public static QBRequestCanceler getUserByLogin(String login, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setLogin(login);
      return getFilteredUser(user, 0, callback);
   }

   public static QBUser getUserByLogin(String login) throws QBResponseException {
      QBUser user = new QBUser();
      user.setLogin(login);
      return getFilteredUser(user, 0);
   }

   public static QBRequestCanceler getUserByFacebookId(String facebookId, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setFacebookId(facebookId);
      return getFilteredUser(user, 2, callback);
   }

   public static QBUser getUserByFacebookId(String facebookId) throws QBResponseException {
      QBUser user = new QBUser();
      user.setFacebookId(facebookId);
      return getFilteredUser(user, 2);
   }

   public static QBRequestCanceler getUserByTwitterId(String twitterId, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setTwitterId(twitterId);
      return getFilteredUser(user, 3, callback);
   }

   public static QBUser getUserByTwitterId(String twitterId) throws QBResponseException {
      QBUser user = new QBUser();
      user.setTwitterId(twitterId);
      return getFilteredUser(user, 3);
   }

   public static QBRequestCanceler getUserByTwitterDigitsId(String twitterDigitsId, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setTwitterDigitsId(twitterDigitsId);
      return getFilteredUser(user, 5, callback);
   }

   public static QBUser getUserByTwitterDigitsId(String twitterDigitsId) throws QBResponseException {
      QBUser user = new QBUser();
      user.setTwitterDigitsId(twitterDigitsId);
      return getFilteredUser(user, 5);
   }

   public static QBRequestCanceler getUserByEmail(String email, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setEmail(email);
      return getFilteredUser(user, 1, callback);
   }

   public static QBUser getUserByEmail(String email) throws QBResponseException {
      QBUser user = new QBUser();
      user.setEmail(email);
      return getFilteredUser(user, 1);
   }

   public static QBRequestCanceler getUserByExternalId(String externalId, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setExternalId(externalId);
      return getFilteredUser(user, 4, callback);
   }

   public static QBUser getUserByExternalId(String externalId) throws QBResponseException {
      QBUser user = new QBUser();
      user.setExternalId(externalId);
      return getFilteredUser(user, 4);
   }

   public static QBRequestCanceler updateUser(QBUser user, QBEntityCallback callback) {
      QueryUpdateUser query = new QueryUpdateUser(user);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static QBUser updateUser(QBUser user) throws QBResponseException {
      QueryUpdateUser query = new QueryUpdateUser(user);
      return (QBUser)query.perform((Bundle)null);
   }

   public static QBRequestCanceler deleteUser(int userId, QBEntityCallback callback) {
      QueryDeleteUser query = new QueryDeleteUser(new QBUser(Integer.valueOf(userId)));
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static void deleteUser(int userId) throws QBResponseException {
      QueryDeleteUser query = new QueryDeleteUser(new QBUser(Integer.valueOf(userId)));
      query.perform((Bundle)null);
   }

   public static QBRequestCanceler deleteByExternalId(String externalId, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setExternalId(externalId);
      QueryDeleteUser query = new QueryDeleteUser(user, Integer.valueOf(0));
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static void deleteByExternalId(String externalId) throws QBResponseException {
      QBUser user = new QBUser();
      user.setExternalId(externalId);
      QueryDeleteUser query = new QueryDeleteUser(user, Integer.valueOf(0));
      query.perform((Bundle)null);
   }

   public static QBRequestCanceler resetPassword(String email, QBEntityCallback callback) {
      QBUser user = new QBUser();
      user.setEmail(email);
      QueryResetPassword query = new QueryResetPassword(user);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   public static void resetPassword(String email) throws QBResponseException {
      QBUser user = new QBUser();
      user.setEmail(email);
      QueryResetPassword query = new QueryResetPassword(user);
      query.perform((Bundle)null);
   }

   private static QBRequestCanceler getFilteredUser(QBUser user, int filterType, QBEntityCallback callback) {
      QueryGetUser query = new QueryGetUser(user, filterType);
      return new QBRequestCanceler(query.performAsyncWithCallback(callback));
   }

   private static QBUser getFilteredUser(QBUser user, int filterType) throws QBResponseException {
      QueryGetUser query = new QueryGetUser(user, filterType);
      return (QBUser)query.perform((Bundle)null);
   }
}
