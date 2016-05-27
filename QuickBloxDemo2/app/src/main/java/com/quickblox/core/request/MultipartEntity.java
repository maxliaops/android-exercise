package com.quickblox.core.request;

import java.io.File;
import java.util.Map;

public class MultipartEntity {

   private Map values;
   private File uploadFile;
   private String fieldName;


   public void addParam(Map values) {
      this.values = values;
   }

   public void addFilePart(String fieldName, File uploadFile) {
      this.fieldName = fieldName;
      this.uploadFile = uploadFile;
   }

   public Map getPart() {
      return this.values;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   public File getUploadFile() {
      return this.uploadFile;
   }
}
