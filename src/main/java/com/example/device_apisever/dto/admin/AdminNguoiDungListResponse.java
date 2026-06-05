package com.example.device_apisever.dto.admin;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminNguoiDungListResponse {
    private Integer nguoiDungId;
    private String maNguoiDung;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private Integer vaiTroId;
    private String tenVaiTro;
    private Integer trangThaiId;
    private String tenTrangThai;
    private Integer loaiKhachHangId;
    private String tenLoaiKhachHang;
    private String avt;
    private LocalDateTime ngayTao;
}
