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

    public QrCodeService(ThietBiRepository thietBiRepository) {
        this.thietBiRepository = thietBiRepository;
    }

    /**
     * Lấy hoặc tạo mới QR code cho thiết bị theo ID.
     * - Nếu thiết bị đã có qrCodeUrl → trả về URL cũ.
     * - Nếu chưa → tạo ảnh QR, lưu file PNG, cập nhật DB, trả về URL mới.
     *
     * @param thietBiId ID thiết bị
     * @return đường dẫn public truy cập ảnh QR (VD: /uploads/qrcode/TB001.png)
     */
    public String getOrCreateQrCode(Integer thietBiId) {
        // 1. Tìm thiết bị
        ThietBi tb = thietBiRepository.findById(thietBiId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay thiet bi voi ID: " + thietBiId));

        // 2. Nếu đã có QR → trả về luôn
        if (tb.getQrCodeUrl() != null && !tb.getQrCodeUrl().isBlank()) {
            return tb.getQrCodeUrl();
        }

        // 3. Tạo QR content: DEVICE:<maTaiSan>
        String qrContent = "DEVICE:" + tb.getMaTaiSan();

        // 4. Tạo file name: <maTaiSan>.png
        String fileName = tb.getMaTaiSan() + ".png";
        Path dirPath = Paths.get(QR_DIR);
        Path filePath = dirPath.resolve(fileName);

        try {
            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(dirPath);

            // 5. Tạo QR code bằng ZXing
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 2);

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrContent, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);

            // 6. Lưu file PNG
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

        } catch (WriterException | IOException e) {
            throw new BusinessException("Khong the tao QR code: " + e.getMessage());
        }

        // 7. Tạo URL public (relative path phục vụ qua static resource)
        String qrUrl = "/uploads/qrcode/" + fileName;

        // 8. Cập nhật DB
        tb.setQrCodeUrl(qrUrl);
        thietBiRepository.save(tb);

        return qrUrl;
    }
}
