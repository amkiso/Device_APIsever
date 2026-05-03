package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TrangThaiBaoTri")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrangThaiBaoTri {

    @Id
    @Column(name = "TrangThaiID")
    private Integer trangThaiId;

    @Column(name = "TenTrangThai", nullable = false, length = 50)
    private String tenTrangThai;
}
