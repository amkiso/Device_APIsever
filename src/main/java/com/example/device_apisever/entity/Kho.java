package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Kho")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Kho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KhoID")
    private Integer khoId;

    @Column(name = "TenKho", nullable = false, length = 200)
    private String tenKho;

    @Column(name = "DiaChi", length = 500)
    private String diaChi;

    @Column(name = "NguoiPhuTrach", length = 100)
    private String nguoiPhuTrach;

    @Column(name = "SoDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "QuanLyID")
    private Integer quanLyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuanLyID", insertable = false, updatable = false)
    private NguoiDung quanLy;
}
