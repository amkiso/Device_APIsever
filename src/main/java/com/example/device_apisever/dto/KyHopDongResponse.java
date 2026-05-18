package com.example.device_apisever.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO response sau khi ký hợp đồng điện tử.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KyHopDongResponse {

    private Integer hopDongId;
    private String trangThai;
    private LocalDateTime ngayKy;
    private String urlThanhToan;
}
