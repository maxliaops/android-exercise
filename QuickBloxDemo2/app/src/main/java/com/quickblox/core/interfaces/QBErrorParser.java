package com.quickblox.core.interfaces;

import com.quickblox.core.exception.QBResponseException;
import java.util.List;

public interface QBErrorParser {

   List parseError(String var1) throws QBResponseException;
}
