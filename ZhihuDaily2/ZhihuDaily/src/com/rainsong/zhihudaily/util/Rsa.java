package com.rainsong.zhihudaily.util;

import android.util.Base64;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class Rsa {

    private static final String ALGORITHM = "RSA";
    private static final String RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOUNnc+XMHDOpVYmyCuAc0VbXB\nYP/+JBnxSVGmra7sZ3m+FUTd2JSSEKg5U2hsIf2NLWbI0gjU41bT7ar1gEu9gaCG\nnOuyb5VbvLosTwYr57poZkO/wxTPR18ZdHEZcIPwJFU3jwReS86Vl5uqYZXk+50E\nAticlcY6h9dkfI9Y6wIDAQAB";
    private final String TAG = "Rsa";

    public static String decryptByPublic(String param0) {
        return null;
    }

    public static String encryptByPublic(String var0) {
        try {
            PublicKey var2 = getPublicKeyFromX509(
                    ALGORITHM,
                    RSA_PUBLICE);
            Cipher var3 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            var3.init(1, var2);
            String var4 = new String(Base64.encode(
                    var3.doFinal(var0.getBytes("UTF-8")), 0));
            return var4;
        } catch (Exception var5) {
            return null;
        }
    }

    private static PublicKey getPublicKeyFromX509(String var0, String var1)
            throws NoSuchAlgorithmException, Exception {
        X509EncodedKeySpec var2 = new X509EncodedKeySpec(Base64.decode(var1, 0));
        return KeyFactory.getInstance(var0).generatePublic(var2);
    }
}
