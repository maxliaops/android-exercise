package com.quickblox.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.quickblox.core.QBSettingsSaver;
import com.quickblox.core.account.model.QBAccountSettings;
import java.util.Date;

public class QBPreferenceSettingsSaver implements QBSettingsSaver {

   private final SharedPreferences preferences;


   public QBPreferenceSettingsSaver(Context applicationContext, String settingsKey) {
      this.preferences = applicationContext.getSharedPreferences(settingsKey, 0);
   }

   public void save(QBAccountSettings settings, Date date) {
      Editor editor = this.preferences.edit();
      this.saveValue("qb_api_domain", settings.getApiEndpoint(), editor);
      this.saveValue("qb_chat_domain", settings.getChatEndpoint(), editor);
      this.saveLongValue("qb_last_update_time", date.getTime(), editor);
      editor.apply();
   }

   private void saveValue(String field, String value, Editor editor) {
      editor.putString(field, value);
   }

   private void saveLongValue(String field, long value, Editor editor) {
      editor.putLong(field, value);
   }

   public Date restore(QBAccountSettings settings) {
      String apiDomain = this.restoreValue("qb_api_domain");
      String chatDomain = this.restoreValue("qb_chat_domain");
      settings.setApiEndpoint(apiDomain);
      settings.setChatEndpoint(chatDomain);
      long lastUpdateTime = this.restoreLongValue("qb_last_update_time");
      return new Date(lastUpdateTime);
   }

   private String restoreValue(String field) {
      return this.preferences.getString(field, "");
   }

   private long restoreLongValue(String field) {
      return this.preferences.getLong(field, 0L);
   }
}
