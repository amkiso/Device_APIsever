package com.example.device_apisever.dto.baotri;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaoTriThongKeDto {
    private BigDecimal tongChiPhi;
    private BigDecimal chiPhiDinhKy;
    private BigDecimal chiPhiCaiTien;
    private BigDecimal chiPhiLoiKhongBoiThuong;
    private BigDecimal chiPhiSuCoCoBoiThuong;
}
