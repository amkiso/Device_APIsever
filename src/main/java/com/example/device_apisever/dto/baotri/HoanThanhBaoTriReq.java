package com.example.device_apisever.dto.baotri;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class HoanThanhBaoTriReq {
    private String noiDungBaoTri;
    private BigDecimal chiPhi;
    private String ghiChu;
    private Boolean tinhVaoBoiThuong; // Có tính phí bồi thường hay không
    private List<String> imageUrls; // List of image URLs
}
