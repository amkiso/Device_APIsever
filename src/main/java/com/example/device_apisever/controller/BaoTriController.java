package com.example.device_apisever.controller;

import com.example.device_apisever.dto.baotri.*;
import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.service.IBaoTriService;
import com.example.device_apisever.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bao-tri")
public class BaoTriController {

    @Autowired
    private IBaoTriService baoTriService;

    @Autowired
    private JwtService jwtService;

    private Integer extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtService.extractNguoiDungId(authHeader.substring(7));
        }
        throw new RuntimeException("Lỗi xác thực (Missing Token)");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BaoTriDto>>> getDanhSachBaoTri(
            @RequestParam(required = false) Integer trangThaiId,
            @RequestParam(required = false) Integer nguoiDungId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<BaoTriDto> result = baoTriService.getDanhSachBaoTri(trangThaiId, nguoiDungId, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BaoTriDetailDto>> getChiTietBaoTri(@PathVariable Integer id) {
        BaoTriDetailDto dto = baoTriService.getChiTietBaoTri(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping("/thong-ke")
    @PreAuthorize("hasRole('ADMIN')") // Only Admin can see this
    public ResponseEntity<ApiResponse<BaoTriThongKeDto>> getThongKeBaoTri() {
        BaoTriThongKeDto dto = baoTriService.getThongKeBaoTri();
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BaoTriDto>> createBaoTri(
            @RequestBody BaoTriCreateReq req,
            HttpServletRequest request) {
        Integer userId = extractUserId(request);
        BaoTriDto dto = baoTriService.createBaoTri(req, userId);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @PutMapping("/{id}/xac-nhan")
    public ResponseEntity<ApiResponse<BaoTriDto>> xacNhanYeuCau(
            @PathVariable Integer id,
            HttpServletRequest request) {
        Integer technicianId = extractUserId(request);
        BaoTriDto dto = baoTriService.xacNhanYeuCau(id, technicianId);
        return ResponseEntity.ok(ApiResponse.ok("Xác nhận yêu cầu thành công", dto));
    }

    @PostMapping("/{id}/ghi-nhan-su-co")
    public ResponseEntity<ApiResponse<BaoTriDto>> ghiNhanSuCo(
            @PathVariable Integer id,
            @RequestBody GhiNhanSuCoReq req,
            HttpServletRequest request) {
        Integer technicianId = extractUserId(request);
        BaoTriDto dto = baoTriService.ghiNhanSuCo(id, req, technicianId);
        return ResponseEntity.ok(ApiResponse.ok("Ghi nhận sự cố thành công", dto));
    }

    @PostMapping("/{id}/hoan-thanh")
    public ResponseEntity<ApiResponse<BaoTriDto>> hoanThanhBaoTri(
            @PathVariable Integer id,
            @RequestBody HoanThanhBaoTriReq req,
            HttpServletRequest request) {
        Integer technicianId = extractUserId(request);
        BaoTriDto dto = baoTriService.hoanThanhBaoTri(id, req, technicianId);
        return ResponseEntity.ok(ApiResponse.ok("Hoàn thành bảo trì thành công", dto));
    }
}
