package com.quickblox.chat.query;

import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBChatMessageDeserializer;
import com.quickblox.core.RestMethod;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import java.util.List;
import java.util.Map;

public class QueryCreateMessage extends JsonQuery {

   private QBChatMessage message;


   public QueryCreateMessage(QBChatMessage message) {
      this.message = message;
      QBJsonParser parser = this.getParser();
      parser.setDeserializer(QBChatMessage.class);
      parser.putJsonTypeAdapter(QBChatMessage.class, new QBChatMessageDeserializer());
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.POST);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Message"});
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      if(this.message.getDialogId() != null) {
         parameters.put("chat_dialog_id", this.message.getDialogId());
      }

      if(this.message.getBody() != null) {
         parameters.put("message", this.message.getBody());
      }

      if(this.message.getRecipientId() != null) {
         parameters.put("recipient_id", this.message.getRecipientId());
      }

      if(this.message.getProperties() != null) {
         parameters.putAll(this.message.getProperties());
      }

      if(this.message.getAttachments() != null) {
         for(int i = 0; i < this.message.getAttachments().size(); ++i) {
            QBAttachment attach = (QBAttachment)((List)this.message.getAttachments()).get(i);
            StringBuilder sbBasic = new StringBuilder();
            sbBasic.append("attachments").append("[").append(i).append("]");
            StringBuilder sbId;
            if(attach.getId() != null) {
               sbId = new StringBuilder();
               sbId.append(sbBasic.toString()).append("[").append("id").append("]");
               parameters.put(sbId.toString(), attach.getId());
            }

            if(attach.getType() != null) {
               sbId = new StringBuilder();
               sbId.append(sbBasic.toString()).append("[").append("type").append("]");
               parameters.put(sbId.toString(), attach.getType());
            }

            if(attach.getUrl() != null) {
               sbId = new StringBuilder();
               sbId.append(sbBasic.toString()).append("[").append("url").append("]");
               parameters.put(sbId.toString(), attach.getUrl());
            }

            if(attach.getName() != null) {
               sbId = new StringBuilder();
               sbId.append(sbBasic.toString()).append("[").append("name").append("]");
               parameters.put(sbId.toString(), attach.getName());
            }

            if(attach.getContentType() != null) {
               sbId = new StringBuilder();
               sbId.append(sbBasic.toString()).append("[").append("content-type").append("]");
               parameters.put(sbId.toString(), attach.getContentType());
            }

            if(attach.getSize() > 0.0D) {
               sbId = new StringBuilder();
               sbId.append(sbBasic.toString()).append("[").append("size").append("]");
               parameters.put(sbId.toString(), Double.valueOf(attach.getSize()));
            }
         }
      }

   }
}
