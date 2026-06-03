package com.example.device_apisever.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminHopDongResponse {
    private Integer hopDongId;
    private String maHopDong; // Dạng HD-YYYY-XXXXX
    private String tenKhachHang;
    private String soDienThoai;
    private Integer loaiHopDongId;
    private Boolean laHoaToc;
    private BigDecimal tongTienThue;
    private BigDecimal tienCoc;
    private Integer trangThaiId;
    private String tenTrangThai;
    private LocalDateTime ngayLap;
    private LocalDateTime ngayBatDauThue;
    private LocalDateTime ngayDuKienTra;
    private LocalDateTime ngayTraThucTe;
}
