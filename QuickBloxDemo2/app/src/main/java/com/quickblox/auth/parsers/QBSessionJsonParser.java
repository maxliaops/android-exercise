package com.quickblox.auth.parsers;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.JsonQuery;

public class QBSessionJsonParser extends QBJsonParser {

   public QBSessionJsonParser(JsonQuery query) {
      super(query);
   }

   protected QBSession extractEntity(Object parsedEntity) {
      QBSession session = (QBSession)super.extractEntity(parsedEntity);

      try {
         QBAuth.getBaseService().setToken(session.getToken());
      } catch (BaseServiceException var4) {
         var4.printStackTrace();
      }

      return session;
   }
}
