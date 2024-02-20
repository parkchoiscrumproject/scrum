package com.parkchoi.scrum.util.encrypt;

import com.parkchoi.scrum.util.encrypt.exception.DecryptionException;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Decryptor extends Crypto{
    public static String decrypt(final String encryptedStr) {
        try {
            byte[] encryptedBytes = Base64.getDecoder()
                    .decode(encryptedStr.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = getCipher(ALGORITHM, Cipher.DECRYPT_MODE);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String str = new String(decryptedBytes, StandardCharsets.UTF_8);
            return str;
        } catch (Exception e) {
            throw new DecryptionException(e.getMessage());
        }

    }
}
