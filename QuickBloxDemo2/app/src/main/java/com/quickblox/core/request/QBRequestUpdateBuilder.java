package com.quickblox.core.request;

import com.quickblox.core.request.QBLimitedRequestBuilder;
import com.quickblox.core.request.QueryRule;

public class QBRequestUpdateBuilder extends QBLimitedRequestBuilder {

   public QBRequestUpdateBuilder addRule(String fieldName, String rule, Object value) {
      this.rules.add(new QueryRule(fieldName, rule, value));
      return this;
   }

   public QBRequestUpdateBuilder inc(String fieldName, Object value) {
      this.addRule(fieldName, "inc", value);
      return this;
   }

   public QBRequestUpdateBuilder pull(String fieldName, Object value) {
      this.addRule(fieldName, "pull", value);
      return this;
   }

   public QBRequestUpdateBuilder pullAll(String fieldName, Object ... values) {
      Object[] arr$ = values;
      int len$ = values.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object value = arr$[i$];
         this.addRule(fieldName, "pull_all", value);
      }

      return this;
   }

   public QBRequestUpdateBuilder pullWithFilter(String fieldName, String filterOperator, Object value) {
      this.addRule("pull[" + fieldName + "]" + "[" + filterOperator + "]", "pull_filter", value);
      return this;
   }

   public QBRequestUpdateBuilder pop(String fieldName, Object value) {
      this.addRule(fieldName, "pop", value);
      return this;
   }

   public QBRequestUpdateBuilder push(String fieldName, Object value) {
      this.addRule(fieldName, "push", value);
      return this;
   }

   @Deprecated
   public QBRequestUpdateBuilder push(String fieldName, Object ... values) {
      return this.pushAll(fieldName, values);
   }

   public QBRequestUpdateBuilder pushAll(String fieldName, Object ... values) {
      Object[] arr$ = values;
      int len$ = values.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object value = arr$[i$];
         this.addRule(fieldName, "push_all", value);
      }

      return this;
   }

   public QBRequestUpdateBuilder addToSet(String fieldName, Object value) {
      this.addRule(fieldName, "add_to_set", value);
      return this;
   }

   public QBRequestUpdateBuilder updateArrayValue(String fieldName, int index, Object value) {
      this.addRule(String.valueOf(index), fieldName, value);
      return this;
   }
}
