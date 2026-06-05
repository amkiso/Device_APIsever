package com.example.device_apisever.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KyHopDongRequest {
    @NotBlank(message = "Tên file chữ ký không được để trống")
    private String fileName;

    @NotBlank(message = "Mã PIN không được để trống")
    private String maPin;
}
