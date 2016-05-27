package com.quickblox.core.query;

import com.quickblox.core.Query;
import com.quickblox.core.parser.QBJsonParser;

public class JsonQuery extends Query {

   public JsonQuery() {
      this.qbResponseParser = new QBJsonParser(this);
   }

   public void setParser(QBJsonParser parser) {
      this.qbResponseParser = parser;
   }

   public QBJsonParser getParser() {
      return (QBJsonParser)this.qbResponseParser;
   }
}
