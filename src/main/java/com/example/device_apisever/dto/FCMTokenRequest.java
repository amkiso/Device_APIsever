package com.example.device_apisever.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO để client đăng ký FCM token sau khi đăng nhập.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FCMTokenRequest {

    @NotBlank(message = "FCM token khong duoc de trong")
    private String token;

    private String deviceName; // Tên thiết bị (optional)
}
