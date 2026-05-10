package com.example.device_apisever.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO cho request cập nhật số lượng giỏ hàng.
 * PUT /api/gio-hang/{id}  →  { "soLuong": 3 }
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GioHangUpdateRequest {

    @NotNull(message = "soLuong không được để trống")
    @Min(value = 1, message = "soLuong phải >= 1")
    private Integer soLuong;
}
