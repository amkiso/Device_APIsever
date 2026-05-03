package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ChiTietPhieuNhap")
@IdClass(ChiTietPhieuNhapId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChiTietPhieuNhap {

    @Id
    @Column(name = "PhieuNhapID")
    private Integer phieuNhapId;

    @Id
    @Column(name = "LoaiThietBiID")
    private Integer loaiThietBiId;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "DonGiaNhap", nullable = false, precision = 18, scale = 2)
    private BigDecimal donGiaNhap;
}
