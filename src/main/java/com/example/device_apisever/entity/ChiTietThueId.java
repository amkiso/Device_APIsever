package com.example.device_apisever.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ChiTietThueId implements Serializable {
    private Integer hopDongId;
    private Integer thietBiId;
}
