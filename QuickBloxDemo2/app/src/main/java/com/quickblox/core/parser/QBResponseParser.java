package com.quickblox.core.parser;

import android.os.Bundle;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.interfaces.QBCancelable;
import com.quickblox.core.rest.RestResponse;

public interface QBResponseParser extends QBCancelable {

   Object parse(RestResponse var1, Bundle var2) throws QBResponseException;
}
