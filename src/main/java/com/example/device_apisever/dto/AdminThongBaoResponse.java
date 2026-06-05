package com.example.device_apisever.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminThongBaoResponse {
    private Integer thongBaoId;
    private String tieuDe;
    private String noiDung;
    private Integer loaiThongBao;
    private String nguoiGui;
    private LocalDateTime ngayTao;
    private Integer soNguoiNhan;
    private Integer soNguoiDaDoc;
}
