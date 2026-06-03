package com.example.device_apisever.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThietBiHopDongResponse {
    private Integer chiTietId;
    private Integer thietBiId;
    private String maTaiSan;
    private String tenLoaiThietBi; // Lấy từ LoaiThietBi
    private String soSerial;
    private BigDecimal giaThueThang;
    private BigDecimal giaTriMay;
    private Integer tinhTrangId; // Tình trạng hiện tại của thiết bị
    private String tenTinhTrang;
    private String hinhAnhUrl; // Lấy từ HinhAnhThietBi hoặc LoaiThietBi
    private String tinhTrangGiao;
    private String tinhTrangTra;
    private LocalDateTime ngayGiao;
    private LocalDateTime ngayTra;
}
