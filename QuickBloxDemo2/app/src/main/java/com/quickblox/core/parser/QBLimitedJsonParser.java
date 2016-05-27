package com.quickblox.core.parser;

import android.os.Bundle;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.model.QBEntityLimited;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.result.Result;

public class QBLimitedJsonParser extends QBJsonParser {

   public QBLimitedJsonParser(JsonQuery query) {
      super(query);
   }

   protected Object parseJsonResponse(RestResponse response, Result result) throws QBResponseException {
      Object jsonResponse = super.parseJsonResponse(response, result);
      this.obtainAdditionalData(jsonResponse);
      return jsonResponse;
   }

   protected void obtainAdditionalData(Object parsedObject) {
      if(parsedObject instanceof QBEntityLimited) {
         QBEntityLimited entityLimited = (QBEntityLimited)parsedObject;
         Bundle bundle = this.getBundle();
         if(bundle != null) {
            this.putLimit(bundle, entityLimited.getLimit());
            this.putSkip(bundle, entityLimited.getSkip());
            this.putTotalEntries(bundle, entityLimited.getTotalEntries());
         }

      }
   }

   protected void putLimit(Bundle bundle, Integer limit) {
      if(limit != null) {
         bundle.putInt("limit", limit.intValue());
      }

   }

   protected void putSkip(Bundle bundle, Integer skip) {
      if(skip != null) {
         bundle.putInt("skip", skip.intValue());
      }

   }

   protected void putTotalEntries(Bundle bundle, Integer total) {
      if(total != null) {
         bundle.putInt("total_entries", total.intValue());
      }

   }
}
