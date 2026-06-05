package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PhieuNhap")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PhieuNhapID")
    private Integer phieuNhapId;

    @Column(name = "NhaCungCapID", nullable = false)
    private Integer nhaCungCapId;

    @Column(name = "NguoiDungNhapID", nullable = false)
    private Integer nhanVienNhapId;

    @Column(name = "NgayNhap", nullable = false)
    private LocalDateTime ngayNhap;

    @Column(name = "TongTienNhap", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTienNhap;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;
}
