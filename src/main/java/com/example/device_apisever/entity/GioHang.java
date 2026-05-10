package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Giỏ hàng tạm cho Khách hàng (VaiTroID=4).
 * Xóa sau khi tạo hợp đồng thành công.
 * UNIQUE(NguoiDungID, LoaiThietBiID) — không thêm trùng loại thiết bị.
 */
@Entity
@Table(name = "GioHang", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NguoiDungID", "LoaiThietBiID"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GioHangID")
    private Integer gioHangId;

    @Column(name = "NguoiDungID", nullable = false)
    private Integer nguoiDungId;

    @Column(name = "LoaiThietBiID", nullable = false)
    private Integer loaiThietBiId;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "NgayThem", nullable = false, updatable = false)
    private LocalDateTime ngayThem;

    @PrePersist
    protected void onCreate() {
        if (ngayThem == null) ngayThem = LocalDateTime.now();
        if (soLuong == null) soLuong = 1;
    }
}
