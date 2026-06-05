package com.example.device_apisever.service;

import com.example.device_apisever.dto.ThietBiDetailResponse;
import com.example.device_apisever.entity.*;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ThietBiService {

    private final ThietBiRepository thietBiRepository;
    private final LoaiThietBiRepository loaiThietBiRepository;
    private final DanhMucThietBiRepository danhMucThietBiRepository;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final TinhTrangThietBiRepository tinhTrangThietBiRepository;
    private final KhoRepository khoRepository;
    private final HinhAnhThietBiRepository hinhAnhThietBiRepository;
    private final LoaiThietBiService loaiThietBiService;
    private final ChiTietThueThietBiRepository chiTietThueThietBiRepository;
    private final HopDongThueRepository hopDongThueRepository;
    private final LichSuBaoTriRepository lichSuBaoTriRepository;
    private final S3StorageService s3StorageService;

    public ThietBiService(ThietBiRepository thietBiRepository,
                          LoaiThietBiRepository loaiThietBiRepository,
                          DanhMucThietBiRepository danhMucThietBiRepository,
                          NhaCungCapRepository nhaCungCapRepository,
                          TinhTrangThietBiRepository tinhTrangThietBiRepository,
                          KhoRepository khoRepository,
                          HinhAnhThietBiRepository hinhAnhThietBiRepository,
                          LoaiThietBiService loaiThietBiService,
                          ChiTietThueThietBiRepository chiTietThueThietBiRepository,
                          HopDongThueRepository hopDongThueRepository,
                          LichSuBaoTriRepository lichSuBaoTriRepository,
                          S3StorageService s3StorageService) {
        this.thietBiRepository = thietBiRepository;
        this.loaiThietBiRepository = loaiThietBiRepository;
        this.danhMucThietBiRepository = danhMucThietBiRepository;
        this.nhaCungCapRepository = nhaCungCapRepository;
        this.tinhTrangThietBiRepository = tinhTrangThietBiRepository;
        this.khoRepository = khoRepository;
        this.hinhAnhThietBiRepository = hinhAnhThietBiRepository;
        this.loaiThietBiService = loaiThietBiService;
        this.chiTietThueThietBiRepository = chiTietThueThietBiRepository;
        this.hopDongThueRepository = hopDongThueRepository;
        this.lichSuBaoTriRepository = lichSuBaoTriRepository;
        this.s3StorageService = s3StorageService;
    }

    // 1. Lấy tất cả thiết bị
    public List<ThietBi> findAll(Integer khoHienTaiId) {
        if (khoHienTaiId != null) {
            return thietBiRepository.findByKhoHienTaiId(khoHienTaiId);
        }
        return thietBiRepository.findAll();
    }

    // 2. Tìm một thiết bị theo ID
    public Optional<ThietBi> findById(Integer id) {
        return thietBiRepository.findById(id);
    }

    // 3. Thêm mới hoặc Cập nhật thiết bị
    public ThietBi save(ThietBi thietBi) {
        return thietBiRepository.save(thietBi);
    }

    /**
     * Sinh mã tài sản tự động: MTS-L{loaiThietBiId}-{index}
     * VD: MTS-L01-02
     */
    public String generateMaTaiSan(Integer loaiThietBiId) {
        long count = thietBiRepository.countByLoaiThietBiId(loaiThietBiId);
        return String.format("MTS-L%02d-%02d", loaiThietBiId, count + 1);
    }

    /**
     * Tạo thiết bị mới + tự sinh mã tài sản + tự tạo QR code
     */
    public ThietBi createWithAutoQr(ThietBi thietBi) {
        // Auto sinh mã tài sản
        if (thietBi.getMaTaiSan() == null || thietBi.getMaTaiSan().isBlank()) {
            thietBi.setMaTaiSan(generateMaTaiSan(thietBi.getLoaiThietBiId()));
        }
        // Mặc định tình trạng sẵn sàng
        if (thietBi.getTinhTrangId() == null) {
            thietBi.setTinhTrangId(1);
        }
        ThietBi saved = thietBiRepository.save(thietBi);
        return saved;
    }

    /**
     * Cập nhật thông tin thiết bị
     */
    public ThietBi updateThietBi(Integer id, ThietBi updated) {
        ThietBi existing = thietBiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị: " + id));
        if (updated.getSoSerial() != null) existing.setSoSerial(updated.getSoSerial());
        if (updated.getKhoHienTaiId() != null) existing.setKhoHienTaiId(updated.getKhoHienTaiId());
        if (updated.getMucDichSuDung() != null) existing.setMucDichSuDung(updated.getMucDichSuDung());
        if (updated.getGiaTriMay() != null) existing.setGiaTriMay(updated.getGiaTriMay());
        if (updated.getNgayKiemDinh() != null) existing.setNgayKiemDinh(updated.getNgayKiemDinh());
        if (updated.getTinhTrangBanGiao() != null) existing.setTinhTrangBanGiao(updated.getTinhTrangBanGiao());
        return thietBiRepository.save(existing);
    }

    // 4. Xóa thiết bị
    public void deleteById(Integer id) {
        thietBiRepository.deleteById(id);
    }

    /**
     * Kiểm tra thiết bị có thể xóa (không có FK references)
     */
    public boolean canDelete(Integer thietBiId) {
        return !chiTietThueThietBiRepository.existsByThietBiId(thietBiId)
                && !lichSuBaoTriRepository.existsByThietBiId(thietBiId);
    }

    /**
     * Vô hiệu hóa thiết bị (tinhTrangId=5)
     */
    public ThietBi disableDevice(Integer id) {
        ThietBi tb = thietBiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị: " + id));
        tb.setTinhTrangId(5);
        return thietBiRepository.save(tb);
    }

    /**
     * 5. Tra cứu toàn bộ thông tin thiết bị theo Mã Tài Sản.
     * Gộp dữ liệu từ: ThietBi, LoaiThietBi, DanhMucThietBi,
     * NhaCungCap, TinhTrangThietBi, Kho, HinhAnhThietBi.
     */
    public ThietBiDetailResponse findDetailByMaTaiSan(String maTaiSan) {
        // 1. Tìm thiết bị
        ThietBi tb = thietBiRepository.findByMaTaiSan(maTaiSan)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay thiet bi voi ma tai san: " + maTaiSan));

        // 2. Lấy thông tin loại thiết bị
        LoaiThietBi loai = loaiThietBiRepository.findById(tb.getLoaiThietBiId())
                .orElse(null);

        // 3. Lấy danh mục
        String tenDanhMuc = null;
        Integer danhMucId = null;
        if (loai != null) {
            DanhMucThietBi dm = danhMucThietBiRepository.findById(loai.getDanhMucId())
                    .orElse(null);
            if (dm != null) {
                danhMucId = dm.getDanhMucId();
                tenDanhMuc = dm.getTenDanhMuc();
            }
        }

        // 4. Lấy nhà cung cấp
        String tenNhaCungCap = null;
        Integer nhaCungCapId = null;
        if (loai != null) {
            NhaCungCap ncc = nhaCungCapRepository.findById(loai.getNhaCungCapId())
                    .orElse(null);
            if (ncc != null) {
                nhaCungCapId = ncc.getNhaCungCapId();
                tenNhaCungCap = ncc.getTenNhaCungCap();
            }
        }

        // 5. Lấy tình trạng
        TinhTrangThietBi tt = tinhTrangThietBiRepository.findById(tb.getTinhTrangId())
                .orElse(null);

        // 6. Lấy kho hiện tại
        Kho kho = khoRepository.findById(tb.getKhoHienTaiId())
                .orElse(null);

        // 7. Lấy danh sách hình ảnh (chuyển đổi relative path sang public URL)
        List<HinhAnhThietBi> hinhAnhs = hinhAnhThietBiRepository.findByThietBiId(tb.getThietBiId());
        List<ThietBiDetailResponse.HinhAnhInfo> hinhAnhInfos = hinhAnhs.stream()
                .map(ha -> ThietBiDetailResponse.HinhAnhInfo.builder()
                        .hinhAnhId(ha.getHinhAnhId())
                        .urlAnh(getFullImageUrlFromHinhAnh(ha)) // Convert qua public url
                        .loaiAnhId(ha.getLoaiAnhId())
                        .ngayChup(ha.getNgayChup())
                        .build())
                .collect(Collectors.toList());

        // 8. Gộp tất cả vào DTO
        return ThietBiDetailResponse.builder()
                .thietBiId(tb.getThietBiId())
                .maTaiSan(tb.getMaTaiSan())
                .ngayBaoTriTiepTheo(tb.getNgayBaoTriTiepTheo())
                // Loại thiết bị
                .loaiThietBiId(loai != null ? loai.getLoaiThietBiId() : null)
                .tenLoaiThietBi(loai != null ? loai.getTenLoaiThietBi() : null)
                .thongSoKyThuat(loai != null ? loai.getThongSoKyThuat() : null)
                .giaThueThamKhao(loai != null ? loai.getGiaThueThamKhao() : null)
                .anhDaiDien(loai != null ? loaiThietBiService.getFullImageUrl(loai.getAnhDaiDien()) : null)
                // Danh mục
                .danhMucId(danhMucId)
                .tenDanhMuc(tenDanhMuc)
                // Nhà cung cấp
                .nhaCungCapId(nhaCungCapId)
                .tenNhaCungCap(tenNhaCungCap)
                // Tình trạng
                .tinhTrangId(tt != null ? tt.getTinhTrangId() : null)
                .tenTinhTrang(tt != null ? tt.getTenTinhTrang() : null)
                // Kho
                .khoHienTaiId(kho != null ? kho.getKhoId() : null)
                .tenKho(kho != null ? kho.getTenKho() : null)
                .diaChiKho(kho != null ? kho.getDiaChi() : null)
                // Hình ảnh
                .hinhAnhs(hinhAnhInfos)
                // QR code
                .qrCodeUrl(tb.getQrCodeUrl() != null ? s3StorageService.getPublicUrl(tb.getQrCodeUrl()) : null)
                .build();
    }

    /**
     * Lấy danh sách thiết bị kèm chi tiết cho một kho
     */
    public List<ThietBiDetailResponse> findDetailsByKhoHienTaiId(Integer khoHienTaiId) {
        List<ThietBi> thietBis = thietBiRepository.findByKhoHienTaiId(khoHienTaiId);
        return thietBis.stream().map(tb -> findDetailByMaTaiSan(tb.getMaTaiSan())).collect(Collectors.toList());
    }

    /**
     * 6. Lấy danh sách thiết bị cụ thể (serial) theo loại thiết bị.
     * Gộp dữ liệu từ: ThietBi, TinhTrangThietBi, Kho.
     * Dùng cho: GET /api/thiet-bi?loaiThietBiId={id}
     */
    public List<com.example.device_apisever.dto.ThietBiByLoaiDTO> findByLoaiThietBiId(Integer loaiThietBiId, Integer khoHienTaiId) {
        List<ThietBi> thietBis;
        if (khoHienTaiId != null) {
            thietBis = thietBiRepository.findByLoaiThietBiIdAndKhoHienTaiId(loaiThietBiId, khoHienTaiId);
        } else {
            thietBis = thietBiRepository.findByLoaiThietBiId(loaiThietBiId);
        }
        return thietBis.stream().map(tb -> {
            // Lấy tên tình trạng
            String tenTinhTrang = null;
            TinhTrangThietBi tt = tinhTrangThietBiRepository.findById(tb.getTinhTrangId()).orElse(null);
            if (tt != null) {
                tenTinhTrang = tt.getTenTinhTrang();
            }

            // Lấy tên kho
            String tenKho = null;
            Kho kho = khoRepository.findById(tb.getKhoHienTaiId()).orElse(null);
            if (kho != null) {
                tenKho = kho.getTenKho();
            }

            return com.example.device_apisever.dto.ThietBiByLoaiDTO.builder()
                    .thietBiId(tb.getThietBiId())
                    .maTaiSan(tb.getMaTaiSan())
                    .tinhTrangId(tb.getTinhTrangId())
                    .tenTinhTrang(tenTinhTrang)
                    .khoHienTaiId(tb.getKhoHienTaiId())
                    .tenKho(tenKho)
                    .ngayBaoTriTiepTheo(tb.getNgayBaoTriTiepTheo())
                    .qrCodeUrl(tb.getQrCodeUrl() != null ? s3StorageService.getPublicUrl(tb.getQrCodeUrl()) : null)
                    .soSerial(tb.getSoSerial())
                    .giaTriMay(tb.getGiaTriMay())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * Lấy danh sách hợp đồng mà thiết bị đã tham gia
     */
    public List<HopDongThue> getDeviceContracts(Integer thietBiId) {
        List<Integer> hopDongIds = chiTietThueThietBiRepository.findHopDongIdsByThietBiId(thietBiId);
        return hopDongThueRepository.findAllById(hopDongIds);
    }

    /**
     * Lấy lịch sử bảo trì của thiết bị
     */
    public List<LichSuBaoTri> getMaintenanceHistory(Integer thietBiId) {
        return lichSuBaoTriRepository.findByThietBiIdOrderByNgayThucHienDesc(thietBiId);
    }

    /**
     * Cập nhật trạng thái của thiết bị (Bảo trì / Hoàn thành bảo trì)
     */
    public ThietBi updateStatus(Integer thietBiId, Integer tinhTrangId) {
        if (tinhTrangId != 1 && tinhTrangId != 3) {
            throw new IllegalArgumentException("Chỉ được chuyển sang trạng thái 1 (Sẵn sàng) hoặc 3 (Đang bảo trì)");
        }
        
        ThietBi tb = thietBiRepository.findById(thietBiId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị: " + thietBiId));
                
        tb.setTinhTrangId(tinhTrangId);
        return thietBiRepository.save(tb);
    }
    private String getFullImageUrlFromHinhAnh(HinhAnhThietBi ha) {
        if (ha == null || ha.getUrlAnh() == null || ha.getUrlAnh().isBlank()) return null;
        String fileName = ha.getUrlAnh();
        if (fileName.startsWith("http")) return fileName;
        if (fileName.contains("/")) {
            return s3StorageService.getPublicUrl(fileName);
        }
        
        String category = "products";
        if (ha.getBanGiaoId() != null || ha.getBaoTriId() != null || (ha.getLoaiAnhId() != null && ha.getLoaiAnhId() == 2)) {
            category = "work";
        }
        return s3StorageService.getPublicUrlByCategory(category, fileName);
    }
}