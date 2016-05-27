package com.quickblox.users.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.deserializer.QBStringifyArrayListDeserializer;
import com.quickblox.users.model.QBUser;
import com.quickblox.users.model.QBUserWrap;
import java.util.Map;

public class QueryGetUser extends JsonQuery {

   private QBUser user;
   private Integer filterType;
   public static final int BY_LOGIN = 0;
   public static final int BY_EMAIL = 1;
   public static final int BY_FACEBOOK_ID = 2;
   public static final int BY_TWITTER_ID = 3;
   public static final int BY_EXTERNAL_USER_ID = 4;
   public static final int BY_TWITTER_DIGITS_ID = 5;
   private static String[] filters = new String[]{"login", "email", "facebook_id", "twitter_id", "external", "twitter_digits_id"};


   public QueryGetUser(QBUser user) {
      this.getParser().initParser(QBUserWrap.class, StringifyArrayList.class, new QBStringifyArrayListDeserializer());
      this.user = user;
      this.setOriginalObject(user);
   }

   public QueryGetUser(QBUser user, int filterType) {
      this(user);
      this.filterType = Integer.valueOf(filterType);
   }

   protected void setParams(RestRequest request) {
      if(this.filterType != null && this.filterType.intValue() != 4) {
         String value;
         switch(this.filterType.intValue()) {
         case 0:
            value = this.user.getLogin();
            break;
         case 1:
            value = this.user.getEmail();
            break;
         case 2:
            value = this.user.getFacebookId().toString();
            break;
         case 3:
            value = this.user.getTwitterId().toString();
            break;
         case 4:
         default:
            value = null;
            break;
         case 5:
            value = this.user.getTwitterDigitsId().toString();
         }

         Map parametersMap = request.getParameters();
         this.putValue(parametersMap, filters[this.filterType.intValue()], value);
      }

   }

   protected String getUrl() {
      if(this.filterType != null) {
         if(this.filterType.intValue() != 4) {
            String filterString = "by_" + filters[this.filterType.intValue()];
            return this.buildQueryUrl(new Object[]{"users", filterString});
         } else {
            return this.buildQueryUrl(new Object[]{"users", "external", this.user.getExternalId()});
         }
      } else {
         return this.buildQueryUrl(new Object[]{"users", this.user.getId()});
      }
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

}
