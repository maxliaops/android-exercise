package com.quickblox.core.model;

import com.qb.gson.annotations.SerializedName;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QBEntity implements Serializable {

   @SerializedName("id")
   protected Integer id;
   @SerializedName("created_at")
   protected Date createdAt;
   @SerializedName("updated_at")
   protected Date updatedAt;
   private SimpleDateFormat sdf;


   public QBEntity() {}

   public QBEntity(int id) {
      this.id = Integer.valueOf(id);
   }

   public Integer getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = Integer.valueOf(id);
   }

   public Date getCreatedAt() {
      return this.createdAt;
   }

   public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
   }

   public Date getUpdatedAt() {
      return this.updatedAt;
   }

   public void setUpdatedAt(Date updatedAt) {
      this.updatedAt = updatedAt;
   }

   public void copyFieldsTo(QBEntity entity) {
      if(entity != null) {
         entity.setId(this.id.intValue());
         entity.setCreatedAt(this.createdAt);
         entity.setUpdatedAt(this.updatedAt);
      }

   }

   public String getFCreatedAt() {
      if(this.createdAt != null) {
         if(this.sdf == null) {
            this.sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
         }

         return this.sdf.format(this.createdAt);
      } else {
         return null;
      }
   }

   public String getFUpdatedAt() {
      if(this.updatedAt != null) {
         if(this.sdf == null) {
            this.sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
         }

         return this.sdf.format(this.updatedAt);
      } else {
         return null;
      }
   }

   public String toString() {
      return "QBEntity{id=" + this.id + ", createdAt=" + this.getFCreatedAt() + ", updatedAt=" + this.getFUpdatedAt() + '}';
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o != null && this.getClass() == o.getClass()) {
         QBEntity qbEntity = (QBEntity)o;
         return this.id.equals(qbEntity.id);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.id.hashCode();
   }
}
