package com.example.device_apisever.controller;

import com.example.device_apisever.config.JwtService;
import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.SasUploadResponse;
import com.example.device_apisever.dto.bangiao.*;
import com.example.device_apisever.service.BanGiaoService;
import com.example.device_apisever.service.S3StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/hop-dong")
@PreAuthorize("hasAnyRole('ADMIN', 'KY_THUAT')")
public class BanGiaoController {

    private final BanGiaoService banGiaoService;
    private final S3StorageService s3StorageService;
    private final JwtService jwtService;

    public BanGiaoController(BanGiaoService banGiaoService,
                             S3StorageService s3StorageService,
                             JwtService jwtService) {
        this.banGiaoService = banGiaoService;
        this.s3StorageService = s3StorageService;
        this.jwtService = jwtService;
    }

    /**
     * API Bàn giao thiết bị cho khách hàng
     * POST /api/admin/hop-dong/{hopDongId}/ban-giao
     */
    @PostMapping("/{hopDongId}/ban-giao")
    public ResponseEntity<ApiResponse<Void>> banGiaoThietBi(
            @PathVariable Integer hopDongId,
            @RequestBody BanGiaoHopDongRequest request,
            HttpServletRequest httpRequest) {

        Integer nguoiThucHienId = extractUserId(httpRequest);
        banGiaoService.banGiaoThietBi(hopDongId, request, nguoiThucHienId);
        return ResponseEntity.ok(ApiResponse.ok("Bàn giao thiết bị thành công", null));
    }

    /**
     * Lấy SAS URL để upload chữ ký bàn giao (bucket bảo mật "sign")
     * GET /api/admin/hop-dong/{hopDongId}/ban-giao/signature/upload-url
     */
    @GetMapping("/{hopDongId}/ban-giao/signature/upload-url")
    public ResponseEntity<ApiResponse<SasUploadResponse>> getSignatureUploadUrl(
            @PathVariable Integer hopDongId,
            @RequestParam(defaultValue = "png") String extension) {

        String contentType = extension.equalsIgnoreCase("png") ? "image/png" : "image/jpeg";
        String fileName = s3StorageService.generateFileName(extension);
        String sasUrl = s3StorageService.generateSasUploadUrl("sign", fileName, contentType);

        SasUploadResponse response = SasUploadResponse.builder()
            .sasUrl(sasUrl)
            .fileName(fileName)
            .publicUrl(null) // bucket private
            .category("sign")
            .build();

        return ResponseEntity.ok(ApiResponse.ok("Tạo SAS URL chữ ký bàn giao thành công", response));
    }

    /**
     * API Thu hồi toàn bộ thiết bị
     * POST /api/admin/hop-dong/{hopDongId}/thu-hoi
     */
    @PostMapping("/{hopDongId}/thu-hoi")
    public ResponseEntity<ApiResponse<Void>> thuHoiThietBi(
            @PathVariable Integer hopDongId,
            @RequestBody ThuHoiHopDongRequest request,
            HttpServletRequest httpRequest) {

        Integer nguoiThucHienId = extractUserId(httpRequest);
        banGiaoService.thuHoiThietBi(hopDongId, request, nguoiThucHienId);
        return ResponseEntity.ok(ApiResponse.ok("Thu hồi thiết bị thành công", null));
    }

    private Integer extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtService.extractNguoiDungId(authHeader.substring(7));
        }
        throw new RuntimeException("Không tìm thấy JWT token");
    }
}
