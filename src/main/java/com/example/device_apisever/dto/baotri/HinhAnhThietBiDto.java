package com.example.device_apisever.dto.baotri;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhThietBiDto {
    private Integer hinhAnhId;
    private String urlAnh;
    private Integer loaiAnhId;
    private LocalDateTime ngayChup;
}
