package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.entity.DanhMucThietBi;
import com.example.device_apisever.service.DanhMucService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danh-muc")
public class DanhMucController {

    private final DanhMucService danhMucService;

    public DanhMucController(DanhMucService danhMucService) {
        this.danhMucService = danhMucService;
    }

    /**
     * Lấy tất cả danh mục thiết bị
     * GET /api/danh-muc
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DanhMucThietBi>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(danhMucService.findAll()));
    }

    /**
     * Lấy 1 danh mục theo ID
     * GET /api/danh-muc/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DanhMucThietBi>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(danhMucService.findById(id)));
    }

    /**
     * Tạo danh mục mới
     * POST /api/danh-muc
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DanhMucThietBi>> create(@RequestBody DanhMucThietBi danhMuc) {
        DanhMucThietBi created = danhMucService.create(danhMuc);
        return ResponseEntity.ok(ApiResponse.ok("Tạo danh mục thành công", created));
    }

    /**
     * Cập nhật danh mục
     * PUT /api/danh-muc/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DanhMucThietBi>> update(
            @PathVariable Integer id,
            @RequestBody DanhMucThietBi danhMuc) {
        DanhMucThietBi updated = danhMucService.update(id, danhMuc);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật danh mục thành công", updated));
    }

    /**
     * Xóa danh mục
     * DELETE /api/danh-muc/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        danhMucService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa danh mục ID: " + id, null));
    }
}
