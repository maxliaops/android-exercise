package com.quickblox.core;


public enum RestMethod {

   DELETE("DELETE", 0, "DELETE"),
   GET("GET", 1, "GET"),
   POST("POST", 2, "POST"),
   PUT("PUT", 3, "PUT");
   private String caption;
   // $FF: synthetic field
   private static final RestMethod[] $VALUES = new RestMethod[]{DELETE, GET, POST, PUT};


   private RestMethod(String var1, int var2, String caption) {
      this.caption = caption;
   }

   public String toString() {
      return this.caption;
   }

   public String getCaption() {
      return this.caption;
   }

   public void setCaption(String caption) {
      this.caption = caption;
   }

}
