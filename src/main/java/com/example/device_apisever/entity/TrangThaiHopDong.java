package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TrangThaiHopDong")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrangThaiHopDong {

    @Id
    @Column(name = "TrangThaiID")
    private Integer trangThaiId;

    @Column(name = "TenTrangThai", nullable = false, length = 60)
    private String tenTrangThai;
}
