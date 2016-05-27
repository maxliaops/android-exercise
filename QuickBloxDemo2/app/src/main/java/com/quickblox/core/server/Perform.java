package com.quickblox.core.server;

import android.os.Bundle;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.Query;
import com.quickblox.core.exception.QBResponseException;

public interface Perform {

   Query performAsyncWithCallback(QBEntityCallback var1);

   Object perform(Bundle var1) throws QBResponseException;
}
