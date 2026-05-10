package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.GioHangRequest;
import com.example.device_apisever.dto.GioHangResponse;
import com.example.device_apisever.dto.GioHangUpdateRequest;
import com.example.device_apisever.service.GioHangService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gio-hang")
public class GioHangController {

    private final GioHangService gioHangService;

    public GioHangController(GioHangService gioHangService) {
        this.gioHangService = gioHangService;
    }

    /**
     * GET /api/gio-hang — Lấy danh sách giỏ hàng của user (từ JWT)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<GioHangResponse>>> getCart(Authentication authentication) {
        List<GioHangResponse> items = gioHangService.getByUser(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(items));
    }

    /**
     * POST /api/gio-hang — Thêm item vào giỏ hàng
     * Body: { "loaiThietBiId": 1, "soLuong": 1 }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<GioHangResponse>> addItem(
            Authentication authentication,
            @Valid @RequestBody GioHangRequest request) {
        GioHangResponse item = gioHangService.addItem(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.ok("Đã thêm vào giỏ hàng", item));
    }

    /**
     * PUT /api/gio-hang/{id} — Cập nhật số lượng
     * Body: { "soLuong": 3 }
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GioHangResponse>> updateQuantity(
            Authentication authentication,
            @PathVariable Integer id,
            @Valid @RequestBody GioHangUpdateRequest request) {
        GioHangResponse item = gioHangService.updateQuantity(authentication.getName(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật số lượng thành công", item));
    }

    /**
     * DELETE /api/gio-hang/{id} — Xóa item khỏi giỏ hàng
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            Authentication authentication,
            @PathVariable Integer id) {
        gioHangService.deleteItem(authentication.getName(), id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa khỏi giỏ hàng", null));
    }

    /**
     * GET /api/gio-hang/count — Đếm tổng items (badge)
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> countItems(Authentication authentication) {
        Integer count = gioHangService.countItems(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(count));
    }
}
