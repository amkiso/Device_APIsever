package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoaiGiaoDich")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoaiGiaoDich {

    @Id
    @Column(name = "LoaiGiaoDichID")
    private Integer loaiGiaoDichId;

    @Column(name = "TenLoai", nullable = false, length = 50)
    private String tenLoai;
}
