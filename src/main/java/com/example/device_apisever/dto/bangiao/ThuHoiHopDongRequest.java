package com.example.device_apisever.dto.bangiao;

import lombok.Data;
import java.util.List;

@Data
public class ThuHoiHopDongRequest {
    private List<ThuHoiThietBiRequest> danhSachThuHoi;
}
