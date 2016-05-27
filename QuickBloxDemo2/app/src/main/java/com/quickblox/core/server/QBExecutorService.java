package com.quickblox.core.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QBExecutorService {

   private static byte POOL_SIZE = 5;
   private static ExecutorService executorService;


   public static synchronized ExecutorService getInstance() {
      if(executorService == null) {
         executorService = Executors.newFixedThreadPool(POOL_SIZE);
      }

      return executorService;
   }

}
