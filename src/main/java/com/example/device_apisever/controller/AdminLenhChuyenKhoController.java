package com.example.device_apisever.controller;

import com.example.device_apisever.config.JwtService;
import com.example.device_apisever.dto.admin.*;
import com.example.device_apisever.entity.LenhChuyenKho;
import com.example.device_apisever.service.AdminLenhChuyenKhoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/lenh-chuyen-kho")
public class AdminLenhChuyenKhoController {

    private final AdminLenhChuyenKhoService adminLenhChuyenKhoService;
    private final JwtService jwtService;

    public AdminLenhChuyenKhoController(AdminLenhChuyenKhoService adminLenhChuyenKhoService, JwtService jwtService) {
        this.adminLenhChuyenKhoService = adminLenhChuyenKhoService;
        this.jwtService = jwtService;
    }

    private Integer getUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.extractNguoiDungId(token);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) Integer khoId,
            @RequestParam(required = false) Integer nguoiThucHienId) {
        
        Page<AdminLenhChuyenKhoListResponse> result = adminLenhChuyenKhoService.getAll(page, size, trangThai, khoId, nguoiThucHienId);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "content", result.getContent(),
                        "totalPages", result.getTotalPages(),
                        "totalElements", result.getTotalElements(),
                        "currentPage", result.getNumber()
                )
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Integer id) {
        AdminLenhChuyenKhoDetailResponse detail = adminLenhChuyenKhoService.getDetail(id);
        return ResponseEntity.ok(Map.of("success", true, "data", detail));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody AdminLenhChuyenKhoRequest request,
            @RequestHeader("Authorization") String authHeader) {
        Integer userId = getUserIdFromToken(authHeader);
        LenhChuyenKho newLenh = adminLenhChuyenKhoService.create(request, userId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Tạo lệnh chuyển kho thành công",
                "data", newLenh
        ));
    }

    @PutMapping("/{id}/duyet")
    public ResponseEntity<?> duyetLenh(
            @PathVariable Integer id,
            @Valid @RequestBody AdminDuyetLenhRequest request,
            @RequestHeader("Authorization") String authHeader) {
        Integer userId = getUserIdFromToken(authHeader);
        adminLenhChuyenKhoService.duyetLenh(id, request, userId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật lệnh chuyển kho thành công"));
    }
}
