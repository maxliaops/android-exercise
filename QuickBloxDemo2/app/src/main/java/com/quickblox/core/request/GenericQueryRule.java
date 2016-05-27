package com.quickblox.core.request;


public class GenericQueryRule {

   public static final String CURRENT_PAGE = "page";
   public static final String PER_PAGE = "per_page";
   protected String paramName;
   protected Object value;


   public GenericQueryRule(String paramName, Object value) {
      this.paramName = paramName;
      this.value = value;
   }

   public String getParamName() {
      return this.paramName;
   }

   public void setParamName(String paramName) {
      this.paramName = paramName;
   }

   public Object getValue() {
      return this.value;
   }

   public String getParamValue() {
      return this.value.toString();
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public String getRuleString() {
      return String.format("%s=%s", new Object[]{this.getParamName(), this.getValue()});
   }

   public String toString() {
      return "GenericQueryRule{paramName=\'" + this.paramName + '\'' + ", value=" + this.value + '}';
   }
}
