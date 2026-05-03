package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Lịch sử bàn giao thiết bị (mỗi lần xuất / thu hồi / chuyển kho = 1 dòng).
 * DenKhoID = NULL khi giao ra ngoài cho khách hàng.
 * HopDongID = NULL khi chuyển kho nội bộ không gắn với HĐ nào.
 */
@Entity
@Table(name = "LichSuBanGiao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LichSuBanGiao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BanGiaoID")
    private Integer banGiaoId;

    @Column(name = "HopDongID")
    private Integer hopDongId;

    @Column(name = "ThietBiID", nullable = false)
    private Integer thietBiId;

    @Column(name = "TuKhoID", nullable = false)
    private Integer tuKhoId;

    @Column(name = "DenKhoID")
    private Integer denKhoId;

    @Column(name = "NguoiDungThucHienID", nullable = false)
    private Integer nguoiDungThucHienId;

    @Column(name = "NgayGiaoNhan", nullable = false)
    private LocalDateTime ngayGiaoNhan;

    @Column(name = "NguoiNhan", length = 100)
    private String nguoiNhan;

    @Column(name = "HinhAnhXacNhan", length = 500)
    private String hinhAnhXacNhan;

    @Column(name = "LoaiGiaoDichID", nullable = false)
    private Integer loaiGiaoDichId;

    @Column(name = "MucDanhGiaID")
    private Integer mucDanhGiaId;

    @Column(name = "GhiChu_TinhTrang", length = 500)
    private String ghiChuTinhTrang;

    @PrePersist
    protected void onCreate() {
        if (ngayGiaoNhan == null) ngayGiaoNhan = LocalDateTime.now();
    }
}
