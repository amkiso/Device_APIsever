package com.example.device_apisever.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoiMatKhauRequest {

    @NotBlank(message = "Mat khau cu khong duoc de trong")
    private String matKhauCu;

    @NotBlank(message = "Mat khau moi khong duoc de trong")
    @Size(min = 6, message = "Mat khau moi phai co it nhat 6 ky tu")
    private String matKhauMoi;
}
