package com.quickblox.chat.utils;

import android.os.Bundle;
import java.util.concurrent.Executor;

public abstract class ThreadTask implements Runnable {

   public ThreadTask(Bundle params, Executor executor) {
      executor.execute(this);
   }

   public void run() {
      this.performInAsync();
   }

   public abstract void performInAsync();
}
