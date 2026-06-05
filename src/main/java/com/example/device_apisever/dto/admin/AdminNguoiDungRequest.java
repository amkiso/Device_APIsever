package com.example.device_apisever.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminNguoiDungRequest {
    @NotBlank private String hoTen;
    private String taiKhoan;
    private String matKhau;
    @NotNull private Integer vaiTroId;
    private Integer khoId;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private Integer loaiKhachHangId;
    private String maSoThue;
    private String cccd;
    private String cccdNgayCap;
    private String cccdNoiCap;
    private String donViCongTac;
}
