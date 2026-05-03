package com.example.device_apisever.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private Integer nguoiDungId;
    private String hoTen;
    private String maNguoiDung;
    private Integer vaiTroId;
    private String tenVaiTro;
    private Integer khoId;           // null cho Admin và KH
    private Boolean doiMatKhauLanDau; // true = phải đổi mật khẩu trước khi sử dụng
}
