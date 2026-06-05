package com.example.device_apisever.dto.baotri;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaoTriDetailDto extends BaoTriDto {
    private List<HinhAnhThietBiDto> anhGhiNhanSuCo;
    private List<HinhAnhThietBiDto> anhBanGiao;
}
