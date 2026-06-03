package com.example.device_apisever.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuKyDienTuResponse {
    private Integer chuKyId;
    private Integer hopDongId;
    private Integer nguoiDungId;
    private String tenNguoiDung;
    private String urlChuKy; // Public URL để hiển thị ảnh
    private LocalDateTime ngayKy;
    private String ipAddress;
    private String thietBiKy;
}
