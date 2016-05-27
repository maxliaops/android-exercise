package com.quickblox.core.helper;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import com.quickblox.core.helper.Lo;
import java.util.UUID;

public class Utils {

   public static String generateDeviceId(Context context) {
      String deviceIndustrialName = "";
      String deviceSerialNumber = "";
      String androidDeviceId = "";

      try {
         deviceSerialNumber = "" + Build.SERIAL;
         deviceIndustrialName = "" + Build.DEVICE;
         androidDeviceId = "" + Secure.getString(context.getContentResolver(), "android_id");
      } catch (Exception var5) {
         Lo.g(!TextUtils.isEmpty(var5.getMessage())?var5.getMessage():"Error generating device id");
      }

      UUID deviceUuid = new UUID((long)androidDeviceId.hashCode(), (long)deviceIndustrialName.hashCode() << 32 | (long)deviceSerialNumber.hashCode());
      Lo.g("DEVICE_ID = " + deviceUuid);
      return deviceUuid.toString();
   }
}
