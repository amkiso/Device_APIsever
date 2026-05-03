package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Phiếu bảo trì & sự cố.
 * HopDongID = NULL → bảo trì định kỳ độc lập.
 * HopDongID = x → phát sinh từ hợp đồng x.
 * TinhVaoBoiThuong = true → chi phí cộng vào HopDongThue.PhiBoiThuong khi hoàn thành.
 */
@Entity
@Table(name = "LichSuBaoTri")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LichSuBaoTri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BaoTriID")
    private Integer baoTriId;

    @Column(name = "ThietBiID", nullable = false)
    private Integer thietBiId;

    @Column(name = "NguoiDungBaoTriID", nullable = false)
    private Integer nguoiDungBaoTriId;

    @Column(name = "HopDongID")
    private Integer hopDongId;

    @Column(name = "NgayThucHien", nullable = false)
    private LocalDateTime ngayThucHien;

    @Column(name = "LoaiBaoTriID", nullable = false)
    private Integer loaiBaoTriId;

    @Column(name = "NoiDungBaoTri", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDungBaoTri;

    @Column(name = "ChiPhi", nullable = false, precision = 18, scale = 2)
    private BigDecimal chiPhi;

    @Column(name = "TrangThaiID", nullable = false)
    private Integer trangThaiId;

    @Column(name = "TinhVaoBoiThuong", nullable = false)
    private Boolean tinhVaoBoiThuong;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;

    @PrePersist
    protected void onCreate() {
        if (ngayThucHien == null) ngayThucHien = LocalDateTime.now();
        if (chiPhi == null) chiPhi = BigDecimal.ZERO;
        if (trangThaiId == null) trangThaiId = 1;
        if (tinhVaoBoiThuong == null) tinhVaoBoiThuong = false;
    }
}
