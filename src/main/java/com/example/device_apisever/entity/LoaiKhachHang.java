package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoaiKhachHang")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoaiKhachHang {

    @Id
    @Column(name = "LoaiID")
    private Integer loaiId;

    @Column(name = "TenLoai", nullable = false, length = 50)
    private String tenLoai;
}
