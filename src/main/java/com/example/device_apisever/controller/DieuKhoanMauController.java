package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.DieuKhoanMauResponse;
import com.example.device_apisever.service.DieuKhoanMauService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dieu-khoan-mau")
public class DieuKhoanMauController {

    private final DieuKhoanMauService service;

    public DieuKhoanMauController(DieuKhoanMauService service) {
        this.service = service;
    }

    /**
     * GET /api/dieu-khoan-mau — Lấy toàn bộ điều khoản mẫu
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DieuKhoanMauResponse>>> getAll() {
        List<DieuKhoanMauResponse> list = service.getAll();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}
