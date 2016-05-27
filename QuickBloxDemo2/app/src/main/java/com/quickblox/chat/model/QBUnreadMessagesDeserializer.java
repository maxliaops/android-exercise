package com.quickblox.chat.model;

import com.qb.gson.JsonDeserializationContext;
import com.qb.gson.JsonDeserializer;
import com.qb.gson.JsonElement;
import com.qb.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class QBUnreadMessagesDeserializer implements JsonDeserializer {

   public Map deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      HashMap unreadByDialogs = new HashMap();

      try {
         JSONObject e = new JSONObject(json.toString());
         Iterator keys = e.keys();

         while(keys.hasNext()) {
            String key = (String)keys.next();
            Object value = e.get(key);
            unreadByDialogs.put(key, (Integer)value);
         }
      } catch (JSONException var9) {
         var9.printStackTrace();
      }

      return unreadByDialogs;
   }
}
