package com.example.device_apisever.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO cho request thêm item vào giỏ hàng.
 * POST /api/gio-hang  →  { "loaiThietBiId": 1, "soLuong": 1 }
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GioHangRequest {

    @NotNull(message = "loaiThietBiId không được để trống")
    private Integer loaiThietBiId;

    @NotNull(message = "soLuong không được để trống")
    @Min(value = 1, message = "soLuong phải >= 1")
    private Integer soLuong;
}
