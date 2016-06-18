package com.hiremeplz.hireme.bean;

import java.io.Serializable;
import java.util.List;

public class DetailsInfo implements Serializable {

   private int code;
   private DetailsInfo.DataEntity data;
   private String msg;


   public int getCode() {
      return this.code;
   }

   public DetailsInfo.DataEntity getData() {
      return this.data;
   }

   public String getMsg() {
      return this.msg;
   }

   public void setCode(int var1) {
      this.code = var1;
   }

   public void setData(DetailsInfo.DataEntity var1) {
      this.data = var1;
   }

   public void setMsg(String var1) {
      this.msg = var1;
   }

   public static class DataEntity {

      private List<MsgEntity> msg;
      private int status;


      public List<MsgEntity> getMsg() {
         return this.msg;
      }

      public int getStatus() {
         return this.status;
      }

      public void setMsg(List var1) {
         this.msg = var1;
      }

      public void setStatus(int var1) {
         this.status = var1;
      }
   }

   public static class MsgEntity {

      private String age;
      private String alias;
      private String apply_message;
      private String appointment_desc;
      private int auth;
      private String auto_match;
      private String city;
      private String constellation;
      private String created_at;
      private int employer_age;
      private String extensions;
      private String gender;
      private String has_appeal;
      private int hassave;
      private String height;
      private List hobbys;
      private String id;
      private List img;
      private String is_appeal;
      private String job;
      private int jobber_age;
      private String jobber_id;
      private String kaopu;
      private String level;
      private String like_num;
      private String mobile;
      private String mobile_verified;
      private String name;
      private String no_need_exam;
      private String photo_1;
      private String photo_2;
      private String photo_3;
      private String photo_4;
      private String photo_5;
      private String photo_6;
      private String photo_7;
      private String photo_8;
      private String price;
      private int price_int;
      private String province;
      private String push_time;
      private String qrcode;
      private String refund_status;
      private String reject_reason;
      private String rent_num;
      private String rent_reason;
      private int rent_reason_0;
      private String salary;
      private String scope_other;
      private List scopes;
      private String secret_pass;
      private String sex_orientation;
      private String status;
      private String updated_at;
      private String user_id;
      private int vip;
      private int zan;


      public String getAge() {
         return this.age;
      }

      public String getAlias() {
         return this.alias;
      }

      public String getApply_message() {
         return this.apply_message;
      }

      public String getAppointment_desc() {
         return this.appointment_desc;
      }

      public int getAuth() {
         return this.auth;
      }

      public String getAuto_match() {
         return this.auto_match;
      }

      public String getCity() {
         return this.city;
      }

      public String getConstellation() {
         return this.constellation;
      }

      public String getCreated_at() {
         return this.created_at;
      }

      public int getEmployer_age() {
         return this.employer_age;
      }

      public String getExtensions() {
         return this.extensions;
      }

      public String getGender() {
         return this.gender;
      }

      public String getHas_appeal() {
         return this.has_appeal;
      }

      public int getHassave() {
         return this.hassave;
      }

      public String getHeight() {
         return this.height;
      }

      public List getHobbys() {
         return this.hobbys;
      }

      public String getId() {
         return this.id;
      }

      public List getImg() {
         return this.img;
      }

      public String getIs_appeal() {
         return this.is_appeal;
      }

      public String getJob() {
         return this.job;
      }

      public int getJobber_age() {
         return this.jobber_age;
      }

      public String getJobber_id() {
         return this.jobber_id;
      }

      public String getKaopu() {
         return this.kaopu;
      }

      public String getLevel() {
         return this.level;
      }

      public String getLike_num() {
         return this.like_num;
      }

      public String getMobile() {
         return this.mobile;
      }

      public String getMobile_verified() {
         return this.mobile_verified;
      }

      public String getName() {
         return this.name;
      }

      public String getNo_need_exam() {
         return this.no_need_exam;
      }

      public String getPhoto_1() {
         return this.photo_1;
      }

      public String getPhoto_2() {
         return this.photo_2;
      }

      public String getPhoto_3() {
         return this.photo_3;
      }

      public String getPhoto_4() {
         return this.photo_4;
      }

      public String getPhoto_5() {
         return this.photo_5;
      }

      public String getPhoto_6() {
         return this.photo_6;
      }

      public String getPhoto_7() {
         return this.photo_7;
      }

      public String getPhoto_8() {
         return this.photo_8;
      }

      public String getPrice() {
         return this.price;
      }

      public int getPrice_int() {
         return this.price_int;
      }

      public String getProvince() {
         return this.province;
      }

      public String getPush_time() {
         return this.push_time;
      }

      public String getQrcode() {
         return this.qrcode;
      }

