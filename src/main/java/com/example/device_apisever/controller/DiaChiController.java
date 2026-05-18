package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.DiaChiRequest;
import com.example.device_apisever.dto.DiaChiResponse;
import com.example.device_apisever.service.DiaChiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dia-chi")
public class DiaChiController {

    private final DiaChiService diaChiService;

    public DiaChiController(DiaChiService diaChiService) {
        this.diaChiService = diaChiService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiaChiResponse>>> getAll(Authentication auth) {
        List<DiaChiResponse> list = diaChiService.getByUser(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DiaChiResponse>> create(
            Authentication auth, @Valid @RequestBody DiaChiRequest request) {
        DiaChiResponse res = diaChiService.create(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.ok("Tạo địa chỉ thành công", res));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiaChiResponse>> update(
            Authentication auth, @PathVariable Integer id,
            @Valid @RequestBody DiaChiRequest request) {
        DiaChiResponse res = diaChiService.update(auth.getName(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật địa chỉ thành công", res));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            Authentication auth, @PathVariable Integer id) {
        diaChiService.delete(auth.getName(), id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa địa chỉ", null));
    }
}
