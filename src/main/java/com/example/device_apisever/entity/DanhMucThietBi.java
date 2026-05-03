package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DanhMucThietBi")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DanhMucThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DanhMucID")
    private Integer danhMucId;

    @Column(name = "TenDanhMuc", nullable = false, length = 100)
    private String tenDanhMuc;
}
