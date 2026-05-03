package com.example.device_apisever.repository;

import com.example.device_apisever.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Integer> {

    /**
     * Lấy tất cả FCM token của 1 user (có thể đăng nhập nhiều thiết bị)
     */
    List<FCMToken> findByNguoiDungId(Integer nguoiDungId);

    /**
     * Lấy tất cả token của nhiều user (gửi push theo nhóm)
     */
    List<FCMToken> findByNguoiDungIdIn(List<Integer> nguoiDungIds);

    /**
     * Tìm token cụ thể (dùng để update hoặc kiểm tra trùng)
     */
    Optional<FCMToken> findByNguoiDungIdAndToken(Integer nguoiDungId, String token);
}
