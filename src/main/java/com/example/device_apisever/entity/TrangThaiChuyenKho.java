package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TrangThaiChuyenKho")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrangThaiChuyenKho {

    @Id
    @Column(name = "TrangThaiID")
    private Integer trangThaiId;

    @Column(name = "TenTrangThai", nullable = false, length = 100)
    private String tenTrangThai;
}
