package com.example.device_apisever.dto.admin;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminNguoiDungDetailResponse {
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
    
    private String taiKhoan;
    private String diaChi;
    private String donViCongTac;
    private Integer khoId;
    private String tenKho;
    private LocalDateTime lanDangNhapCuoi;
    private Boolean doiMatKhauLanDau;
    private Boolean hasPin;
}
