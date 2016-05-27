package com.quickblox.core.server;

import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.BaseServiceException;
import java.util.Date;
import java.util.TimeZone;

public class BaseService {

   private static BaseService baseService;
   private String token;
   private Date tokenExpirationDate;


   public static synchronized BaseService getBaseService() throws BaseServiceException {
      if(baseService == null) {
         throw new BaseServiceException("\nYou have missed the authorization call.\nPlease insert following code inside your application:\n    QBAuth.createSession(new QBEntityCallback() { ... });\nbefore any other code, that uses our service. Thank you.");
      } else {
         return baseService;
      }
   }

   protected static void createBaseService() {
      if(baseService == null) {
         QBSettings.getInstance().checkInit();
         baseService = new BaseService();
      }

   }

   public static BaseService createFromExistentToken(String token, Date tokenExpirationDate) throws BaseServiceException {
      if(token != null && tokenExpirationDate != null) {
         BaseService service = null;

         try {
            service = getBaseService();
         } catch (BaseServiceException var4) {
            ;
         }

         if(service == null) {
            createBaseService();
            service = baseService;
         }

         if(service != null) {
            service.setToken(token);
            service.setTokenExpirationDate(tokenExpirationDate);
         }

         return service;
      } else {
         return getBaseService();
      }
   }

   public void resetCredentials() {
      this.token = null;
      this.tokenExpirationDate = null;
   }

   public String getToken() {
      return this.token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public Date getTokenExpirationDate() {
      return this.tokenExpirationDate;
   }

   public void setTokenExpirationDate(Date tokenExpirationDate) {
      int offset = TimeZone.getDefault().getOffset(System.currentTimeMillis());
      this.tokenExpirationDate = new Date(tokenExpirationDate.getTime() + (long)offset);
   }
}
