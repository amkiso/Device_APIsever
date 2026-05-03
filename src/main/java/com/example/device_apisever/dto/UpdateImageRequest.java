package com.example.device_apisever.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO nhận từ client sau khi đã upload ảnh lên Azure thành công.
 * <p>
 * Client gửi fileName (tên file UUID mà server đã cấp) để server lưu vào bảng HinhAnhThietBi.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateImageRequest {

    /** Tên file UUID đã upload lên Azure (bắt buộc) */
    @NotBlank(message = "fileName không được để trống")
    private String fileName;

    /** Loại ảnh: 1 = Ảnh sản phẩm, 2 = Biên bản, 3 = Sự cố... (tùy chọn, mặc định = 1) */
    private Integer loaiAnhId = 1;

    /** ID bàn giao liên kết (tùy chọn) */
    private Integer banGiaoId;

    /** ID bảo trì liên kết (tùy chọn) */
    private Integer baoTriId;
}
