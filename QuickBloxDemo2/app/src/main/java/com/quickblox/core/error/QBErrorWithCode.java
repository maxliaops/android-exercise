package com.quickblox.core.error;

import com.quickblox.core.error.QBIError;
import java.util.ArrayList;

public class QBErrorWithCode implements QBIError {

   String code;
   String message;


   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public ArrayList getErrors() {
      ArrayList errors = new ArrayList();
      errors.add(this.message);
      return errors;
   }
}
