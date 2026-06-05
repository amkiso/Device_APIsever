package com.example.device_apisever.dto.bangiao;

import lombok.Data;
import java.util.List;

@Data
public class BanGiaoThietBiRequest {
    private Integer thietBiId;
    private String nguoiNhan;
    private String ghiChuTinhTrang;
    private List<String> imageFileNames;
    private String signatureFileName;
}
