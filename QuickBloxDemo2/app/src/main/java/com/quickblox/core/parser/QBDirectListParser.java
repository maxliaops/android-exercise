package com.quickblox.core.parser;

import com.quickblox.core.model.QBEntityWrap;
import com.quickblox.core.parser.QBListJsonParser;
import com.quickblox.core.query.JsonQuery;
import java.util.ArrayList;
import java.util.Iterator;

public class QBDirectListParser extends QBListJsonParser {

   public QBDirectListParser(JsonQuery query) {
      super(query);
   }

   protected ArrayList extractEntity(Object parsedEntity) {
      ArrayList wrapArrayList = (ArrayList)parsedEntity;
      ArrayList items = new ArrayList();
      Iterator i$ = wrapArrayList.iterator();

      while(i$.hasNext()) {
         QBEntityWrap sw = (QBEntityWrap)i$.next();
         items.add(sw.getEntity());
      }

      return items;
   }
}
