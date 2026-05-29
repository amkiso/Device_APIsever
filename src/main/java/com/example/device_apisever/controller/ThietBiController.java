package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.QrCodeResponse;
import com.example.device_apisever.dto.ThietBiByLoaiDTO;
import com.example.device_apisever.dto.ThietBiDetailResponse;
import com.example.device_apisever.entity.ThietBi;
import com.example.device_apisever.service.QrCodeService;
import com.example.device_apisever.service.ThietBiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thiet-bi")
public class ThietBiController {

    private final ThietBiService thietBiService;
    private final QrCodeService qrCodeService;

    public ThietBiController(ThietBiService thietBiService, QrCodeService qrCodeService) {
        this.thietBiService = thietBiService;
        this.qrCodeService = qrCodeService;
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

    /**
     * Lấy hoặc tạo QR code cho thiết bị.
     * - Nếu đã có ảnh QR → trả về đường dẫn ảnh cũ.
     * - Nếu chưa có → tạo QR mới, lưu file, cập nhật DB, trả về đường dẫn.
     *
     * QR content format: DEVICE:<maTaiSan>
     *
     * Ví dụ: GET /api/thiet-bi/5/qr-code
     */
    @GetMapping("/{id}/qr-code")
    public ResponseEntity<ApiResponse<QrCodeResponse>> getQrCode(@PathVariable Integer id) {
        String qrUrl = qrCodeService.getOrCreateQrCode(id);

        // Lấy thông tin thiết bị để build response
        ThietBi tb = thietBiService.findById(id).orElse(null);

        QrCodeResponse response = QrCodeResponse.builder()
                .thietBiId(id)
                .maTaiSan(tb != null ? tb.getMaTaiSan() : null)
                .qrContent("DEVICE:" + (tb != null ? tb.getMaTaiSan() : ""))
                .qrCodeUrl(qrUrl)
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Lay QR code thanh cong", response));
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

    /**
     * Lấy danh sách hợp đồng mà thiết bị đã tham gia
     */
    @GetMapping("/{id}/hop-dong")
    public ResponseEntity<ApiResponse<java.util.List<com.example.device_apisever.entity.HopDongThue>>> getHopDongsByThietBi(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(thietBiService.getDeviceContracts(id)));
    }

    /**
     * Lấy lịch sử bảo trì của thiết bị
     */
    @GetMapping("/{id}/lich-su-bao-tri")
    public ResponseEntity<ApiResponse<java.util.List<com.example.device_apisever.entity.LichSuBaoTri>>> getLichSuBaoTriByThietBi(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(thietBiService.getMaintenanceHistory(id)));
    }

    /**
     * Cập nhật trạng thái của thiết bị
     * body: {"tinhTrangId": 3} (1=Sẵn sàng, 3=Đang bảo trì)
     */
    @PutMapping("/{id}/cap-nhat-tinh-trang")
    public ResponseEntity<ApiResponse<ThietBi>> updateTinhTrang(
            @PathVariable Integer id,
            @RequestBody java.util.Map<String, Integer> body) {
        Integer tinhTrangId = body.get("tinhTrangId");
        if (tinhTrangId == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu tinhTrangId"));
        }
        
        try {
            ThietBi updatedTb = thietBiService.updateStatus(id, tinhTrangId);
            return ResponseEntity.ok(ApiResponse.ok("Cập nhật trạng thái thành công", updatedTb));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}