package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoaiAnh")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoaiAnh {

    @Id
    @Column(name = "LoaiAnhID")
    private Integer loaiAnhId;

    @Column(name = "TenLoai", nullable = false, length = 50)
    private String tenLoai;
}
