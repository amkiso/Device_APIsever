package com.example.device_apisever.service;

import com.example.device_apisever.dto.OtpData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service để quản lý bộ nhớ đệm (In-Memory Cache) cho OTP.
 * Tránh việc phải dùng Redis cho hệ thống vừa và nhỏ.
 */
@Service
public class OtpService {

    // Bộ nhớ đệm lưu trữ OTP. Key: Email, Value: OtpData
    private final Map<String, OtpData> otpCache = new ConcurrentHashMap<>();
    private final Random random = new Random();

    /**
     * Sinh OTP ngẫu nhiên 6 chữ số và lưu vào cache kèm payload (thời hạn 5 phút).
     */
    public String generateAndCacheOtp(String email, Object payload) {
        // Sinh 6 số ngẫu nhiên
        String otp = String.format("%06d", random.nextInt(999999));
        
        // Hết hạn sau 5 phút
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        
        OtpData otpData = new OtpData(otp, expiryTime, payload);
        otpCache.put(email, otpData);
        
        return otp;
    }

    /**
     * Xác thực OTP. Trả về true nếu hợp lệ, ngược lại false.
     * Tự động xóa OTP khỏi cache nếu hợp lệ hoặc đã hết hạn.
     */
    public boolean validateOtp(String email, String otp) {
        OtpData data = otpCache.get(email);
        
        if (data == null) {
            return false;
        }

        // Kiểm tra thời hạn
        if (data.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpCache.remove(email); // Xóa nếu hết hạn
            return false;
        }

        // So khớp mã OTP
        if (data.getOtp().equals(otp)) {
            return true;
        }

        return false;
    }

    /**
     * Lấy payload đã lưu trữ (vd: DangKyRequest) từ cache sau khi xác thực hợp lệ.
     */
    public Object getPayload(String email) {
        OtpData data = otpCache.get(email);
        return (data != null) ? data.getPayload() : null;
    }

    /**
     * Xóa cache thủ công
     */
    public void clearCache(String email) {
        otpCache.remove(email);
    }
}
