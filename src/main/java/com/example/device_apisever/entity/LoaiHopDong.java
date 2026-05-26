package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Phân loại hợp đồng: 1=Cá nhân, 2=Doanh nghiệp, 3=Hỏa tốc
 */
@Entity
@Table(name = "LoaiHopDong")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoaiHopDong {

    @Id
    @Column(name = "LoaiHopDongID")
    private Integer loaiHopDongId;

    @Column(name = "TenLoai", nullable = false, length = 50)
    private String tenLoai;

    @Column(name = "MoTa", length = 255)
    private String moTa;
}
