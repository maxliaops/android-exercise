package com.quickblox.users.query;

import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.deserializer.QBStringifyArrayListDeserializer;
import com.quickblox.users.model.QBUser;
import com.quickblox.users.model.QBUserWrap;
import com.quickblox.users.parsers.QBUserJsonParser;
import java.util.Map;

public abstract class QueryBaseCreateUser extends JsonQuery {

   protected QBUser user;


   public QueryBaseCreateUser(QBUser user) {
      this.user = user;
      this.setOriginalObject(user);
      QBUserJsonParser qbUserJsonParser = new QBUserJsonParser(this);
      qbUserJsonParser.setDeserializer(QBUserWrap.class);
      qbUserJsonParser.putJsonTypeAdapter(StringifyArrayList.class, new QBStringifyArrayListDeserializer());
      this.setParser(qbUserJsonParser);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"users"});
   }

   protected void setParams(RestRequest request) {
      Map parametersMap = request.getParameters();
      this.putValue(parametersMap, "user[login]", this.user.getLogin());
      this.putValue(parametersMap, "user[blob_id]", this.user.getFileId());
      this.putValue(parametersMap, "user[email]", this.user.getEmail());
      this.putValue(parametersMap, "user[external_user_id]", this.user.getExternalId());
      this.putValue(parametersMap, "user[facebook_id]", this.user.getFacebookId());
      this.putValue(parametersMap, "user[twitter_id]", this.user.getTwitterId());
      this.putValue(parametersMap, "user[twitter_digits_id]", this.user.getTwitterDigitsId());
      this.putValue(parametersMap, "user[full_name]", this.user.getFullName());
      this.putValue(parametersMap, "user[phone]", this.user.getPhone());
      this.putValue(parametersMap, "user[website]", this.user.getWebsite());
      this.putValue(parametersMap, "user[custom_data]", this.user.getCustomData());
      this.putValue(parametersMap, "user[tag_list]", this.user.getTags().getItemsAsString());
   }
}
