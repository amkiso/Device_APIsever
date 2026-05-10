package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.ThietBiByLoaiDTO;
import com.example.device_apisever.dto.ThietBiDetailResponse;
import com.example.device_apisever.entity.ThietBi;
import com.example.device_apisever.service.ThietBiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thiet-bi")
public class ThietBiController {

    private final ThietBiService thietBiService;

    public ThietBiController(ThietBiService thietBiService) {
        this.thietBiService = thietBiService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer loaiThietBiId) {
        if (loaiThietBiId != null) {
            // Lọc thiết bị theo loại → trả về DTO có tên kho, tên tình trạng
            List<ThietBiByLoaiDTO> result = thietBiService.findByLoaiThietBiId(loaiThietBiId);
            return ResponseEntity.ok(ApiResponse.ok(result));
        }
        // Lấy tất cả thiết bị (raw entity)
        return ResponseEntity.ok(ApiResponse.ok(thietBiService.findAll()));
    }

    /**
     * Tra cứu toàn bộ thông tin thiết bị theo Mã Tài Sản.
     * Ví dụ: GET /api/thiet-bi/tra-cuu/TB001
     */
    @GetMapping("/tra-cuu/{maTaiSan}")
    public ResponseEntity<ApiResponse<ThietBiDetailResponse>> traCuuTheoMa(@PathVariable String maTaiSan) {
        ThietBiDetailResponse detail = thietBiService.findDetailByMaTaiSan(maTaiSan);
        return ResponseEntity.ok(ApiResponse.ok(detail));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ThietBi>> create(@RequestBody ThietBi thietBi) {
        return ResponseEntity.ok(ApiResponse.ok("Tao thiet bi thanh cong", thietBiService.save(thietBi)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        thietBiService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("Da xoa thiet bi ID: " + id, null));
    }
}