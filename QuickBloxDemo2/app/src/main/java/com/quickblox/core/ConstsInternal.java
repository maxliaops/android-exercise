package com.quickblox.core;


public class ConstsInternal {

   public static final String DEFAULT_ENCODING = "UTF-8";
   public static final String REQUEST_FORMAT = ".json";
   public static final String XML_PREFIX = "<?xml";
   public static final String API_QB_DOMAIN = "https://api.quickblox.com";
   public static final String BLOBS_SERVER_NAME = "qbprod";
   public static final String REST_API_VERSION = "0.1.1";
   public static final String CHAT_SERVER_DOMAIN = "chat.quickblox.com";
   public static final String HEADER_API_VERSION = "QuickBlox-REST-API-Version";
   public static final String HEADER_TOKEN = "QB-Token";
   public static final String HEADER_TOKEN_EXPIRATION_DATE = "QB-Token-ExpirationDate";
   public static final String HEADER_FRAMEWORK_VERSION = "QB-SDK";
   public static final String HEADER_FRAMEWORK_VERSION_VALUE_PREFIX = "Android";
   public static final String EXCEPTION_MISSED_AUTHORIZATION = "\nYou have missed the authorization call.\nPlease insert following code inside your application:\n    QBAuth.createSession(new QBEntityCallback() { ... });\nbefore any other code, that uses our service. Thank you.";
   public static final String EXCEPTION_INVALID_ACCOUNT_KEY = "\nSomething wrong with your Account Key. Please check it in QuickBlox Admin Panel (https://admin.quickblox.com/account), Settings tab.";
   public static final String EXCEPTION_CONNECTION_FAILED = "Connection failed. Please check your internet connection.";
   public static final String EXCEPTION_PARSER_NOT_SPECIFIED = "Response parser was not specified";
   public static final String BASE_SERVICE_ERROR_TIMEOUT = "Connection closed due to timeout. Please check your internet connection.";
   public static final String BASE_SERVICE_ERROR_NOT_FOUND = "Entity you are looking for was not found.";
   public static final String BASE_SERVICE_QB_SERVER_ERROR = "We\'re sorry, but something went wrong. We\'ve been notified about this issue and we\'ll take a look at it shortly.";
   public static final String EMPTY_STRING = "";
   public static final String TOKEN_EXPIRATION_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   public static final String ERRORS_MSG = "errors";
   public static final String ERROR_MSG = "error";
   public static final String ON_ERROR_MSG = "onError";
   public static final String ERROR_CODE_MSG = "code";
   public static final String ERROR_BASE_MSG = "base";
   public static final int UNDEFINED_STATUS_CODE = -1;


}
