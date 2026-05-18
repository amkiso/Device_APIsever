package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Mẫu điều khoản hợp đồng (6 điều), quản lý/cập nhật từ admin.
 */
@Entity
@Table(name = "DieuKhoanMauHopDong")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DieuKhoanMauHopDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DieuKhoanID")
    private Integer dieuKhoanId;

    @Column(name = "SoDieu", nullable = false)
    private Integer soDieu;

    @Column(name = "TieuDe", nullable = false, length = 200)
    private String tieuDe;

    @Column(name = "NoiDung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Column(name = "PhienBan", nullable = false)
    private Integer phienBan;

    @Column(name = "DangHoatDong", nullable = false)
    private Boolean dangHoatDong;

    @Column(name = "NgayTao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (phienBan == null) phienBan = 1;
        if (dangHoatDong == null) dangHoatDong = true;
    }
}
