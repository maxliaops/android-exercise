package com.quickblox.chat.query;

import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.model.QBDialogCustomDataDeserializer;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.JsonQuery;

public abstract class QueryAbsDialog extends JsonQuery {

   public QueryAbsDialog() {
      QBJsonParser parser = this.getParser();
      parser.setDeserializer(QBDialog.class);
      parser.putJsonTypeAdapter(QBDialogCustomData.class, new QBDialogCustomDataDeserializer());
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Dialog"});
   }
}
