package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LenhChuyenKho")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LenhChuyenKho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LenhChuyenKhoID")
    private Integer lenhChuyenKhoId;

    @Column(name = "TuKhoID", nullable = false)
    private Integer tuKhoId;

    @Column(name = "DenKhoID", nullable = false)
    private Integer denKhoId;

    @Column(name = "NguoiTaoID", nullable = false)
    private Integer nguoiTaoId;

    @Column(name = "NguoiThucHienID")
    private Integer nguoiThucHienId;

    @Column(name = "NguoiXacNhanID")
    private Integer nguoiXacNhanId;

    @Column(name = "TrangThai", nullable = false)
    private Integer trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TrangThai", insertable = false, updatable = false)
    private TrangThaiChuyenKho trangThaiChuyenKho;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;

    @Column(name = "NgayTao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "NgayHoanThat")
    private LocalDateTime ngayHoanThat;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = LocalDateTime.now();
        }
        if (trangThai == null) {
            trangThai = 1;
        }
    }
}
