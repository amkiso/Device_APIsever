package com.example.device_apisever.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO phản hồi cho giỏ hàng — bao gồm thông tin loại thiết bị.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GioHangResponse {

    private Integer gioHangId;
    private Integer loaiThietBiId;
    private String tenLoaiThietBi;
    private String anhDaiDien;
    private BigDecimal giaThueThamKhao;
    private Integer soLuong;
    private BigDecimal thanhTien;         // giaThueThamKhao * soLuong
    private LocalDateTime ngayThem;
}
