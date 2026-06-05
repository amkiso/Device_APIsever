package com.example.device_apisever.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NhapKhoRequest {
    private Integer loaiThietBiId;
    private String maTaiSan;
    private String soSerial;
    private BigDecimal donGiaNhap;
}
