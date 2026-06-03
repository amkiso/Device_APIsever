package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Chữ ký điện tử của khách hàng khi ký hợp đồng trên App.
 */
@Entity
@Table(name = "ChuKyDienTu")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChuKyDienTu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChuKyID")
    private Integer chuKyId;

    @Column(name = "HopDongID", nullable = false)
    private Integer hopDongId;

    @Column(name = "NguoiDungID", nullable = false)
    private Integer nguoiDungId;

    @Column(name = "TenFileChuKy", nullable = false, length = 255)
    private String tenFileChuKy;



    @Column(name = "NgayKy", nullable = false)
    private LocalDateTime ngayKy;

    @Column(name = "IpAddress", length = 45)
    private String ipAddress;

    @Column(name = "ThietBiKy", length = 200)
    private String thietBiKy;

    @PrePersist
    protected void onCreate() {
        if (ngayKy == null) ngayKy = LocalDateTime.now();
    }
}
