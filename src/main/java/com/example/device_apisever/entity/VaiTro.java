package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VaiTro")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VaiTro {

    @Id
    @Column(name = "VaiTroID")
    private Integer vaiTroId;

    @Column(name = "TenVaiTro", nullable = false, length = 50)
    private String tenVaiTro;

    @Column(name = "MoTa", length = 255)
    private String moTa;
}