      public String getRefund_status() {
         return this.refund_status;
      }

      public String getReject_reason() {
         return this.reject_reason;
      }

      public String getRent_num() {
         return this.rent_num;
      }

      public String getRent_reason() {
         return this.rent_reason;
      }

      public int getRent_reason_0() {
         return this.rent_reason_0;
      }

      public String getSalary() {
         return this.salary;
      }

      public String getScope_other() {
         return this.scope_other;
      }

      public List getScopes() {
         return this.scopes;
      }

      public String getSecret_pass() {
         return this.secret_pass;
      }

      public String getSex_orientation() {
         return this.sex_orientation;
      }

      public String getStatus() {
         return this.status;
      }

      public String getUpdated_at() {
         return this.updated_at;
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

      public void setAlias(String var1) {
         this.alias = var1;
      }

      public void setApply_message(String var1) {
         this.apply_message = var1;
      }

      public void setAppointment_desc(String var1) {
         this.appointment_desc = var1;
      }

      public void setAuth(int var1) {
         this.auth = var1;
      }

      public void setAuto_match(String var1) {
         this.auto_match = var1;
      }

      public void setCity(String var1) {
         this.city = var1;
      }

      public void setConstellation(String var1) {
         this.constellation = var1;
      }

      public void setCreated_at(String var1) {
         this.created_at = var1;
      }

      public void setEmployer_age(int var1) {
         this.employer_age = var1;
      }

      public void setExtensions(String var1) {
         this.extensions = var1;
      }

      public void setGender(String var1) {
         this.gender = var1;
      }

      public void setHas_appeal(String var1) {
         this.has_appeal = var1;
      }

      public void setHassave(int var1) {
         this.hassave = var1;
      }

      public void setHeight(String var1) {
         this.height = var1;
      }

      public void setHobbys(List var1) {
         this.hobbys = var1;
      }

      public void setId(String var1) {
         this.id = var1;
      }

      public void setImg(List var1) {
         this.img = var1;
      }

      public void setIs_appeal(String var1) {
         this.is_appeal = var1;
      }

      public void setJob(String var1) {
         this.job = var1;
      }

      public void setJobber_age(int var1) {
         this.jobber_age = var1;
      }

      public void setJobber_id(String var1) {
         this.jobber_id = var1;
      }

      public void setKaopu(String var1) {
         this.kaopu = var1;
      }

      public void setLevel(String var1) {
         this.level = var1;
      }

      public void setLike_num(String var1) {
         this.like_num = var1;
      }

      public void setMobile(String var1) {
         this.mobile = var1;
      }

      public void setMobile_verified(String var1) {
         this.mobile_verified = var1;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public void setNo_need_exam(String var1) {
         this.no_need_exam = var1;
      }

      public void setPhoto_1(String var1) {
         this.photo_1 = var1;
      }

      public void setPhoto_2(String var1) {
         this.photo_2 = var1;
      }

      public void setPhoto_3(String var1) {
         this.photo_3 = var1;
      }

      public void setPhoto_4(String var1) {
         this.photo_4 = var1;
      }

      public void setPhoto_5(String var1) {
         this.photo_5 = var1;
      }

      public void setPhoto_6(String var1) {
         this.photo_6 = var1;
      }

      public void setPhoto_7(String var1) {
         this.photo_7 = var1;
      }

      public void setPhoto_8(String var1) {
         this.photo_8 = var1;
      }

      public void setPrice(String var1) {
         this.price = var1;
      }

      public void setPrice_int(int var1) {
         this.price_int = var1;
      }

      public void setProvince(String var1) {
         this.province = var1;
      }

      public void setPush_time(String var1) {
         this.push_time = var1;
      }

      public void setQrcode(String var1) {
         this.qrcode = var1;
      }

      public void setRefund_status(String var1) {
         this.refund_status = var1;
      }

      public void setReject_reason(String var1) {
         this.reject_reason = var1;
      }

      public void setRent_num(String var1) {
         this.rent_num = var1;
      }

      public void setRent_reason(String var1) {
         this.rent_reason = var1;
      }

      public void setRent_reason_0(int var1) {
         this.rent_reason_0 = var1;
      }

      public void setSalary(String var1) {
         this.salary = var1;
      }

      public void setScope_other(String var1) {
         this.scope_other = var1;
      }

      public void setScopes(List var1) {
         this.scopes = var1;
      }

      public void setSecret_pass(String var1) {
         this.secret_pass = var1;
      }

      public void setSex_orientation(String var1) {
         this.sex_orientation = var1;
      }

      public void setStatus(String var1) {
         this.status = var1;
      }

      public void setUpdated_at(String var1) {
         this.updated_at = var1;
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
