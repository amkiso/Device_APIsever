package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.request.UpdateTrangThaiHopDongRequest;
import com.example.device_apisever.dto.response.AdminHopDongResponse;
import com.example.device_apisever.dto.response.ChiTietHopDongAdminResponse;
import com.example.device_apisever.dto.response.ChuKyDienTuResponse;
import com.example.device_apisever.service.AdminHopDongService;
import com.example.device_apisever.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/hop-dong")
public class AdminHopDongController {

    private final AdminHopDongService adminHopDongService;
    private final JwtService jwtService;

    public AdminHopDongController(AdminHopDongService adminHopDongService, JwtService jwtService) {
        this.adminHopDongService = adminHopDongService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminHopDongResponse>>> getDanhSachHopDong(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Integer trangThaiId,
            @RequestParam(required = false) Integer loaiHopDongId) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminHopDongResponse> result = adminHopDongService.getDanhSachHopDong(q, startDate, endDate, trangThaiId, loaiHopDongId, pageable);
        
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách thành công", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChiTietHopDongAdminResponse>> getChiTietHopDong(@PathVariable Integer id) {
        ChiTietHopDongAdminResponse response = adminHopDongService.getChiTietHopDong(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy chi tiết thành công", response));
    }

    @PutMapping("/{id}/trang-thai")
    public ResponseEntity<ApiResponse<Void>> updateTrangThai(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateTrangThaiHopDongRequest request) {
        adminHopDongService.updateTrangThai(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật trạng thái thành công", null));
    }

    @GetMapping("/{id}/chu-ky")
    public ResponseEntity<ApiResponse<ChuKyDienTuResponse>> getChuKyKhachHang(
            @PathVariable Integer id,
            Authentication auth,
            HttpServletRequest request) {
        
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        Integer currentUserId = null;
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                currentUserId = jwtService.extractNguoiDungId(authHeader.substring(7));
            }
        } catch (Exception ignored) { }

        ChuKyDienTuResponse response = adminHopDongService.getChuKyKhachHang(id, currentUserId, isAdmin);
        return ResponseEntity.ok(ApiResponse.ok("Lấy chữ ký thành công", response));
    }
}
