package com.example.device_apisever.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QrCodeResponse {

    /** ID thiết bị */
    private Integer thietBiId;

    /** Mã tài sản của thiết bị */
    private String maTaiSan;

    /** Nội dung được encode trong QR (VD: DEVICE:TB001) */
    private String qrContent;

    /** Đường dẫn public truy cập ảnh QR PNG */
    private String qrCodeUrl;
}
