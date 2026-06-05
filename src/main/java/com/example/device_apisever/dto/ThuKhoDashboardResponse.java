package com.example.device_apisever.dto;

import com.example.device_apisever.dto.DashboardResponse.NhacNhoItem;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThuKhoDashboardResponse {
    private long tongSoThietBi;
    private long soThietBiDangThue;
    private long soThietBiThanhLyHong;
    private long soThietBiDangBaoTri;
    private long soHopDongChoDuyet;
    private long soHopDongSapGiao;
    private long tonKhoHienTai;
    private List<NhacNhoItem> nhacNhoHomNay;
}
