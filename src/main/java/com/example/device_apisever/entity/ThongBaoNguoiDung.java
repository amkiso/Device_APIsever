package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Bảng trung gian: Trạng thái đọc/chưa đọc cho từng user.
 */
@Entity
@Table(name = "ThongBaoNguoiDung")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThongBaoNguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ThongBaoNguoiDungID")
    private Integer thongBaoNguoiDungId;

    @Column(name = "ThongBaoID", nullable = false)
    private Integer thongBaoId;

    @Column(name = "NguoiDungID", nullable = false)
    private Integer nguoiDungId;

    @Column(name = "DaDoc", nullable = false)
    private Boolean daDoc;

    @Column(name = "NgayDoc")
    private LocalDateTime ngayDoc;

    @PrePersist
    protected void onCreate() {
        if (daDoc == null) daDoc = false;
    }
}
