package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.entity.NhaCungCap;
import com.example.device_apisever.service.NhaCungCapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nha-cung-cap")
public class NhaCungCapController {

    private final NhaCungCapService nhaCungCapService;

    public NhaCungCapController(NhaCungCapService nhaCungCapService) {
        this.nhaCungCapService = nhaCungCapService;
    }

    /**
     * Lấy tất cả nhà cung cấp
     * GET /api/nha-cung-cap
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NhaCungCap>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(nhaCungCapService.findAll()));
    }

    /**
     * Lấy 1 nhà cung cấp theo ID
     * GET /api/nha-cung-cap/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NhaCungCap>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(nhaCungCapService.findById(id)));
    }
}
