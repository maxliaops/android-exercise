//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.users.task;

import android.os.Bundle;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBRequestCanceler;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.task.QueriesTaskEntity;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class TaskEntitySignUpSignIn extends QueriesTaskEntity<QBUser> {
    private QBUser user;
    TaskEntitySignUpSignIn.STATUS status;
    QBEntityCallback<QBUser> signUpSignInCallback;

    public TaskEntitySignUpSignIn(QBUser user, QBEntityCallback<QBUser> callback) {
        super(callback);
        this.status = TaskEntitySignUpSignIn.STATUS.CREATING;
        this.signUpSignInCallback = new QBEntityCallback<QBUser>() {
            public void onSuccess(QBUser qbUser, Bundle params) {
                if(TaskEntitySignUpSignIn.STATUS.CREATING.equals(TaskEntitySignUpSignIn.this.status)) {
                    qbUser.setPassword(TaskEntitySignUpSignIn.this.user.getPassword());
                    TaskEntitySignUpSignIn.this.setCanceler(QBUsers.signIn(qbUser, TaskEntitySignUpSignIn.this.signUpSignInCallback));
                    TaskEntitySignUpSignIn.this.status = TaskEntitySignUpSignIn.STATUS.LOGINNING;
                } else if(TaskEntitySignUpSignIn.STATUS.LOGINNING.equals(TaskEntitySignUpSignIn.this.status)) {
                    TaskEntitySignUpSignIn.this.completeTaskSuccess(qbUser, (Bundle)null);
                }

            }

            public void onError(QBResponseException responseException) {
                TaskEntitySignUpSignIn.this.completeTaskErrors(responseException);
            }
        };
        this.user = user;
    }

    public QBRequestCanceler performTask() {
        this.status = TaskEntitySignUpSignIn.STATUS.CREATING;
        this.setCanceler(QBUsers.signUp(this.user, this.signUpSignInCallback));
        return this.canceler;
    }

    static enum STATUS {
        CREATING,
        LOGINNING;

        private STATUS() {
        }
    }
}
