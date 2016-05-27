package com.quickblox.chat.model;

import com.qb.gson.JsonDeserializationContext;
import com.qb.gson.JsonDeserializer;
import com.qb.gson.JsonElement;
import com.qb.gson.JsonParseException;
import com.quickblox.chat.model.QBDialogCustomData;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QBDialogCustomDataDeserializer implements JsonDeserializer {

   public QBDialogCustomData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      QBDialogCustomData qbDialogCustomData = new QBDialogCustomData();

      try {
         JSONObject e = new JSONObject(json.toString());
         Iterator keys = e.keys();

         while(keys.hasNext()) {
            String key = (String)keys.next();
            Object value = e.get(key);
            if("class_name".equals(key)) {
               qbDialogCustomData.setClassName(String.valueOf(value));
            } else if(value instanceof JSONArray) {
               JSONArray valueArray = (JSONArray)value;
               this.parseArrayValue(valueArray, qbDialogCustomData, key);
            } else {
               if(value.equals("null")) {
                  value = null;
               }

               qbDialogCustomData.getFields().put(key, value);
            }
         }
      } catch (JSONException var10) {
         var10.printStackTrace();
      }

      return qbDialogCustomData;
   }

   private boolean parseArrayValue(JSONArray values, QBDialogCustomData qbDialogCustomData, String key) {
      ArrayList vl = new ArrayList();

      for(int i = 0; i < values.length(); ++i) {
         try {
            vl.add(values.get(i));
         } catch (JSONException var7) {
            var7.printStackTrace();
         }
      }

      qbDialogCustomData.getFields().put(key, vl);
      return true;
   }
}
