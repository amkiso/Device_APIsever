package com.example.device_apisever.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ChiTietPhieuNhapId implements Serializable {
    private Integer phieuNhapId;
    private Integer loaiThietBiId;
}
