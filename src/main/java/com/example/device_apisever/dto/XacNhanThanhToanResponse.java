package com.example.device_apisever.dto;

import lombok.*;

/**
 * DTO response sau khi xác nhận thanh toán.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class XacNhanThanhToanResponse {

    private Integer hopDongId;
    private String trangThai;
    private String maGiaoDich;
}
