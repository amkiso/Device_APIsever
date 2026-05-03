package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Mức đánh giá tình trạng thiết bị khi thu hồi.
 * TinhVaoBoiThuong = true → phát sinh phí bồi thường.
 * 1=Tốt, 2=Hao mòn thông thường, 3=Hư hỏng do sử dụng, 4=Mất thiết bị
 */
@Entity
@Table(name = "MucDanhGia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MucDanhGia {

    @Id
    @Column(name = "MucDanhGiaID")
    private Integer mucDanhGiaId;

    @Column(name = "TenMuc", nullable = false, length = 60)
    private String tenMuc;

    @Column(name = "TinhVaoBoiThuong", nullable = false)
    private Boolean tinhVaoBoiThuong;

    @Column(name = "MoTa", length = 255)
    private String moTa;
}
