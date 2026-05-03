package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Ghi nhận việc khách hàng đã đọc và đồng ý điều khoản — có giá trị pháp lý.
 * PK: (HopDongID, DieuKhoanID)
 */
@Entity
@Table(name = "XacNhanDieuKhoan")
@IdClass(XacNhanDieuKhoanId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class XacNhanDieuKhoan {

    @Id
    @Column(name = "HopDongID")
    private Integer hopDongId;

    @Id
    @Column(name = "DieuKhoanID")
    private Integer dieuKhoanId;

    @Column(name = "ThoiGianXacNhan", nullable = false)
    private LocalDateTime thoiGianXacNhan;

    @Column(name = "DiaChiIP", length = 50)
    private String diaChiIP;

    @PrePersist
    protected void onCreate() {
        if (thoiGianXacNhan == null) thoiGianXacNhan = LocalDateTime.now();
    }
}
