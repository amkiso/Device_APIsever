package com.example.device_apisever.dto.admin;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminLenhChuyenKhoDetailResponse {
    private Integer lenhChuyenKhoId;
    private Integer tuKhoId;
    private String tenTuKho;
    private Integer denKhoId;
    private String tenDenKho;
    private Integer nguoiTaoId;
    private String tenNguoiTao;
    private Integer nguoiThucHienId;
    private String tenNguoiThucHien;
    private Integer nguoiXacNhanId;
    private String tenNguoiXacNhan;
    private Integer trangThai;
    private String ghiChu;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayHoanThat;
    
    private List<ChiTietChuyenKhoDTO> chiTiet;
}
