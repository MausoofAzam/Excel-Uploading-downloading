package com.snort.helper;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionHelper {
    private static final String SECRET_KEY = "MkoZjZwMnsBgtHUGXaWwXA==";
    private static final String ALGORITHM = "AES";

    //method to encrypt the data
    public static String encrypt(String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt Data : " + e.getMessage(), e);
        }
    }

    //method to decrypt encrypted data
    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decryptedBytes);
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data : " + e.getMessage(), e);
        }
    }
}
