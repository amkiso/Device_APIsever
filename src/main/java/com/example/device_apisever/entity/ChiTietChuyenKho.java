package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietChuyenKho")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChiTietChuyenKho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChiTietID")
    private Integer chiTietId;

    @Column(name = "LenhChuyenKhoID", nullable = false)
    private Integer lenhChuyenKhoId;

    @Column(name = "ThietBiID", nullable = false)
    private Integer thietBiId;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;
}
