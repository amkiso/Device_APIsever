package com.example.device_apisever.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO trả về dữ liệu tổng hợp cho trang chủ Admin.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardResponse {

    // ---- Panel 1: Doanh thu ----
    private BigDecimal doanhThuThangNay;
    private BigDecimal doanhThuThangTruoc;
    private Double tiLeTangTruong; // % so với tháng trước

    // ---- Panel 2: Thiết bị đang bảo trì ----
    private long soThietBiDangBaoTri;

    // ---- Panel 3: Hợp đồng đến hạn (7 ngày tới) ----
    private long soHopDongDenHan;

    // ---- Slide bar: Nhắc nhở hôm nay ----
    private List<NhacNhoItem> nhacNhoHomNay;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class NhacNhoItem {
        private String loai;      // "HOP_DONG" | "BAO_TRI" | "BAN_GIAO"
        private String tieuDe;
        private String moTa;
        private Integer referenceId; // ID tham chiếu (HopDongID, BaoTriID, BanGiaoID)
    }
}
