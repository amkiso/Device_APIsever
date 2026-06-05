package com.example.device_apisever.dto.admin;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChiTietChuyenKhoDTO {
    private Integer chiTietId;
    private Integer thietBiId;
    private String maThietBi;
    private String tenThietBi;
    private String ghiChu;
}
