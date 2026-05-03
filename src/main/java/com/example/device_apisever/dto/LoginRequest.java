package com.example.device_apisever.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Tai khoan khong duoc de trong")
    private String taiKhoan;

    @NotBlank(message = "Mat khau khong duoc de trong")
    private String matKhau;
}
