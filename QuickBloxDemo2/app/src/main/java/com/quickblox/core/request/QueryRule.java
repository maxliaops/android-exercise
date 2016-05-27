package com.quickblox.core.request;

import com.quickblox.core.request.GenericQueryRule;

public class QueryRule extends GenericQueryRule {

   public static final String EQ = "eq";
   public static final String OR = "[or]";
   public static final String CTN = "[ctn]";
   public static final String COUNT = "count";
   public static final String LT = "[lt]";
   public static final String LTE = "[lte]";
   public static final String GT = "[gt]";
   public static final String GTE = "[gte]";
   public static final String NE = "[ne]";
   public static final String IN = "[in]";
   public static final String ALL = "[all]";
   public static final String NIN = "[nin]";
   public static final String INCLUDE = "[include]";
   public static final String EXCLUDE = "[exclude]";
   public static final String NEAR = "[near]";
   public static final String SORT_ASC = "sort_asc";
   public static final String SORT_DESC = "sort_desc";
   public static final String OUTPUT = "output";
   public static final String MYLOCATION = "mylocation";
   public static final String INC = "inc";
   public static final String PULL = "pull";
   public static final String PULL_WITH_FILTER = "pull_filter";
   public static final String PULL_ALL = "pull_all";
   public static final String POP = "pop";
   public static final String PUSH = "push";
   public static final String PUSH_ALL = "push_all";
   public static final String ADD_TO_SET = "add_to_set";
   private String rule;
   private String queryType;


   public QueryRule(String paramName, String rule, Object value) {
      super(paramName, value);
      this.rule = rule;
   }

   public String getRule() {
      return this.rule;
   }

   public void setRule(String rule) {
      this.rule = rule;
   }

   public String getRuleAsRequestParamGetQuery() {
      return !this.rule.equals("eq") && !this.rule.equals("count")?this.paramName + this.rule:this.paramName;
   }

   public String getRulesAsRequestParamUpdateQuery() {
      return this.rule.equals("pull_filter")?this.paramName:(this.rule.equals("eq")?this.paramName:(!this.rule.equals("push") && !this.rule.equals("pull") && !this.rule.equals("push_all") && !this.rule.equals("pull_all")?this.rule + "[" + this.paramName + "]".replace(" ", ""):this.rule + "[" + this.paramName + "][]"));
   }

   public String getRuleAsRequestValue() {
      return this.value == null?"":this.value.toString();
   }

   public String toString() {
      return "QueryRule{rule=\'" + this.rule + '\'' + ", paramName=\'" + this.paramName + '\'' + ", value=" + this.value + '}';
   }
}
