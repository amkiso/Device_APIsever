package com.example.device_apisever.config;

import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.repository.NguoiDungRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes default admin account if no users exist in the database.
 * Default credentials: admin / admin123
 * Admin has KhoID = NULL (per CK_NguoiDung_Kho constraint).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final NguoiDungRepository nguoiDungRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(NguoiDungRepository nguoiDungRepository, PasswordEncoder passwordEncoder) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Only create default admin if no users exist
        if (nguoiDungRepository.count() == 0) {
            NguoiDung admin = NguoiDung.builder()
                    .maNguoiDung("NV01")
                    .hoTen("Admin He Thong")
                    .taiKhoan("admin")
                    .matKhau(passwordEncoder.encode("admin123"))
                    .vaiTroId(1)       // Admin
                    .khoId(null)       // Admin không thuộc kho nào (CK_NguoiDung_Kho)
                    .trangThaiId(1)    // Đang hoạt động
                    .doiMatKhauLanDau(false)
                    .build();

            nguoiDungRepository.save(admin);
            System.out.println(">>> Default admin account created: admin / admin123");
        }
    }
}
