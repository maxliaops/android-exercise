package com.quickblox.chat.query;

import android.text.TextUtils;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.query.QueryAbsDialog;
import com.quickblox.core.RestMethod;
import com.quickblox.core.request.GenericQueryRule;
import com.quickblox.core.request.QBRequestUpdateBuilder;
import com.quickblox.core.request.QueryRule;
import com.quickblox.core.rest.RestRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueryUpdateDialog extends QueryAbsDialog {

   private final QBDialog dialog;
   private final QBRequestUpdateBuilder dialogRequestBuilder;


   public QueryUpdateDialog(QBDialog dialog, QBRequestUpdateBuilder dialogRequestBuilder) {
      this.dialog = dialog;
      this.dialogRequestBuilder = dialogRequestBuilder;
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      if(this.dialog.getName() != null) {
         parameters.put("name", this.dialog.getName());
      }

      if(this.dialog.getPhoto() != null) {
         parameters.put("photo", this.dialog.getPhoto());
      }

      if(this.dialogRequestBuilder != null) {
         Iterator customData = this.dialogRequestBuilder.getRules().iterator();

         while(customData.hasNext()) {
            GenericQueryRule i$ = (GenericQueryRule)customData.next();
            QueryRule key = (QueryRule)i$;
            String value = key.getRulesAsRequestParamUpdateQuery();
            String value1 = key.getRuleAsRequestValue();
            Object existentValue = parameters.get(value);
            if(existentValue != null) {
               parameters.put(value, existentValue + "," + value1);
            } else {
               parameters.put(value, value1);
            }
         }
      }

      Object customData1 = null;
      if(this.dialog.getCustomData() != null) {
         customData1 = this.dialog.getCustomData().getFields();
      } else if(this.dialog.getData() != null) {
         customData1 = this.dialog.getData();
      }

      if(customData1 != null) {
         Iterator i$1 = ((Map)customData1).keySet().iterator();

         while(i$1.hasNext()) {
            String key1 = (String)i$1.next();
            Object value2 = ((Map)customData1).get(key1);
            if(value2 instanceof List) {
               this.putValue(parameters, key1, TextUtils.join(",", (List)value2));
            } else {
               this.putValue(parameters, key1, value2.toString());
            }
         }
      }

   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Dialog", this.dialog.getDialogId()});
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.PUT);
   }
}
