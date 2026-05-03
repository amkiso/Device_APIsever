package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TinhTrangThietBi")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TinhTrangThietBi {

    @Id
    @Column(name = "TinhTrangID")
    private Integer tinhTrangId;

    @Column(name = "TenTinhTrang", nullable = false, length = 50)
    private String tenTinhTrang;

    @Column(name = "MoTa", length = 255)
    private String moTa;
}
