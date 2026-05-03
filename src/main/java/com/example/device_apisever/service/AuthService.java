package com.example.device_apisever.service;

import com.example.device_apisever.config.JwtService;
import com.example.device_apisever.dto.DangKyRequest;
import com.example.device_apisever.dto.DoiMatKhauRequest;
import com.example.device_apisever.dto.LoginRequest;
import com.example.device_apisever.dto.LoginResponse;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.entity.VaiTro;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.DuplicateResourceException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.NguoiDungRepository;
import com.example.device_apisever.repository.VaiTroRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final OtpService otpService;

    public AuthService(NguoiDungRepository nguoiDungRepository,
            VaiTroRepository vaiTroRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService,
            OtpService otpService) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.vaiTroRepository = vaiTroRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.otpService = otpService;
    }

    /**
     * UC02 — Đăng nhập cho cả Nhân viên lẫn Khách hàng.
     * Tìm trong bảng NguoiDung thống nhất theo TaiKhoan.
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {

        NguoiDung nd = nguoiDungRepository.findByTaiKhoan(request.getTaiKhoan())
                .orElseThrow(() -> new BusinessException("Sai tai khoan hoac mat khau"));

        if (!passwordEncoder.matches(request.getMatKhau(), nd.getMatKhau())) {
            throw new BusinessException("Sai tai khoan hoac mat khau");
        }

        if (nd.getTrangThaiId() != 1) {
            throw new BusinessException("Tai khoan da bi khoa hoac vo hieu hoa. Vui long lien he quan tri vien.");
        }

        // Cập nhật thời gian đăng nhập cuối
        nd.setLanDangNhapCuoi(LocalDateTime.now());
        nguoiDungRepository.save(nd);

        String token = jwtService.generateToken(
                nd.getNguoiDungId(), nd.getTaiKhoan(),
                nd.getVaiTroId(), nd.getKhoId(), nd.getHoTen());

        // Lấy tên vai trò
        String tenVaiTro = vaiTroRepository.findById(nd.getVaiTroId())
                .map(VaiTro::getTenVaiTro)
                .orElse("Unknown");

        return LoginResponse.builder()
                .token(token)
                .nguoiDungId(nd.getNguoiDungId())
                .hoTen(nd.getHoTen())
                .maNguoiDung(nd.getMaNguoiDung())
                .vaiTroId(nd.getVaiTroId())
                .tenVaiTro(tenVaiTro)
                .khoId(nd.getKhoId())
                .doiMatKhauLanDau(nd.getDoiMatKhauLanDau())
                .build();
    }

    /**
     * BƯỚC 1: Khởi tạo đăng ký - Kiểm tra email & Gửi OTP (Chưa lưu DB)
     */
    public void registerInit(DangKyRequest request) {
        // Kiểm tra email chưa tồn tại trong DB
        if (nguoiDungRepository.existsByTaiKhoan(request.getEmail())) {
            throw new DuplicateResourceException("Email da duoc su dung. Vui long dang nhap hoac dung email khac.");
        }

        // Validate: Doanh nghiệp phải có mã số thuế
        if (request.getLoaiKhachHangId() == 2 &&
                (request.getMaSoThue() == null || request.getMaSoThue().isBlank())) {
            throw new BusinessException("Doanh nghiep phai co ma so thue");
        }

        // Sinh OTP, lưu DangKyRequest vào cache 5 phút
        String otp = otpService.generateAndCacheOtp(request.getEmail(), request);

        // Gửi OTP qua email
        emailService.sendOtpEmailAsync(request.getEmail(), otp);
    }

    /**
     * BƯỚC 2: Xác nhận OTP - Lưu vào DB & Gửi Welcome Email
     */
    @Transactional
    public LoginResponse registerConfirm(String email, String otp) {
        // 1. Xác thực OTP
        if (!otpService.validateOtp(email, otp)) {
            throw new BusinessException("Ma OTP khong hop le hoac da het han.");
        }

        // 2. Lấy dữ liệu tạm từ Cache
        DangKyRequest request = (DangKyRequest) otpService.getPayload(email);
        if (request == null) {
            throw new BusinessException("Khong tim thay thong tin dang ky. Vui long thu lai.");
        }

        // 3. Xóa cache vì đã sử dụng
        otpService.clearCache(email);

        // 4. Các bước lưu User như cũ
        String maNguoiDung = generateMaKhachHang();

        NguoiDung nd = NguoiDung.builder()
                .maNguoiDung(maNguoiDung)
                .hoTen(request.getHoTen())
                .taiKhoan(request.getEmail())
                .matKhau(passwordEncoder.encode(request.getMatKhau()))
                .vaiTroId(4) // Khách hàng
                .khoId(null) // KH không thuộc kho nào
                .soDienThoai(request.getSoDienThoai())
                .email(request.getEmail())
                .diaChi(request.getDiaChi())
                .loaiKhachHangId(request.getLoaiKhachHangId())
                .maSoThue(request.getMaSoThue())
                .trangThaiId(1) // Đang hoạt động
                .doiMatKhauLanDau(false)
                .build();

        nguoiDungRepository.save(nd);

        // Auto-login: sinh token cho KH vừa đăng ký
        String token = jwtService.generateToken(
                nd.getNguoiDungId(), nd.getTaiKhoan(),
                nd.getVaiTroId(), nd.getKhoId(), nd.getHoTen());

        // Gửi email chào mừng
        sendWelcomeEmail(nd.getEmail(), nd.getHoTen(), nd.getMaNguoiDung());

        return LoginResponse.builder()
                .token(token)
                .nguoiDungId(nd.getNguoiDungId())
                .hoTen(nd.getHoTen())
                .maNguoiDung(nd.getMaNguoiDung())
                .vaiTroId(nd.getVaiTroId())
                .tenVaiTro("Khách hàng")
                .khoId(null)
                .doiMatKhauLanDau(false)
                .build();
    }

    /**
     * BƯỚC 1: Khởi tạo Quên mật khẩu - Kiểm tra email & Gửi OTP
     */
    public void forgotPasswordInit(String email) {
        // Kiểm tra email CÓ tồn tại trong DB không
        if (!nguoiDungRepository.existsByTaiKhoan(email)) {
            throw new ResourceNotFoundException("Email khong ton tai trong he thong.");
        }

        // Sinh OTP (không cần payload vì chỉ update mật khẩu)
        String otp = otpService.generateAndCacheOtp(email, null);

        // Gửi OTP qua email
        emailService.sendOtpEmailAsync(email, otp);
    }

    /**
     * BƯỚC 2: Xác nhận OTP Quên mật khẩu & Đổi mật khẩu mới
     */
    @Transactional
    public void forgotPasswordConfirm(String email, String otp, String newPassword) {
        // 1. Xác thực OTP
        if (!otpService.validateOtp(email, otp)) {
            throw new BusinessException("Ma OTP khong hop le hoac da het han.");
        }

        // 2. Lấy User từ DB
        NguoiDung nd = nguoiDungRepository.findByTaiKhoan(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email khong ton tai."));

        // 3. Đổi mật khẩu
        nd.setMatKhau(passwordEncoder.encode(newPassword));
        nd.setDoiMatKhauLanDau(false);
        nguoiDungRepository.save(nd);

        // 4. Xóa cache
        otpService.clearCache(email);
    }

    /**
     * UC04 — Đổi mật khẩu.
     */
    @Transactional
    public void doiMatKhau(String taiKhoan, DoiMatKhauRequest request) {
        NguoiDung nd = nguoiDungRepository.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay tai khoan"));

        if (!passwordEncoder.matches(request.getMatKhauCu(), nd.getMatKhau())) {
            throw new BusinessException("Mat khau cu khong dung");
        }

        nd.setMatKhau(passwordEncoder.encode(request.getMatKhauMoi()));
        nd.setDoiMatKhauLanDau(false); // Tắt cờ bắt đổi mật khẩu
        nguoiDungRepository.save(nd);
    }

    /**
     * Lấy thông tin profile người dùng đang đăng nhập.
     */
    public NguoiDung getProfile(String taiKhoan) {
        return nguoiDungRepository.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay tai khoan"));
    }

    /**
     * Tự sinh mã khách hàng theo format KH00001, KH00002, ...
     */
    private String generateMaKhachHang() {
        return nguoiDungRepository.findMaxMaKhachHang()
                .map(maxMa -> {
                    String numberPart = maxMa.substring(2); // bỏ "KH"
                    int nextNumber = Integer.parseInt(numberPart) + 1;
                    return String.format("KH%05d", nextNumber);
                })
                .orElse("KH00001");
    }

    /**
     * Gửi email chào mừng sau khi đăng ký thành công
     */
    private void sendWelcomeEmail(String toEmail, String hoTen, String maKh) {
        String subject = "Chào mừng bạn đến với Hệ thống Quản lý Thuê Thiết Bị";
        String body = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>"
                + "<h2 style='color: #0056b3;'>Xin chào " + hoTen + ",</h2>"
                + "<p>Cảm ơn bạn đã đăng ký tài khoản tại hệ thống Quản lý Thuê Thiết Bị của chúng tôi.</p>"
                + "<p>Thông tin tài khoản của bạn:</p>"
                + "<ul>"
                + "<li><b>Mã khách hàng:</b> " + maKh + "</li>"
                + "<li><b>Tài khoản đăng nhập:</b> " + toEmail + "</li>"
                + "</ul>"
                + "<p>Bây giờ bạn có thể đăng nhập vào hệ thống và bắt đầu sử dụng các dịch vụ của chúng tôi.</p>"
                + "<br><p>Trân trọng,<br><b>Đội ngũ hỗ trợ</b></p>"
                + "</div>";
        emailService.sendEmailAsync(toEmail, subject, body);
    }
}
