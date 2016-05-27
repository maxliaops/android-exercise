package com.quickblox.core.parser;

import com.qb.gson.Gson;
import com.quickblox.core.error.QBErrorPlain;
import com.quickblox.core.error.QBErrorWithArray;
import com.quickblox.core.error.QBErrorWithBase;
import com.quickblox.core.error.QBErrorWithCode;
import com.quickblox.core.error.QBErrorWithError;
import com.quickblox.core.error.QBIError;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.interfaces.QBErrorParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QBJsonErrorParser implements QBErrorParser {

   private static Map errorHandlers = new HashMap();


   public List parseError(String responseBody) throws QBResponseException {
      ArrayList errors = new ArrayList();

      try {
         JSONObject e = new JSONObject(responseBody);
         Iterator i$ = errorHandlers.entrySet().iterator();

         while(i$.hasNext()) {
            Entry handler = (Entry)i$.next();
            if(e.has((String)handler.getKey())) {
               boolean preCheckParseErrors = this.preCheckParseErrors((String)handler.getKey(), e, errors);
               if(!preCheckParseErrors) {
                  Gson gson = new Gson();
                  QBIError fromJson = (QBIError)gson.fromJson(responseBody, (Class)handler.getValue());
                  if(fromJson != null) {
                     Iterator i$1 = fromJson.getErrors().iterator();

                     while(i$1.hasNext()) {
                        String e1 = (String)i$1.next();
                        errors.add(0, e1);
                     }
                  }
               }
               break;
            }
         }

         return errors;
      } catch (JSONException var11) {
         throw new QBResponseException(var11.getLocalizedMessage());
      }
   }

   public void addErrorSerializer(String error, Class clazz) {
      errorHandlers.put(error, clazz);
   }

   protected boolean preCheckParseErrors(String error, JSONObject jResponse, List errors) {
      if("errors".equals(error)) {
         try {
            JSONObject e = jResponse.optJSONObject(error);
            if(e != null && e.length() > 0) {
               JSONArray fields = e.names();

               for(int i = 0; i < fields.length(); ++i) {
                  String fieldName = fields.get(i).toString();
                  JSONArray values = e.getJSONArray(fieldName);

                  for(int j = 0; j < values.length(); ++j) {
                     String value = values.getString(j);
                     errors.add(0, fieldName + " " + value);
                  }
               }

               return true;
            }
         } catch (JSONException var11) {
            ;
         }
      }

      return false;
   }

   static {
      errorHandlers.put("error", QBErrorWithError.class);
      errorHandlers.put("errors", QBErrorWithArray.class);
      errorHandlers.put("code", QBErrorWithCode.class);
      errorHandlers.put("base", QBErrorWithBase.class);
      errorHandlers.put("onError", QBErrorPlain.class);
   }
}
