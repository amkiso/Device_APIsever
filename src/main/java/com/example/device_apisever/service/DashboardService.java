package com.example.device_apisever.service;

import com.example.device_apisever.dto.DashboardResponse;
import com.example.device_apisever.dto.DashboardResponse.NhacNhoItem;
import com.example.device_apisever.entity.HopDongThue;
import com.example.device_apisever.entity.LichSuBanGiao;
import com.example.device_apisever.entity.LichSuBaoTri;
import com.example.device_apisever.entity.ThietBi;
import com.example.device_apisever.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final HopDongThueRepository hopDongThueRepository;
    private final ThietBiRepository thietBiRepository;
    private final LichSuBaoTriRepository lichSuBaoTriRepository;
    private final LichSuBanGiaoRepository lichSuBanGiaoRepository;
    private final NguoiDungRepository nguoiDungRepository;

    public DashboardService(HopDongThueRepository hopDongThueRepository,
                            ThietBiRepository thietBiRepository,
                            LichSuBaoTriRepository lichSuBaoTriRepository,
                            LichSuBanGiaoRepository lichSuBanGiaoRepository,
                            NguoiDungRepository nguoiDungRepository) {
        this.hopDongThueRepository = hopDongThueRepository;
        this.thietBiRepository = thietBiRepository;
        this.lichSuBaoTriRepository = lichSuBaoTriRepository;
        this.lichSuBanGiaoRepository = lichSuBanGiaoRepository;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    /**
     * Trả về toàn bộ dữ liệu tổng hợp cho trang chủ Admin.
     */
    public DashboardResponse getDashboardData() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // ========== 1. DOANH THU ==========
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        int prevMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int prevYear = currentMonth == 1 ? currentYear - 1 : currentYear;

        BigDecimal doanhThuThangNay = hopDongThueRepository.findDoanhThuByMonth(currentYear, currentMonth);
        BigDecimal doanhThuThangTruoc = hopDongThueRepository.findDoanhThuByMonth(prevYear, prevMonth);

        Double tiLeTangTruong = null;
        if (doanhThuThangTruoc.compareTo(BigDecimal.ZERO) > 0) {
            tiLeTangTruong = doanhThuThangNay.subtract(doanhThuThangTruoc)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(doanhThuThangTruoc, 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        // ========== 2. THIẾT BỊ ĐANG BẢO TRÌ ==========
        // TinhTrangID = 3: Đang bảo trì
        long soThietBiDangBaoTri = thietBiRepository.countByTinhTrangId(3);

        // ========== 3. HỢP ĐỒNG ĐẾN HẠN (7 ngày tới) ==========
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime endOf7Days = today.plusDays(7).atTime(LocalTime.MAX);
        long soHopDongDenHan = hopDongThueRepository.countHopDongDenHan(startOfToday, endOf7Days);

        // ========== 4. NHẮC NHỞ HÔM NAY ==========
        List<NhacNhoItem> nhacNhoHomNay = new ArrayList<>();

        // 4a. Hợp đồng đến hạn trả hôm nay
        List<HopDongThue> hopDongHomNay = hopDongThueRepository.findHopDongDenHanHomNay(now);
        for (HopDongThue hd : hopDongHomNay) {
            String tenKH = nguoiDungRepository.findById(hd.getNguoiDungKhachId())
                    .map(nd -> nd.getHoTen())
                    .orElse("Không rõ");
            nhacNhoHomNay.add(NhacNhoItem.builder()
                    .loai("HOP_DONG")
                    .tieuDe("Hợp đồng #" + hd.getHopDongId() + " đến hạn trả")
                    .moTa("Khách hàng: " + tenKH + " - Địa điểm: " + hd.getDiaDiemGiao())
                    .referenceId(hd.getHopDongId())
                    .build());
        }

        // 4b. Thiết bị cần bảo trì hôm nay
        List<ThietBi> thietBiBaoTri = thietBiRepository.findThietBiCanBaoTriHomNay(now);
        for (ThietBi tb : thietBiBaoTri) {
            nhacNhoHomNay.add(NhacNhoItem.builder()
                    .loai("BAO_TRI")
                    .tieuDe("Thiết bị " + tb.getMaTaiSan() + " cần bảo trì")
                    .moTa("Đã đến ngày bảo trì định kỳ")
                    .referenceId(tb.getThietBiId())
                    .build());
        }

        // 4c. Lịch bàn giao hôm nay
        List<LichSuBanGiao> banGiaoHomNay = lichSuBanGiaoRepository.findBanGiaoHomNay(now);
        for (LichSuBanGiao bg : banGiaoHomNay) {
            nhacNhoHomNay.add(NhacNhoItem.builder()
                    .loai("BAN_GIAO")
                    .tieuDe("Lịch bàn giao #" + bg.getBanGiaoId())
                    .moTa("Thiết bị ID: " + bg.getThietBiId() +
                           (bg.getNguoiNhan() != null ? " - Người nhận: " + bg.getNguoiNhan() : ""))
                    .referenceId(bg.getBanGiaoId())
                    .build());
        }

        // ========== BUILD RESPONSE ==========
        return DashboardResponse.builder()
                .doanhThuThangNay(doanhThuThangNay)
                .doanhThuThangTruoc(doanhThuThangTruoc)
                .tiLeTangTruong(tiLeTangTruong)
                .soThietBiDangBaoTri(soThietBiDangBaoTri)
                .soHopDongDenHan(soHopDongDenHan)
                .nhacNhoHomNay(nhacNhoHomNay)
                .build();
    }

    /**
     * Lấy dữ liệu thống kê cho Thủ Kho
     */
    public com.example.device_apisever.dto.ThuKhoDashboardResponse getThuKhoDashboard(Integer khoId) {
        if (khoId == null) {
            throw new RuntimeException("Tài khoản chưa được phân công kho.");
        }

        long tongSoThietBi = thietBiRepository.countByKhoHienTaiId(khoId);
        long soThietBiDangThue = thietBiRepository.countByKhoHienTaiIdAndTinhTrangId(khoId, 2);
        long soThietBiThanhLyHong = thietBiRepository.countByKhoHienTaiIdAndTinhTrangId(khoId, 4);
        long soThietBiDangBaoTri = thietBiRepository.countByKhoHienTaiIdAndTinhTrangId(khoId, 3);
        long tonKhoHienTai = thietBiRepository.countByKhoHienTaiIdAndTinhTrangId(khoId, 1);
        
        long soHopDongChoDuyet = hopDongThueRepository.countByTrangThaiId(1);
        long soHopDongSapGiao = hopDongThueRepository.countByTrangThaiId(3);

        List<NhacNhoItem> nhacNhoHomNay = new ArrayList<>();
        // 1. Nhắc nhở hợp đồng cần giao hôm nay
        LocalDateTime now = LocalDateTime.now();
        List<HopDongThue> hopDongSapGiao = hopDongThueRepository.findHopDongSapDenHan(now.minusDays(7), now.plusDays(7));
        for (HopDongThue hd : hopDongSapGiao) {
            if (hd.getTrangThaiId() == 3) {
                nhacNhoHomNay.add(NhacNhoItem.builder()
                        .loai("HOP_DONG")
                        .tieuDe("Hợp đồng #" + hd.getHopDongId() + " chờ giao thiết bị")
                        .moTa("Địa điểm: " + hd.getDiaDiemGiao())
                        .referenceId(hd.getHopDongId())
                        .build());
            }
        }

        return com.example.device_apisever.dto.ThuKhoDashboardResponse.builder()
                .tongSoThietBi(tongSoThietBi)
                .soThietBiDangThue(soThietBiDangThue)
                .soThietBiThanhLyHong(soThietBiThanhLyHong)
                .soThietBiDangBaoTri(soThietBiDangBaoTri)
                .soHopDongChoDuyet(soHopDongChoDuyet)
                .soHopDongSapGiao(soHopDongSapGiao)
                .tonKhoHienTai(tonKhoHienTai)
                .nhacNhoHomNay(nhacNhoHomNay)
                .build();
    }

    /**
     * Lấy dữ liệu thống kê cho Kỹ thuật viên
     */
    public com.example.device_apisever.dto.KtvDashboardResponse getKtvDashboard(Integer nguoiDungId) {
        long soPhieuDaHoanThanh = lichSuBaoTriRepository.countByNguoiDungBaoTriIdAndTrangThaiId(nguoiDungId, 2);
        long soPhieuChoNhan = lichSuBaoTriRepository.countByTrangThaiId(4); // Phiếu chờ nhận (chung)
        long soPhieuDangThucHien = lichSuBaoTriRepository.countByNguoiDungBaoTriIdAndTrangThaiIdIn(nguoiDungId, java.util.Arrays.asList(1, 5));
        
        long soYeuCauGiaoNhan = hopDongThueRepository.countByTrangThaiIdIn(java.util.Arrays.asList(3, 8));

        List<NhacNhoItem> lichBaoTriHomNay = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        List<LichSuBaoTri> baoTris = lichSuBaoTriRepository.findLichBaoTriHomNayChoKtv(nguoiDungId, now);
        
        for (LichSuBaoTri bt : baoTris) {
            lichBaoTriHomNay.add(NhacNhoItem.builder()
                    .loai("BAO_TRI")
                    .tieuDe("Phiếu bảo trì #" + bt.getBaoTriId() + " cần xử lý")
                    .moTa(bt.getNoiDungBaoTri() != null ? bt.getNoiDungBaoTri() : "Không có ghi chú")
                    .referenceId(bt.getBaoTriId())
                    .build());
        }

        return com.example.device_apisever.dto.KtvDashboardResponse.builder()
                .soPhieuDaHoanThanh(soPhieuDaHoanThanh)
                .soPhieuChoNhan(soPhieuChoNhan)
                .soPhieuDangThucHien(soPhieuDangThucHien)
                .soYeuCauGiaoNhan(soYeuCauGiaoNhan)
                .lichBaoTriHomNay(lichBaoTriHomNay)
                .build();
    }
}
