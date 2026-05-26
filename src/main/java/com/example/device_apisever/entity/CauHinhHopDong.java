package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Bảng cấu hình linh hoạt cho hợp đồng.
 * Cho phép admin thay đổi phí hỏa tốc, hạn thanh toán, v.v. mà không cần sửa code.
 */
@Entity
@Table(name = "CauHinhHopDong")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CauHinhHopDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CauHinhID")
    private Integer cauHinhId;

    @Column(name = "MaCauHinh", nullable = false, unique = true, length = 50)
    private String maCauHinh;

    @Column(name = "GiaTri", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTri;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "NgayCapNhat", nullable = false)
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        if (ngayCapNhat == null) ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}
