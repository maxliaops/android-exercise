package com.quickblox.core.error;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.error.QBIError;
import java.util.ArrayList;

public class QBErrorWithError implements QBIError {

   @SerializedName("error")
   String message;


   public void setMessage(String message) {
      this.message = message;
   }

   public ArrayList getErrors() {
      ArrayList errors = new ArrayList();
      errors.add(this.message);
      return errors;
   }
}
