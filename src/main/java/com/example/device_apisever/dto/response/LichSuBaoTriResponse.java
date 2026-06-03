package com.example.device_apisever.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuBaoTriResponse {
    private Integer baoTriId;
    private Integer thietBiId;
    private String tenThietBi;
    private Integer nguoiDungBaoTriId;
    private String tenNguoiDungBaoTri;
    private LocalDateTime ngayThucHien;
    private Integer loaiBaoTriId;
    private String noiDungBaoTri;
    private BigDecimal chiPhi;
    private Integer trangThaiId;
    private Boolean tinhVaoBoiThuong;
    private String ghiChu;
}
