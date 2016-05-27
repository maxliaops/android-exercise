package com.quickblox.core.request;

import com.quickblox.core.RestMethod;
import com.quickblox.core.request.QueryRule;

public class QueryMultiRequestRule extends QueryRule {

   private final String rule;
   Object[] values;
   private RestMethod restMethod;


   public QueryMultiRequestRule(String paramName, String rule, Object ... values) {
      super(paramName, rule, values[0]);
      this.rule = rule;
      this.values = values;
   }

   public String getRuleAsRequestParamGetQuery() {
      this.restMethod = RestMethod.GET;
      return super.getRuleAsRequestParamGetQuery();
   }

   public String getRuleAsRequestValue() {
      StringBuilder stringBuilder = new StringBuilder();
      Object[] lastIndexOfAnd = this.values;
      int len$ = lastIndexOfAnd.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object value = lastIndexOfAnd[i$];
         stringBuilder.append(value.toString()).append("&").append(this.getRulesAsRequestParamUpdateQuery()).append("=");
      }

      int var6 = stringBuilder.lastIndexOf("&");
      return var6 != -1?stringBuilder.substring(0, var6):stringBuilder.toString();
   }
}
