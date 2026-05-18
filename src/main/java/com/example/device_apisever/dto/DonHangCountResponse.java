package com.example.device_apisever.dto;

import lombok.*;

/**
 * DTO đếm số đơn hàng theo trạng thái — dùng cho badge trang Hồ sơ.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DonHangCountResponse {

    private long choXetDuyet;     // Chờ ký kết (1)
    private long canThanhToan;    // Đã ký - Chờ thanh toán (2)
    private long choGiaoHang;     // Đã thanh toán - Chờ phê duyệt (3) + Chờ giao hàng (4)
    private long dangThue;        // Đang cho thuê (5)
    private long tongDonHang;     // Tổng tất cả
}
