package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.entity.Kho;
import com.example.device_apisever.repository.KhoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kho")
public class KhoController {

    private final KhoRepository khoRepository;

    public KhoController(KhoRepository khoRepository) {
        this.khoRepository = khoRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Kho>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(khoRepository.findAll()));
    }
}
