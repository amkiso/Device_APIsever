package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Lưu Firebase Cloud Messaging device token.
 * Mỗi user có thể đăng nhập trên nhiều thiết bị → nhiều token.
 */
@Entity
@Table(name = "FCMToken")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FCMToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TokenID")
    private Integer tokenId;

    @Column(name = "NguoiDungID", nullable = false)
    private Integer nguoiDungId;

    @Column(name = "Token", nullable = false, length = 500)
    private String token;

    @Column(name = "DeviceName", length = 100)
    private String deviceName;

    @Column(name = "NgayCapNhat", nullable = false)
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        if (ngayCapNhat == null) ngayCapNhat = LocalDateTime.now();
    }
}
