package com.quickblox.core.result;

import com.quickblox.core.result.RestResult;

public class Result extends RestResult {

   public static final String LOG_MSG_OBJ_PARSED = "Object parsed from JSON : \n";
   public static final String LOG_MSG_OBJ_PARSED_XML = "Object parsed from XML : \n";


   public boolean isSuccess() {
      return this.getErrors().isEmpty() && !this.foundError();
   }

   protected boolean isDeserializable() {
      return this.isSuccess() && !this.isEmpty();
   }
}
