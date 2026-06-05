package com.example.device_apisever.dto.admin;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminLenhChuyenKhoListResponse {
    private Integer lenhChuyenKhoId;
    private Integer tuKhoId;
    private String tenTuKho;
    private Integer denKhoId;
    private String tenDenKho;
    private Integer nguoiTaoId;
    private String tenNguoiTao;
    private Integer trangThai;
    private LocalDateTime ngayTao;
}
