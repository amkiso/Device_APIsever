package com.example.device_apisever.dto;

import lombok.*;

/**
 * DTO trả về cho client khi yêu cầu link upload ảnh.
 * <p>
 * - sasUrl: Đường link SAS để client dùng HTTP PUT upload file trực tiếp lên Azure.
 * - fileName: Tên file UUID server đã tạo, client gửi lại sau khi upload xong để server lưu vào DB.
 * - publicUrl: URL công khai để hiển thị ảnh (nếu container public).
 * - category: Loại container đã chọn ("user", "products", "work").
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SasUploadResponse {
    private String sasUrl;
    private String fileName;
    private String publicUrl;
    private String category;
}
