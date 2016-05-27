package com.quickblox.chat.model;

import java.util.List;

public class QBPrivacyList {

   private boolean isActiveList;
   private boolean isDefaultList;
   private List items;
   private String name;


   public QBPrivacyList() {}

   public QBPrivacyList(List items, String name) {
      this.items = items;
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List getItems() {
      return this.items;
   }

   public void setItems(List items) {
      this.items = items;
   }

   public boolean isActiveList() {
      return this.isActiveList;
   }

   public boolean isDefaultList() {
      return this.isDefaultList;
   }

   public void setActiveList(boolean isActiveList) {
      this.isActiveList = isActiveList;
   }

   public void setDefaultList(boolean isDefaultList) {
      this.isDefaultList = isDefaultList;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder(QBPrivacyList.class.getSimpleName());
      stringBuilder.append("{").append("name").append("=").append(this.getName()).append(", isActive").append("=").append(this.isActiveList()).append(", isDefault").append("=").append(this.isDefaultList()).append(", items").append("=").append(this.getItems()).append("}");
      return stringBuilder.toString();
   }
}
