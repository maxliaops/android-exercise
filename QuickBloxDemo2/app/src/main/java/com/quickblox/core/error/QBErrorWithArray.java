package com.quickblox.core.error;

import com.quickblox.core.error.QBIError;
import java.util.ArrayList;

public class QBErrorWithArray implements QBIError {

   ArrayList errors;


   public ArrayList getErrors() {
      return this.errors;
   }

   public void setErrors(ArrayList errors) {
      this.errors = errors;
   }
}
