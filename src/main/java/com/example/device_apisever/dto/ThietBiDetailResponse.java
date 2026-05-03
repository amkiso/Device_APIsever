package com.example.device_apisever.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO trả về toàn bộ thông tin chi tiết của một thiết bị,
 * gộp dữ liệu từ nhiều bảng: ThietBi, LoaiThietBi, DanhMucThietBi,
 * NhaCungCap, TinhTrangThietBi, Kho, HinhAnhThietBi.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThietBiDetailResponse {

    // ---- Thông tin thiết bị (bảng ThietBi) ----
    private Integer thietBiId;
    private String maTaiSan;
    private LocalDateTime ngayBaoTriTiepTheo;

    // ---- Loại thiết bị (bảng LoaiThietBi) ----
    private Integer loaiThietBiId;
    private String tenLoaiThietBi;
    private String thongSoKyThuat;
    private BigDecimal giaThueThamKhao;
    private String anhDaiDien;

    // ---- Danh mục (bảng DanhMucThietBi) ----
    private Integer danhMucId;
    private String tenDanhMuc;

    // ---- Nhà cung cấp (bảng NhaCungCap) ----
    private Integer nhaCungCapId;
    private String tenNhaCungCap;

    // ---- Tình trạng (bảng TinhTrangThietBi) ----
    private Integer tinhTrangId;
    private String tenTinhTrang;

    // ---- Kho hiện tại (bảng Kho) ----
    private Integer khoHienTaiId;
    private String tenKho;
    private String diaChiKho;

    // ---- Danh sách hình ảnh (bảng HinhAnhThietBi) ----
    private List<HinhAnhInfo> hinhAnhs;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class HinhAnhInfo {
        private Integer hinhAnhId;
        private String urlAnh;
        private Integer loaiAnhId;
        private LocalDateTime ngayChup;
    }
}
