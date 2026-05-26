package com.example.device_apisever.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO chi tiết hợp đồng đầy đủ — dùng cho xem lại hợp đồng.
 * Bao gồm: thông tin khách hàng, thiết bị, chi phí, trạng thái.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HopDongDetailResponse {

    private Integer hopDongId;
    private String maHopDong;
    private Integer trangThaiId;
    private String trangThai;
    private LocalDateTime ngayLap;
    private LocalDateTime ngayBatDauThue;
    private LocalDateTime ngayDuKienTra;
    private LocalDateTime ngayKyDienTu;
    private String diaDiemGiao;
    private String ghiChuKhachHang;
    private Integer soThangThue;

    // Phân loại & Hỏa tốc
    private Boolean laHoaToc;
    private String loaiHopDong;
    private BigDecimal phiHoaToc;
    private LocalDateTime hanThanhToan;
    private String lyDoHuy;
    private BigDecimal phiPhatSinh;
    private Integer phuongThucThanhToan;

    // Thông tin khách hàng (Bên B)
    private KhachHangInfo khachHang;

    // Danh sách thiết bị
    private List<HopDongResponse.ChiTietThietBiResponse> chiTietThietBi;

    // Chi phí
    private HopDongResponse.ChiPhiResponse chiPhi;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class KhachHangInfo {
        private String hoTen;
        private String email;
        private String soDienThoai;
        private String diaChi;
        private String cccd;
        private String cccdNgayCap;
        private String cccdNoiCap;
        private String donViCongTac;
    }
}
