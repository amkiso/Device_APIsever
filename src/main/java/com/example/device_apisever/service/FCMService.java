package com.example.device_apisever.service;

import com.example.device_apisever.entity.FCMToken;
import com.example.device_apisever.repository.FCMTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service giao tiếp với Firebase Cloud Messaging.
 * Gửi push notification tới thiết bị di động.
 */
@Service
public class FCMService {

    private final FCMTokenRepository fcmTokenRepository;

    public FCMService(FCMTokenRepository fcmTokenRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
    }

    /**
     * Đăng ký hoặc cập nhật FCM token cho user.
     * Nếu token đã tồn tại → cập nhật thời gian.
     * Nếu chưa → tạo mới.
     */
    public void registerToken(Integer nguoiDungId, String token, String deviceName) {
        FCMToken existing = fcmTokenRepository.findByNguoiDungIdAndToken(nguoiDungId, token)
                .orElse(null);

        if (existing != null) {
            existing.setNgayCapNhat(LocalDateTime.now());
            existing.setDeviceName(deviceName);
            fcmTokenRepository.save(existing);
        } else {
            FCMToken newToken = FCMToken.builder()
                    .nguoiDungId(nguoiDungId)
                    .token(token)
                    .deviceName(deviceName)
                    .build();
            fcmTokenRepository.save(newToken);
        }
    }

    /**
     * Gửi push notification tới 1 thiết bị.
     */
    @Async
    public void sendToDevice(String fcmToken, String title, String body) {
        System.out.println("==== FCM LOG: Bắt đầu gửi notification tới token: " + fcmToken + " ====");
        System.out.println("==== FCM LOG: Title: " + title + " | Body: " + body + " ====");
        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("==== FCM LOG: Gửi thành công tới " + fcmToken + ". Response: " + response + " ====");
        } catch (Exception e) {
            System.err.println("FCM lỗi khi gửi tới token " + fcmToken + ": " + e.getMessage());
        }
    }

    /**
     * Gửi push notification tới nhiều user (lấy tất cả token của họ).
     */
    @Async
    public void sendToUsers(List<Integer> nguoiDungIds, String title, String body) {
        System.out.println("==== FCM LOG: Tìm token cho " + nguoiDungIds.size() + " người dùng... ====");
        List<FCMToken> tokens = fcmTokenRepository.findByNguoiDungIdIn(nguoiDungIds);
        System.out.println("==== FCM LOG: Đã tìm thấy " + tokens.size() + " tokens. ====");
        for (FCMToken t : tokens) {
            sendToDevice(t.getToken(), title, body);
        }
    }

    /**
     * Gửi push notification tới 1 user cụ thể (tất cả thiết bị).
     */
    @Async
    public void sendToUser(Integer nguoiDungId, String title, String body) {
        System.out.println("==== FCM LOG: Tìm token cho người dùng ID " + nguoiDungId + " ====");
        List<FCMToken> tokens = fcmTokenRepository.findByNguoiDungId(nguoiDungId);
        System.out.println("==== FCM LOG: Đã tìm thấy " + tokens.size() + " tokens. ====");
        for (FCMToken t : tokens) {
            sendToDevice(t.getToken(), title, body);
        }
    }
}
