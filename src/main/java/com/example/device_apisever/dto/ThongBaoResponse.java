package com.example.device_apisever.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO trả về thông báo cho client.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThongBaoResponse {

    private Integer thongBaoId;
    private String tieuDe;
    private String noiDung;
    private Integer loaiThongBao;
    private String nguoiGui; // Tên người gửi
    private LocalDateTime ngayTao;
    private Boolean daDoc;
    private LocalDateTime ngayDoc;
}
