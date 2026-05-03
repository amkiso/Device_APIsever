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
}
