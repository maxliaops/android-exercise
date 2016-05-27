package com.quickblox.chat.model;

import com.quickblox.core.model.QBBaseCustomObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QBDialogCustomData extends QBBaseCustomObject {

   public QBDialogCustomData() {}

   public QBDialogCustomData(String className) {
      super(className);
      this.fields.put(String.format("data[%s]", new Object[]{"class_name"}), className);
   }

   public QBDialogCustomData(HashMap fields) {
      super((String)null);
      this.setFields(fields);
   }

   public QBDialogCustomData put(String fieldName, Object value) {
      this.fields.put(this.setCorrectFieldName(fieldName), value);
      return this;
   }

   public QBDialogCustomData putInteger(String fieldName, int value) {
      return this.put(fieldName, Integer.valueOf(value));
   }

   public QBDialogCustomData putString(String fieldName, String value) {
      return this.put(fieldName, value);
   }

   public QBDialogCustomData putFloat(String fieldName, float value) {
      return this.put(fieldName, Float.valueOf(value));
   }

   public QBDialogCustomData putBoolean(String fieldName, boolean value) {
      return this.put(fieldName, Boolean.valueOf(value));
   }

   public QBDialogCustomData putDate(String fieldName, Date value) {
      return this.put(fieldName, value);
   }

   public QBDialogCustomData putLocation(String fieldName, List value) {
      return this.put(fieldName, value);
   }

   public QBDialogCustomData putArray(String fieldName, List value) {
      return this.put(fieldName, value);
   }

   private String setCorrectFieldName(String fieldName) {
      return this.isCorrectFieldName(fieldName)?fieldName:this.addPrefix(fieldName);
   }

   private boolean isCorrectFieldName(String fieldName) {
      return fieldName.indexOf("data[", 0) == 0 && fieldName.lastIndexOf("]") == fieldName.length() - 1;
   }

   private String addPrefix(String fieldName) {
      return String.format("data[%s]", new Object[]{fieldName});
   }
}
