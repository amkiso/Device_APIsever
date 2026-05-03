package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Hợp đồng thuê thiết bị.
 * NguonTao: 1 = nhân viên tạo thay, 2 = khách hàng tự tạo qua app.
 * Vòng đời: Chờ duyệt(1) → Chờ giao(2) → Đang thuê(3) → Chờ thanh toán(4) → Đã hoàn thành(5)
 *           Hoặc: Đã từ chối(6) / Đã hủy(7)
 */
@Entity
@Table(name = "HopDongThue")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HopDongThue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HopDongID")
    private Integer hopDongId;

    @Column(name = "NguoiDungKhachID", nullable = false)
    private Integer nguoiDungKhachId;

    @Column(name = "NguoiDungTaoID", nullable = false)
    private Integer nguoiDungTaoId;

    @Column(name = "NgayLap", nullable = false, updatable = false)
    private LocalDateTime ngayLap;

    @Column(name = "NgayBatDauThue", nullable = false)
    private LocalDateTime ngayBatDauThue;

    @Column(name = "NgayDuKienTra", nullable = false)
    private LocalDateTime ngayDuKienTra;

    @Column(name = "NgayTraThucTe")
    private LocalDateTime ngayTraThucTe;

    @Column(name = "DiaDiemGiao", nullable = false, length = 500)
    private String diaDiemGiao;

    @Column(name = "TienCoc", nullable = false, precision = 18, scale = 2)
    private BigDecimal tienCoc;

    @Column(name = "TongTienThue", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTienThue;

    @Column(name = "PhiBoiThuong", nullable = false, precision = 18, scale = 2)
    private BigDecimal phiBoiThuong;

    @Column(name = "TrangThaiID", nullable = false)
    private Integer trangThaiId;

    @Column(name = "NguonTao", nullable = false)
    private Integer nguonTao;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;

    @PrePersist
    protected void onCreate() {
        if (ngayLap == null) ngayLap = LocalDateTime.now();
        if (tienCoc == null) tienCoc = BigDecimal.ZERO;
        if (tongTienThue == null) tongTienThue = BigDecimal.ZERO;
        if (phiBoiThuong == null) phiBoiThuong = BigDecimal.ZERO;
        if (trangThaiId == null) trangThaiId = 1;
        if (nguonTao == null) nguonTao = 2;
    }
}
