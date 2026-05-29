package com.example.device_apisever.service;

import com.example.device_apisever.entity.LoaiThietBi;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.LoaiThietBiRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiThietBiService {

    private final LoaiThietBiRepository loaiThietBiRepository;
    private final S3StorageService s3StorageService;

    public LoaiThietBiService(LoaiThietBiRepository loaiThietBiRepository,
                              S3StorageService s3StorageService) {
        this.loaiThietBiRepository = loaiThietBiRepository;
        this.s3StorageService = s3StorageService;
    }

    // ========================================================================================
    //  CHUYỂN ĐỔI TÊN FILE → FULL URL
    // ========================================================================================

    /**
     * Chuyển đổi tên file ảnh đại diện thành full R2 S3 URL.
     * Ảnh LoaiThietBi nằm trong thư mục "products".
     * Ví dụ: "product/uuid.png"
     *     → "https://endpoint.../quanlythietbi/product/uuid.png"
     *
     * @param loaiThietBi entity cần chuyển đổi (sẽ bị mutate trực tiếp)
     * @return entity đã được gắn full URL vào anhDaiDien
     */
    private LoaiThietBi enrichWithImageUrl(LoaiThietBi loaiThietBi) {
        if (loaiThietBi != null && loaiThietBi.getAnhDaiDien() != null
                && !loaiThietBi.getAnhDaiDien().isBlank()
                && !loaiThietBi.getAnhDaiDien().startsWith("http")) {
            
            // Nếu trong DB lưu tên file (chưa có thư mục), ta dùng getPublicUrlByCategory
            // Nếu trong DB lưu relative path (đã có thư mục), ta dùng getPublicUrl
            String path = loaiThietBi.getAnhDaiDien();
            String fullUrl;
            if (path.contains("/")) {
                fullUrl = s3StorageService.getPublicUrl(path);
            } else {
                fullUrl = s3StorageService.getPublicUrlByCategory("products", path);
            }
            loaiThietBi.setAnhDaiDien(fullUrl);
        }
        return loaiThietBi;
    }

    /**
     * Chuyển đổi danh sách LoaiThietBi → gắn full URL cho tất cả.
     */
    private List<LoaiThietBi> enrichListWithImageUrl(List<LoaiThietBi> list) {
        list.forEach(this::enrichWithImageUrl);
        return list;
    }

    // ========================================================================================
    //  PUBLIC METHODS — Trả về full URL cho anhDaiDien
    // ========================================================================================

    public String getFullImageUrl(String fileName) {
        if (fileName == null || fileName.isBlank()) return null;
        if (fileName.startsWith("http")) return fileName;
        if (fileName.contains("/")) {
            return s3StorageService.getPublicUrl(fileName);
        }
        return s3StorageService.getPublicUrlByCategory("products", fileName);
    }

    /**
     * Lấy tất cả loại thiết bị
     */
    public List<LoaiThietBi> findAll() {
        return enrichListWithImageUrl(loaiThietBiRepository.findAll());
    }

    /**
     * Lọc loại thiết bị theo danh mục
     */
    public List<LoaiThietBi> findByDanhMucId(Integer danhMucId) {
        return enrichListWithImageUrl(loaiThietBiRepository.findByDanhMucId(danhMucId));
    }

    /**
     * Tìm loại thiết bị theo ID
     */
    public LoaiThietBi findById(Integer id) {
        LoaiThietBi entity = loaiThietBiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy loại thiết bị với ID: " + id));
        return enrichWithImageUrl(entity);
    }

    /**
     * Tìm kiếm loại thiết bị theo tên (LIKE %keyword%)
     */
    public List<LoaiThietBi> search(String keyword) {
        return enrichListWithImageUrl(
                loaiThietBiRepository.findByTenLoaiThietBiContainingIgnoreCase(keyword));
    }

    /**
     * Tạo loại thiết bị mới
     */
    public LoaiThietBi create(LoaiThietBi loaiThietBi) {
        return enrichWithImageUrl(loaiThietBiRepository.save(loaiThietBi));
    }

    /**
     * Cập nhật loại thiết bị
     */
    public LoaiThietBi update(Integer id, LoaiThietBi request) {
        LoaiThietBi existing = loaiThietBiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy loại thiết bị với ID: " + id));
        existing.setDanhMucId(request.getDanhMucId());
        existing.setNhaCungCapId(request.getNhaCungCapId());
        existing.setTenLoaiThietBi(request.getTenLoaiThietBi());
        existing.setThongSoKyThuat(request.getThongSoKyThuat());
        existing.setGiaThueThamKhao(request.getGiaThueThamKhao());
        existing.setAnhDaiDien(request.getAnhDaiDien());
        return enrichWithImageUrl(loaiThietBiRepository.save(existing));
    }

    /**
     * Xóa loại thiết bị theo ID
     */
    public void deleteById(Integer id) {
        if (!loaiThietBiRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Không tìm thấy loại thiết bị với ID: " + id);
        }
        loaiThietBiRepository.deleteById(id);
    }
}
