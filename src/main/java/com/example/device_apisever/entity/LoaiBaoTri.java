package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoaiBaoTri")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoaiBaoTri {

    @Id
    @Column(name = "LoaiBaoTriID")
    private Integer loaiBaoTriId;

    @Column(name = "TenLoai", nullable = false, length = 50)
    private String tenLoai;
}
