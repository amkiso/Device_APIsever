package com.example.device_apisever.controller;

import com.example.device_apisever.entity.NhaCungCap;
import com.example.device_apisever.service.AdminNhaCungCapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/nha-cung-cap")
public class AdminNhaCungCapController {

    private final AdminNhaCungCapService adminNhaCungCapService;

    public AdminNhaCungCapController(AdminNhaCungCapService adminNhaCungCapService) {
        this.adminNhaCungCapService = adminNhaCungCapService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String keyword) {
        List<NhaCungCap> list = adminNhaCungCapService.getAll(keyword);
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Integer id) {
        NhaCungCap ncc = adminNhaCungCapService.getDetail(id);
        return ResponseEntity.ok(Map.of("success", true, "data", ncc));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NhaCungCap request) {
        NhaCungCap newNcc = adminNhaCungCapService.create(request);
        return ResponseEntity.ok(Map.of("success", true, "message", "Thêm nhà cung cấp thành công", "data", newNcc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody NhaCungCap request) {
        NhaCungCap updated = adminNhaCungCapService.update(id, request);
        return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật nhà cung cấp thành công", "data", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        adminNhaCungCapService.delete(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Xóa nhà cung cấp thành công"));
    }
}
