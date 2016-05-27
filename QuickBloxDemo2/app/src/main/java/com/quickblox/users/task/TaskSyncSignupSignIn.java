package com.quickblox.users.task;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.task.TaskSync;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class TaskSyncSignupSignIn extends TaskSync {

   private QBUser user;


   public TaskSyncSignupSignIn(QBUser user) {
      this.user = user;
   }

   public QBUser execute() throws QBResponseException {
      QBUser qbUser = QBUsers.signUp(this.user);
      qbUser.setPassword(this.user.getPassword());
      qbUser = QBUsers.signIn(qbUser);
      return qbUser;
   }
}
