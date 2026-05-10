package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.entity.LoaiThietBi;
import com.example.device_apisever.service.LoaiThietBiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loai-thiet-bi")
public class LoaiThietBiController {

    private final LoaiThietBiService loaiThietBiService;

    public LoaiThietBiController(LoaiThietBiService loaiThietBiService) {
        this.loaiThietBiService = loaiThietBiService;
    }

    /**
     * Lấy tất cả loại thiết bị, hoặc lọc theo danh mục nếu có query param
     * GET /api/loai-thiet-bi
     * GET /api/loai-thiet-bi?danhMucId={id}
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LoaiThietBi>>> getAll(
            @RequestParam(required = false) Integer danhMucId) {
        List<LoaiThietBi> result;
        if (danhMucId != null) {
            result = loaiThietBiService.findByDanhMucId(danhMucId);
        } else {
            result = loaiThietBiService.findAll();
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * Lấy chi tiết 1 loại thiết bị
     * GET /api/loai-thiet-bi/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoaiThietBi>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(loaiThietBiService.findById(id)));
    }

    /**
     * Tìm kiếm loại thiết bị theo tên
     * GET /api/loai-thiet-bi/search?q={keyword}
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<LoaiThietBi>>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(loaiThietBiService.search(q)));
    }

    /**
     * Tạo loại thiết bị mới
     * POST /api/loai-thiet-bi
     */
    @PostMapping
    public ResponseEntity<ApiResponse<LoaiThietBi>> create(@RequestBody LoaiThietBi loaiThietBi) {
        LoaiThietBi created = loaiThietBiService.create(loaiThietBi);
        return ResponseEntity.ok(ApiResponse.ok("Tạo loại thiết bị thành công", created));
    }

    /**
     * Cập nhật loại thiết bị
     * PUT /api/loai-thiet-bi/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LoaiThietBi>> update(
            @PathVariable Integer id,
            @RequestBody LoaiThietBi loaiThietBi) {
        LoaiThietBi updated = loaiThietBiService.update(id, loaiThietBi);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật loại thiết bị thành công", updated));
    }

    /**
     * Xóa loại thiết bị
     * DELETE /api/loai-thiet-bi/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        loaiThietBiService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa loại thiết bị ID: " + id, null));
    }
}
