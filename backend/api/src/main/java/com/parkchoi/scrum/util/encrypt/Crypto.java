package com.parkchoi.scrum.util.encrypt;

import com.parkchoi.scrum.util.encrypt.exception.CryptoException;
import com.parkchoi.scrum.util.encrypt.properties.EncryptionProperties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    protected static final String ALGORITHM = "AES";

    protected static Cipher getCipher(final String algorithm, final int mode) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(EncryptionProperties.KEY.getBytes(),
                    ALGORITHM);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(mode, secretKeySpec);
            return cipher;
        } catch (Exception e) {
            throw new CryptoException(e.getMessage());
        }
    }
}
