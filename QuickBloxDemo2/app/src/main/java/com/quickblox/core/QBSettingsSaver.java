package com.quickblox.core;

import com.quickblox.core.account.model.QBAccountSettings;
import java.util.Date;

public interface QBSettingsSaver {

   String API_DOMAIN = "qb_api_domain";
   String CHAT_DOMAIN = "qb_chat_domain";
   String LAST_UPDATE_TIME = "qb_last_update_time";


   void save(QBAccountSettings var1, Date var2);

   Date restore(QBAccountSettings var1);
}
