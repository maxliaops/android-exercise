package com.quickblox.users.query;

import android.text.TextUtils;
import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.query.PagedQuery;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.deserializer.QBStringifyArrayListDeserializer;
import com.quickblox.users.model.QBUserPaged;
import java.util.Collection;
import java.util.Map;

public class QueryGetUsers extends PagedQuery {

   private String fullName;
   private Collection tags;


   public QueryGetUsers(QBPagedRequestBuilder requestBuilder) {
      this.setRequestBuilder(requestBuilder);
      this.getParser().initParser(QBUserPaged.class, StringifyArrayList.class, new QBStringifyArrayListDeserializer());
   }

   public QueryGetUsers(Collection tags, QBPagedRequestBuilder requestBuilder) {
      this(requestBuilder);
      this.tags = tags;
   }

   public QueryGetUsers(String fullName, QBPagedRequestBuilder requestBuilder) {
      this(requestBuilder);
      this.fullName = fullName;
   }

   public String getUrl() {
      return this.fullName != null?this.buildQueryUrl(new Object[]{"users", "by_full_name"}):(this.tags != null?this.buildQueryUrl(new Object[]{"users", "by_tags"}):this.buildQueryUrl(new Object[]{"users"}));
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parametersMap = request.getParameters();
      String tagsString = null;
      if(this.tags != null && !this.tags.isEmpty()) {
         tagsString = TextUtils.join(",", this.tags);
      }

      this.putValue(parametersMap, "tags", tagsString);
      this.putValue(parametersMap, "full_name", this.fullName);
   }

   public String getFullName() {
      return this.fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }
}
