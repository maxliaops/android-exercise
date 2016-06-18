package com.rainsong.zhihudaily.entity;

import java.io.Serializable;
import java.util.List;

public class ListJobbersResponseInfo implements Serializable {

   private static final long serialVersionUID = 10L;
   private int code;
   private ListJobbersResponseInfo.DataEntity data;
   private String msg;


   public int getCode() {
      return this.code;
   }

   public ListJobbersResponseInfo.DataEntity getData() {
      return this.data;
   }

   public String getMsg() {
      return this.msg;
   }

   public void setCode(int var1) {
      this.code = var1;
   }

   public void setData(ListJobbersResponseInfo.DataEntity var1) {
      this.data = var1;
   }

   public void setMsg(String var1) {
      this.msg = var1;
   }

   public static class DataEntity {

      private int clientRand;
      private List<RetDataEntity> retData;
      private int status;
      private int total;


      public int getClientRand() {
         return this.clientRand;
      }

      public List<RetDataEntity> getRetData() {
         return this.retData;
      }

      public int getStatus() {
         return this.status;
      }

      public int getTotal() {
         return this.total;
      }

      public void setClientRand(int var1) {
         this.clientRand = var1;
      }

      public void setRetData(List var1) {
         this.retData = var1;
      }

      public void setStatus(int var1) {
         this.status = var1;
      }

      public void setTotal(int var1) {
         this.total = var1;
      }
   }

   public static class RetDataEntity {

      private String age;
      private String appointment_desc;
      private int auth;
      private String constellation;
      private String height;
      private String id;
      private String job;
      private String jobber_id;
      private String like_num;
      private String name;
      private String photo_1;
      private String price;
      private String status;
      private String user_id;
      private int vip;
      private int zan;


      public String getAge() {
         return this.age;
      }

      public String getAppointment_desc() {
         return this.appointment_desc;
      }

      public int getAuth() {
         return this.auth;
      }

      public String getConstellation() {
         return this.constellation;
      }

      public String getHeight() {
         return this.height;
      }

      public String getId() {
         return this.id;
      }

      public String getJob() {
         return this.job;
      }

      public String getJobber_id() {
         return this.jobber_id;
      }

      public String getLike_num() {
         return this.like_num;
      }

      public String getName() {
         return this.name;
      }

      public String getPhoto_1() {
         return this.photo_1;
      }

      public String getPrice() {
         return this.price;
      }

      public String getStatus() {
         return this.status;
      }

      public String getUser_id() {
         return this.user_id;
      }

      public int getVip() {
         return this.vip;
      }

      public int getZan() {
         return this.zan;
      }

      public void setAge(String var1) {
         this.age = var1;
      }

      public void setAppointment_desc(String var1) {
         this.appointment_desc = var1;
      }

      public void setAuth(int var1) {
         this.auth = var1;
      }

      public void setConstellation(String var1) {
         this.constellation = var1;
      }

      public void setHeight(String var1) {
         this.height = var1;
      }

      public void setId(String var1) {
         this.id = var1;
      }

      public void setJob(String var1) {
         this.job = var1;
      }

      public void setJobber_id(String var1) {
         this.jobber_id = var1;
      }

      public void setLike_num(String var1) {
         this.like_num = var1;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public void setPhoto_1(String var1) {
         this.photo_1 = var1;
      }

      public void setPrice(String var1) {
         this.price = var1;
      }

      public void setStatus(String var1) {
         this.status = var1;
      }

      public void setUser_id(String var1) {
         this.user_id = var1;
      }

      public void setVip(int var1) {
         this.vip = var1;
      }

      public void setZan(int var1) {
         this.zan = var1;
      }
   }
}
