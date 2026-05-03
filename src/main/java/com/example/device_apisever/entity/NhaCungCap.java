package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NhaCungCap")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NhaCungCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NhaCungCapID")
    private Integer nhaCungCapId;

    @Column(name = "TenNhaCungCap", nullable = false, length = 150)
    private String tenNhaCungCap;

    @Column(name = "NguoiLienHe", length = 100)
    private String nguoiLienHe;

    @Column(name = "SoDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;
}
