package com.example.device_apisever.dto.baotri;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaoTriDto {
    private Integer baoTriId;
    private Integer thietBiId;
    private String maThietBi;
    private String tenLoaiThietBi;
    
    private Integer nguoiDungBaoTriId;
    private String tenNguoiDungBaoTri;
    
    private Integer hopDongId;
    private LocalDateTime ngayThucHien;
    private Integer loaiBaoTriId;
    private String tenLoaiBaoTri;
    
    private String noiDungBaoTri;
    private BigDecimal chiPhi;
    private Integer trangThaiId;
    private String tenTrangThai;
    private Boolean tinhVaoBoiThuong;
    
    private String ghiChu;
    private LocalDateTime ngayHoanThanh;
}
