package com.quickblox.core.query;

import com.quickblox.core.Query;
import com.quickblox.core.parser.QBXmlParser;

public class XmlQuery extends Query {

   public XmlQuery() {
      this.qbResponseParser = new QBXmlParser();
   }

   public void setParser(QBXmlParser parser) {
      this.qbResponseParser = parser;
   }

   public QBXmlParser getParser() {
      return (QBXmlParser)this.qbResponseParser;
   }
}
