package com.quickblox.chat.model;


public class QBAttachment {

   public static final String ID_KEY = "id";
   public static final String TYPE_KEY = "type";
   public static final String URL_KEY = "url";
   public static final String CONTENT_TYPE_KEY = "content-type";
   public static final String SIZE_KEY = "size";
   public static final String NAME_KEY = "name";
   public static final String AUDIO_TYPE = "audio";
   public static final String VIDEO_TYPE = "video";
   public static final String PHOTO_TYPE = "photo";
   private String name;
   private String contentType;
   private String type;
   private String url;
   private String id;
   private double size;


   public QBAttachment(String type) {
      this.type = type;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public double getSize() {
      return this.size;
   }

   public void setSize(double size) {
      this.size = size;
   }

   public String getContentType() {
      return this.contentType;
   }

   public void setContentType(String contentType) {
      this.contentType = contentType;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(!(o instanceof QBAttachment)) {
         return false;
      } else {
         QBAttachment that = (QBAttachment)o;
         if(this.id != null) {
            if(!this.id.equals(that.id)) {
               return false;
            }
         } else if(that.id != null) {
            return false;
         }

         if(!this.type.equals(that.type)) {
            return false;
         } else {
            if(this.url != null) {
               if(this.url.equals(that.url)) {
                  return true;
               }
            } else if(that.url == null) {
               return true;
            }

            return false;
         }
      }
   }

   public int hashCode() {
      int result = this.type.hashCode();
      result = 31 * result + (this.url != null?this.url.hashCode():0);
      result = 31 * result + (this.id != null?this.id.hashCode():0);
      return result;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{").append("type").append("=").append(this.getType()).append(", url").append("=").append(this.getUrl() == null?"null":this.getUrl()).append(", id").append("=").append(this.getId() == null?"null":this.getId()).append("}");
      return stringBuilder.toString();
   }
}
