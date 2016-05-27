//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.videochat.webrtc;

import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

final class AppRTCUtils {
    private AppRTCUtils() {
    }

    public static void assertIsTrue(boolean condition) {
        if(!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    public static String getThreadInfo() {
        return "@[name=" + Thread.currentThread().getName() + ", id=" + Thread.currentThread().getId() + "]";
    }

    public static void logDeviceInfo(String tag) {
        Log.d(tag, "Android SDK: " + VERSION.SDK_INT + ", " + "Release: " + VERSION.RELEASE + ", " + "Brand: " + Build.BRAND + ", " + "Device: " + Build.DEVICE + ", " + "Id: " + Build.ID + ", " + "Hardware: " + Build.HARDWARE + ", " + "Manufacturer: " + Build.MANUFACTURER + ", " + "Model: " + Build.MODEL + ", " + "Product: " + Build.PRODUCT);
    }

    public static class NonThreadSafe {
        private final Long threadId = Long.valueOf(Thread.currentThread().getId());

        public NonThreadSafe() {
        }

        public boolean calledOnValidThread() {
            return this.threadId.equals(Long.valueOf(Thread.currentThread().getId()));
        }
    }
}
