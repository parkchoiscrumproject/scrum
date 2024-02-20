package com.parkchoi.scrum.util.encrypt;

import com.parkchoi.scrum.util.encrypt.exception.EncryptionException;
import org.apache.commons.codec.digest.Crypt;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Encryptor extends Crypto{
    public static String encrypt(final String str) {
        try {

            Cipher cipher = getCipher(ALGORITHM, Cipher.ENCRYPT_MODE);
            byte[] encryptedBytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }

    }
}
