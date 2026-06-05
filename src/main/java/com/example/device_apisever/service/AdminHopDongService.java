package com.example.device_apisever.service;

import com.example.device_apisever.dto.request.UpdateTrangThaiHopDongRequest;
import com.example.device_apisever.dto.response.*;
import com.example.device_apisever.entity.*;
import com.example.device_apisever.repository.*;
import com.example.device_apisever.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminHopDongService {

    private final HopDongThueRepository hopDongThueRepository;
    private final ChiTietThueThietBiRepository chiTietThueThietBiRepository;
    private final LichSuBaoTriRepository lichSuBaoTriRepository;
    private final ChuKyDienTuRepository chuKyDienTuRepository;
    private final ThietBiRepository thietBiRepository;
    private final LoaiThietBiRepository loaiThietBiRepository;
    private final HinhAnhThietBiRepository hinhAnhThietBiRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final TrangThaiHopDongRepository trangThaiHopDongRepository;
    private final TinhTrangThietBiRepository tinhTrangThietBiRepository;
    private final S3StorageService s3StorageService;

    public AdminHopDongService(HopDongThueRepository hopDongThueRepository,
                               ChiTietThueThietBiRepository chiTietThueThietBiRepository,
                               LichSuBaoTriRepository lichSuBaoTriRepository,
                               ChuKyDienTuRepository chuKyDienTuRepository,
                               ThietBiRepository thietBiRepository,
                               LoaiThietBiRepository loaiThietBiRepository,
                               HinhAnhThietBiRepository hinhAnhThietBiRepository,
                               NguoiDungRepository nguoiDungRepository,
                               TrangThaiHopDongRepository trangThaiHopDongRepository,
                               TinhTrangThietBiRepository tinhTrangThietBiRepository,
                               S3StorageService s3StorageService) {
        this.hopDongThueRepository = hopDongThueRepository;
        this.chiTietThueThietBiRepository = chiTietThueThietBiRepository;
        this.lichSuBaoTriRepository = lichSuBaoTriRepository;
        this.chuKyDienTuRepository = chuKyDienTuRepository;
        this.thietBiRepository = thietBiRepository;
        this.loaiThietBiRepository = loaiThietBiRepository;
        this.hinhAnhThietBiRepository = hinhAnhThietBiRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.trangThaiHopDongRepository = trangThaiHopDongRepository;
        this.tinhTrangThietBiRepository = tinhTrangThietBiRepository;
        this.s3StorageService = s3StorageService;
    }

    public Page<AdminHopDongResponse> getDanhSachHopDong(String q, LocalDateTime startDate, LocalDateTime endDate,
                                                         Integer trangThaiId, Integer loaiHopDongId, Pageable pageable) {
        Page<HopDongThue> page = hopDongThueRepository.searchAndFilter(q, startDate, endDate, trangThaiId, loaiHopDongId, pageable);
        
        // Cache map trạng thái
        Map<Integer, String> statusMap = trangThaiHopDongRepository.findAll().stream()
                .collect(Collectors.toMap(TrangThaiHopDong::getTrangThaiId, TrangThaiHopDong::getTenTrangThai));

        return page.map(hd -> {
            NguoiDung nd = nguoiDungRepository.findById(hd.getNguoiDungKhachId()).orElse(null);
            return AdminHopDongResponse.builder()
                    .hopDongId(hd.getHopDongId())
                    .maHopDong(String.format("HD-%d-%05d", hd.getNgayLap().getYear(), hd.getHopDongId()))
                    .tenKhachHang(nd != null ? nd.getHoTen() : "Không xác định")
                    .soDienThoai(nd != null ? nd.getSoDienThoai() : "")
                    .loaiHopDongId(hd.getLoaiHopDongId())
                    .laHoaToc(hd.getLaHoaToc())
                    .tongTienThue(hd.getTongTienThue())
                    .tienCoc(hd.getTienCoc())
                    .trangThaiId(hd.getTrangThaiId())
                    .tenTrangThai(statusMap.getOrDefault(hd.getTrangThaiId(), "Trạng thái " + hd.getTrangThaiId()))
                    .ngayLap(hd.getNgayLap())
                    .ngayBatDauThue(hd.getNgayBatDauThue())
                    .ngayDuKienTra(hd.getNgayDuKienTra())
                    .ngayTraThucTe(hd.getNgayTraThucTe())
                    .build();
        });
    }

    public ChiTietHopDongAdminResponse getChiTietHopDong(Integer hopDongId) {
        HopDongThue hd = hopDongThueRepository.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hợp đồng ID: " + hopDongId));

        NguoiDung kh = nguoiDungRepository.findById(hd.getNguoiDungKhachId()).orElse(null);
        TrangThaiHopDong status = trangThaiHopDongRepository.findById(hd.getTrangThaiId()).orElse(null);

        // Map KhachHangInfo
        ChiTietHopDongAdminResponse.KhachHangInfo khInfo = null;
        if (kh != null) {
            khInfo = ChiTietHopDongAdminResponse.KhachHangInfo.builder()
                    .nguoiDungId(kh.getNguoiDungId())
                    .hoTen(kh.getHoTen())
                    .email(kh.getEmail())
                    .soDienThoai(kh.getSoDienThoai())
                    .cccd(kh.getCccd())
                    .build();
        }

        // Map ChiPhiInfo
        ChiTietHopDongAdminResponse.ChiPhiInfo cpInfo = ChiTietHopDongAdminResponse.ChiPhiInfo.builder()
                .tongTienThue(hd.getTongTienThue())
                .tienCoc(hd.getTienCoc())
                .thueVat(hd.getThueVat())
                .phiTreHanPhanTram(hd.getPhiTreHanPhanTram())
                .soNgayTreHanMoiKy(hd.getSoNgayTreHanMoiKy())
                .soNgayViPhamChamDut(hd.getSoNgayViPhamChamDut())
                .phiVeSinhChuyenSau(hd.getPhiVeSinhChuyenSau())
                .phiGianDoanPhanTram(hd.getPhiGianDoanPhanTram())
                .build();

        // Danh sách thiết bị
        List<ChiTietThueThietBi> chiTiets = chiTietThueThietBiRepository.findByHopDongId(hopDongId);
        List<ThietBiHopDongResponse> tbResponses = chiTiets.stream().map(ct -> {
            ThietBi tb = thietBiRepository.findById(ct.getThietBiId()).orElse(null);
            String maTaiSan = "", tenLoai = "", soSerial = "", tenTinhTrang = "", hinhAnhUrl = "";
            Integer tinhTrangId = 1;
            if (tb != null) {
                maTaiSan = tb.getMaTaiSan();
                soSerial = tb.getSoSerial();
                tinhTrangId = tb.getTinhTrangId();
                LoaiThietBi loaiTB = loaiThietBiRepository.findById(tb.getLoaiThietBiId()).orElse(null);
                if (loaiTB != null) {
                    tenLoai = loaiTB.getTenLoaiThietBi();
                    hinhAnhUrl = getFullImageUrl(loaiTB.getAnhDaiDien());
                }
                TinhTrangThietBi tt = tinhTrangThietBiRepository.findById(tinhTrangId).orElse(null);
                if (tt != null) tenTinhTrang = tt.getTenTinhTrang();
                
                // Thử lấy ảnh thực tế của thiết bị (chỉ lấy ảnh sản phẩm, không lấy ảnh nghiệp vụ)
                List<HinhAnhThietBi> anhTBs = hinhAnhThietBiRepository.findByThietBiId(tb.getThietBiId());
                List<HinhAnhThietBi> productImages = anhTBs.stream()
                        .filter(ha -> ha.getBanGiaoId() == null && ha.getBaoTriId() == null && (ha.getLoaiAnhId() == null || ha.getLoaiAnhId() == 1))
                        .collect(Collectors.toList());
                
                if (!productImages.isEmpty()) {
                    HinhAnhThietBi latestAnh = productImages.get(productImages.size() - 1); // Lấy ảnh sản phẩm mới nhất
                    if (latestAnh.getUrlAnh() != null) {
                        hinhAnhUrl = getFullImageUrlFromHinhAnh(latestAnh);
                    }
                }
            }
            return ThietBiHopDongResponse.builder()
                    .chiTietId(ct.getChiTietId())
                    .thietBiId(ct.getThietBiId())
                    .maTaiSan(maTaiSan)
                    .tenLoaiThietBi(tenLoai)
                    .soSerial(soSerial)
                    .giaThueThang(ct.getGiaThueThang())
                    .giaTriMay(ct.getGiaTriMay())
                    .tinhTrangId(tinhTrangId)
                    .tenTinhTrang(tenTinhTrang)
                    .hinhAnhUrl(hinhAnhUrl)
                    .tinhTrangGiao(ct.getTinhTrangGiao())
                    .tinhTrangTra(ct.getTinhTrangTra())
                    .ngayGiao(ct.getNgayGiao())
                    .ngayTra(ct.getNgayTra())
                    .build();
        }).collect(Collectors.toList());

        // Danh sách bảo trì
        List<LichSuBaoTri> lsbts = lichSuBaoTriRepository.findByHopDongId(hopDongId);
        List<LichSuBaoTriResponse> btResponses = lsbts.stream().map(bt -> {
            String tenNguoiDung = "";
            NguoiDung nd = nguoiDungRepository.findById(bt.getNguoiDungBaoTriId()).orElse(null);
            if (nd != null) tenNguoiDung = nd.getHoTen();
            
            String tenThietBi = "";
            ThietBi tb = thietBiRepository.findById(bt.getThietBiId()).orElse(null);
            if (tb != null) {
                LoaiThietBi loaiTB = loaiThietBiRepository.findById(tb.getLoaiThietBiId()).orElse(null);
                if (loaiTB != null) tenThietBi = loaiTB.getTenLoaiThietBi() + " (" + tb.getMaTaiSan() + ")";
            }

            return LichSuBaoTriResponse.builder()
                    .baoTriId(bt.getBaoTriId())
                    .thietBiId(bt.getThietBiId())
                    .tenThietBi(tenThietBi)
                    .nguoiDungBaoTriId(bt.getNguoiDungBaoTriId())
                    .tenNguoiDungBaoTri(tenNguoiDung)
                    .ngayThucHien(bt.getNgayThucHien())
                    .loaiBaoTriId(bt.getLoaiBaoTriId())
                    .noiDungBaoTri(bt.getNoiDungBaoTri())
                    .chiPhi(bt.getChiPhi())
                    .trangThaiId(bt.getTrangThaiId())
                    .tinhVaoBoiThuong(bt.getTinhVaoBoiThuong())
                    .ghiChu(bt.getGhiChu())
                    .build();
        }).collect(Collectors.toList());

        // Phí hỏa tốc: map thành 0 nếu laHoaToc = false
        BigDecimal phiHoaTocDisplay = (hd.getLaHoaToc() != null && hd.getLaHoaToc()) 
                                        ? hd.getPhiHoaToc() 
                                        : BigDecimal.ZERO;

        return ChiTietHopDongAdminResponse.builder()
                .hopDongId(hd.getHopDongId())
                .maHopDong(String.format("HD-%d-%05d", hd.getNgayLap().getYear(), hd.getHopDongId()))
                .loaiHopDongId(hd.getLoaiHopDongId())
                .laHoaToc(hd.getLaHoaToc() != null && hd.getLaHoaToc())
                .phiHoaToc(phiHoaTocDisplay)
                .trangThaiId(hd.getTrangThaiId())
                .tenTrangThai(status != null ? status.getTenTrangThai() : "Không xác định")
                .ngayLap(hd.getNgayLap())
                .ngayBatDauThue(hd.getNgayBatDauThue())
                .ngayDuKienTra(hd.getNgayDuKienTra())
                .ngayTraThucTe(hd.getNgayTraThucTe())
                .diaDiemGiao(hd.getDiaDiemGiao())
                .ghiChuKhachHang(hd.getGhiChuKhachHang())
                .lyDoHuy(hd.getLyDoHuy())
                .khachHang(khInfo)
                .chiPhi(cpInfo)
                .danhSachThietBi(tbResponses)
                .lichSuBaoTri(btResponses)
                .build();
    }

    @Transactional
    public void updateTrangThai(Integer hopDongId, UpdateTrangThaiHopDongRequest request) {
        HopDongThue hd = hopDongThueRepository.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hợp đồng ID: " + hopDongId));

        Integer oldStatus = hd.getTrangThaiId();
        Integer newStatus = request.getTrangThaiId();

        if (oldStatus == 12) {
            throw new IllegalArgumentException("Không thể cập nhật trạng thái của hợp đồng đã hoàn tất.");
        }
        if (oldStatus == 7 || oldStatus == 11) {
            throw new IllegalArgumentException("Không thể cập nhật trạng thái của hợp đồng đã bị hủy.");
        }
        if (newStatus == 11 && (oldStatus > 3)) {
            throw new IllegalArgumentException("Chỉ có thể hủy hợp đồng khi chưa bắt đầu cho thuê (Trạng thái 1, 2, 3).");
        }

        hd.setTrangThaiId(newStatus);
        
        // Cập nhật ngày trả thực tế nếu hoàn tất hoặc thu hồi
        if (newStatus == 8 || newStatus == 12) {
            if (hd.getNgayTraThucTe() == null) {
                hd.setNgayTraThucTe(LocalDateTime.now());
            }
        }
        
        if (newStatus == 11 && request.getLyDoHuy() != null) {
            hd.setLyDoHuy(request.getLyDoHuy());
        }

        hopDongThueRepository.save(hd);
    }

    public ChuKyDienTuResponse getChuKyKhachHang(Integer hopDongId, Integer currentUserId, boolean isAdmin) {
        HopDongThue hd = hopDongThueRepository.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hợp đồng ID: " + hopDongId));

        if (!isAdmin && !hd.getNguoiDungKhachId().equals(currentUserId)) {
            throw new IllegalArgumentException("Bạn không có quyền xem chữ ký của hợp đồng này.");
        }

        ChuKyDienTu ck = chuKyDienTuRepository.findByHopDongId(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Chưa có chữ ký điện tử cho hợp đồng này."));

        NguoiDung nd = nguoiDungRepository.findById(ck.getNguoiDungId()).orElse(null);

        return ChuKyDienTuResponse.builder()
                .chuKyId(ck.getChuKyId())
                .hopDongId(ck.getHopDongId())
                .nguoiDungId(ck.getNguoiDungId())
                .tenNguoiDung(nd != null ? nd.getHoTen() : "")
                .urlChuKy(s3StorageService.generateSasReadUrl(ck.getTenFileChuKy()))
                .ngayKy(ck.getNgayKy())
                .ipAddress(ck.getIpAddress())
                .thietBiKy(ck.getThietBiKy())
                .build();
    }

    private String getFullImageUrl(String fileName) {
        if (fileName == null || fileName.isBlank()) return null;
        if (fileName.startsWith("http")) return fileName;
        if (fileName.contains("/")) {
            return s3StorageService.getPublicUrl(fileName);
        }
        return s3StorageService.getPublicUrlByCategory("products", fileName);
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
