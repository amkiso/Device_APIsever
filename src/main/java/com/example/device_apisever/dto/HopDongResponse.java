package com.example.device_apisever.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO response cho hợp đồng thuê.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HopDongResponse {

    private Integer hopDongId;
    private String maHopDong;
    private String trangThai;
    private List<ChiTietThietBiResponse> chiTietThietBi;
    private ChiPhiResponse chiPhi;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ChiTietThietBiResponse {
        private String tenThietBi;
        private String soSerial;
        private String tinhTrangBanGiao;
        private String mucDichSuDung;
        private BigDecimal giaTriMay;
        private BigDecimal giaThueThang;
        private String ngayKiemDinh;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ChiPhiResponse {
        private BigDecimal tongTienThue;
        private BigDecimal tienCoc;
        private BigDecimal thueVAT;
        private BigDecimal phiTreHanPhanTram;
        private Integer soNgayTreHanMoiKy;
        private Integer soNgayViPhamChamDut;
        private BigDecimal phiVeSinhChuyenSau;
        private BigDecimal khauHaoHaoMonNam;
        private BigDecimal phiGianDoanPhanTram;
    }
}
