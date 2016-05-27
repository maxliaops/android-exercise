package com.quickblox.core.model;

import com.quickblox.core.model.QBEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public abstract class QBBaseCustomObject extends QBEntity {

   protected String className;
   protected HashMap fields = new HashMap();


   public QBBaseCustomObject() {}

   public QBBaseCustomObject(String className) {
      this.className = className;
   }

   public String getClassName() {
      return this.className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public HashMap getFields() {
      return this.fields;
   }

   public void setFields(HashMap fields) {
      this.fields = fields;
   }

   public void putFields(HashMap fields) {
      this.fields.putAll(fields);
   }

   public Object get(String fieldName) {
      return this.fields.get(fieldName);
   }

   public Object remove(String fieldName) {
      return this.fields.remove(fieldName);
   }

   public abstract QBBaseCustomObject put(String var1, Object var2);

   public abstract QBBaseCustomObject putInteger(String var1, int var2);

   public Integer getInteger(String fieldName) throws NumberFormatException {
      Object valueOrNull = this.fields.get(fieldName);
      return valueOrNull == null?null:Integer.valueOf(Integer.parseInt(valueOrNull.toString()));
   }

   public abstract QBBaseCustomObject putString(String var1, String var2);

   public String getString(String fieldName) {
      Object valueOrNull = this.fields.get(fieldName);
      return valueOrNull == null?null:String.valueOf(valueOrNull);
   }

   public abstract QBBaseCustomObject putFloat(String var1, float var2);

   public Float getFloat(String fieldName) throws NumberFormatException {
      Object valueOrNull = this.fields.get(fieldName);
      return valueOrNull == null?null:Float.valueOf(Float.parseFloat(valueOrNull.toString()));
   }

   public abstract QBBaseCustomObject putBoolean(String var1, boolean var2);

   public Boolean getBoolean(String fieldName) {
      Object valueOrNull = this.fields.get(fieldName);
      return valueOrNull == null?null:Boolean.valueOf(Boolean.parseBoolean(valueOrNull.toString()));
   }

   public abstract QBBaseCustomObject putDate(String var1, Date var2);

   public Date getDate(String fieldName, SimpleDateFormat formatter) throws ParseException {
      Object valueOrNull = this.fields.get(fieldName);
      return valueOrNull != null && formatter != null?formatter.parse(valueOrNull.toString()):null;
   }

   public abstract QBBaseCustomObject putLocation(String var1, List var2);

   public List getLocation(String fieldName) throws NumberFormatException {
      Object valueOrNull = this.fields.get(fieldName);
      if(valueOrNull == null) {
         return null;
      } else if(valueOrNull instanceof List) {
         return (List)valueOrNull;
      } else {
         String[] locationPoints = valueOrNull.toString().split(",");
         if(locationPoints.length == 2) {
            ArrayList locationValue = new ArrayList(2);
            locationValue.add(0, Double.valueOf(Double.parseDouble(locationPoints[0])));
            locationValue.add(1, Double.valueOf(Double.parseDouble(locationPoints[1])));
            return locationValue;
         } else {
            return null;
         }
      }
   }

   public abstract QBBaseCustomObject putArray(String var1, List var2);

   public List getArray(String fieldName) {
      Object valueOrNull = this.fields.get(fieldName);
      return valueOrNull == null?null:(!(valueOrNull instanceof List)?null:(List)valueOrNull);
   }

   public String toString() {
      return "QBBaseCustomObject{className=\'" + this.className + '\'' + ", fields=" + this.fields + '}';
   }

   public void copyFieldsTo(QBBaseCustomObject baseObject) {
      if(baseObject != null) {
         super.copyFieldsTo(baseObject);
         baseObject.setClassName(this.className);
         baseObject.setFields(this.fields);
      }

   }
}
