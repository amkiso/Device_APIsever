package com.example.device_apisever.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO để Admin tạo thông báo mới.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ThongBaoRequest {

    @NotBlank(message = "Tieu de khong duoc de trong")
    @Size(max = 200, message = "Tieu de toi da 200 ky tu")
    private String tieuDe;

    @NotBlank(message = "Noi dung khong duoc de trong")
    private String noiDung;

    @NotNull(message = "Loai thong bao khong duoc de trong")
    @Min(value = 1, message = "Loai thong bao khong hop le (1-3)")
    @Max(value = 3, message = "Loai thong bao khong hop le (1-3)")
    private Integer loaiThongBao; // 1=Tất cả, 2=Theo vai trò, 3=Cá nhân

    // Dùng khi loaiThongBao = 2 (gửi theo nhóm vai trò)
    private Integer vaiTroNhanId;

    // Dùng khi loaiThongBao = 3 (gửi cho 1 cá nhân cụ thể)
    private Integer nguoiDungNhanId;
}
