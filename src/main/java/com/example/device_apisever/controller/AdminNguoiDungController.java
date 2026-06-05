package com.example.device_apisever.controller;

import com.example.device_apisever.config.JwtService;
import com.example.device_apisever.dto.admin.*;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.service.AdminNguoiDungService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/nguoi-dung")
public class AdminNguoiDungController {

    private final AdminNguoiDungService adminNguoiDungService;
    private final JwtService jwtService;

    public AdminNguoiDungController(AdminNguoiDungService adminNguoiDungService, JwtService jwtService) {
        this.adminNguoiDungService = adminNguoiDungService;
        this.jwtService = jwtService;
    }

    private Integer getAdminIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.extractNguoiDungId(token);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer vaiTroId,
            @RequestParam(required = false) Integer trangThaiId,
            @RequestParam(required = false) Integer loaiKhachHangId,
            @RequestParam(required = false) String keyword) {
        
        Page<AdminNguoiDungListResponse> result = adminNguoiDungService.getAll(page, size, vaiTroId, trangThaiId, loaiKhachHangId, keyword);
        
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
        AdminNguoiDungDetailResponse detail = adminNguoiDungService.getDetail(id);
        return ResponseEntity.ok(Map.of("success", true, "data", detail));
    }

    @PostMapping("/{id}/thong-tin-nhan-cam")
    public ResponseEntity<?> getThongTinNhanCam(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {
        
        String maPin = body.get("maPin");
        if (maPin == null || maPin.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Vui lòng nhập mã PIN"));
        }

        Integer adminId = getAdminIdFromToken(authHeader);
        ThongTinNhanCamResponse data = adminNguoiDungService.getThongTinNhanCam(id, adminId, maPin);
        
        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AdminNguoiDungRequest request) {
        NguoiDung newUser = adminNguoiDungService.create(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Thêm người dùng thành công",
                "data", newUser
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody AdminNguoiDungRequest request) {
        NguoiDung updatedUser = adminNguoiDungService.update(id, request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cập nhật người dùng thành công",
                "data", updatedUser
        ));
    }

    @PutMapping("/{id}/trang-thai")
    public ResponseEntity<?> changeStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body,
            @RequestHeader("Authorization") String authHeader) {
        
        Integer trangThaiId = body.get("trangThaiId");
        if (trangThaiId == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Trạng thái không hợp lệ"));
        }

        Integer adminId = getAdminIdFromToken(authHeader);
        adminNguoiDungService.changeStatus(id, trangThaiId, adminId);
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật trạng thái thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        adminNguoiDungService.delete(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Xóa người dùng thành công"));
    }
}
