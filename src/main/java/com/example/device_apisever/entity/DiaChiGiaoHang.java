package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Quản lý nhiều địa chỉ giao hàng cho mỗi khách hàng.
 * LoaiDiaChi: 1=Nhà riêng, 2=Văn phòng/BV
 */
@Entity
@Table(name = "DiaChiGiaoHang")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiaChiGiaoHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DiaChiID")
    private Integer diaChiId;

    @Column(name = "NguoiDungID", nullable = false)
    private Integer nguoiDungId;

    @Column(name = "TenNguoiNhan", nullable = false, length = 150)
    private String tenNguoiNhan;

    @Column(name = "SoDienThoai", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "TinhThanhPho", nullable = false, length = 100)
    private String tinhThanhPho;

    @Column(name = "PhuongXa", nullable = false, length = 100)
    private String phuongXa;

    @Column(name = "DiaChiChiTiet", nullable = false, length = 500)
    private String diaChiChiTiet;

    @Column(name = "DonVi", length = 200)
    private String donVi;

    @Column(name = "LoaiDiaChi", nullable = false)
    private Integer loaiDiaChi;

    @Column(name = "LaMacDinh", nullable = false)
    private Boolean laMacDinh;

    @Column(name = "NgayTao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (loaiDiaChi == null) loaiDiaChi = 2;
        if (laMacDinh == null) laMacDinh = false;
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}
