package com.example.device_apisever.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO tóm tắt hợp đồng cho khách hàng.
 * Dùng cho: danh sách đơn hàng, label trang chủ.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HopDongSummaryResponse {

    private Integer hopDongId;
    private String maHopDong;
    private Integer trangThaiId;
    private String trangThai;
    private LocalDateTime ngayLap;
    private LocalDateTime ngayBatDauThue;
    private LocalDateTime ngayDuKienTra;
    private BigDecimal tongTienThue;
    private BigDecimal tienCoc;
    private Integer soThietBi;
    private String diaDiemGiao;
    private Boolean laHoaToc;
    private String loaiHopDong;
    private LocalDateTime hanThanhToan;
}
