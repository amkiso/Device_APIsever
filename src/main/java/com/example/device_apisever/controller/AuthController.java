package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.CheckEmailResponse;
import com.example.device_apisever.dto.DangKyRequest;
import com.example.device_apisever.dto.DoiMatKhauRequest;
import com.example.device_apisever.dto.LoginRequest;
import com.example.device_apisever.dto.LoginResponse;
import com.example.device_apisever.dto.OtpConfirmRequest;
import com.example.device_apisever.dto.ForgotPasswordInitRequest;
import com.example.device_apisever.dto.ForgotPasswordConfirmRequest;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * UC02 — Đăng nhập (Khách hàng + Nhân viên dùng chung)
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Dang nhap thanh cong!", response));
    }

    /**
     * BƯỚC 1: Đăng ký - Nhận thông tin, sinh OTP và gửi qua Email
     */
    @PostMapping("/register-init")
    public ResponseEntity<ApiResponse<Void>> registerInit(@Valid @RequestBody DangKyRequest request) {
        authService.registerInit(request);
        return ResponseEntity.ok(ApiResponse.ok("Vui long kiem tra email de lay ma OTP", null));
    }

    /**
     * BƯỚC 2: Đăng ký - Nhận OTP để xác nhận và lưu vào DB
     */
    @PostMapping("/register-confirm")
    public ResponseEntity<ApiResponse<LoginResponse>> registerConfirm(@Valid @RequestBody OtpConfirmRequest request) {
        LoginResponse response = authService.registerConfirm(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.ok("Dang ky thanh cong!", response));
    }

    /**
     * BƯỚC 1: Quên mật khẩu - Nhận Email, sinh OTP
     */
    @PostMapping("/forgot-password-init")
    public ResponseEntity<ApiResponse<Void>> forgotPasswordInit(@Valid @RequestBody ForgotPasswordInitRequest request) {
        authService.forgotPasswordInit(request.getEmail());
        return ResponseEntity.ok(ApiResponse.ok("Vui long kiem tra email de lay ma OTP", null));
    }

    /**
     * BƯỚC 2: Quên mật khẩu - Nhận OTP và đổi mật khẩu mới
     */
    @PostMapping("/forgot-password-confirm")
    public ResponseEntity<ApiResponse<Void>> forgotPasswordConfirm(@Valid @RequestBody ForgotPasswordConfirmRequest request) {
        authService.forgotPasswordConfirm(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.ok("Doi mat khau thanh cong! Ban co the dang nhap bang mat khau moi.", null));
    }

    /**
     * Kiểm tra email trước khi quên mật khẩu.
     * Nếu là khách hàng → cho phép tự reset.
     * Nếu là nhân viên → trả SĐT admin để liên hệ.
     */
    @PostMapping("/check-email-reset")
    public ResponseEntity<ApiResponse<CheckEmailResponse>> checkEmailForReset(
            @RequestBody ForgotPasswordInitRequest request) {
        CheckEmailResponse response = authService.checkEmailForReset(request.getEmail());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    /**
     * UC04 — Đổi mật khẩu (tất cả vai trò)
     */
    @PostMapping("/doi-mat-khau")
    public ResponseEntity<ApiResponse<Void>> doiMatKhau(
            Authentication authentication,
            @Valid @RequestBody DoiMatKhauRequest request) {
        authService.doiMatKhau(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.ok("Doi mat khau thanh cong!", null));
    }

    /**
     * Lấy thông tin người dùng đang đăng nhập
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<NguoiDung>> getProfile(Authentication authentication) {
        NguoiDung nd = authService.getProfile(authentication.getName());
        // Không trả mật khẩu ra client
        nd.setMatKhau(null);
        return ResponseEntity.ok(ApiResponse.ok(nd));
    }
}
