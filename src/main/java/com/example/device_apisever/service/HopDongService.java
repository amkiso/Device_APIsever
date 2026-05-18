package com.example.device_apisever.service;

import com.example.device_apisever.dto.*;
import com.example.device_apisever.entity.*;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class HopDongService {

    private final HopDongThueRepository hopDongRepo;
    private final NguoiDungRepository nguoiDungRepo;
    private final ThietBiRepository thietBiRepo;
    private final LoaiThietBiRepository loaiThietBiRepo;
    private final DiaChiGiaoHangRepository diaChiRepo;
    private final ChiTietThueThietBiRepository chiTietRepo;
    private final GioHangRepository gioHangRepo;
    private final TrangThaiHopDongRepository trangThaiRepo;
    private final ChuKyDienTuRepository chuKyRepo;
    private final ThanhToanRepository thanhToanRepo;
    private final PasswordEncoder passwordEncoder;

    public HopDongService(HopDongThueRepository hopDongRepo,
                          NguoiDungRepository nguoiDungRepo,
                          ThietBiRepository thietBiRepo,
                          LoaiThietBiRepository loaiThietBiRepo,
                          DiaChiGiaoHangRepository diaChiRepo,
                          ChiTietThueThietBiRepository chiTietRepo,
                          GioHangRepository gioHangRepo,
                          TrangThaiHopDongRepository trangThaiRepo,
                          ChuKyDienTuRepository chuKyRepo,
                          ThanhToanRepository thanhToanRepo,
                          PasswordEncoder passwordEncoder) {
        this.hopDongRepo = hopDongRepo;
        this.nguoiDungRepo = nguoiDungRepo;
        this.thietBiRepo = thietBiRepo;
        this.loaiThietBiRepo = loaiThietBiRepo;
        this.diaChiRepo = diaChiRepo;
        this.chiTietRepo = chiTietRepo;
        this.gioHangRepo = gioHangRepo;
        this.trangThaiRepo = trangThaiRepo;
        this.chuKyRepo = chuKyRepo;
        this.thanhToanRepo = thanhToanRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private NguoiDung resolveNguoiDung(String taiKhoan) {
        return nguoiDungRepo.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    }

    private String getTrangThaiName(Integer id) {
        return trangThaiRepo.findById(id)
                .map(TrangThaiHopDong::getTenTrangThai).orElse("Không xác định");
    }

    /**
     * Tạo hợp đồng mới từ checkout
     */
    @Transactional
    public HopDongResponse taoHopDong(String taiKhoan, TaoHopDongRequest req) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);

        // Validate địa chỉ
        DiaChiGiaoHang dc = diaChiRepo.findById(req.getDiaChiGiaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại"));
        if (!dc.getNguoiDungId().equals(khach.getNguoiDungId())) {
            throw new BusinessException("Địa chỉ không thuộc về bạn");
        }

        // Parse ngày
        LocalDate ngayBD = LocalDate.parse(req.getNgayBatDauThue());
        LocalDateTime ngayBatDau = ngayBD.atStartOfDay();
        LocalDateTime ngayDuKienTra = ngayBD.plusMonths(req.getSoThangThue()).atStartOfDay();

        // Build địa chỉ giao dạng text
        String diaChiText = dc.getDiaChiChiTiet() + ", " + dc.getPhuongXa() + ", " + dc.getTinhThanhPho();

        // Xử lý danh sách thiết bị và tính tiền
        BigDecimal tongTienThue = BigDecimal.ZERO;
        BigDecimal tongGiaTriMay = BigDecimal.ZERO;
        List<ChiTietThueThietBi> chiTietList = new ArrayList<>();
        List<HopDongResponse.ChiTietThietBiResponse> chiTietResponses = new ArrayList<>();

        for (TaoHopDongRequest.ThietBiThueItem item : req.getDanhSachThietBi()) {
            for (int i = 0; i < item.getSoLuong(); i++) {
                // Tìm 1 thiết bị sẵn sàng (TinhTrangID=1) theo loại
                ThietBi tb = thietBiRepo.findByLoaiThietBiId(item.getThietBiId())
                        .stream()
                        .filter(t -> t.getTinhTrangId() == 1)
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(
                                "Không đủ thiết bị sẵn sàng cho loại ID: " + item.getThietBiId()));

                LoaiThietBi ltb = loaiThietBiRepo.findById(tb.getLoaiThietBiId()).orElse(null);
                BigDecimal giaThue = ltb != null ? ltb.getGiaThueThamKhao() : BigDecimal.ZERO;
                BigDecimal giaTriMay = tb.getGiaTriMay() != null ? tb.getGiaTriMay() : BigDecimal.ZERO;

                BigDecimal tienThueItem = giaThue.multiply(BigDecimal.valueOf(req.getSoThangThue()));
                tongTienThue = tongTienThue.add(tienThueItem);
                tongGiaTriMay = tongGiaTriMay.add(giaTriMay);

                ChiTietThueThietBi ct = ChiTietThueThietBi.builder()
                        .thietBiId(tb.getThietBiId())
                        .giaThueThang(giaThue)
                        .giaTriMay(giaTriMay)
                        .tinhTrangGiao(tb.getTinhTrangBanGiao())
                        .build();
                chiTietList.add(ct);

                chiTietResponses.add(HopDongResponse.ChiTietThietBiResponse.builder()
                        .tenThietBi(ltb != null ? ltb.getTenLoaiThietBi() : "N/A")
                        .soSerial(tb.getSoSerial())
                        .tinhTrangBanGiao(tb.getTinhTrangBanGiao())
                        .mucDichSuDung(tb.getMucDichSuDung())
                        .giaTriMay(giaTriMay)
                        .giaThueThang(giaThue)
                        .ngayKiemDinh(tb.getNgayKiemDinh() != null
                                ? tb.getNgayKiemDinh().format(DateTimeFormatter.ISO_LOCAL_DATE) : null)
                        .build());

                // Đánh dấu thiết bị đang cho thuê
                tb.setTinhTrangId(2);
                thietBiRepo.save(tb);
            }
        }

        // Tính tiền cọc = 50% tổng tiền thuê
        BigDecimal tienCoc = tongTienThue.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        BigDecimal thueVAT = tongTienThue.multiply(new BigDecimal("0.1"))
                .setScale(2, RoundingMode.HALF_UP);

        // Tạo hợp đồng
        HopDongThue hd = HopDongThue.builder()
                .nguoiDungKhachId(khach.getNguoiDungId())
                .nguoiDungTaoId(khach.getNguoiDungId())
                .ngayBatDauThue(ngayBatDau)
                .ngayDuKienTra(ngayDuKienTra)
                .diaDiemGiao(diaChiText)
                .tienCoc(tienCoc)
                .tongTienThue(tongTienThue)
                .phiBoiThuong(BigDecimal.ZERO)
                .trangThaiId(1) // Chờ ký kết
                .nguonTao(2) // Khách tạo qua app
                .diaChiGiaoId(req.getDiaChiGiaoId())
                .phuongThucThanhToan(req.getPhuongThucThanhToan())
                .thueVat(thueVAT)
                .ghiChuKhachHang(req.getGhiChuKhachHang())
                .build();

        hopDongRepo.save(hd);

        // Lưu chi tiết thiết bị
        for (ChiTietThueThietBi ct : chiTietList) {
            ct.setHopDongId(hd.getHopDongId());
            chiTietRepo.save(ct);
        }

        // Xóa giỏ hàng
        gioHangRepo.deleteByNguoiDungId(khach.getNguoiDungId());

        // Build mã hợp đồng
        String maHD = String.format("HD-%d-%05d",
                LocalDate.now().getYear(), hd.getHopDongId());

        return HopDongResponse.builder()
                .hopDongId(hd.getHopDongId())
                .maHopDong(maHD)
                .trangThai(getTrangThaiName(1))
                .chiTietThietBi(chiTietResponses)
                .chiPhi(HopDongResponse.ChiPhiResponse.builder()
                        .tongTienThue(tongTienThue)
                        .tienCoc(tienCoc)
                        .thueVAT(thueVAT)
                        .phiTreHanPhanTram(hd.getPhiTreHanPhanTram())
                        .soNgayTreHanMoiKy(hd.getSoNgayTreHanMoiKy())
                        .soNgayViPhamChamDut(hd.getSoNgayViPhamChamDut())
                        .phiVeSinhChuyenSau(hd.getPhiVeSinhChuyenSau())
                        .khauHaoHaoMonNam(hd.getKhauHaoHaoMonNam())
                        .phiGianDoanPhanTram(hd.getPhiGianDoanPhanTram())
                        .build())
                .build();
    }

    /**
     * Ký hợp đồng điện tử
     */
    @Transactional
    public KyHopDongResponse kyHopDong(String taiKhoan, Integer hopDongId,
                                        byte[] chuKyData, String maPin,
                                        String ipAddress, String thietBiKy) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        HopDongThue hd = hopDongRepo.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Hợp đồng không tồn tại"));

        if (!hd.getNguoiDungKhachId().equals(khach.getNguoiDungId())) {
            throw new BusinessException("Bạn không có quyền ký hợp đồng này");
        }
        if (hd.getTrangThaiId() != 1) {
            throw new BusinessException("Hợp đồng không ở trạng thái chờ ký kết");
        }

        // Lưu chữ ký điện tử
        ChuKyDienTu ck = ChuKyDienTu.builder()
                .hopDongId(hopDongId)
                .nguoiDungId(khach.getNguoiDungId())
                .duLieuChuKy(chuKyData)
                .maPinHash(passwordEncoder.encode(maPin))
                .ipAddress(ipAddress)
                .thietBiKy(thietBiKy)
                .build();
        chuKyRepo.save(ck);

        // Cập nhật trạng thái hợp đồng
        hd.setTrangThaiId(2); // Đã ký - Chờ thanh toán
        hd.setNgayKyDienTu(LocalDateTime.now());
        hd.setMaPinXacNhan(passwordEncoder.encode(maPin));
        hopDongRepo.save(hd);

        return KyHopDongResponse.builder()
                .hopDongId(hopDongId)
                .trangThai(getTrangThaiName(2))
                .ngayKy(hd.getNgayKyDienTu())
                .urlThanhToan(null) // Placeholder - tích hợp cổng thanh toán sau
                .build();
    }

    /**
     * Xác nhận thanh toán (callback từ cổng)
     */
    @Transactional
    public XacNhanThanhToanResponse xacNhanThanhToan(Integer hopDongId,
                                                      XacNhanThanhToanRequest req) {
        HopDongThue hd = hopDongRepo.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Hợp đồng không tồn tại"));

        if (hd.getTrangThaiId() != 2) {
            throw new BusinessException("Hợp đồng không ở trạng thái chờ thanh toán");
        }

        // Lưu giao dịch thanh toán
        ThanhToan tt = ThanhToan.builder()
                .hopDongId(hopDongId)
                .phuongThuc(req.getPhuongThuc())
                .loaiThanhToan(1) // Cọc
                .maGiaoDich(req.getMaGiaoDich())
                .soTien(req.getSoTien())
                .trangThai(req.getTrangThai())
                .ngayThanhToan(LocalDateTime.now())
                .build();
        thanhToanRepo.save(tt);

        // Nếu thanh toán thành công
        if (req.getTrangThai() == 1) {
            hd.setTrangThaiId(3); // Đã thanh toán - Chờ phê duyệt
            hopDongRepo.save(hd);
        }

        return XacNhanThanhToanResponse.builder()
                .hopDongId(hopDongId)
                .trangThai(getTrangThaiName(hd.getTrangThaiId()))
                .maGiaoDich(req.getMaGiaoDich())
                .build();
    }

    // ─────────────────────────────────────────────────────
    //  19. DANH SÁCH & CHI TIẾT HỢP ĐỒNG KHÁCH HÀNG
    // ─────────────────────────────────────────────────────

    /**
     * Lấy danh sách hợp đồng của khách hàng (sắp xếp theo ngày lập mới nhất)
     */
    public List<HopDongSummaryResponse> getMyContracts(String taiKhoan) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        List<HopDongThue> contracts = hopDongRepo
                .findByNguoiDungKhachIdOrderByNgayLapDesc(khach.getNguoiDungId());

        return contracts.stream().map(hd -> {
            long soTB = chiTietRepo.countByHopDongId(hd.getHopDongId());
            String maHD = String.format("HD-%d-%05d",
                    hd.getNgayLap().getYear(), hd.getHopDongId());

            return HopDongSummaryResponse.builder()
                    .hopDongId(hd.getHopDongId())
                    .maHopDong(maHD)
                    .trangThaiId(hd.getTrangThaiId())
                    .trangThai(getTrangThaiName(hd.getTrangThaiId()))
                    .ngayLap(hd.getNgayLap())
                    .ngayBatDauThue(hd.getNgayBatDauThue())
                    .ngayDuKienTra(hd.getNgayDuKienTra())
                    .tongTienThue(hd.getTongTienThue())
                    .tienCoc(hd.getTienCoc())
                    .soThietBi((int) soTB)
                    .diaDiemGiao(hd.getDiaDiemGiao())
                    .build();
        }).toList();
    }

    /**
     * Lấy N hợp đồng gần nhất (cho label trang chủ carousel)
     */
    public List<HopDongSummaryResponse> getRecentContracts(String taiKhoan, int limit) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        List<HopDongThue> contracts = hopDongRepo
                .findTopNByNguoiDungKhachId(khach.getNguoiDungId(), limit);

        return contracts.stream().map(hd -> {
            long soTB = chiTietRepo.countByHopDongId(hd.getHopDongId());
            String maHD = String.format("HD-%d-%05d",
                    hd.getNgayLap().getYear(), hd.getHopDongId());

            return HopDongSummaryResponse.builder()
                    .hopDongId(hd.getHopDongId())
                    .maHopDong(maHD)
                    .trangThaiId(hd.getTrangThaiId())
                    .trangThai(getTrangThaiName(hd.getTrangThaiId()))
                    .ngayLap(hd.getNgayLap())
                    .ngayBatDauThue(hd.getNgayBatDauThue())
                    .ngayDuKienTra(hd.getNgayDuKienTra())
                    .tongTienThue(hd.getTongTienThue())
                    .tienCoc(hd.getTienCoc())
                    .soThietBi((int) soTB)
                    .diaDiemGiao(hd.getDiaDiemGiao())
                    .build();
        }).toList();
    }

    /**
     * Xem chi tiết hợp đồng (để xem lại hợp đồng đã tạo)
     */
    public HopDongDetailResponse getContractDetail(String taiKhoan, Integer hopDongId) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        HopDongThue hd = hopDongRepo.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Hợp đồng không tồn tại"));

        if (!hd.getNguoiDungKhachId().equals(khach.getNguoiDungId())) {
            throw new BusinessException("Bạn không có quyền xem hợp đồng này");
        }

        String maHD = String.format("HD-%d-%05d",
                hd.getNgayLap().getYear(), hd.getHopDongId());

        // Lấy chi tiết thiết bị
        List<ChiTietThueThietBi> chiTietList = chiTietRepo.findByHopDongId(hd.getHopDongId());
        List<HopDongResponse.ChiTietThietBiResponse> chiTietResponses = chiTietList.stream()
                .map(ct -> {
                    ThietBi tb = thietBiRepo.findById(ct.getThietBiId()).orElse(null);
                    LoaiThietBi ltb = tb != null
                            ? loaiThietBiRepo.findById(tb.getLoaiThietBiId()).orElse(null) : null;

                    return HopDongResponse.ChiTietThietBiResponse.builder()
                            .tenThietBi(ltb != null ? ltb.getTenLoaiThietBi() : "N/A")
                            .soSerial(tb != null ? tb.getSoSerial() : "")
                            .tinhTrangBanGiao(ct.getTinhTrangGiao())
                            .mucDichSuDung(tb != null ? tb.getMucDichSuDung() : "")
                            .giaTriMay(ct.getGiaTriMay())
                            .giaThueThang(ct.getGiaThueThang())
                            .ngayKiemDinh(tb != null && tb.getNgayKiemDinh() != null
                                    ? tb.getNgayKiemDinh().format(DateTimeFormatter.ISO_LOCAL_DATE) : null)
                            .build();
                }).toList();

        // Tính số tháng thuê từ ngày bắt đầu → ngày dự kiến trả
        int soThangThue = (int) java.time.temporal.ChronoUnit.MONTHS.between(
                hd.getNgayBatDauThue().toLocalDate(),
                hd.getNgayDuKienTra().toLocalDate());

        // Thông tin khách hàng
        HopDongDetailResponse.KhachHangInfo khInfo = HopDongDetailResponse.KhachHangInfo.builder()
                .hoTen(khach.getHoTen())
                .email(khach.getEmail())
                .soDienThoai(khach.getSoDienThoai())
                .diaChi(khach.getDiaChi())
                .cccd(khach.getCccd())
                .cccdNgayCap(khach.getCccdNgayCap() != null
                        ? khach.getCccdNgayCap().format(DateTimeFormatter.ISO_LOCAL_DATE) : null)
                .cccdNoiCap(khach.getCccdNoiCap())
                .donViCongTac(khach.getDonViCongTac())
                .build();

        return HopDongDetailResponse.builder()
                .hopDongId(hd.getHopDongId())
                .maHopDong(maHD)
                .trangThaiId(hd.getTrangThaiId())
                .trangThai(getTrangThaiName(hd.getTrangThaiId()))
                .ngayLap(hd.getNgayLap())
                .ngayBatDauThue(hd.getNgayBatDauThue())
                .ngayDuKienTra(hd.getNgayDuKienTra())
                .ngayKyDienTu(hd.getNgayKyDienTu())
                .diaDiemGiao(hd.getDiaDiemGiao())
                .ghiChuKhachHang(hd.getGhiChuKhachHang())
                .soThangThue(soThangThue)
                .khachHang(khInfo)
                .chiTietThietBi(chiTietResponses)
                .chiPhi(HopDongResponse.ChiPhiResponse.builder()
                        .tongTienThue(hd.getTongTienThue())
                        .tienCoc(hd.getTienCoc())
                        .thueVAT(hd.getThueVat())
                        .phiTreHanPhanTram(hd.getPhiTreHanPhanTram())
                        .soNgayTreHanMoiKy(hd.getSoNgayTreHanMoiKy())
                        .soNgayViPhamChamDut(hd.getSoNgayViPhamChamDut())
                        .phiVeSinhChuyenSau(hd.getPhiVeSinhChuyenSau())
                        .khauHaoHaoMonNam(hd.getKhauHaoHaoMonNam())
                        .phiGianDoanPhanTram(hd.getPhiGianDoanPhanTram())
                        .build())
                .build();
    }

    /**
     * Đếm số đơn hàng theo trạng thái cho trang Hồ sơ
     */
    public DonHangCountResponse getDonHangCount(String taiKhoan) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        Integer uid = khach.getNguoiDungId();

        long choXetDuyet = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 1);
        long canThanhToan = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 2);
        long choGiao3 = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 3);
        long choGiao4 = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 4);
        long dangThue = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 5);
        long tong = hopDongRepo.countByNguoiDungKhachId(uid);

        return DonHangCountResponse.builder()
                .choXetDuyet(choXetDuyet)
                .canThanhToan(canThanhToan)
                .choGiaoHang(choGiao3 + choGiao4)
                .dangThue(dangThue)
                .tongDonHang(tong)
                .build();
    }
}

