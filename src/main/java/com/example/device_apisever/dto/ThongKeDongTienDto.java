package com.example.device_apisever.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThongKeDongTienDto {
    private BigDecimal tongDoanhThu;
    private BigDecimal tienHopDongDaThu;
    private BigDecimal tienCocDaThu;
    private BigDecimal tienBoiThuongDaThu;
    private BigDecimal chiPhiBaoTri;
    private BigDecimal tienChuaThu;
    private List<GiaoDichDto> giaoDichGanDay;
}
