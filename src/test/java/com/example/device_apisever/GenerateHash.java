package com.example.device_apisever;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    @Test
    public void printHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("abc123@");
        System.out.println("=== BCRYPT HASH FOR abc123@ ===");
        System.out.println(hash);
        System.out.println("=== VERIFY ===");
        System.out.println("matches: " + encoder.matches("abc123@", hash));
    }
}
