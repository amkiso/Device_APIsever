package com.example.device_apisever.controller;

import com.example.device_apisever.dto.*;
import com.example.device_apisever.service.HopDongService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/hop-dong")
public class HopDongController {

    private final HopDongService hopDongService;

    public HopDongController(HopDongService hopDongService) {
        this.hopDongService = hopDongService;
    }

    /**
     * POST /api/hop-dong/tao — Tạo hợp đồng mới từ checkout
     */
    @PostMapping("/tao")
    public ResponseEntity<ApiResponse<HopDongResponse>> taoHopDong(
            Authentication auth,
            @Valid @RequestBody TaoHopDongRequest request) {
        HopDongResponse res = hopDongService.taoHopDong(auth.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo hợp đồng thành công", res));
    }

    /**
     * POST /api/hop-dong/{id}/ky-ket — Ký hợp đồng điện tử
     */
    @PostMapping(value = "/{id}/ky-ket", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<KyHopDongResponse>> kyHopDong(
            Authentication auth,
            @PathVariable Integer id,
            @Valid @RequestBody KyHopDongRequest request,
            HttpServletRequest httpRequest) {
        try {
            String ip = httpRequest.getRemoteAddr();
            String device = httpRequest.getHeader("User-Agent");

            KyHopDongResponse res = hopDongService.kyHopDong(
                    auth.getName(), id, request.getFileName(), request.getMaPin(), ip, device);
            return ResponseEntity.ok(ApiResponse.ok("Ký hợp đồng thành công", res));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi ký hợp đồng: " + e.getMessage()));
        }
    }

    /**
     * POST /api/hop-dong/{id}/xac-nhan-thanh-toan — Callback xác nhận thanh toán
     */
    @PostMapping("/{id}/xac-nhan-thanh-toan")
    public ResponseEntity<ApiResponse<XacNhanThanhToanResponse>> xacNhanThanhToan(
            @PathVariable Integer id,
            @Valid @RequestBody XacNhanThanhToanRequest request) {
        XacNhanThanhToanResponse res = hopDongService.xacNhanThanhToan(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Xác nhận thanh toán thành công", res));
    }

    /**
     * GET /api/hop-dong/cua-toi — Lấy danh sách tất cả hợp đồng của khách hàng
     */
    @GetMapping("/cua-toi")
    public ResponseEntity<ApiResponse<java.util.List<HopDongSummaryResponse>>> getMyContracts(
            Authentication auth) {
        var list = hopDongService.getMyContracts(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Success", list));
    }

    /**
     * GET /api/hop-dong/gan-nhat?limit=5 — Lấy N hợp đồng gần nhất (cho label trang chủ)
     */
    @GetMapping("/gan-nhat")
    public ResponseEntity<ApiResponse<java.util.List<HopDongSummaryResponse>>> getRecentContracts(
            Authentication auth,
            @RequestParam(defaultValue = "5") int limit) {
        var list = hopDongService.getRecentContracts(auth.getName(), limit);
        return ResponseEntity.ok(ApiResponse.ok("Success", list));
    }

    /**
     * GET /api/hop-dong/{id}/chi-tiet — Xem chi tiết hợp đồng (xem lại hợp đồng)
     */
    @GetMapping("/{id}/chi-tiet")
    public ResponseEntity<ApiResponse<HopDongDetailResponse>> getContractDetail(
            Authentication auth,
            @PathVariable Integer id) {
        HopDongDetailResponse res = hopDongService.getContractDetail(auth.getName(), id);
        return ResponseEntity.ok(ApiResponse.ok("Success", res));
    }

    /**
     * GET /api/hop-dong/don-hang-count — Đếm số đơn hàng theo trạng thái (cho badge hồ sơ)
     */
    @GetMapping("/don-hang-count")
    public ResponseEntity<ApiResponse<DonHangCountResponse>> getDonHangCount(
            Authentication auth) {
        DonHangCountResponse res = hopDongService.getDonHangCount(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Success", res));
    }

    /**
     * POST /api/hop-dong/{id}/huy — Khách hàng hủy hợp đồng (chỉ khi chờ xác nhận)
     */
    @PostMapping("/{id}/huy")
    public ResponseEntity<ApiResponse<Void>> huyHopDong(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody(required = false) HuyHopDongRequest request) {
        String lyDo = request != null ? request.getLyDoHuy() : "Khách hàng tự hủy";
        hopDongService.huyHopDong(auth.getName(), id, lyDo);
        return ResponseEntity.ok(ApiResponse.ok("Hủy hợp đồng thành công", null));
    }

    /**
     * POST /api/hop-dong/{id}/yeu-cau-ho-tro — Gửi yêu cầu hỗ trợ / bảo trì
     */
    @PostMapping("/{id}/yeu-cau-ho-tro")
    public ResponseEntity<ApiResponse<Void>> yeuCauHoTro(
            Authentication auth,
            @PathVariable Integer id,
            @RequestBody YeuCauHoTroRequest request) {
        hopDongService.guiYeuCauHoTro(auth.getName(), id, request);
        return ResponseEntity.ok(ApiResponse.ok("Đã gửi yêu cầu hỗ trợ", null));
    }

    /**
     * POST /api/hop-dong/{id}/thanh-toan-demo — Demo thanh toán (cọc hoặc nợ)
     */
    @PostMapping("/{id}/thanh-toan-demo")
    public ResponseEntity<ApiResponse<XacNhanThanhToanResponse>> thanhToanDemo(
            Authentication auth,
            @PathVariable Integer id) {
        XacNhanThanhToanResponse res = hopDongService.thanhToanDemo(auth.getName(), id);
        return ResponseEntity.ok(ApiResponse.ok("Thanh toán thành công", res));
    }
}

