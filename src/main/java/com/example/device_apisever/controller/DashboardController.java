package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.DashboardResponse;
import com.example.device_apisever.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final com.example.device_apisever.config.JwtService jwtService;

    public DashboardController(DashboardService dashboardService, com.example.device_apisever.config.JwtService jwtService) {
        this.dashboardService = dashboardService;
        this.jwtService = jwtService;
    }

    /**
     * Lấy dữ liệu tổng hợp cho trang chủ Admin.
     * Yêu cầu đăng nhập (JWT token).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        DashboardResponse data = dashboardService.getDashboardData();
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * Lấy dữ liệu tổng hợp cho trang chủ Thủ Kho.
     */
    @GetMapping("/thu-kho")
    public ResponseEntity<ApiResponse<com.example.device_apisever.dto.ThuKhoDashboardResponse>> getThuKhoDashboard(
            jakarta.servlet.http.HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        Integer khoId = jwtService.extractKhoId(authHeader.substring(7));
        com.example.device_apisever.dto.ThuKhoDashboardResponse data = dashboardService.getThuKhoDashboard(khoId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * Lấy dữ liệu tổng hợp cho trang chủ Kỹ thuật viên.
     */
    @GetMapping("/ktv")
    public ResponseEntity<ApiResponse<com.example.device_apisever.dto.KtvDashboardResponse>> getKtvDashboard(
            jakarta.servlet.http.HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        Integer nguoiDungId = jwtService.extractNguoiDungId(authHeader.substring(7));
        com.example.device_apisever.dto.KtvDashboardResponse data = dashboardService.getKtvDashboard(nguoiDungId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
