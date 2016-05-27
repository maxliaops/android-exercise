package com.quickblox.auth.parsers;

import com.qb.gson.Gson;
import com.qb.gson.JsonSyntaxException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.result.Result;

public class QBSGetSessionJsonParser extends QBJsonParser {

   public QBSGetSessionJsonParser(JsonQuery query) {
      super(query);
   }

   protected Object parseJsonResponse(RestResponse response, Result result) throws QBResponseException {
      Object parsedObj = null;
      if(this.deserializer != null) {
         String stringToParse = this.wrapAsSession(response.getRawBody());
         Gson gson = new Gson();

         try {
            parsedObj = gson.fromJson(stringToParse, this.deserializer);
         } catch (JsonSyntaxException var7) {
            throw new QBResponseException(var7.getLocalizedMessage());
         }
      }

      return parsedObj;
   }

   private String wrapAsSession(String rawBody) {
      StringBuilder wrappedRawBody = new StringBuilder();
      wrappedRawBody.append(rawBody);
      int bodyStart = wrappedRawBody.indexOf("{");
      wrappedRawBody.insert(bodyStart, "{\"session\":");
      int bodyFinish = wrappedRawBody.lastIndexOf("}");
      wrappedRawBody.insert(bodyFinish, "}");
      return wrappedRawBody.toString();
   }
}
