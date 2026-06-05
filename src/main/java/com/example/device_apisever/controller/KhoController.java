package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.entity.Kho;
import com.example.device_apisever.service.KhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kho")
public class KhoController {

    private final KhoService khoService;

    public KhoController(KhoService khoService) {
        this.khoService = khoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Kho>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(khoService.getAllKho()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Kho>> create(@RequestBody Kho request) {
        return ResponseEntity.ok(ApiResponse.ok(khoService.createKho(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Kho>> update(@PathVariable Integer id, @RequestBody Kho request) {
        return ResponseEntity.ok(ApiResponse.ok(khoService.updateKho(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        khoService.deleteKho(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
