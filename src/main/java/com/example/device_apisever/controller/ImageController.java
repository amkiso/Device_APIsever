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
            @RequestParam(defaultValue = "jpg") String extension,
            @RequestParam(defaultValue = "image/jpeg") String contentType) {

        // 1. Sinh tên file duy nhất bằng UUID
        String fileName = s3StorageService.generateFileName(extension);

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

    /**
     * Cập nhật ảnh đại diện cho người dùng hiện tại.
     * <p>
     * Sau khi client upload ảnh lên container "user" thành công,
     * gọi endpoint này để server lưu URL ảnh vào trường tùy chỉnh trong CSDL.
     * <p>
     * Hiện tại server lưu URL avatar dưới dạng metadata (trả về URL cho client tự quản lý).
     * Có thể mở rộng thêm cột "AnhDaiDien" vào bảng NguoiDung nếu cần.
     *
     * @param request     Body chứa fileName đã upload
     * @param httpRequest để trích xuất JWT token
     * @return URL ảnh đại diện mới
     */
    @PostMapping("/user/update-avatar")
    public ResponseEntity<ApiResponse<Map<String, String>>> updateAvatar(
            @Valid @RequestBody UpdateImageRequest request,
            HttpServletRequest httpRequest) {

        // 1. Trích xuất NguoiDungID từ JWT token
        Integer nguoiDungId = extractNguoiDungIdFromToken(httpRequest);

        // 2. Kiểm tra người dùng tồn tại
        NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay nguoi dung voi ID: " + nguoiDungId));

        // 3. Lấy public URL từ fileName (dùng để trả về)
        String avatarUrl = s3StorageService.getPublicUrlByCategory("user", request.getFileName());

        // Nếu bảng NguoiDung có lưu ảnh thì lưu Dạng Relative Path thay vì Full URL
        // String relativePath = s3StorageService.getRelativePath("user", request.getFileName());
        // nguoiDung.setAnhDaiDien(relativePath);
        // nguoiDungRepository.save(nguoiDung);

        Map<String, String> result = Map.of(
                "nguoiDungId", nguoiDungId.toString(),
                "avatarUrl", avatarUrl
        );

        return ResponseEntity.ok(ApiResponse.ok("Cap nhat anh dai dien thanh cong!", result));
    }

    // ========================================================================================
    // CONTAINER "products": Gắn ảnh cho thiết bị/sản phẩm
    // ========================================================================================

    /**
     * Lưu thông tin ảnh sản phẩm vào bảng HinhAnhThietBi.
     * <p>
     * Sau khi client upload ảnh lên container "products" thành công,
     * gọi endpoint này để server tạo bản ghi trong CSDL liên kết ảnh với thiết bị.
     *
     * @param thietBiId   ID của thiết bị cần gắn ảnh
     * @param request     Body chứa fileName và metadata ảnh
     * @param httpRequest để trích xuất JWT token
     * @return Bản ghi HinhAnhThietBi vừa được tạo
     */
    @PostMapping("/products/thiet-bi/{thietBiId}/update-image")
    public ResponseEntity<ApiResponse<HinhAnhThietBi>> updateProductImage(
            @PathVariable Integer thietBiId,
            @Valid @RequestBody UpdateImageRequest request,
            HttpServletRequest httpRequest) {

        // 1. Kiểm tra thiết bị có tồn tại không
        ThietBi thietBi = thietBiRepository.findById(thietBiId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay thiet bi voi ID: " + thietBiId));

        // 2. Trích xuất NguoiDungID từ JWT token (người chụp ảnh)
        Integer nguoiDungId = extractNguoiDungIdFromToken(httpRequest);

        // 3. Tạo đường dẫn tương đối (Relative Path) để lưu database
        String relativePath = s3StorageService.getRelativePath("products", request.getFileName());

        // 4. Tạo bản ghi HinhAnhThietBi
        HinhAnhThietBi hinhAnh = HinhAnhThietBi.builder()
                .thietBiId(thietBi.getThietBiId())
                .nguoiDungChupId(nguoiDungId)
                .urlAnh(relativePath) // CHỈ LƯU RELATIVE PATH
                .loaiAnhId(request.getLoaiAnhId() != null ? request.getLoaiAnhId() : 1)
                .banGiaoId(request.getBanGiaoId())
                .baoTriId(request.getBaoTriId())
                .build();

        // 5. Lưu vào Database
        HinhAnhThietBi saved = hinhAnhThietBiRepository.save(hinhAnh);

        return ResponseEntity.ok(ApiResponse.ok("Luu anh san pham thanh cong!", saved));
    }

    // ========================================================================================
    // CONTAINER "work": Gắn ảnh nghiệp vụ (giao nhận, bàn giao, bảo trì)
    // ========================================================================================

    /**
     * Lưu thông tin ảnh nghiệp vụ vào bảng HinhAnhThietBi.
     * <p>
     * Dùng cho các ảnh liên quan đến công việc: biên bản giao nhận, hiện trường bảo trì,
     * ảnh sự cố, chữ ký giao nhận...
     * <p>
     * Yêu cầu: PHẢI truyền ít nhất một trong hai: banGiaoId hoặc baoTriId
     * để liên kết ảnh với phiên bàn giao / phiếu bảo trì cụ thể.
     *
     * @param thietBiId   ID của thiết bị liên quan
     * @param request     Body chứa fileName, banGiaoId hoặc baoTriId
     * @param httpRequest để trích xuất JWT token
     * @return Bản ghi HinhAnhThietBi vừa được tạo
     */
    @PostMapping("/work/thiet-bi/{thietBiId}/update-image")
    public ResponseEntity<ApiResponse<HinhAnhThietBi>> updateWorkImage(
            @PathVariable Integer thietBiId,
            @Valid @RequestBody UpdateImageRequest request,
            HttpServletRequest httpRequest) {

        // 1. Kiểm tra thiết bị có tồn tại không
        ThietBi thietBi = thietBiRepository.findById(thietBiId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay thiet bi voi ID: " + thietBiId));

        // 2. Validate: ảnh nghiệp vụ phải liên kết với bàn giao hoặc bảo trì
        if (request.getBanGiaoId() == null && request.getBaoTriId() == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Anh nghi vu phai co banGiaoId hoac baoTriId"));
        }

        // 3. Trích xuất NguoiDungID từ JWT token
        Integer nguoiDungId = extractNguoiDungIdFromToken(httpRequest);

        // 4. Tạo đường dẫn tương đối để lưu database
        String relativePath = s3StorageService.getRelativePath("work", request.getFileName());

        // 5. Tạo bản ghi HinhAnhThietBi
        HinhAnhThietBi hinhAnh = HinhAnhThietBi.builder()
                .thietBiId(thietBi.getThietBiId())
                .nguoiDungChupId(nguoiDungId)
                .urlAnh(relativePath) // CHỈ LƯU RELATIVE PATH
                .loaiAnhId(request.getLoaiAnhId() != null ? request.getLoaiAnhId() : 2)  // Mặc định loại 2 (biên bản)
                .banGiaoId(request.getBanGiaoId())
                .baoTriId(request.getBaoTriId())
                .build();

        // 6. Lưu vào Database
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

        // 2. Trích xuất relative path từ URL lưu trong DB (đã là relative path rồi)
        String relativePath = hinhAnh.getUrlAnh();

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
