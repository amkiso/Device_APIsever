package com.example.device_apisever.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO trả về thông tin thiết bị cụ thể (serial) khi lọc theo loại thiết bị.
 * Gộp dữ liệu từ: ThietBi, TinhTrangThietBi, Kho.
 * Dùng cho: GET /api/thiet-bi?loaiThietBiId={id}
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThietBiByLoaiDTO {

    private Integer thietBiId;
    private String maTaiSan;           // "MK-001"
    private Integer tinhTrangId;        // 1,2,3,4
    private String tenTinhTrang;        // "Sẵn sàng"
    private Integer khoHienTaiId;
    private String tenKho;              // "Kho A" — JOIN từ bảng Kho
    private LocalDateTime ngayBaoTriTiepTheo;
    private String qrCodeUrl;
}
