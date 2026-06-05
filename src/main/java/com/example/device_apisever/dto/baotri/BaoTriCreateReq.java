package com.example.device_apisever.dto.baotri;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaoTriCreateReq {
    private Integer thietBiId;
    private Integer hopDongId;
    private Integer loaiBaoTriId;
    private String noiDungBaoTri;
    private LocalDateTime ngayThucHien;
    private Boolean tinhVaoBoiThuong;
}
