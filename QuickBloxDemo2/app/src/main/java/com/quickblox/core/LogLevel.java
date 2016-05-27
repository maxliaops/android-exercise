package com.quickblox.core;


public enum LogLevel {

   NOTHING("NOTHING", 0, "nothing"),
   DEBUG("DEBUG", 1, "debug");
   private String caption;
   // $FF: synthetic field
   private static final LogLevel[] $VALUES = new LogLevel[]{NOTHING, DEBUG};


   private LogLevel(String var1, int var2, String caption) {
      this.caption = caption;
   }

}
