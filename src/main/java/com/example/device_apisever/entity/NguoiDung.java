package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Bảng trung tâm — mọi tài khoản (Admin, Thủ kho, KTV, Khách hàng) đều nằm đây.
 * VaiTroID phân biệt loại: 1=Admin | 2=Thủ kho | 3=KTV | 4=Khách hàng
 * KhoID chỉ có giá trị với Thủ kho (2) và KTV (3); NULL với Admin (1) và KH (4)
 */
@Entity
@Table(name = "NguoiDung")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NguoiDungID")
    private Integer nguoiDungId;

    @Column(name = "MaNguoiDung", nullable = false, unique = true, length = 20)
    private String maNguoiDung;

    @Column(name = "HoTen", nullable = false, length = 150)
    private String hoTen;

    @Column(name = "TaiKhoan", nullable = false, unique = true, length = 100)
    private String taiKhoan;

    @Column(name = "MatKhau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "VaiTroID", nullable = false)
    private Integer vaiTroId;

    @Column(name = "KhoID")
    private Integer khoId;

    @Column(name = "SoDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "LoaiKhachHangID")
    private Integer loaiKhachHangId;

    @Column(name = "MaSoThue", length = 50)
    private String maSoThue;

    @Column(name = "TrangThaiID", nullable = false)
    private Integer trangThaiId;

    @Column(name = "NgayTao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "LanDangNhapCuoi")
    private LocalDateTime lanDangNhapCuoi;

    @Column(name = "DoiMatKhauLanDau", nullable = false)
    private Boolean doiMatKhauLanDau;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (trangThaiId == null) trangThaiId = 1;
        if (doiMatKhauLanDau == null) doiMatKhauLanDau = false;
    }
}
