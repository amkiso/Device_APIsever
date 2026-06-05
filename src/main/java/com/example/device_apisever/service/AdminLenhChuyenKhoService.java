package com.example.device_apisever.service;

import com.example.device_apisever.dto.admin.*;
import com.example.device_apisever.entity.*;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminLenhChuyenKhoService {

    private final LenhChuyenKhoRepository lenhChuyenKhoRepository;
    private final ChiTietChuyenKhoRepository chiTietChuyenKhoRepository;
    private final KhoRepository khoRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final ThietBiRepository thietBiRepository;
    private final ThongBaoService thongBaoService;

    public AdminLenhChuyenKhoService(LenhChuyenKhoRepository lenhChuyenKhoRepository,
                                     ChiTietChuyenKhoRepository chiTietChuyenKhoRepository,
                                     KhoRepository khoRepository,
                                     NguoiDungRepository nguoiDungRepository,
                                     ThietBiRepository thietBiRepository,
                                     ThongBaoService thongBaoService) {
        this.lenhChuyenKhoRepository = lenhChuyenKhoRepository;
        this.chiTietChuyenKhoRepository = chiTietChuyenKhoRepository;
        this.khoRepository = khoRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.thietBiRepository = thietBiRepository;
        this.thongBaoService = thongBaoService;
    }

    private String getTenKho(Integer id) {
        if (id == null) return "Không xác định";
        return khoRepository.findById(id).map(Kho::getTenKho).orElse("Không xác định");
    }

    private String getHoTen(Integer id) {
        if (id == null) return null;
        return nguoiDungRepository.findById(id).map(NguoiDung::getHoTen).orElse("Không xác định");
    }

    public Page<AdminLenhChuyenKhoListResponse> getAll(int page, int size, Integer trangThai, Integer khoId, Integer nguoiThucHienId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LenhChuyenKho> lenhPage = lenhChuyenKhoRepository.findAllWithFilters(trangThai, khoId, nguoiThucHienId, pageable);

        return lenhPage.map(l -> AdminLenhChuyenKhoListResponse.builder()
                .lenhChuyenKhoId(l.getLenhChuyenKhoId())
                .tuKhoId(l.getTuKhoId())
                .tenTuKho(getTenKho(l.getTuKhoId()))
                .denKhoId(l.getDenKhoId())
                .tenDenKho(getTenKho(l.getDenKhoId()))
                .nguoiTaoId(l.getNguoiTaoId())
                .tenNguoiTao(getHoTen(l.getNguoiTaoId()))
                .trangThai(l.getTrangThai())
                .ngayTao(l.getNgayTao())
                .build());
    }

    public AdminLenhChuyenKhoDetailResponse getDetail(Integer lenhId) {
        LenhChuyenKho l = lenhChuyenKhoRepository.findById(lenhId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lệnh chuyển kho"));

        List<ChiTietChuyenKho> chiTiets = chiTietChuyenKhoRepository.findByLenhChuyenKhoId(lenhId);
        List<ChiTietChuyenKhoDTO> chiTietDTOs = chiTiets.stream().map(ct -> {
            ThietBi tb = thietBiRepository.findById(ct.getThietBiId()).orElse(null);
            return ChiTietChuyenKhoDTO.builder()
                    .chiTietId(ct.getChiTietId())
                    .thietBiId(ct.getThietBiId())
                    .maThietBi(tb != null ? tb.getMaTaiSan() : "N/A")
                    // If ThietBi has tenThietBi, you could add it here.
                    .tenThietBi(tb != null ? tb.getMucDichSuDung() : "N/A") 
                    .ghiChu(ct.getGhiChu())
                    .build();
        }).collect(Collectors.toList());

        return AdminLenhChuyenKhoDetailResponse.builder()
                .lenhChuyenKhoId(l.getLenhChuyenKhoId())
                .tuKhoId(l.getTuKhoId())
                .tenTuKho(getTenKho(l.getTuKhoId()))
                .denKhoId(l.getDenKhoId())
                .tenDenKho(getTenKho(l.getDenKhoId()))
                .nguoiTaoId(l.getNguoiTaoId())
                .tenNguoiTao(getHoTen(l.getNguoiTaoId()))
                .nguoiThucHienId(l.getNguoiThucHienId())
                .tenNguoiThucHien(getHoTen(l.getNguoiThucHienId()))
                .nguoiXacNhanId(l.getNguoiXacNhanId())
                .tenNguoiXacNhan(getHoTen(l.getNguoiXacNhanId()))
                .trangThai(l.getTrangThai())
                .ghiChu(l.getGhiChu())
                .ngayTao(l.getNgayTao())
                .ngayHoanThat(l.getNgayHoanThat())
                .chiTiet(chiTietDTOs)
                .build();
    }

    @Transactional
    public LenhChuyenKho create(AdminLenhChuyenKhoRequest request, Integer nguoiTaoId) {
        NguoiDung nguoiTao = nguoiDungRepository.findById(nguoiTaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Người tạo không tồn tại"));

        Integer tuKhoId = request.getTuKhoId();
        
        Kho tuKho = khoRepository.findById(tuKhoId)
                .orElseThrow(() -> new BusinessException("Kho xuất không tồn tại"));
        if (tuKho.getQuanLyId() == null) {
            throw new BusinessException("Kho xuất chưa có người quản lý, không thể tạo lệnh");
        }

        Kho denKho = khoRepository.findById(request.getDenKhoId())
                .orElseThrow(() -> new BusinessException("Kho nhận không tồn tại"));
        if (denKho.getQuanLyId() == null) {
            throw new BusinessException("Kho nhận chưa có người quản lý, không thể tạo lệnh");
        }

        if (nguoiTao.getVaiTroId() == 2 && !tuKhoId.equals(nguoiTao.getKhoId())) {
            throw new BusinessException("Thủ kho chỉ được tạo lệnh từ kho của mình");
        }

        if (tuKhoId.equals(request.getDenKhoId())) {
            throw new BusinessException("Kho nhận phải khác kho xuất");
        }

        LenhChuyenKho lenh = LenhChuyenKho.builder()
                .tuKhoId(tuKhoId)
                .denKhoId(request.getDenKhoId())
                .nguoiTaoId(nguoiTaoId)
                .nguoiThucHienId(request.getNguoiThucHienId())
                .trangThai(1) // 1=Chờ
                .ghiChu(request.getGhiChu())
                .build();
        
        lenh = lenhChuyenKhoRepository.save(lenh);

        for (AdminLenhChuyenKhoRequest.ChiTietRequest ctReq : request.getChiTiet()) {
            ThietBi tb = thietBiRepository.findById(ctReq.getThietBiId())
                    .orElseThrow(() -> new BusinessException("Thiết bị không tồn tại: " + ctReq.getThietBiId()));
            
            if (!tb.getKhoHienTaiId().equals(tuKhoId)) {
                throw new BusinessException("Thiết bị " + tb.getMaTaiSan() + " không thuộc kho xuất");
            }
            // Optional: Check if ThietBi is available (TinhTrangID == 1)
            
            ChiTietChuyenKho ct = ChiTietChuyenKho.builder()
                    .lenhChuyenKhoId(lenh.getLenhChuyenKhoId())
                    .thietBiId(tb.getThietBiId())
                    .ghiChu(ctReq.getGhiChu())
                    .build();
            chiTietChuyenKhoRepository.save(ct);
        }

        // Bắn thông báo
        com.example.device_apisever.dto.ThongBaoRequest tbReq = new com.example.device_apisever.dto.ThongBaoRequest();
        tbReq.setTieuDe("Lệnh chuyển kho mới");
        tbReq.setNoiDung("Có lệnh chuyển kho mới từ " + tuKho.getTenKho() + " đến " + denKho.getTenKho() + " cần vận chuyển.");
        if (request.getNguoiThucHienId() != null) {
            tbReq.setLoaiThongBao(3); // Cá nhân
            tbReq.setNguoiDungNhanId(request.getNguoiThucHienId());
        } else {
            tbReq.setLoaiThongBao(2); // Vai trò
            tbReq.setVaiTroNhanId(3); // KTV
        }
        try {
            thongBaoService.taoThongBao(tbReq, nguoiTaoId);
        } catch (Exception e) {
            // Log error
            System.err.println("Lỗi gửi thông báo KTV: " + e.getMessage());
        }

        return lenh;
    }

    @Transactional
    public void duyetLenh(Integer lenhId, AdminDuyetLenhRequest request, Integer userId) {
        LenhChuyenKho lenh = lenhChuyenKhoRepository.findById(lenhId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lệnh chuyển kho"));

        NguoiDung user = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        Integer newState = request.getTrangThai();
        Integer oldState = lenh.getTrangThai();

        // 1 -> 2 (Chờ -> Đang VC)
        if (oldState == 1 && newState == 2) {
            // Thủ kho xuất duyệt
            if (user.getVaiTroId() != 1 && !user.getKhoId().equals(lenh.getTuKhoId())) {
                throw new BusinessException("Chỉ Admin hoặc Thủ kho xuất mới được duyệt chuyển đi");
            }
            lenh.setNguoiThucHienId(userId);
            lenh.setTrangThai(2);
        } 
        // 2 -> 3 (Đang VC -> Hoàn tất)
        else if (oldState == 2 && newState == 3) {
            // Thủ kho nhận duyệt
            if (user.getVaiTroId() != 1 && !user.getKhoId().equals(lenh.getDenKhoId())) {
                throw new BusinessException("Chỉ Admin hoặc Thủ kho nhận mới được xác nhận hoàn tất");
            }
            lenh.setNguoiXacNhanId(userId);
            lenh.setTrangThai(3);
            lenh.setNgayHoanThat(LocalDateTime.now());
            
            // Cập nhật KhoHienTaiID của các thiết bị
            List<ChiTietChuyenKho> chiTiets = chiTietChuyenKhoRepository.findByLenhChuyenKhoId(lenhId);
            for (ChiTietChuyenKho ct : chiTiets) {
                ThietBi tb = thietBiRepository.findById(ct.getThietBiId()).orElse(null);
                if (tb != null) {
                    tb.setKhoHienTaiId(lenh.getDenKhoId());
                    thietBiRepository.save(tb);
                }
            }
        } 
        // 1, 2 -> 4 (Hủy)
        else if ((oldState == 1 || oldState == 2) && newState == 4) {
            if (user.getVaiTroId() != 1 && !user.getNguoiDungId().equals(lenh.getNguoiTaoId())) {
                throw new BusinessException("Chỉ Admin hoặc người tạo mới được hủy lệnh");
            }
            lenh.setTrangThai(4);
        } else {
            throw new BusinessException("Chuyển trạng thái không hợp lệ");
        }

        if (request.getGhiChu() != null) {
            lenh.setGhiChu(lenh.getGhiChu() + " | " + request.getGhiChu());
        }

        lenhChuyenKhoRepository.save(lenh);
    }
}
