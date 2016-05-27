package com.quickblox.core.exception;

import com.quickblox.core.helper.ToStringHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QBResponseException extends IOException {

   private List errors;
   private int httpStatusCode;


   public QBResponseException(String msg) {
      super(msg);
      this.errors = new ArrayList(1);
      this.errors.add(msg);
      this.httpStatusCode = -1;
   }

   public QBResponseException(int statusCode, List errors) {
      super(ToStringHelper.toString(errors, ","));
      this.errors = errors;
      this.httpStatusCode = statusCode;
   }

   public QBResponseException(List errors) {
      this(-1, errors);
   }

   public List getErrors() {
      return this.errors;
   }

   public int getHttpStatusCode() {
      return this.httpStatusCode;
   }
}
