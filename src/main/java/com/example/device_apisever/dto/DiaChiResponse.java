package com.example.device_apisever.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO response cho địa chỉ giao hàng.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiaChiResponse {

    private Integer diaChiId;
    private String tenNguoiNhan;
    private String soDienThoai;
    private String tinhThanhPho;
    private String phuongXa;
    private String diaChiChiTiet;
    private String donVi;
    private Integer loaiDiaChi;
    private Boolean laMacDinh;
}
