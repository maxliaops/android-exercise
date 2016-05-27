package com.quickblox.chat.errors;


public final class QBChatErrorsConstants {

   public static final String CHAT_NOT_INITIALIZED = "Chat is not initialized";
   public static final String CHAT_ALREADY_INITIALIZED = "Chat Service is already initialized";
   public static final String ILLEGAL_INITIALIZE_ARGUMENT = "Proxy can\'t be null, use ProxyInfo.forDefaultProxy() instead";
   public static final String NOT_LOGGED_IN = "You have not logged in";
   public static final String ALREADY_LOGGED_IN = "You have already logged in chat";
   public static final String ILLEGAL_LOGIN_ARGUMENT = "User\'s id and password can\'t be null";
   public static final String CONNECTION_FAILED = "Connection failed. Please check your internet connection.";
   public static final String NOT_CONNECTED = "You have not connected";
   public static final String AUTHENTICATION_FAILED = "Authentication failed, check user\'s ID and password";
   public static final String NOT_JOINED_ROOM = "You have not joined chat room!";
   public static final String ERROR_ON_JOIN = "Error occurred while joining to room";
   public static final String ILLEGAL_ROOM_NAME = "Room name and jid can\'t be null or empty";
   public static final String NOT_CREATED_ROOM = "Room wasn\'t created";
   public static final String INCORRECT_PRESENCE_TYPE = "Incorrect presence type";
   public static final String RECIPIENT_ID_IS_NULL = "Recipient ID is null";
   public static final String PING_FAILED = "Server is unavailable";
   public static final String SENDER_ID_NULL = "SenderId is null";
   public static final String ID_NULL = "Id is null";


}
