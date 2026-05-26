package com.example.device_apisever.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class YeuCauHoTroRequest {
    /** Nội dung yêu cầu hỗ trợ */
    private String noiDung;
    /** Loại yêu cầu: 1=Hỗ trợ chung, 2=Bảo trì */
    private Integer loaiYeuCau;
}
