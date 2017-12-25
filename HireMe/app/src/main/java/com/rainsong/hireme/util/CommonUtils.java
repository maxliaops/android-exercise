package com.rainsong.hireme.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by maxliaops on 17-12-20.
 */

public class CommonUtils {

    public static String getStringFromAsset(Context ctx, String fileName) {
        try {
            return getStringFromInputStream(ctx.getAssets().open(fileName,
                    AssetManager.ACCESS_STREAMING));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getStringFromInputStream(InputStream in) {
        if (in == null) {
            return "";
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            final StringBuilder sb = new StringBuilder(in.available());

            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(in);
            closeIO(br);
        }

        return "";
    }

    public static void closeIO(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
