package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ChiTietThue")
@IdClass(ChiTietThueId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChiTietThue {

    @Id
    @Column(name = "HopDongID")
    private Integer hopDongId;

    @Id
    @Column(name = "ThietBiID")
    private Integer thietBiId;

    @Column(name = "GiaThueThucTe", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaThueThucTe;
}
