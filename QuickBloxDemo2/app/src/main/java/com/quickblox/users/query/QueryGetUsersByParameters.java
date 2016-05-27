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

public class QueryGetUsersByParameters extends PagedQuery {

   Collection usersParams;
   private String filterString;


   public QueryGetUsersByParameters(Collection usersParams, String requestedParam, QBPagedRequestBuilder requestBuilder) {
      this.usersParams = usersParams;
      this.requestBuilder = requestBuilder;
      this.filterString = requestedParam;
      this.getParser().initParser(QBUserPaged.class, StringifyArrayList.class, new QBStringifyArrayListDeserializer());
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parametersMap = request.getParameters();
      this.putValue(parametersMap, "filter[]", this.getParameterString() + this.getParamsAsString());
   }

   protected String getParameterString() {
      return this.filterString;
   }

   protected String getParamsAsString() {
      String usersParamsString = null;
      if(this.usersParams != null && !this.usersParams.isEmpty()) {
         usersParamsString = TextUtils.join(",", this.usersParams);
      }

      return usersParamsString;
   }

   protected String getUrl() {
      return this.buildQueryUrl(new Object[]{"users"});
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }
}
