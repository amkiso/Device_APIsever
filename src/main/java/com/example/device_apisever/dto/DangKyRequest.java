package com.example.device_apisever.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO cho đăng ký tài khoản Khách hàng (UC03).
 * Email sẽ được dùng làm TaiKhoan.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DangKyRequest {

    @NotBlank(message = "Ho ten khong duoc de trong")
    @Size(max = 150, message = "Ho ten toi da 150 ky tu")
    private String hoTen;

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong hop le")
    @Size(max = 100, message = "Email toi da 100 ky tu")
    private String email;

    @NotBlank(message = "Mat khau khong duoc de trong")
    @Size(min = 6, message = "Mat khau phai co it nhat 6 ky tu")
    private String matKhau;

    @NotBlank(message = "So dien thoai khong duoc de trong")
    @Size(max = 20, message = "So dien thoai toi da 20 ky tu")
    private String soDienThoai;

    @NotNull(message = "Loai khach hang khong duoc de trong")
    @Min(value = 1, message = "Loai khach hang khong hop le")
    @Max(value = 2, message = "Loai khach hang khong hop le")
    private Integer loaiKhachHangId;  // 1=Cá nhân, 2=Doanh nghiệp

    @Size(max = 255, message = "Dia chi toi da 255 ky tu")
    private String diaChi;

    // Chỉ bắt buộc khi loaiKhachHangId = 2 (Doanh nghiệp)
    @Size(max = 50, message = "Ma so thue toi da 50 ky tu")
    private String maSoThue;
}
