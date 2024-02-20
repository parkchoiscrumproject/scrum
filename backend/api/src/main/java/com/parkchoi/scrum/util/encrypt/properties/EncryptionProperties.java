package com.parkchoi.scrum.util.encrypt.properties;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionProperties {
    @Value("${encryption.secret}")
    private String key;

    public static String KEY;

    @PostConstruct
    public void init() {
        KEY = key;
    }
}
