package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Ảnh đính kèm thiết bị (ảnh hiện trạng, biên bản, sự cố, chữ ký…).
 * BanGiaoID / BaoTriID liên kết ảnh với phiên bàn giao / phiếu bảo trì cụ thể.
 */
@Entity
@Table(name = "HinhAnhThietBi")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HinhAnhThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HinhAnhID")
    private Integer hinhAnhId;

    @Column(name = "ThietBiID", nullable = false)
    private Integer thietBiId;

    @Column(name = "NguoiDungChupID", nullable = false)
    private Integer nguoiDungChupId;

    @Column(name = "UrlAnh", nullable = false, length = 500)
    private String urlAnh;

    @Column(name = "LoaiAnhID", nullable = false)
    private Integer loaiAnhId;

    @Column(name = "NgayChup", nullable = false)
    private LocalDateTime ngayChup;

    @Column(name = "BanGiaoID")
    private Integer banGiaoId;

    @Column(name = "BaoTriID")
    private Integer baoTriId;

    @PrePersist
    protected void onCreate() {
        if (ngayChup == null) ngayChup = LocalDateTime.now();
    }
}
