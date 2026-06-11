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
    private final CauHinhHopDongRepository cauHinhRepo;
    private final LoaiHopDongRepository loaiHopDongRepo;
    private final ThongBaoService thongBaoService;
    private final S3StorageService s3StorageService;

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
                          PasswordEncoder passwordEncoder,
                          CauHinhHopDongRepository cauHinhRepo,
                          LoaiHopDongRepository loaiHopDongRepo,
                          ThongBaoService thongBaoService,
                          S3StorageService s3StorageService) {
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
        this.cauHinhRepo = cauHinhRepo;
        this.loaiHopDongRepo = loaiHopDongRepo;
        this.thongBaoService = thongBaoService;
        this.s3StorageService = s3StorageService;
    }

    private NguoiDung resolveNguoiDung(String taiKhoan) {
        return nguoiDungRepo.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    }

    private String getTrangThaiName(Integer id) {
        return trangThaiRepo.findById(id)
                .map(TrangThaiHopDong::getTenTrangThai).orElse("Không xác định");
    }

    private String getLoaiHopDongName(Integer id) {
        if (id == null) return "Cá nhân";
        return loaiHopDongRepo.findById(id)
                .map(LoaiHopDong::getTenLoai).orElse("Cá nhân");
    }

    /**
     * Tạo hợp đồng mới từ checkout.
     * Trạng thái khởi tạo: 1 (Chờ xác nhận).
     * Tự động phân loại: Cá nhân / Doanh nghiệp / Hỏa tốc.
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

        // ── Phân loại hợp đồng ──
        boolean isDoanhNghiep = khach.getLoaiKhachHangId() != null && khach.getLoaiKhachHangId() == 2;
        boolean isHoaToc = ngayBD.equals(LocalDate.now());

        // Lấy cấu hình phí hỏa tốc (%)
        BigDecimal phiHoaTocPhanTram = cauHinhRepo.findByMaCauHinh("PHI_HOA_TOC")
                .map(CauHinhHopDong::getGiaTri).orElse(new BigDecimal("10"));

        // Lấy hạn thanh toán (ngày trước ngày bắt đầu)
        BigDecimal hanTTNgay = cauHinhRepo.findByMaCauHinh("HAN_THANH_TOAN_NGAY")
                .map(CauHinhHopDong::getGiaTri).orElse(new BigDecimal("2"));

        int loaiHopDongId = isHoaToc ? 3 : (isDoanhNghiep ? 2 : 1);

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
                        .thietBiId(tb.getThietBiId())
                        .tenThietBi(ltb != null ? ltb.getTenLoaiThietBi() : "N/A")
                        .soSerial(tb.getSoSerial())
                        .tinhTrangBanGiao(tb.getTinhTrangBanGiao())
                        .mucDichSuDung(tb.getMucDichSuDung())
                        .giaTriMay(giaTriMay)
                        .giaThueThang(giaThue)
                        .ngayKiemDinh(tb.getNgayKiemDinh() != null
                                ? tb.getNgayKiemDinh().format(DateTimeFormatter.ISO_LOCAL_DATE) : null)
                        .build());

                // KHÔNG đánh dấu thiết bị đang cho thuê ở bước tạo (chờ NV xác nhận mới gán)
                // tb.setTinhTrangId(2); — đã bỏ
            }
        }

        // Tính phí hỏa tốc
        BigDecimal phiHoaToc = isHoaToc
                ? tongTienThue.multiply(phiHoaTocPhanTram).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Tính tiền cọc = 50% tổng tiền thuê
        BigDecimal tienCoc = tongTienThue.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        BigDecimal thueVAT = tongTienThue.multiply(new BigDecimal("0.1"))
                .setScale(2, RoundingMode.HALF_UP);

        // Tính hạn thanh toán
        LocalDateTime hanThanhToan = isHoaToc
                ? LocalDateTime.now().plusHours(12)
                : ngayBatDau.minusDays(hanTTNgay.intValue());

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
                .trangThaiId(1) // Chờ xác nhận
                .nguonTao(2) // Khách tạo qua app
                .diaChiGiaoId(req.getDiaChiGiaoId())
                .phuongThucThanhToan(req.getPhuongThucThanhToan())
                .thueVat(thueVAT)
                .ghiChuKhachHang(req.getGhiChuKhachHang())
                // Phân loại & Hỏa tốc
                .loaiHopDongId(loaiHopDongId)
                .laHoaToc(isHoaToc)
                .phiHoaToc(phiHoaToc)
                .hanThanhToan(hanThanhToan)
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

        // ── Gửi thông báo hỏa tốc đến tất cả nhân viên ──
        if (isHoaToc) {
            try {
                // Gửi cho vai trò 1 (Admin), 2 (Thủ kho), 3 (Kỹ thuật)
                for (int vaiTro : new int[]{1, 2, 3}) {
                    ThongBaoRequest tbRequest = new ThongBaoRequest();
                    tbRequest.setTieuDe("🔥 Đơn hỏa tốc mới: " + maHD);
                    tbRequest.setNoiDung("Khách hàng " + khach.getHoTen()
                            + " đặt đơn hỏa tốc cần xử lý ngay! Số thiết bị: "
                            + chiTietList.size());
                    tbRequest.setLoaiThongBao(2); // By role
                    tbRequest.setVaiTroNhanId(vaiTro);
                    thongBaoService.taoThongBao(tbRequest, khach.getNguoiDungId());
                }
            } catch (Exception e) {
                // Không để lỗi thông báo ảnh hưởng tạo hợp đồng
            }
        }

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
     * Ký hợp đồng điện tử — upload chữ ký lên Azure Blob (Private).
     */
    @Transactional
    public KyHopDongResponse kyHopDong(String taiKhoan, Integer hopDongId,
                                        String fileName, String maPin,
                                        String ipAddress, String thietBiKy) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        HopDongThue hd = hopDongRepo.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Hợp đồng không tồn tại"));

        if (!hd.getNguoiDungKhachId().equals(khach.getNguoiDungId())) {
            throw new BusinessException("Bạn không có quyền ký hợp đồng này");
        }
        if (hd.getTrangThaiId() != 2) {
            throw new BusinessException("Hợp đồng không ở trạng thái chờ ký kết");
        }

        // Lấy relative path từ fileName (client đã upload)
        String relativePath = s3StorageService.getRelativePath("sign", fileName);

        // Lưu tên file vào DB (Relative path)
        ChuKyDienTu ck = ChuKyDienTu.builder()
                .hopDongId(hopDongId)
                .nguoiDungId(khach.getNguoiDungId())
                .tenFileChuKy(relativePath) // LƯU RELATIVE PATH

                .ipAddress(ipAddress)
                .thietBiKy(thietBiKy)
                .build();
        chuKyRepo.save(ck);

        // Cập nhật trạng thái hợp đồng
        hd.setTrangThaiId(2); // Đã xác nhận - Chờ thanh toán cọc
        hd.setNgayKyDienTu(LocalDateTime.now());
        hd.setMaPinXacNhan(passwordEncoder.encode(maPin));
        hopDongRepo.save(hd);

        return KyHopDongResponse.builder()
                .hopDongId(hopDongId)
                .trangThai(getTrangThaiName(2))
                .ngayKy(hd.getNgayKyDienTu())
                .urlThanhToan(null)
                .build();
    }

    /**
     * Khách hàng hủy hợp đồng — chỉ cho phép khi trạng thái = 1 (Chờ xác nhận).
     */
    @Transactional
    public void huyHopDong(String taiKhoan, Integer hopDongId, String lyDoHuy) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        HopDongThue hd = hopDongRepo.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Hợp đồng không tồn tại"));

        if (!hd.getNguoiDungKhachId().equals(khach.getNguoiDungId())) {
            throw new BusinessException("Bạn không có quyền hủy hợp đồng này");
        }
        if (hd.getTrangThaiId() != 1) {
            throw new BusinessException("Chỉ có thể hủy hợp đồng ở trạng thái 'Chờ xác nhận'");
        }

        hd.setTrangThaiId(7); // Đã hủy bởi khách hàng
        hd.setLyDoHuy(lyDoHuy != null ? lyDoHuy : "Khách hàng tự hủy");
        hopDongRepo.save(hd);
    }

    /**
     * Gửi yêu cầu hỗ trợ / bảo trì — tạo thông báo đến nhân viên.
     */
    public void guiYeuCauHoTro(String taiKhoan, Integer hopDongId, YeuCauHoTroRequest request) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        HopDongThue hd = hopDongRepo.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Hợp đồng không tồn tại"));

        if (!hd.getNguoiDungKhachId().equals(khach.getNguoiDungId())) {
            throw new BusinessException("Bạn không có quyền thao tác hợp đồng này");
        }

        // Yêu cầu bảo trì chỉ khi đang cho thuê (trạng thái 4)
        if (request.getLoaiYeuCau() != null && request.getLoaiYeuCau() == 2 && hd.getTrangThaiId() != 4) {
            throw new BusinessException("Chỉ có thể yêu cầu bảo trì khi hợp đồng đang cho thuê");
        }

        String maHD = String.format("HD-%d-%05d", hd.getNgayLap().getYear(), hd.getHopDongId());
        String loaiStr = (request.getLoaiYeuCau() != null && request.getLoaiYeuCau() == 2) ? "Bảo trì" : "Hỗ trợ";

        // Gửi thông báo đến tất cả nhân viên (role 1, 2, 3)
        for (int vaiTro : new int[]{1, 2, 3}) {
            try {
                ThongBaoRequest tbRequest = new ThongBaoRequest();
                tbRequest.setTieuDe("📋 Yêu cầu " + loaiStr + ": " + maHD);
                tbRequest.setNoiDung("Khách hàng " + khach.getHoTen() + " yêu cầu "
                        + loaiStr.toLowerCase() + " cho hợp đồng " + maHD + ": "
                        + (request.getNoiDung() != null ? request.getNoiDung() : ""));
                tbRequest.setLoaiThongBao(2);
                tbRequest.setVaiTroNhanId(vaiTro);
                thongBaoService.taoThongBao(tbRequest, khach.getNguoiDungId());
            } catch (Exception e) {
                // Không để lỗi thông báo ảnh hưởng
            }
        }
    }

    /**
     * Thanh toán demo — bất kỳ phương thức nào cũng đều chấp nhận.
     * - Trạng thái 2 (Chờ TT cọc) → 3 (Chờ nhận thiết bị)
     * - Trạng thái 10 (Chờ TT nợ) → 12 (Hoàn tất)
     */
    @Transactional
    public XacNhanThanhToanResponse thanhToanDemo(String taiKhoan, Integer hopDongId) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        HopDongThue hd = hopDongRepo.findById(hopDongId)
                .orElseThrow(() -> new ResourceNotFoundException("Hợp đồng không tồn tại"));

        if (!hd.getNguoiDungKhachId().equals(khach.getNguoiDungId())) {
            throw new BusinessException("Bạn không có quyền thao tác hợp đồng này");
        }

        // Thanh toán cọc (trạng thái 2 → 3)
        if (hd.getTrangThaiId() == 2) {
            ThanhToan tt = ThanhToan.builder()
                    .hopDongId(hopDongId)
                    .phuongThuc(hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan() : 1)
                    .loaiThanhToan(1) // Cọc
                    .maGiaoDich("DEMO-" + System.currentTimeMillis())
                    .soTien(hd.getTienCoc())
                    .trangThai(1) // Success
                    .ngayThanhToan(LocalDateTime.now())
                    .build();
            thanhToanRepo.save(tt);

            hd.setTrangThaiId(3); // Chờ nhận thiết bị
            hopDongRepo.save(hd);

            return XacNhanThanhToanResponse.builder()
                    .hopDongId(hopDongId)
                    .trangThai(getTrangThaiName(3))
                    .maGiaoDich(tt.getMaGiaoDich())
                    .build();
        }
        // Thanh toán nợ (trạng thái 10 → 12)
        else if (hd.getTrangThaiId() == 10) {
            BigDecimal soTienNo = hd.getTongTienThue()
                    .add(hd.getPhiBoiThuong())
                    .subtract(hd.getTienCoc());

            ThanhToan tt = ThanhToan.builder()
                    .hopDongId(hopDongId)
                    .phuongThuc(hd.getPhuongThucThanhToan() != null ? hd.getPhuongThucThanhToan() : 1)
                    .loaiThanhToan(3) // Phí phát sinh
                    .maGiaoDich("DEMO-DEBT-" + System.currentTimeMillis())
                    .soTien(soTienNo)
                    .trangThai(1) // Success
                    .ngayThanhToan(LocalDateTime.now())
                    .build();
            thanhToanRepo.save(tt);

            hd.setTrangThaiId(12); // Hoàn tất
            hopDongRepo.save(hd);

            return XacNhanThanhToanResponse.builder()
                    .hopDongId(hopDongId)
                    .trangThai(getTrangThaiName(12))
                    .maGiaoDich(tt.getMaGiaoDich())
                    .build();
        } else {
            throw new BusinessException("Hợp đồng không ở trạng thái cần thanh toán");
        }
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
            hd.setTrangThaiId(3); // Chờ nhận thiết bị
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

        return contracts.stream().map(this::buildSummaryResponse).toList();
    }

    /**
     * Lấy N hợp đồng gần nhất (cho label trang chủ carousel)
     */
    public List<HopDongSummaryResponse> getRecentContracts(String taiKhoan, int limit) {
        NguoiDung khach = resolveNguoiDung(taiKhoan);
        List<HopDongThue> contracts = hopDongRepo
                .findTopNByNguoiDungKhachId(khach.getNguoiDungId(),
                        org.springframework.data.domain.PageRequest.of(0, limit));

        return contracts.stream().map(this::buildSummaryResponse).toList();
    }

    /** Helper — Build summary response cho 1 hợp đồng */
    private HopDongSummaryResponse buildSummaryResponse(HopDongThue hd) {
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
                .laHoaToc(hd.getLaHoaToc())
                .loaiHopDong(getLoaiHopDongName(hd.getLoaiHopDongId()))
                .hanThanhToan(hd.getHanThanhToan())
                .build();
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
                            .thietBiId(tb != null ? tb.getThietBiId() : null)
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
                // Phân loại & Hỏa tốc
                .laHoaToc(hd.getLaHoaToc())
                .loaiHopDong(getLoaiHopDongName(hd.getLoaiHopDongId()))
                .phiHoaToc(hd.getPhiHoaToc())
                .hanThanhToan(hd.getHanThanhToan())
                .lyDoHuy(hd.getLyDoHuy())
                .phiPhatSinh(hd.getPhiBoiThuong())
                .phuongThucThanhToan(hd.getPhuongThucThanhToan())
                // Khách hàng & chi tiết
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

        long choXacNhan = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 1);
        long canThanhToan = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 2);
        long choNhanTB = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 3);
        long dangThue = hopDongRepo.countByNguoiDungKhachIdAndTrangThaiId(uid, 4);
        long tong = hopDongRepo.countByNguoiDungKhachId(uid);

        return DonHangCountResponse.builder()
                .choXetDuyet(choXacNhan)
                .canThanhToan(canThanhToan)
                .choGiaoHang(choNhanTB)
                .dangThue(dangThue)
                .tongDonHang(tong)
                .build();
    }
}
