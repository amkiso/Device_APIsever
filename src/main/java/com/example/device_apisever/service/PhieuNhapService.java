package com.example.device_apisever.service;

import com.example.device_apisever.dto.NhapKhoFormRequest;
import com.example.device_apisever.dto.NhapKhoRequest;
import com.example.device_apisever.entity.ChiTietPhieuNhap;
import com.example.device_apisever.entity.PhieuNhap;
import com.example.device_apisever.entity.ThietBi;
import com.example.device_apisever.repository.ChiTietPhieuNhapRepository;
import com.example.device_apisever.repository.PhieuNhapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final ThietBiService thietBiService;
    private final QrCodeService qrCodeService;

    public PhieuNhapService(PhieuNhapRepository phieuNhapRepository,
                            ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
                            ThietBiService thietBiService,
                            QrCodeService qrCodeService) {
        this.phieuNhapRepository = phieuNhapRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.thietBiService = thietBiService;
        this.qrCodeService = qrCodeService;
    }

    @Transactional
    public void createPhieuNhap(NhapKhoFormRequest request, Integer nhanVienNhapId) {
        if (request.getThietBis() == null || request.getThietBis().isEmpty()) {
            throw new IllegalArgumentException("Danh sách thiết bị trống");
        }

        BigDecimal tongTien = BigDecimal.ZERO;
        Map<Integer, Integer> loaiThietBiCount = new HashMap<>();
        Map<Integer, BigDecimal> loaiThietBiPrice = new HashMap<>();

        for (NhapKhoRequest tbReq : request.getThietBis()) {
            BigDecimal price = tbReq.getDonGiaNhap() != null ? tbReq.getDonGiaNhap() : BigDecimal.ZERO;
            tongTien = tongTien.add(price);

            int count = loaiThietBiCount.getOrDefault(tbReq.getLoaiThietBiId(), 0);
            loaiThietBiCount.put(tbReq.getLoaiThietBiId(), count + 1);
            loaiThietBiPrice.put(tbReq.getLoaiThietBiId(), price); // Lấy giá cuối cùng làm giá nhập loại
        }

        // Tạo Phiếu Nhập
        PhieuNhap phieuNhap = new PhieuNhap();
        phieuNhap.setNhaCungCapId(request.getNhaCungCapId() != null ? request.getNhaCungCapId() : 1);
        phieuNhap.setNhanVienNhapId(nhanVienNhapId);
        phieuNhap.setNgayNhap(LocalDateTime.now());
        phieuNhap.setTongTienNhap(tongTien);
        phieuNhap.setGhiChu(request.getGhiChu());
        PhieuNhap savedPhieuNhap = phieuNhapRepository.save(phieuNhap);

        // Tạo Chi Tiết Phiếu Nhập
        for (Map.Entry<Integer, Integer> entry : loaiThietBiCount.entrySet()) {
            ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
            chiTiet.setPhieuNhapId(savedPhieuNhap.getPhieuNhapId());
            chiTiet.setLoaiThietBiId(entry.getKey());
            chiTiet.setSoLuong(entry.getValue());
            chiTiet.setDonGiaNhap(loaiThietBiPrice.get(entry.getKey()));
            chiTietPhieuNhapRepository.save(chiTiet);
        }

        // Tạo Thiết Bị mới
        for (NhapKhoRequest tbReq : request.getThietBis()) {
            ThietBi thietBi = new ThietBi();
            thietBi.setLoaiThietBiId(tbReq.getLoaiThietBiId());
            thietBi.setMaTaiSan(tbReq.getMaTaiSan());
            thietBi.setSoSerial(tbReq.getSoSerial());
            thietBi.setTinhTrangId(1); // Tình trạng sẵn sàng
            thietBi.setKhoHienTaiId(request.getKhoHienTaiId());
            thietBi.setGiaTriMay(tbReq.getDonGiaNhap());

            ThietBi savedTb = thietBiService.createWithAutoQr(thietBi);
            try {
                qrCodeService.getOrCreateQrCode(savedTb.getThietBiId());
            } catch (Exception e) {
                // Ignore QR error
            }
        }
    }
}
