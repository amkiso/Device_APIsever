package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Giỏ hàng tạm cho Khách hàng (VaiTroID=4).
 * Xóa sau khi tạo hợp đồng thành công.
 * UNIQUE(NguoiDungID, ThietBiID) — không thêm trùng thiết bị.
 */
@Entity
@Table(name = "GioHang", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NguoiDungID", "ThietBiID"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GioHangID")
    private Integer gioHangId;

    @Column(name = "NguoiDungID", nullable = false)
    private Integer nguoiDungId;

    @Column(name = "ThietBiID", nullable = false)
    private Integer thietBiId;

    @Column(name = "NgayThem", nullable = false, updatable = false)
    private LocalDateTime ngayThem;

    @PrePersist
    protected void onCreate() {
        if (ngayThem == null) ngayThem = LocalDateTime.now();
    }
}
