package com.example.device_apisever.controller;

import com.example.device_apisever.config.JwtService;
import com.example.device_apisever.dto.*;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.entity.ThongBao;
import com.example.device_apisever.repository.NguoiDungRepository;
import com.example.device_apisever.service.FCMService;
import com.example.device_apisever.service.ThongBaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ThongBaoController {

    private final ThongBaoService thongBaoService;
    private final FCMService fcmService;
    private final JwtService jwtService;
    private final NguoiDungRepository nguoiDungRepository;

    public ThongBaoController(ThongBaoService thongBaoService,
                              FCMService fcmService,
                              JwtService jwtService,
                              NguoiDungRepository nguoiDungRepository) {
        this.thongBaoService = thongBaoService;
        this.fcmService = fcmService;
        this.jwtService = jwtService;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    /**
     * Admin tạo thông báo mới.
     * POST /api/thong-bao
     */
    @PostMapping("/thong-bao")
    public ResponseEntity<ApiResponse<ThongBao>> taoThongBao(
            @Valid @RequestBody ThongBaoRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Lấy NguoiDungID từ JWT token
        String token = authHeader.replace("Bearer ", "");
        Integer nguoiGuiId = jwtService.extractNguoiDungId(token);

        ThongBao thongBao = thongBaoService.taoThongBao(request, nguoiGuiId);
        return ResponseEntity.ok(ApiResponse.ok("Tao thong bao thanh cong!", thongBao));
    }

    /**
     * Lấy danh sách thông báo của user đang đăng nhập.
     * GET /api/thong-bao
     */
    @GetMapping("/thong-bao")
    public ResponseEntity<ApiResponse<List<ThongBaoResponse>>> layDanhSachThongBao(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Integer nguoiDungId = jwtService.extractNguoiDungId(token);

        List<ThongBaoResponse> danhSach = thongBaoService.layDanhSachThongBao(nguoiDungId);
        return ResponseEntity.ok(ApiResponse.ok(danhSach));
    }

    /**
     * Đếm số thông báo chưa đọc (dùng cho badge chuông).
     * GET /api/thong-bao/chua-doc
     */
    @GetMapping("/thong-bao/chua-doc")
    public ResponseEntity<ApiResponse<Long>> demChuaDoc(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Integer nguoiDungId = jwtService.extractNguoiDungId(token);

        long count = thongBaoService.demChuaDoc(nguoiDungId);
        return ResponseEntity.ok(ApiResponse.ok(count));
    }

    /**
     * Đánh dấu 1 thông báo đã đọc.
     * PUT /api/thong-bao/{id}/da-doc
     */
    @PutMapping("/thong-bao/{id}/da-doc")
    public ResponseEntity<ApiResponse<Void>> danhDaDoc(
            @PathVariable("id") Integer thongBaoId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Integer nguoiDungId = jwtService.extractNguoiDungId(token);

        thongBaoService.danhDaDoc(thongBaoId, nguoiDungId);
        return ResponseEntity.ok(ApiResponse.ok("Da danh dau doc", null));
    }
    /**
     * Đánh dấu tất cả thông báo đã đọc.
     * PUT /api/thong-bao/da-doc-tat-ca
     */
    @PutMapping("/thong-bao/da-doc-tat-ca")
    public ResponseEntity<ApiResponse<Void>> danhDaDocTatCa(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Integer nguoiDungId = jwtService.extractNguoiDungId(token);

        thongBaoService.danhDaDocTatCa(nguoiDungId);
        return ResponseEntity.ok(ApiResponse.ok("Da danh dau doc tat ca", null));
    }

    /**
     * Client đăng ký FCM token sau khi đăng nhập.
     * POST /api/fcm/register-token
     */
    @PostMapping("/fcm/register-token")
    public ResponseEntity<ApiResponse<Void>> registerFCMToken(
            @Valid @RequestBody FCMTokenRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Integer nguoiDungId = jwtService.extractNguoiDungId(token);

        fcmService.registerToken(nguoiDungId, request.getToken(), request.getDeviceName());
        return ResponseEntity.ok(ApiResponse.ok("Dang ky FCM token thanh cong", null));
    }

    // === CÁC API DÀNH CHO ADMIN ===

    @GetMapping("/admin/thong-bao")
    public ResponseEntity<?> getAllForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer nguoiDungId,
            @RequestParam(required = false) String keyword) {
        Page<AdminThongBaoResponse> result = thongBaoService.layTatCaThongBaoAdmin(page, size, nguoiDungId, keyword);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "content", result.getContent(),
                        "totalPages", result.getTotalPages(),
                        "totalElements", result.getTotalElements(),
                        "currentPage", result.getNumber()
                )
        ));
    }

    @DeleteMapping("/admin/thong-bao/{id}")
    public ResponseEntity<?> deleteThongBao(@PathVariable Integer id) {
        thongBaoService.xoaThongBao(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Đã xóa thông báo"));
    }

    @GetMapping("/admin/thong-bao/nguoi-dung")
    public ResponseEntity<?> getDsNguoiDung() {
        List<NguoiDung> users = nguoiDungRepository.findAll();
        List<Map<String, Object>> result = users.stream()
                .map(u -> Map.<String, Object>of(
                        "nguoiDungId", u.getNguoiDungId(), 
                        "hoTen", u.getHoTen(), 
                        "vaiTroId", u.getVaiTroId()
                ))
                .toList();
        return ResponseEntity.ok(Map.of("success", true, "data", result));
    }
}
