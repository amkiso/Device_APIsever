package com.example.device_apisever.service;

import com.example.device_apisever.dto.admin.*;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.entity.Kho;
import com.example.device_apisever.entity.VaiTro;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class AdminNguoiDungService {

    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;
    private final KhoRepository khoRepository;
    private final HopDongThueRepository hopDongThueRepository;
    private final LichSuBanGiaoRepository lichSuBanGiaoRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminNguoiDungService(NguoiDungRepository nguoiDungRepository,
                                 VaiTroRepository vaiTroRepository,
                                 KhoRepository khoRepository,
                                 HopDongThueRepository hopDongThueRepository,
                                 LichSuBanGiaoRepository lichSuBanGiaoRepository,
                                 PasswordEncoder passwordEncoder) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.vaiTroRepository = vaiTroRepository;
        this.khoRepository = khoRepository;
        this.hopDongThueRepository = hopDongThueRepository;
        this.lichSuBanGiaoRepository = lichSuBanGiaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String getTenVaiTro(Integer id) {
        if (id == null) return null;
        return vaiTroRepository.findById(id).map(VaiTro::getTenVaiTro).orElse("Không xác định");
    }

    private String getTenTrangThai(Integer id) {
        if (id == null) return "Không xác định";
        switch (id) {
            case 1: return "Hoạt động";
            case 2: return "Đã khóa";
            case 3: return "Vô hiệu hóa";
            default: return "Không xác định";
        }
    }

    private String getTenLoaiKhachHang(Integer id) {
        if (id == null) return null;
        return id == 1 ? "Cá nhân" : "Doanh nghiệp";
    }

    private String getTenKho(Integer id) {
        if (id == null) return null;
        return khoRepository.findById(id).map(Kho::getTenKho).orElse("Không xác định");
    }

    public Page<AdminNguoiDungListResponse> getAll(int page, int size, Integer vaiTroId, Integer trangThaiId, Integer loaiKhachHangId, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NguoiDung> usersPage = nguoiDungRepository.findAllWithFilters(vaiTroId, trangThaiId, loaiKhachHangId, keyword, pageable);

        return usersPage.map(u -> AdminNguoiDungListResponse.builder()
                .nguoiDungId(u.getNguoiDungId())
                .maNguoiDung(u.getMaNguoiDung())
                .hoTen(u.getHoTen())
                .email(u.getEmail())
                .soDienThoai(u.getSoDienThoai())
                .vaiTroId(u.getVaiTroId())
                .tenVaiTro(getTenVaiTro(u.getVaiTroId()))
                .trangThaiId(u.getTrangThaiId())
                .tenTrangThai(getTenTrangThai(u.getTrangThaiId()))
                .loaiKhachHangId(u.getLoaiKhachHangId())
                .tenLoaiKhachHang(getTenLoaiKhachHang(u.getLoaiKhachHangId()))
                .avt(u.getAvt())
                .ngayTao(u.getNgayTao())
                .build());
    }

    public AdminNguoiDungDetailResponse getDetail(Integer nguoiDungId) {
        NguoiDung u = nguoiDungRepository.findById(nguoiDungId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        return AdminNguoiDungDetailResponse.builder()
                .nguoiDungId(u.getNguoiDungId())
                .maNguoiDung(u.getMaNguoiDung())
                .hoTen(u.getHoTen())
                .email(u.getEmail())
                .soDienThoai(u.getSoDienThoai())
                .vaiTroId(u.getVaiTroId())
                .tenVaiTro(getTenVaiTro(u.getVaiTroId()))
                .trangThaiId(u.getTrangThaiId())
                .tenTrangThai(getTenTrangThai(u.getTrangThaiId()))
                .loaiKhachHangId(u.getLoaiKhachHangId())
                .tenLoaiKhachHang(getTenLoaiKhachHang(u.getLoaiKhachHangId()))
                .avt(u.getAvt())
                .ngayTao(u.getNgayTao())
                .taiKhoan(u.getTaiKhoan())
                .diaChi(u.getDiaChi())
                .donViCongTac(u.getDonViCongTac())
                .khoId(u.getKhoId())
                .tenKho(getTenKho(u.getKhoId()))
                .lanDangNhapCuoi(u.getLanDangNhapCuoi())
                .doiMatKhauLanDau(u.getDoiMatKhauLanDau())
                .hasPin(u.getMaPin() != null && !u.getMaPin().isEmpty())
                .build();
    }

    public ThongTinNhanCamResponse getThongTinNhanCam(Integer targetUserId, Integer adminId, String maPin) {
        NguoiDung admin = nguoiDungRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy admin"));

        if (admin.getMaPin() == null || !passwordEncoder.matches(maPin, admin.getMaPin())) {
            throw new IllegalArgumentException("Mã PIN không chính xác");
        }

        NguoiDung target = nguoiDungRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng mục tiêu"));

        // Format is yyyy-MM-dd, our entity stores LocalDateTime, we just format it as string or pass it directly.
        // The implementation_plan shows string for cccdNgayCap in ThongTinNhanCamResponse
        String cccdNgayCapStr = null;
        if (target.getCccdNgayCap() != null) {
            cccdNgayCapStr = target.getCccdNgayCap().toLocalDate().toString();
        }

        return new ThongTinNhanCamResponse(
                target.getCccd(),
                cccdNgayCapStr,
                target.getCccdNoiCap(),
                target.getMaSoThue()
        );
    }

    @Transactional
    public NguoiDung create(AdminNguoiDungRequest request) {
        if (request.getTaiKhoan() == null || request.getTaiKhoan().isBlank()) {
            throw new BusinessException("Tài khoản không được để trống");
        }
        if (nguoiDungRepository.existsByTaiKhoan(request.getTaiKhoan())) {
            throw new BusinessException("Tài khoản đã tồn tại");
        }
        if (request.getMatKhau() == null || request.getMatKhau().isBlank()) {
            throw new BusinessException("Mật khẩu không được để trống");
        }

        // Validate khoId for VaiTro 2, 3
        if ((request.getVaiTroId() == 2 || request.getVaiTroId() == 3) && request.getKhoId() == null) {
            throw new BusinessException("Nhân viên quản lý kho hoặc kỹ thuật viên phải thuộc một kho cụ thể");
        }

        // Generate maNguoiDung
        String nextMa;
        if (request.getVaiTroId() == 4) {
            Optional<String> maxMa = nguoiDungRepository.findMaxMaKhachHang();
            if (maxMa.isPresent()) {
                int number = Integer.parseInt(maxMa.get().substring(2)) + 1;
                nextMa = String.format("KH%05d", number);
            } else {
                nextMa = "KH00001";
            }
        } else {
            Optional<String> maxMa = nguoiDungRepository.findMaxMaNhanVien();
            if (maxMa.isPresent()) {
                int number = Integer.parseInt(maxMa.get().substring(2)) + 1;
                nextMa = String.format("NV%05d", number);
            } else {
                nextMa = "NV00001";
            }
        }
        
        LocalDateTime cccdNgayCapDT = null;
        if (request.getCccdNgayCap() != null && !request.getCccdNgayCap().isBlank()) {
            try {
                // simple yyyy-mm-dd to LocalDateTime
                cccdNgayCapDT = LocalDateTime.parse(request.getCccdNgayCap() + "T00:00:00");
            } catch (Exception e) {}
        }

        NguoiDung newUser = NguoiDung.builder()
                .maNguoiDung(nextMa)
                .hoTen(request.getHoTen())
                .taiKhoan(request.getTaiKhoan())
                .matKhau(passwordEncoder.encode(request.getMatKhau()))
                .vaiTroId(request.getVaiTroId())
                .khoId(request.getKhoId())
                .soDienThoai(request.getSoDienThoai())
                .email(request.getEmail())
                .diaChi(request.getDiaChi())
                .loaiKhachHangId(request.getLoaiKhachHangId())
                .maSoThue(request.getMaSoThue())
                .cccd(request.getCccd())
                .cccdNgayCap(cccdNgayCapDT)
                .cccdNoiCap(request.getCccdNoiCap())
                .donViCongTac(request.getDonViCongTac())
                .trangThaiId(1) // Default to Active
                .doiMatKhauLanDau(true)
                .build();

        return nguoiDungRepository.save(newUser);
    }

    @Transactional
    public NguoiDung update(Integer nguoiDungId, AdminNguoiDungRequest request) {
        NguoiDung u = nguoiDungRepository.findById(nguoiDungId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Do not update Email for KhachHang (VaiTroID=4)
        if (u.getVaiTroId() != 4) {
            u.setEmail(request.getEmail());
        }

        u.setHoTen(request.getHoTen());
        u.setSoDienThoai(request.getSoDienThoai());
        u.setDiaChi(request.getDiaChi());
        u.setVaiTroId(request.getVaiTroId());
        
        // Validate khoId for VaiTro 2, 3
        if ((request.getVaiTroId() == 2 || request.getVaiTroId() == 3) && request.getKhoId() == null) {
            throw new BusinessException("Nhân viên quản lý kho hoặc kỹ thuật viên phải thuộc một kho cụ thể");
        }
        u.setKhoId(request.getKhoId());

        u.setLoaiKhachHangId(request.getLoaiKhachHangId());
        u.setMaSoThue(request.getMaSoThue());
        u.setCccd(request.getCccd());
        
        if (request.getCccdNgayCap() != null && !request.getCccdNgayCap().isBlank()) {
            try {
                u.setCccdNgayCap(LocalDateTime.parse(request.getCccdNgayCap() + "T00:00:00"));
            } catch (Exception e) {}
        } else {
            u.setCccdNgayCap(null);
        }
        
        u.setCccdNoiCap(request.getCccdNoiCap());
        u.setDonViCongTac(request.getDonViCongTac());

        return nguoiDungRepository.save(u);
    }

    @Transactional
    public void changeStatus(Integer targetUserId, Integer trangThaiId, Integer adminId) {
        if (targetUserId.equals(adminId)) {
            throw new BusinessException("Không thể tự thay đổi trạng thái của chính mình");
        }

        NguoiDung u = nguoiDungRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        u.setTrangThaiId(trangThaiId);
        nguoiDungRepository.save(u);
    }

    @Transactional
    public void delete(Integer nguoiDungId) {
        // Kiểm tra hợp đồng
        if (hopDongThueRepository.existsByNguoiDungKhachId(nguoiDungId) || hopDongThueRepository.existsByNguoiDungTaoId(nguoiDungId)) {
            throw new BusinessException("Người dùng đã có hợp đồng. Vui lòng vô hiệu hóa thay vì xóa.");
        }
        
        // Kiểm tra lịch sử bàn giao
        if (lichSuBanGiaoRepository.existsByNguoiDungThucHienId(nguoiDungId)) {
            throw new BusinessException("Người dùng đã thực hiện bàn giao. Vui lòng vô hiệu hóa thay vì xóa.");
        }

        // Hard delete
        nguoiDungRepository.deleteById(nguoiDungId);
    }
}
