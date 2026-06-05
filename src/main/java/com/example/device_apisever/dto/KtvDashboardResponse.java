package com.example.device_apisever.dto;

import com.example.device_apisever.dto.DashboardResponse.NhacNhoItem;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KtvDashboardResponse {
    private long soPhieuDaHoanThanh;
    private long soPhieuChoNhan;
    private long soPhieuDangThucHien;
    private long soYeuCauGiaoNhan;
    private List<NhacNhoItem> lichBaoTriHomNay;
}
