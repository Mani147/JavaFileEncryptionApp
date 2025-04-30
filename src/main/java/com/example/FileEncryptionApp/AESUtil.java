package com.example.FileEncryptionApp;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
    private static final String ALGORITHM = "AES";

    public static byte[] encrypt(byte[] data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Make sure padding is consistent
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKey(key), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] encryptedData, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Must match encryption
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKey(key), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return cipher.doFinal(encryptedData);
    }

    private static byte[] getKey(String key) {
        byte[] keyBytes = key.getBytes();
        byte[] keyPadded = new byte[16]; // AES key should be 16 bytes
        System.arraycopy(keyBytes, 0, keyPadded, 0, Math.min(keyBytes.length, keyPadded.length));
        return keyPadded;
    }
}
