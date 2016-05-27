package com.quickblox.users.parsers;

import android.os.Bundle;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.users.model.QBUser;

public class QBUserJsonParser extends QBJsonParser {

   private final QBUser user;


   public QBUserJsonParser(JsonQuery query) {
      super(query);
      this.user = (QBUser)query.getOriginalObject();
   }

   public QBUser parse(RestResponse restResponse, Bundle bundle) throws QBResponseException {
      QBUser qbUserParsed = (QBUser)super.parse(restResponse, bundle);
      qbUserParsed.copyFieldsTo(this.user);
      return qbUserParsed;
   }
}
