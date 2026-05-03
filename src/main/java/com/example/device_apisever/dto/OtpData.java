package com.example.device_apisever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OtpData {
    private String otp;
    private LocalDateTime expiryTime;
    private Object payload; // Chứa dữ liệu RegisterDTO tạm thời
}
