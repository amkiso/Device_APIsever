package com.example.device_apisever.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NhapKhoFormRequest {
    private Integer nhaCungCapId;
    private String ghiChu;
    private Integer khoHienTaiId;
    private List<NhapKhoRequest> thietBis;
}
