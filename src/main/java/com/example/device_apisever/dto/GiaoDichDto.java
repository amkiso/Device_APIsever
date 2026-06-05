package com.example.device_apisever.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaoDichDto {
    private Integer giaoDichId;
    private String loaiGiaoDich; // "Thanh toán", "Bảo trì", "Cọc"
    private String maGiaoDich;
    private BigDecimal soTien;
    private LocalDateTime thoiGian;
    private String moTa;
    private String trangThai; // "Thành công", "Thất bại", "Hoàn thành"
    private Boolean isThuVao; // true: dòng tiền vào (doanh thu/cọc), false: dòng tiền ra (chi phí)
}
