package com.example.device_apisever.controller;

import com.example.device_apisever.config.JwtService;
import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.SasUploadResponse;
import com.example.device_apisever.entity.ChuKyDienTu;
import com.example.device_apisever.entity.HopDongThue;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.ChuKyDienTuRepository;
import com.example.device_apisever.repository.HopDongThueRepository;
import com.example.device_apisever.service.S3StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/signatures")
public class SignatureController {

    private final S3StorageService s3StorageService;
    private final HopDongThueRepository hopDongThueRepository;
    private final ChuKyDienTuRepository chuKyDienTuRepository;
    private final JwtService jwtService;

    public SignatureController(S3StorageService s3StorageService,
                               HopDongThueRepository hopDongThueRepository,
                               ChuKyDienTuRepository chuKyDienTuRepository,
                               JwtService jwtService) {
        this.s3StorageService = s3StorageService;
        this.hopDongThueRepository = hopDongThueRepository;
        this.chuKyDienTuRepository = chuKyDienTuRepository;
        this.jwtService = jwtService;
    }

    /**
     * Lấy URL Upload chữ ký có thời hạn 5 phút.
     */
    @GetMapping("/contract/{hopDongId}/get-upload-url")
    public ResponseEntity<ApiResponse<SasUploadResponse>> getUploadUrl(
            @PathVariable Integer hopDongId,
            @RequestParam(defaultValue = "png") String extension,
            HttpServletRequest request) {

        if (!extension.equalsIgnoreCase("png") && !extension.equalsIgnoreCase("jpg")) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Chi ho tro dinh dang png hoac jpg."));
        }

        validateAccess(hopDongId, request);

        String contentType = extension.equalsIgnoreCase("png") ? "image/png" : "image/jpeg";
        String fileName = s3StorageService.generateFileName(extension);
        String sasUrl = s3StorageService.generateSasUploadUrl("sign", fileName, contentType);

        SasUploadResponse response = SasUploadResponse.builder()
                .sasUrl(sasUrl)
                .fileName(fileName)
                .publicUrl(null) // Bucket private ko co public URL
                .category("sign")
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Tao SAS URL upload chu ky thanh cong.", response));
    }

    /**
     * Lấy URL để đọc/xem chữ ký đã upload.
     */
    @GetMapping("/contract/{hopDongId}/read-url")
    public ResponseEntity<ApiResponse<Map<String, String>>> getReadUrl(
            @PathVariable Integer hopDongId,
            @RequestParam String fileName,
            HttpServletRequest request) {

        validateAccess(hopDongId, request);

        String relativePath = s3StorageService.getRelativePath("sign", fileName);
        String sasReadUrl = s3StorageService.generateSasReadUrl(relativePath);

        return ResponseEntity.ok(ApiResponse.ok("Tao SAS URL doc chu ky thanh cong.", Map.of("readUrl", sasReadUrl)));
    }

    /**
     * Xác nhận upload đã được gộp chung vào /api/hop-dong/{id}/ky-ket.
     * Vui lòng gọi API đó để xác nhận và thay đổi trạng thái hợp đồng.
     */

    /**
     * Kiểm tra quyền truy cập: Phải là Admin hoặc Khách hàng sở hữu hợp đồng.
     */
    private HopDongThue validateAccess(Integer hopDongId, HttpServletRequest request) {
        HopDongThue hopDong = hopDongThueRepository.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay hop dong ID: " + hopDongId));

        Integer nguoiDungId = extractNguoiDungIdFromToken(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }
        }

        if (!isAdmin && !hopDong.getNguoiDungKhachId().equals(nguoiDungId)) {
            throw new org.springframework.security.access.AccessDeniedException("Khong co quyen truy cap chu ky hop dong nay.");
        }

        return hopDong;
    }

    private Integer extractNguoiDungIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            return jwtService.extractNguoiDungId(jwt);
        }
        throw new RuntimeException("Khong tim thay JWT token trong request");
    }
}
