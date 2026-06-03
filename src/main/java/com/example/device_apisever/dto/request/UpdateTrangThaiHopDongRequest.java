package com.example.device_apisever.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrangThaiHopDongRequest {
    @NotNull(message = "Trạng thái ID không được để trống")
    private Integer trangThaiId;
    
    private String lyDoHuy; // Có thể null nếu không phải là thao tác hủy
}
