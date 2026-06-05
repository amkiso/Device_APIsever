package com.example.device_apisever.dto.bangiao;

import lombok.Data;
import java.util.List;

@Data
public class ThuHoiThietBiRequest {
    private Integer thietBiId;
    private Integer mucDanhGiaId;
    private String ghiChuTinhTrang;
    private List<String> imageFileNames;
}
