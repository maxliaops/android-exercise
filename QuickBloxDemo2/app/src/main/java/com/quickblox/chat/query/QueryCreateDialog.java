package com.quickblox.chat.query;

import android.text.TextUtils;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.query.QueryAbsDialog;
import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.RestRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueryCreateDialog extends QueryAbsDialog {

   private QBDialog dialog;


   public QueryCreateDialog(QBDialog dialog) {
      this.dialog = dialog;
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      if(this.dialog != null) {
         if(this.dialog.getName() != null) {
            parameters.put("name", this.dialog.getName());
         }

         if(this.dialog.getPhoto() != null) {
            parameters.put("photo", this.dialog.getPhoto());
         }

         parameters.put("type", Integer.valueOf(this.dialog.getType().getCode()));
         ArrayList occupants = this.dialog.getOccupants();
         if(occupants != null && occupants.size() > 0) {
            String customData = TextUtils.join(",", occupants);
            parameters.put("occupants_ids", customData);
         }

         Object customData1 = null;
         if(this.dialog.getCustomData() != null) {
            customData1 = this.dialog.getCustomData().getFields();
         } else if(this.dialog.getData() != null) {
            customData1 = this.dialog.getData();
         }

         if(customData1 != null) {
            Iterator i$ = ((Map)customData1).keySet().iterator();

            while(i$.hasNext()) {
               String key = (String)i$.next();
               Object value = ((Map)customData1).get(key);
               if(value instanceof List) {
                  this.putValue(parameters, key, TextUtils.join(",", (List)value));
               } else {
                  this.putValue(parameters, key, value.toString());
               }
            }
         }
      }

   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.POST);
   }
}
