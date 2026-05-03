package com.example.device_apisever.entity;

import lombok.*;
import java.io.Serializable;

/**
 * Composite key cho bảng XacNhanDieuKhoan: (HopDongID, DieuKhoanID)
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class XacNhanDieuKhoanId implements Serializable {
    private Integer hopDongId;
    private Integer dieuKhoanId;
}
