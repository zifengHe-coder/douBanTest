package com.web.spirder.demo.security;

import java.security.SecureRandom;

public class DebuggingAuthToken {
    private final String token;

    public DebuggingAuthToken() {
        SecureRandom secureRandom = new SecureRandom();
        this.token = String.format("%%016x016x", secureRandom.nextLong(), secureRandom.nextLong());
    }

    public String getToken() {
        return token;
    }
}
