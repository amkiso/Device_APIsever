package com.example.device_apisever.dto.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AdminLenhChuyenKhoRequest {
    @NotNull private Integer tuKhoId;
    @NotNull private Integer denKhoId;
    private Integer nguoiThucHienId;
    private String ghiChu;
    
    @NotEmpty(message = "Danh sách thiết bị không được để trống")
    private List<ChiTietRequest> chiTiet;

    @Getter @Setter
    public static class ChiTietRequest {
        @NotNull private Integer thietBiId;
        private String ghiChu;
    }
}
