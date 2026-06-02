package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "LoaiThietBi")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoaiThietBi {

    @Transient
    private Integer tongSoLuong;

    @Transient
    private Integer soConLai;

    @Transient
    private Integer soDangThue;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LoaiThietBiID")
    private Integer loaiThietBiId;

    @Column(name = "DanhMucID", nullable = false)
    private Integer danhMucId;

    @Column(name = "NhaCungCapID", nullable = false)
    private Integer nhaCungCapId;

    @Column(name = "TenLoaiThietBi", nullable = false, length = 150)
    private String tenLoaiThietBi;

    @Column(name = "ThongSoKyThuat", columnDefinition = "NVARCHAR(MAX)")
    private String thongSoKyThuat;

    @Column(name = "GiaThueThamKhao", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaThueThamKhao;

    @Column(name = "AnhDaiDien", length = 500)
    private String anhDaiDien;
}
