package com.example.device_apisever.service;

import com.example.device_apisever.entity.ThietBi;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.ThietBiRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeService {

    private static final int QR_WIDTH = 400;
    private static final int QR_HEIGHT = 400;
    private static final String QR_DIR = "uploads/qrcode";

    private final ThietBiRepository thietBiRepository;
    private final S3StorageService s3StorageService;

    public QrCodeService(ThietBiRepository thietBiRepository, S3StorageService s3StorageService) {
        this.thietBiRepository = thietBiRepository;
        this.s3StorageService = s3StorageService;
    }

    /**
     * Lấy hoặc tạo mới QR code cho thiết bị theo ID.
     * - Nếu thiết bị đã có qrCodeUrl → trả về URL public.
     * - Nếu chưa → tạo ảnh QR, upload lên Cloudflare R2, cập nhật DB, trả về URL mới.
     *
     * @param thietBiId ID thiết bị
     * @return đường dẫn public truy cập ảnh QR
     */
    public String getOrCreateQrCode(Integer thietBiId) {
        // 1. Tìm thiết bị
        ThietBi tb = thietBiRepository.findById(thietBiId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay thiet bi voi ID: " + thietBiId));

        // 2. Nếu đã có QR → trả về public URL luôn
        if (tb.getQrCodeUrl() != null && !tb.getQrCodeUrl().isBlank()) {
            return s3StorageService.getPublicUrl(tb.getQrCodeUrl());
        }

        // 3. Tạo QR content: DEVICE:<maTaiSan>
        String qrContent = "DEVICE:" + tb.getMaTaiSan();

        // 4. Tạo file name: <maTaiSan>.png
        String fileName = tb.getMaTaiSan() + ".png";

        try {
            // 5. Tạo QR code bằng ZXing
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 2);

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrContent, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);

            // 6. Chuyển BitMatrix thành byte[]
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            byte[] qrBytes = baos.toByteArray();

            // 7. Upload lên Cloudflare R2 thông qua S3StorageService
            String relativePath = s3StorageService.uploadByteData("qrcode", fileName, qrBytes, "image/png");

            // 8. Cập nhật DB (Lưu relative path)
            tb.setQrCodeUrl(relativePath);
            thietBiRepository.save(tb);

            // 9. Trả về public URL
            return s3StorageService.getPublicUrl(relativePath);

        } catch (WriterException | IOException e) {
            throw new BusinessException("Khong the tao QR code: " + e.getMessage());
        }
    }
}
