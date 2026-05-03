package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Điều khoản hợp đồng theo phiên bản.
 * Admin quản lý; mỗi hợp đồng liên kết với phiên bản đang hiệu lực tại thời điểm ký.
 */
@Entity
@Table(name = "DieuKhoanHopDong")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DieuKhoanHopDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DieuKhoanID")
    private Integer dieuKhoanId;

    @Column(name = "TieuDe", nullable = false, length = 200)
    private String tieuDe;

    @Column(name = "NoiDung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Column(name = "PhienBan", nullable = false, length = 20)
    private String phienBan;

    @Column(name = "NgayHieuLuc", nullable = false)
    private LocalDate ngayHieuLuc;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        if (isActive == null) isActive = true;
    }
}
