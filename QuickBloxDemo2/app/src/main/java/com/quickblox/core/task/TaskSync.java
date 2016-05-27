package com.quickblox.core.task;

import com.quickblox.core.exception.QBResponseException;

public abstract class TaskSync {

   public abstract Object execute() throws QBResponseException;
}
