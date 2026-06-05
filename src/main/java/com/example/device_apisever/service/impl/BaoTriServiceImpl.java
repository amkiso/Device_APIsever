package com.example.device_apisever.service.impl;

import com.example.device_apisever.dto.baotri.*;
import com.example.device_apisever.entity.HinhAnhThietBi;
import com.example.device_apisever.entity.LichSuBaoTri;
import com.example.device_apisever.entity.ThietBi;
import com.example.device_apisever.repository.HinhAnhThietBiRepository;
import com.example.device_apisever.repository.LichSuBaoTriRepository;
import com.example.device_apisever.repository.ThietBiRepository;
import com.example.device_apisever.service.IBaoTriService;
import com.example.device_apisever.service.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaoTriServiceImpl implements IBaoTriService {

    @Autowired
    private LichSuBaoTriRepository lichSuBaoTriRepository;

    @Autowired
    private HinhAnhThietBiRepository hinhAnhThietBiRepository;

    @Autowired
    private ThietBiRepository thietBiRepository;

    @Autowired
    private S3StorageService s3StorageService;

    @Override
    public Page<BaoTriDto> getDanhSachBaoTri(Integer trangThaiId, Integer nguoiDungId, String keyword, Pageable pageable) {
        return lichSuBaoTriRepository.searchBaoTri(trangThaiId, nguoiDungId, keyword, pageable);
    }

    @Override
    public BaoTriDetailDto getChiTietBaoTri(Integer baoTriId) {
        BaoTriDto dto = lichSuBaoTriRepository.findDtoById(baoTriId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bảo trì"));

        List<HinhAnhThietBi> hinhAnhList = hinhAnhThietBiRepository.findByBaoTriId(baoTriId);

        List<HinhAnhThietBiDto> anhGhiNhan = hinhAnhList.stream()
                .filter(h -> h.getLoaiAnhId() == 4)
                .map(h -> HinhAnhThietBiDto.builder()
                        .hinhAnhId(h.getHinhAnhId())
                        .urlAnh(s3StorageService.getPublicUrlByCategory("work", h.getUrlAnh()))
                        .loaiAnhId(h.getLoaiAnhId())
                        .ngayChup(h.getNgayChup())
                        .build())
                .collect(Collectors.toList());

        List<HinhAnhThietBiDto> anhBanGiao = hinhAnhList.stream()
                .filter(h -> h.getLoaiAnhId() == 3)
                .map(h -> HinhAnhThietBiDto.builder()
                        .hinhAnhId(h.getHinhAnhId())
                        .urlAnh(s3StorageService.getPublicUrlByCategory("work", h.getUrlAnh()))
                        .loaiAnhId(h.getLoaiAnhId())
                        .ngayChup(h.getNgayChup())
                        .build())
                .collect(Collectors.toList());

        return BaoTriDetailDto.builder()
                .baoTriId(dto.getBaoTriId())
                .thietBiId(dto.getThietBiId())
                .maThietBi(dto.getMaThietBi())
                .tenLoaiThietBi(dto.getTenLoaiThietBi())
                .nguoiDungBaoTriId(dto.getNguoiDungBaoTriId())
                .tenNguoiDungBaoTri(dto.getTenNguoiDungBaoTri())
                .hopDongId(dto.getHopDongId())
                .ngayThucHien(dto.getNgayThucHien())
                .loaiBaoTriId(dto.getLoaiBaoTriId())
                .tenLoaiBaoTri(dto.getTenLoaiBaoTri())
                .noiDungBaoTri(dto.getNoiDungBaoTri())
                .chiPhi(dto.getChiPhi())
                .trangThaiId(dto.getTrangThaiId())
                .tenTrangThai(dto.getTenTrangThai())
                .tinhVaoBoiThuong(dto.getTinhVaoBoiThuong())
                .ghiChu(dto.getGhiChu())
                .ngayHoanThanh(dto.getNgayHoanThanh())
                .anhGhiNhanSuCo(anhGhiNhan)
                .anhBanGiao(anhBanGiao)
                .build();
    }

    @Override
    public BaoTriThongKeDto getThongKeBaoTri() {
        return BaoTriThongKeDto.builder()
                .tongChiPhi(lichSuBaoTriRepository.sumAllChiPhi().orElse(BigDecimal.ZERO))
                .chiPhiDinhKy(lichSuBaoTriRepository.sumChiPhiDinhKy().orElse(BigDecimal.ZERO))
                .chiPhiCaiTien(lichSuBaoTriRepository.sumChiPhiCaiTien().orElse(BigDecimal.ZERO))
                .chiPhiLoiKhongBoiThuong(lichSuBaoTriRepository.sumChiPhiLoiKhongBoiThuong().orElse(BigDecimal.ZERO))
                .chiPhiSuCoCoBoiThuong(lichSuBaoTriRepository.sumChiPhiSuCoCoBoiThuong().orElse(BigDecimal.ZERO))
                .build();
    }

    @Override
    @Transactional
    public BaoTriDto createBaoTri(BaoTriCreateReq req, Integer createdBy) {
        // Kiểm tra xem thiết bị đã có yêu cầu bảo trì đang mở hay chưa (trạng thái 1, 4, 5)
        List<Integer> activeStatuses = java.util.Arrays.asList(1, 4, 5);
        if (lichSuBaoTriRepository.existsByThietBiIdAndTrangThaiIdIn(req.getThietBiId(), activeStatuses)) {
            throw new RuntimeException("Thiết bị này đang có yêu cầu bảo trì chưa hoàn tất!");
        }

        LichSuBaoTri bt = new LichSuBaoTri();
        bt.setThietBiId(req.getThietBiId());
        bt.setLoaiBaoTriId(req.getLoaiBaoTriId());
        bt.setNoiDungBaoTri(req.getNoiDungBaoTri());
        bt.setNgayThucHien(req.getNgayThucHien() != null ? req.getNgayThucHien() : LocalDateTime.now());
        bt.setTinhVaoBoiThuong(req.getTinhVaoBoiThuong() != null ? req.getTinhVaoBoiThuong() : false);
        bt.setChiPhi(BigDecimal.ZERO);
        
        // Nếu là sự cố do admin/khách tạo thì chờ KTV xác nhận (4)
        // NguoiDungBaoTriId sẽ được set khi KTV nhận việc (nên gán null ban đầu)
        // Nếu là KTV tự tạo định kỳ/cải tiến thì mặc định đang thực hiện (1) và nhận việc luôn
        if (req.getLoaiBaoTriId() == 2) {
            bt.setTrangThaiId(4); // Chờ kỹ thuật xác nhận
            bt.setNguoiDungBaoTriId(null);
        } else {
            bt.setTrangThaiId(1); // Đang thực hiện
            bt.setNguoiDungBaoTriId(createdBy);
        }

        bt = lichSuBaoTriRepository.save(bt);
        return lichSuBaoTriRepository.findDtoById(bt.getBaoTriId()).orElse(null);
    }

    @Override
    @Transactional
    public BaoTriDto xacNhanYeuCau(Integer baoTriId, Integer technicianId) {
        LichSuBaoTri bt = lichSuBaoTriRepository.findById(baoTriId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bảo trì"));
        
        if (bt.getTrangThaiId() != 4) {
            throw new RuntimeException("Phiếu bảo trì không ở trạng thái chờ xác nhận");
        }
        
        bt.setTrangThaiId(1); // Đang thực hiện
        bt.setNguoiDungBaoTriId(technicianId);
        lichSuBaoTriRepository.save(bt);
        
        return lichSuBaoTriRepository.findDtoById(baoTriId).orElse(null);
    }

    @Override
    @Transactional
    public BaoTriDto ghiNhanSuCo(Integer baoTriId, GhiNhanSuCoReq req, Integer technicianId) {
        LichSuBaoTri bt = lichSuBaoTriRepository.findById(baoTriId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bảo trì"));

        if (bt.getTrangThaiId() != 1) {
            throw new RuntimeException("Phiếu bảo trì không ở trạng thái đang thực hiện");
        }

        bt.setTrangThaiId(5); // Đang bảo trì
        lichSuBaoTriRepository.save(bt);

        ThietBi tb = thietBiRepository.findById(bt.getThietBiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị"));
        tb.setTinhTrangId(3); // 3 = Đang bảo trì
        thietBiRepository.save(tb);

        if (req.getImageUrls() != null && !req.getImageUrls().isEmpty()) {
            for (String url : req.getImageUrls()) {
                HinhAnhThietBi ha = new HinhAnhThietBi();
                ha.setBaoTriId(baoTriId);
                ha.setThietBiId(tb.getThietBiId());
                ha.setNguoiDungChupId(technicianId);
                ha.setUrlAnh(url);
                ha.setLoaiAnhId(4); // 4 = Ghi nhận sự cố
                ha.setNgayChup(LocalDateTime.now());
                hinhAnhThietBiRepository.save(ha);
            }
        }

        return lichSuBaoTriRepository.findDtoById(baoTriId).orElse(null);
    }

    @Override
    @Transactional
    public BaoTriDto hoanThanhBaoTri(Integer baoTriId, HoanThanhBaoTriReq req, Integer technicianId) {
        LichSuBaoTri bt = lichSuBaoTriRepository.findById(baoTriId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bảo trì"));

        if (bt.getTrangThaiId() != 5 && bt.getTrangThaiId() != 1) { // 1 or 5
            throw new RuntimeException("Trạng thái không hợp lệ để hoàn thành");
        }

        bt.setTrangThaiId(2); // Hoàn thành
        bt.setNgayHoanThanh(LocalDateTime.now());
        if (req.getChiPhi() != null) bt.setChiPhi(req.getChiPhi());
        if (req.getNoiDungBaoTri() != null && !req.getNoiDungBaoTri().isEmpty()) {
            bt.setNoiDungBaoTri(req.getNoiDungBaoTri());
        }
        if (req.getTinhVaoBoiThuong() != null) {
            bt.setTinhVaoBoiThuong(req.getTinhVaoBoiThuong());
        }
        bt.setGhiChu(req.getGhiChu());
        lichSuBaoTriRepository.save(bt);

        ThietBi tb = thietBiRepository.findById(bt.getThietBiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị"));
        if (lichSuBaoTriRepository.isThietBiDangChoThue(bt.getThietBiId())) {
            tb.setTinhTrangId(2); // 2 = Đang cho thuê
        } else {
            tb.setTinhTrangId(1); // 1 = Sẵn sàng cho thuê
        }
        thietBiRepository.save(tb);

        if (req.getImageUrls() != null && !req.getImageUrls().isEmpty()) {
            for (String url : req.getImageUrls()) {
                HinhAnhThietBi ha = new HinhAnhThietBi();
                ha.setBaoTriId(baoTriId);
                ha.setThietBiId(tb.getThietBiId());
                ha.setNguoiDungChupId(technicianId);
                ha.setUrlAnh(url);
                ha.setLoaiAnhId(3); // 3 = Biên bản bảo trì / sau bảo trì
                ha.setNgayChup(LocalDateTime.now());
                hinhAnhThietBiRepository.save(ha);
            }
        }

        return lichSuBaoTriRepository.findDtoById(baoTriId).orElse(null);
    }
}
