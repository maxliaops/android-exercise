package com.quickblox.core.request;

import android.text.TextUtils;
import com.quickblox.core.request.GenericQueryRule;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class QBRequestBuilder {

   protected ArrayList rules = new ArrayList();


   protected String arrayToString(Object ... values) {
      return TextUtils.join(",", values);
   }

   protected void addRule(String param, Object value) {
      this.rules.add(new GenericQueryRule(param, value));
   }

   public void fillParametersMap(Map parametersMap) {
      Iterator i$ = this.rules.iterator();

      while(i$.hasNext()) {
         GenericQueryRule qr = (GenericQueryRule)i$.next();
         parametersMap.put(qr.getParamName(), qr.getParamValue());
      }

   }

   public ArrayList getRules() {
      return this.rules;
   }

   public void setRules(ArrayList rules) {
      this.rules = rules;
   }

   public String toString() {
      return "QBRequestBuilder{rules=" + this.rules + '}';
   }
}
