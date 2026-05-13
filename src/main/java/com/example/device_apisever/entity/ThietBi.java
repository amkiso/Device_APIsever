package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThietBi")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ThietBiID")
    private Integer thietBiId;

    @Column(name = "LoaiThietBiID", nullable = false)
    private Integer loaiThietBiId;

    @Column(name = "MaTaiSan", nullable = false, unique = true, length = 50)
    private String maTaiSan;

    @Column(name = "TinhTrangID", nullable = false)
    private Integer tinhTrangId;

    @Column(name = "KhoHienTaiID", nullable = false)
    private Integer khoHienTaiId;

    @Column(name = "NgayBaoTriTiepTheo")
    private LocalDateTime ngayBaoTriTiepTheo;

    @Column(name = "QrCodeUrl", length = 500)
    private String qrCodeUrl;
}