package com.example.device_apisever.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminDuyetLenhRequest {
    @NotNull private Integer trangThai; // 2=Đang VC, 3=Hoàn tất, 4=Hủy
    private String ghiChu;
}
