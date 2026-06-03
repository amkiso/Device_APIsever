package com.example.device_apisever.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietHopDongAdminResponse {
    private Integer hopDongId;
    private String maHopDong;
    private Integer loaiHopDongId;
    private Boolean laHoaToc;
    private BigDecimal phiHoaToc; // Trả về 0 nếu laHoaToc = false

    private Integer trangThaiId;
    private String tenTrangThai;

    private LocalDateTime ngayLap;
    private LocalDateTime ngayBatDauThue;
    private LocalDateTime ngayDuKienTra;
    private LocalDateTime ngayTraThucTe;

    private String diaDiemGiao;
    private String ghiChuKhachHang;
    private String lyDoHuy;

    private KhachHangInfo khachHang;
    private ChiPhiInfo chiPhi;

    private List<ThietBiHopDongResponse> danhSachThietBi;
    private List<LichSuBaoTriResponse> lichSuBaoTri;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class KhachHangInfo {
        private Integer nguoiDungId;
        private String hoTen;
        private String email;
        private String soDienThoai;
        private String cccd;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ChiPhiInfo {
        private BigDecimal tongTienThue;
        private BigDecimal tienCoc;
        private BigDecimal thueVat;
        private BigDecimal phiTreHanPhanTram;
        private Integer soNgayTreHanMoiKy;
        private Integer soNgayViPhamChamDut;
        private BigDecimal phiVeSinhChuyenSau;
        private BigDecimal phiGianDoanPhanTram;
    }
}
