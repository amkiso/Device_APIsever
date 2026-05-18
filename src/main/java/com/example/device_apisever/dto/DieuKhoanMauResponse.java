package com.example.device_apisever.dto;

import lombok.*;

/**
 * DTO response cho điều khoản mẫu hợp đồng.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DieuKhoanMauResponse {

    private Integer soDieu;
    private String tieuDe;
    private String noiDung;
}
