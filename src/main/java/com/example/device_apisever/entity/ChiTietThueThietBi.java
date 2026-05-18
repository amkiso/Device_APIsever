package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Chi tiết từng thiết bị trong hợp đồng.
 * 1 hợp đồng có thể thuê nhiều thiết bị, mỗi thiết bị có giá riêng tại thời điểm ký.
 */
@Entity
@Table(name = "ChiTietThueThietBi")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChiTietThueThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChiTietID")
    private Integer chiTietId;

    @Column(name = "HopDongID", nullable = false)
    private Integer hopDongId;

    @Column(name = "ThietBiID", nullable = false)
    private Integer thietBiId;

    @Column(name = "GiaThueThang", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaThueThang;

    @Column(name = "GiaTriMay", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTriMay;

    @Column(name = "TinhTrangGiao", length = 1000)
    private String tinhTrangGiao;

    @Column(name = "TinhTrangTra", length = 1000)
    private String tinhTrangTra;

    @Column(name = "NgayGiao")
    private LocalDateTime ngayGiao;

    @Column(name = "NgayTra")
    private LocalDateTime ngayTra;
}
