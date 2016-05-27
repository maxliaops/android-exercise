package com.quickblox.chat.model;


public enum QBDialogType {

   PUBLIC_GROUP("PUBLIC_GROUP", 0, 1),
   GROUP("GROUP", 1, 2),
   PRIVATE("PRIVATE", 2, 3);
   private int code;
   // $FF: synthetic field
   private static final QBDialogType[] $VALUES = new QBDialogType[]{PUBLIC_GROUP, GROUP, PRIVATE};


   private QBDialogType(String var1, int var2, int code) {
      this.code = code;
   }

   public int getCode() {
      return this.code;
   }

   public static QBDialogType parseByCode(int code) {
      QBDialogType[] values = values();
      QBDialogType result = null;
      QBDialogType[] arr$ = values;
      int len$ = values.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         QBDialogType value = arr$[i$];
         if(value.getCode() == code) {
            result = value;
            break;
         }
      }

      return result;
   }

}
