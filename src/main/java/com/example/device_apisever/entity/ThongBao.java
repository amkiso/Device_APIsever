package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Bảng ThongBao — Lưu nội dung thông báo.
 * LoaiThongBao: 1=Tất cả, 2=Theo vai trò, 3=Cá nhân
 */
@Entity
@Table(name = "ThongBao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ThongBaoID")
    private Integer thongBaoId;

    @Column(name = "TieuDe", nullable = false, length = 200)
    private String tieuDe;

    @Column(name = "NoiDung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Column(name = "LoaiThongBao", nullable = false)
    private Integer loaiThongBao; // 1=Tất cả, 2=Theo vai trò, 3=Cá nhân

    @Column(name = "NguoiGuiID", nullable = false)
    private Integer nguoiGuiId;

    @Column(name = "VaiTroNhanID")
    private Integer vaiTroNhanId; // Dùng khi LoaiThongBao = 2

    @Column(name = "NgayTao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (loaiThongBao == null) loaiThongBao = 1;
    }
}
