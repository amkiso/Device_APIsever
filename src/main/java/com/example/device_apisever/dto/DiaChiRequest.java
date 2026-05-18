package com.example.device_apisever.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO cho request tạo/cập nhật địa chỉ giao hàng.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiaChiRequest {

    @NotBlank(message = "Tên người nhận không được để trống")
    private String tenNguoiNhan;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String soDienThoai;

    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String tinhThanhPho;

    @NotBlank(message = "Phường/Xã không được để trống")
    private String phuongXa;

    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    private String diaChiChiTiet;

    private String donVi;

    @NotNull(message = "Loại địa chỉ không được để trống")
    private Integer loaiDiaChi;

    private Boolean laMacDinh;
}
