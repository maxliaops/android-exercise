package com.quickblox.chat.model;

import com.qb.gson.Gson;
import com.qb.gson.GsonBuilder;
import com.qb.gson.JsonDeserializationContext;
import com.qb.gson.JsonDeserializer;
import com.qb.gson.JsonElement;
import com.qb.gson.JsonParseException;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatMessage;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QBChatMessageDeserializer implements JsonDeserializer {

   public QBChatMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      QBChatMessage message = new QBChatMessage();

      try {
         String e = jsonElement.toString();
         JSONObject jsonObject = new JSONObject(e);
         Iterator keys = jsonObject.keys();

         while(keys.hasNext()) {
            String key = (String)keys.next();
            String value = jsonObject.getString(key);
            if(key.equals("_id")) {
               message.setId(value);
            } else if(key.equals("chat_dialog_id")) {
               message.setDialogId(value);
            } else if(key.equals("date_sent")) {
               long var19 = Long.parseLong(value);
               message.setDateSent(var19);
            } else if(!key.equals("created_at")) {
               if(key.equals("message")) {
                  message.setBody(value);
               } else {
                  ArrayList gsonBuilder;
                  JSONArray gson;
                  int attachmentsArray;
                  if(key.equals("read_ids")) {
                     gsonBuilder = new ArrayList();
                     gson = jsonObject.getJSONArray(key);
                     if(gson != null) {
                        for(attachmentsArray = 0; attachmentsArray < gson.length(); ++attachmentsArray) {
                           gsonBuilder.add(Integer.valueOf(gson.getInt(attachmentsArray)));
                        }

                        message.setReadIds(gsonBuilder);
                     }
                  } else if(!key.equals("delivered_ids")) {
                     int var17;
                     if(key.equals("recipient_id")) {
                        if(value != null && !value.equals("null")) {
                           var17 = Integer.parseInt(value);
                           message.setRecipientId(Integer.valueOf(var17));
                        }
                     } else if(key.equals("sender_id")) {
                        var17 = Integer.parseInt(value);
                        message.setSenderId(Integer.valueOf(var17));
                     } else if(key.equals("attachments")) {
                        GsonBuilder var15 = new GsonBuilder();
                        Gson var16 = var15.create();
                        QBAttachment[] var18 = (QBAttachment[])var16.fromJson(value, QBAttachment[].class);
                        ArrayList attachments = new ArrayList(Arrays.asList(var18));
                        message.setAttachments(attachments);
                     } else {
                        message.setProperty(key, value);
                     }
                  } else {
                     gsonBuilder = new ArrayList();
                     gson = jsonObject.getJSONArray(key);
                     if(gson != null) {
                        for(attachmentsArray = 0; attachmentsArray < gson.length(); ++attachmentsArray) {
                           gsonBuilder.add(Integer.valueOf(gson.getInt(attachmentsArray)));
                        }

                        message.setDeliveredIds(gsonBuilder);
                     }
                  }
               }
            }
         }
      } catch (JSONException var14) {
         var14.printStackTrace();
      }

      return message;
   }
}
