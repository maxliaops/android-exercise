package com.quickblox.core.request;

import com.quickblox.core.helper.ToStringHelper;
import com.quickblox.core.request.QBLimitedRequestBuilder;
import com.quickblox.core.request.QueryRule;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class QBRequestGetBuilder extends QBLimitedRequestBuilder {

   public QBRequestGetBuilder addRule(String fieldName, String rule, Object value) {
      this.rules.add(new QueryRule(fieldName, rule, value));
      return this;
   }

   public QBRequestGetBuilder lt(String fieldName, Object value) {
      this.addRule(fieldName, "[lt]", value);
      return this;
   }

   public QBRequestGetBuilder lte(String fieldName, Object value) {
      this.addRule(fieldName, "[lte]", value);
      return this;
   }

   public QBRequestGetBuilder gt(String fieldName, Object value) {
      this.addRule(fieldName, "[gt]", value);
      return this;
   }

   public QBRequestGetBuilder gte(String fieldName, Object value) {
      this.addRule(fieldName, "[gte]", value);
      return this;
   }

   public QBRequestGetBuilder all(String fieldName, Object ... values) {
      this.addRule(fieldName, "[all]", this.arrayToString(values));
      return this;
   }

   public QBRequestGetBuilder in(String fieldName, Object ... values) {
      this.addRule(fieldName, "[in]", this.arrayToString(values));
      return this;
   }

   public QBRequestGetBuilder nin(String fieldName, Object ... values) {
      this.addRule(fieldName, "[nin]", this.arrayToString(values));
      return this;
   }

   public QBRequestGetBuilder or(String fieldName, Object ... values) {
      this.addRule(fieldName, "[or]", this.arrayToString(values));
      return this;
   }

   public QBRequestGetBuilder ctn(String fieldName, Object value) {
      this.addRule(fieldName, "[ctn]", value);
      return this;
   }

   public QBRequestGetBuilder ne(String fieldName, Object value) {
      this.addRule(fieldName, "[ne]", value);
      return this;
   }

   public QBRequestGetBuilder eq(String fieldName, Object value) {
      this.addRule(fieldName, "eq", value);
      return this;
   }

   public QBRequestGetBuilder count() {
      this.addRule("count", "count", "1");
      return this;
   }

   public QBRequestGetBuilder sortAsc(String fieldName) {
      this.addRule("sort_asc", "", fieldName);
      return this;
   }

   public QBRequestGetBuilder sortDesc(String fieldName) {
      this.addRule("sort_desc", "", fieldName);
      return this;
   }

   @Deprecated
   public QBRequestGetBuilder output(List values) {
      this.addRule("output[include]", "", ToStringHelper.toString(values, ","));
      return this;
   }

   public QBRequestGetBuilder outputInclude(List values) {
      this.addRule("output[include]", "", ToStringHelper.toString(values, ","));
      return this;
   }

   public QBRequestGetBuilder outputExclude(List values) {
      this.addRule("output[exclude]", "", ToStringHelper.toString(values, ","));
      return this;
   }

   public QBRequestGetBuilder near(String fieldName, Double[] location, int radius) {
      double longitude = location[0].doubleValue();
      double latitude = location[1].doubleValue();
      this.addRule(fieldName + "[near]", "", longitude + "," + latitude + ";" + radius);
      return this;
   }

   protected String arrayToString(Object ... values) {
      StringBuilder arrayString = new StringBuilder();
      String delimiter = "";

      for(Iterator i$ = Arrays.asList(values).iterator(); i$.hasNext(); delimiter = ",") {
         Object obj = i$.next();
         arrayString.append(delimiter);
         arrayString.append(obj);
      }

      return arrayString.toString().replace("[", "").replace("]", "");
   }
}
