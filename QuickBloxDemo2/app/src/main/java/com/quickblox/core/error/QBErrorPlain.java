package com.quickblox.core.error;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.error.QBIError;
import java.util.ArrayList;

public class QBErrorPlain implements QBIError {

   @SerializedName("onError")
   String message;


   public ArrayList getErrors() {
      ArrayList errors = new ArrayList();
      errors.add(this.message);
      return errors;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
