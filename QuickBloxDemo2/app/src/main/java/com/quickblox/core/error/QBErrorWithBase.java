package com.quickblox.core.error;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.error.QBIError;
import java.util.ArrayList;

public class QBErrorWithBase implements QBIError {

   @SerializedName("base")
   ArrayList base;


   public void setBase(ArrayList base) {
      this.base = base;
   }

   public ArrayList getErrors() {
      return this.base;
   }
}
