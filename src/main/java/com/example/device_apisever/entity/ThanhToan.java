package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Lịch sử giao dịch thanh toán.
 * PhuongThuc: 1=MoMo, 2=ZaloPay, 3=Tiền mặt
 * LoaiThanhToan: 1=Cọc, 2=Thuê định kỳ, 3=Phí phát sinh
 * TrangThai: 0=Pending, 1=Success, 2=Failed, 3=Refunded
 */
@Entity
@Table(name = "ThanhToan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ThanhToanID")
    private Integer thanhToanId;

    @Column(name = "HopDongID", nullable = false)
    private Integer hopDongId;

    @Column(name = "PhuongThuc", nullable = false)
    private Integer phuongThuc;

    @Column(name = "LoaiThanhToan", nullable = false)
    private Integer loaiThanhToan;

    @Column(name = "MaGiaoDich", length = 100)
    private String maGiaoDich;

    @Column(name = "SoTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal soTien;

    @Column(name = "TrangThai", nullable = false)
    private Integer trangThai;

    @Column(name = "NgayThanhToan")
    private LocalDateTime ngayThanhToan;

    @Column(name = "NgayTao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (loaiThanhToan == null) loaiThanhToan = 1;
        if (trangThai == null) trangThai = 0;
    }
}
