package com.example.device_apisever.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePinRequest {
    @NotBlank(message = "Mã PIN hiện tại không được để trống")
    private String oldPin;

    @NotBlank(message = "Mã PIN mới không được để trống")
    @Pattern(regexp = "^[0-9]{6}$", message = "Mã PIN mới phải bao gồm đúng 6 chữ số")
    private String newPin;
}
