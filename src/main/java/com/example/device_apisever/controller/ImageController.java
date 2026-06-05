package com.example.device_apisever.controller;

import com.example.device_apisever.config.JwtService;
import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.SasUploadResponse;
import com.example.device_apisever.dto.UpdateImageRequest;
import com.example.device_apisever.entity.HinhAnhThietBi;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.entity.ThietBi;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.HinhAnhThietBiRepository;
import com.example.device_apisever.repository.NguoiDungRepository;
import com.example.device_apisever.repository.ThietBiRepository;
import com.example.device_apisever.service.S3StorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller quản lý upload ảnh qua Azure Blob Storage (kiến trúc SAS Token + Multi-Container).
 * <p>
 * Hệ thống chia ảnh vào 3 container riêng biệt theo mục đích sử dụng:
 * <ul>
 *   <li><b>"user"</b> — Ảnh cá nhân (avatar, ảnh đại diện người dùng)</li>
 *   <li><b>"products"</b> — Ảnh sản phẩm/thiết bị</li>
 *   <li><b>"work"</b> — Ảnh nghiệp vụ (giao nhận, bàn giao, hiện trường, bảo trì)</li>
 * </ul>
 * <p>
 * Luồng hoạt động:
 * <ol>
 *   <li>Client gọi GET /api/images/{category}/get-upload-url → Nhận sasUrl + fileName</li>
 *   <li>Client dùng HTTP PUT sasUrl để upload file trực tiếp lên Azure</li>
 *   <li>Client gọi POST endpoint xác nhận để server lưu thông tin vào Database</li>
 * </ol>
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final S3StorageService s3StorageService;
    private final HinhAnhThietBiRepository hinhAnhThietBiRepository;
    private final ThietBiRepository thietBiRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final JwtService jwtService;

    public ImageController(S3StorageService s3StorageService,
                           HinhAnhThietBiRepository hinhAnhThietBiRepository,
                           ThietBiRepository thietBiRepository,
                           NguoiDungRepository nguoiDungRepository,
                           JwtService jwtService) {
        this.s3StorageService = s3StorageService;
        this.hinhAnhThietBiRepository = hinhAnhThietBiRepository;
        this.thietBiRepository = thietBiRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.jwtService = jwtService;
    }

    // ========================================================================================
    // CHUNG: Lấy SAS URL để client upload ảnh trực tiếp lên Azure (dùng cho cả 3 container)
    // ========================================================================================

    /**
     * Tạo SAS Token URL cho phép client upload ảnh trực tiếp lên Azure Blob Storage.
     * <p>
     * Endpoint chung cho cả 3 loại ảnh — client chỉ cần truyền đúng category:
     * <ul>
     *   <li>/api/images/<b>user</b>/get-upload-url — upload ảnh đại diện</li>
     *   <li>/api/images/<b>products</b>/get-upload-url — upload ảnh sản phẩm</li>
     *   <li>/api/images/<b>work</b>/get-upload-url — upload ảnh nghiệp vụ</li>
     * </ul>
     *
     * @param category  Loại container: "user", "products", hoặc "work"
     * @param extension Đuôi file mong muốn (mặc định: "jpg"). Ví dụ: ?extension=png
     * @return SasUploadResponse chứa sasUrl, fileName, publicUrl, category
     */
    @GetMapping("/{category}/get-upload-url")
    public ResponseEntity<ApiResponse<SasUploadResponse>> getUploadUrl(
            @PathVariable String category,
            @RequestParam(required = false) String originalName,
            @RequestParam(defaultValue = "jpg") String extension,
            @RequestParam(defaultValue = "image/jpeg") String contentType) {

        // 1. Sinh tên file (dùng tên gốc hoặc UUID)
        String fileName;
        if (originalName != null && !originalName.isBlank()) {
            // Loại bỏ khoảng trắng và ký tự đặc biệt, thêm timestamp nhẹ để tránh trùng lặp nếu cần
            // Nhưng theo yêu cầu của client, giữ nguyên tên ảnh nhất có thể
            fileName = originalName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
        } else {
            fileName = s3StorageService.generateFileName(extension);
        }

        // 2. Tạo SAS URL (Write-only, 5 phút) cho container tương ứng
        String sasUrl = s3StorageService.generateSasUploadUrl(category, fileName, contentType);

        // 3. Tạo public URL (để client lưu/hiển thị sau khi upload xong)
        String publicUrl = s3StorageService.getPublicUrlByCategory(category, fileName);

        // 4. Trả về response kèm thông tin category
        SasUploadResponse response = SasUploadResponse.builder()
                .sasUrl(sasUrl)
                .fileName(fileName)
                .publicUrl(publicUrl)
                .category(category.toLowerCase())
                .build();

        return ResponseEntity.ok(ApiResponse.ok(
                "Tao SAS URL thanh cong cho container '" + category + "'. URL co hieu luc trong 5 phut.",
                response));
    }

    // ========================================================================================
    // CONTAINER "user": Cập nhật ảnh đại diện người dùng
    // ========================================================================================

    @PostMapping("/user/update-avatar")
    public ResponseEntity<ApiResponse<Map<String, String>>> updateAvatar(
            @Valid @RequestBody UpdateImageRequest request,
            HttpServletRequest httpRequest) {

        Integer nguoiDungId = extractNguoiDungIdFromToken(httpRequest);

        NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay nguoi dung voi ID: " + nguoiDungId));

        String avatarUrl = s3StorageService.getPublicUrlByCategory("user", request.getFileName());

        Map<String, String> result = Map.of(
                "nguoiDungId", nguoiDungId.toString(),
                "avatarUrl", avatarUrl
        );

        return ResponseEntity.ok(ApiResponse.ok("Cap nhat anh dai dien thanh cong!", result));
    }

    // ========================================================================================
    // CONTAINER "products": Gắn ảnh cho thiết bị/sản phẩm
    // ========================================================================================

    @PostMapping("/products/thiet-bi/{thietBiId}/update-image")
    public ResponseEntity<ApiResponse<HinhAnhThietBi>> updateProductImage(
            @PathVariable Integer thietBiId,
            @Valid @RequestBody UpdateImageRequest request,
            HttpServletRequest httpRequest) {

        ThietBi thietBi = thietBiRepository.findById(thietBiId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay thiet bi voi ID: " + thietBiId));

        Integer nguoiDungId = extractNguoiDungIdFromToken(httpRequest);

        // 4. Tạo bản ghi HinhAnhThietBi (CHỈ LƯU TÊN ẢNH THEO YÊU CẦU CLIENT)
        HinhAnhThietBi hinhAnh = HinhAnhThietBi.builder()
                .thietBiId(thietBi.getThietBiId())
                .nguoiDungChupId(nguoiDungId)
                .urlAnh(request.getFileName()) // CHỈ LƯU TÊN ẢNH
                .loaiAnhId(request.getLoaiAnhId() != null ? request.getLoaiAnhId() : 1)
                .banGiaoId(request.getBanGiaoId())
                .baoTriId(request.getBaoTriId())
                .build();

        HinhAnhThietBi saved = hinhAnhThietBiRepository.save(hinhAnh);

        return ResponseEntity.ok(ApiResponse.ok("Luu anh san pham thanh cong!", saved));
    }

    // ========================================================================================
    // CONTAINER "work": Gắn ảnh nghiệp vụ (giao nhận, bàn giao, bảo trì)
    // ========================================================================================

    @PostMapping("/work/thiet-bi/{thietBiId}/update-image")
    public ResponseEntity<ApiResponse<HinhAnhThietBi>> updateWorkImage(
            @PathVariable Integer thietBiId,
            @Valid @RequestBody UpdateImageRequest request,
            HttpServletRequest httpRequest) {

        ThietBi thietBi = thietBiRepository.findById(thietBiId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay thiet bi voi ID: " + thietBiId));

        if (request.getBanGiaoId() == null && request.getBaoTriId() == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Anh nghi vu phai co banGiaoId hoac baoTriId"));
        }

        Integer nguoiDungId = extractNguoiDungIdFromToken(httpRequest);

        // 5. Tạo bản ghi HinhAnhThietBi (CHỈ LƯU TÊN ẢNH)
        HinhAnhThietBi hinhAnh = HinhAnhThietBi.builder()
                .thietBiId(thietBi.getThietBiId())
                .nguoiDungChupId(nguoiDungId)
                .urlAnh(request.getFileName()) // CHỈ LƯU TÊN ẢNH
                .loaiAnhId(request.getLoaiAnhId() != null ? request.getLoaiAnhId() : 2)
                .banGiaoId(request.getBanGiaoId())
                .baoTriId(request.getBaoTriId())
                .build();

        HinhAnhThietBi saved = hinhAnhThietBiRepository.save(hinhAnh);

        return ResponseEntity.ok(ApiResponse.ok("Luu anh nghiep vu thanh cong!", saved));
    }

    // ========================================================================================
    // TIỆN ÍCH: Xóa ảnh (tự phát hiện container từ URL)
    // ========================================================================================

    /**
     * Xóa ảnh: xóa blob trên Azure + xóa bản ghi trong Database.
     * <p>
     * Server tự phát hiện container (user/products/work) từ URL lưu trong DB,
     * nên client không cần truyền category.
     *
     * @param hinhAnhId ID bản ghi HinhAnhThietBi cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{hinhAnhId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Integer hinhAnhId) {

        // 1. Tìm bản ghi trong DB
        HinhAnhThietBi hinhAnh = hinhAnhThietBiRepository.findById(hinhAnhId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay hinh anh voi ID: " + hinhAnhId));

        // 2. Trích xuất relative path từ URL lưu trong DB
        // Do DB hiện tại chỉ lưu tên file, ta cần xác định category dựa vào loaiAnhId hoặc các ID liên quan
        String category = "products";
        if (hinhAnh.getBanGiaoId() != null || hinhAnh.getBaoTriId() != null || (hinhAnh.getLoaiAnhId() != null && hinhAnh.getLoaiAnhId() == 2)) {
            category = "work";
        }
        
        String relativePath = s3StorageService.getRelativePath(category, hinhAnh.getUrlAnh());

        // 3. Xóa file trên R2
        s3StorageService.deleteFileByRelativePath(relativePath);

        // 4. Xóa bản ghi trong Database
        hinhAnhThietBiRepository.delete(hinhAnh);

        return ResponseEntity.ok(ApiResponse.ok("Da xoa anh ID: " + hinhAnhId, null));
    }

    // ========================================================================================
    // PRIVATE HELPER
    // ========================================================================================

    /**
     * Trích xuất NguoiDungID từ JWT token trong Authorization header.
     */
    private Integer extractNguoiDungIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            return jwtService.extractNguoiDungId(jwt);
        }
        throw new RuntimeException("Khong tim thay JWT token trong request");
    }
}
