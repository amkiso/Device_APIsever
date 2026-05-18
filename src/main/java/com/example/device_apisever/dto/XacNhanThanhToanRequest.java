package com.example.device_apisever.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

/**
 * DTO request callback xác nhận thanh toán từ cổng MoMo/ZaloPay.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class XacNhanThanhToanRequest {

    private String maGiaoDich;

    @NotNull(message = "Số tiền không được để trống")
    private BigDecimal soTien;

    @NotNull(message = "Trạng thái không được để trống")
    private Integer trangThai;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private Integer phuongThuc;
}
