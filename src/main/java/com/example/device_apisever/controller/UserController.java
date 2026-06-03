package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.request.ChangePinRequest;
import com.example.device_apisever.dto.request.SetupPinRequest;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.repository.NguoiDungRepository;
import com.example.device_apisever.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(NguoiDungRepository nguoiDungRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    private Integer extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtService.extractNguoiDungId(authHeader.substring(7));
        }
        throw new RuntimeException("Lỗi xác thực");
    }

    @PostMapping("/setup-pin")
    public ResponseEntity<ApiResponse<Void>> setupPin(@Valid @RequestBody SetupPinRequest request, HttpServletRequest httpRequest) {
        Integer userId = extractUserId(httpRequest);
        NguoiDung nd = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (nd.getMaPin() != null && !nd.getMaPin().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mã PIN đã được thiết lập trước đó"));
        }

        nd.setMaPin(passwordEncoder.encode(request.getNewPin()));
        nguoiDungRepository.save(nd);

        return ResponseEntity.ok(ApiResponse.ok("Thiết lập mã PIN thành công", null));
    }

    @PutMapping("/change-pin")
    public ResponseEntity<ApiResponse<Void>> changePin(@Valid @RequestBody ChangePinRequest request, HttpServletRequest httpRequest) {
        Integer userId = extractUserId(httpRequest);
        NguoiDung nd = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (nd.getMaPin() == null || nd.getMaPin().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Người dùng chưa thiết lập mã PIN"));
        }

        if (!passwordEncoder.matches(request.getOldPin(), nd.getMaPin())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mã PIN hiện tại không chính xác"));
        }

        nd.setMaPin(passwordEncoder.encode(request.getNewPin()));
        nguoiDungRepository.save(nd);

        return ResponseEntity.ok(ApiResponse.ok("Đổi mã PIN thành công", null));
    }
}
