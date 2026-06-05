package com.example.device_apisever.service;

import com.example.device_apisever.dto.GiaoDichDto;
import com.example.device_apisever.dto.ThongKeDongTienDto;
import com.example.device_apisever.entity.ThanhToan;
import com.example.device_apisever.repository.HopDongThueRepository;
import com.example.device_apisever.repository.LichSuBaoTriRepository;
import com.example.device_apisever.repository.ThanhToanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThongKeService {

    private final ThanhToanRepository thanhToanRepository;
    private final LichSuBaoTriRepository baoTriRepository;
    private final HopDongThueRepository hopDongRepository;

    public ThongKeDongTienDto getThongKeDongTien() {
        // 1. Tiền hợp đồng đã thu (Loai = 2)
        BigDecimal tienHopDongDaThu = thanhToanRepository.sumSoTienByLoaiThanhToanAndSuccess(2);
        
        // 2. Tiền đền bù/phí phát sinh (Loai = 3)
        BigDecimal tienBoiThuongDaThu = thanhToanRepository.sumSoTienByLoaiThanhToanAndSuccess(3);
        
        // 3. Tiền cọc (Loai = 1)
        BigDecimal tienCocDaThu = thanhToanRepository.sumSoTienByLoaiThanhToanAndSuccess(1);
        
        // Tổng doanh thu = Tiền HĐ + Tiền Đền bù
        BigDecimal tongDoanhThu = tienHopDongDaThu.add(tienBoiThuongDaThu);

        // 4. Chi phí bảo trì (cty chịu)
        BigDecimal chiPhiBaoTri = baoTriRepository.sumChiPhiBaoTri();

        // 5. Tiền chưa thu (Công nợ)
        // Các hợp đồng đang hiệu lực: TrangThai IN (1,2,3,4,6,8,9,10) - Loại trừ Hủy (7,11) và Kết thúc (5) có thể không đòi được hoặc đã xử lý cọc
        List<Integer> activeStatuses = Arrays.asList(2, 3, 4, 6, 8, 9, 10);
        BigDecimal tongGiaTriHopDongActive = hopDongRepository.sumTongGiaTriByTrangThaiIn(activeStatuses);
        
        // Số tiền ĐÃ THU từ các hợp đồng này thực tế có thể phức tạp hơn (cần query group theo hopDongId). 
        // Nhưng để đơn giản thống kê, lấy Tổng Giá Trị trừ đi Tiền Hợp Đồng Đã Thu (tổng).
        BigDecimal tienChuaThu = tongGiaTriHopDongActive.subtract(tienHopDongDaThu);
        if (tienChuaThu.compareTo(BigDecimal.ZERO) < 0) {
            tienChuaThu = BigDecimal.ZERO;
        }

        // 6. 10 giao dịch gần nhất
        List<ThanhToan> top10ThanhToan = thanhToanRepository.findTop10ByOrderByNgayTaoDesc();
        List<GiaoDichDto> giaoDichGanDay = new ArrayList<>();
        for (ThanhToan t : top10ThanhToan) {
            String loaiGd = switch (t.getLoaiThanhToan()) {
                case 1 -> "Cọc";
                case 2 -> "Thanh toán";
                case 3 -> "Bồi thường/Phí";
                default -> "Khác";
            };
            String trangThai = switch (t.getTrangThai()) {
                case 0 -> "Đang chờ";
                case 1 -> "Thành công";
                case 2 -> "Thất bại";
                case 3 -> "Hoàn tiền";
                default -> "Không rõ";
            };
            Boolean isThuVao = (t.getTrangThai() == 1 || t.getTrangThai() == 0); // Thường là tiền vào
            
            giaoDichGanDay.add(GiaoDichDto.builder()
                    .giaoDichId(t.getThanhToanId())
                    .loaiGiaoDich(loaiGd)
                    .maGiaoDich(t.getMaGiaoDich() != null ? t.getMaGiaoDich() : "N/A")
                    .soTien(t.getSoTien())
                    .thoiGian(t.getNgayTao())
                    .moTa(t.getGhiChu() != null ? t.getGhiChu() : ("Thanh toán cho HĐ #" + t.getHopDongId()))
                    .trangThai(trangThai)
                    .isThuVao(isThuVao)
                    .build());
        }

        return ThongKeDongTienDto.builder()
                .tongDoanhThu(tongDoanhThu)
                .tienHopDongDaThu(tienHopDongDaThu)
                .tienCocDaThu(tienCocDaThu)
                .tienBoiThuongDaThu(tienBoiThuongDaThu)
                .chiPhiBaoTri(chiPhiBaoTri)
                .tienChuaThu(tienChuaThu)
                .giaoDichGanDay(giaoDichGanDay)
                .build();
    }
}
