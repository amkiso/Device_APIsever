package com.example.device_apisever.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

/**
 * Service xử lý nghiệp vụ liên quan đến Cloudflare R2 (tương thích S3 API).
 * <p>
 * Hệ thống tạo SAS URL (Presigned URL) cho phép client upload file.
 * Chỉ lưu đường dẫn tương đối (Relative Path) vào database.
 */
@Service
public class S3StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${r2.bucket.public}")
    private String bucketPublic;

    @Value("${r2.bucket.private}")
    private String bucketPrivate;

    @Value("${r2.endpoint}")
    private String endpoint;

    @Value("${r2.public.endpoint:}")
    private String publicEndpoint;

    /** Mapping tên category → tên thư mục thực trên R2 */
    private final Map<String, String> prefixMap;

    public S3StorageService(S3Client s3Client,
                            S3Presigner s3Presigner,
                            @Value("${r2.prefix.user:user}") String userPrefix,
                            @Value("${r2.prefix.products:product}") String productsPrefix,
                            @Value("${r2.prefix.work:work}") String workPrefix,
                            @Value("${r2.prefix.sign:sign}") String signPrefix,
                            @Value("${r2.prefix.qrcode:QrCode}") String qrcodePrefix) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.prefixMap = Map.of(
                "user", userPrefix,
                "products", productsPrefix,
                "work", workPrefix,
                "sign", signPrefix,
                "qrcode", qrcodePrefix
        );
    }

    public String resolvePrefix(String category) {
        String prefix = prefixMap.get(category.toLowerCase());
        if (prefix == null) {
            throw new IllegalArgumentException(
                    "Category khong hop le: '" + category + "'.");
        }
        return prefix;
    }

    public String generateFileName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getTargetBucket(String category) {
        if ("sign".equalsIgnoreCase(category)) {
            return bucketPrivate;
        }
        return bucketPublic;
    }

    private String getTargetBucketFromPath(String relativePath) {
        if (relativePath != null && relativePath.toLowerCase().startsWith("sign/")) {
            return bucketPrivate;
        }
        return bucketPublic;
    }

    /**
     * Lấy đường dẫn tương đối (Relative Path) để lưu vào database.
     * Ví dụ: "product/uuid.jpg"
     */
    public String getRelativePath(String category, String fileName) {
        return resolvePrefix(category) + "/" + fileName;
    }

    /**
     * Sinh Presigned URL để upload file lên Cloudflare R2 (thời hạn 5 phút).
     */
    public String generateSasUploadUrl(String category, String fileName, String contentType) {
        String key = getRelativePath(category, fileName);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(getTargetBucket(category))
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(objectRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    /**
     * Sinh URL công khai bằng cách nối endpoint, bucket và key.
     * Cần cho các bucket đã public.
     * Ví dụ: https://endpoint.com/bucket/prefix/file.jpg
     * Nếu có r2.public.endpoint, không cần tên bucket.
     */
    public String getPublicUrl(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) return null;
        if (relativePath.startsWith("http")) return relativePath; // Đã là full url
        
        if (publicEndpoint != null && !publicEndpoint.isBlank()) {
            return publicEndpoint + "/" + relativePath;
        }
        return endpoint + "/" + getTargetBucketFromPath(relativePath) + "/" + relativePath;
    }

    /**
     * Sinh URL công khai nếu biết category và fileName
     */
    public String getPublicUrlByCategory(String category, String fileName) {
        return getPublicUrl(getRelativePath(category, fileName));
    }

    /**
     * Xóa file trên R2 thông qua relative path.
     */
    public void deleteFileByRelativePath(String relativePath) {
        if (relativePath == null || relativePath.isBlank() || relativePath.startsWith("http")) {
            return;
        }
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(getTargetBucketFromPath(relativePath))
                .key(relativePath)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    /**
     * Xoá file qua category và filename (dùng khi tương thích)
     */
    public void deleteFile(String category, String fileName) {
        deleteFileByRelativePath(getRelativePath(category, fileName));
    }

    /**
     * Tạo Presigned URL để đọc file bảo mật (thời hạn 5 phút).
     */
    public String generateSasReadUrl(String relativePath) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(getTargetBucketFromPath(relativePath))
                .key(relativePath)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
    }

    /**
     * Upload mảng byte trực tiếp lên R2 từ Backend (như chữ ký).
     * @return Đường dẫn tương đối đã upload (relative path)
     */
    public String uploadByteData(String category, String fileName, byte[] data, String contentType) {
        String key = getRelativePath(category, fileName);

        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(getTargetBucket(category))
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(putOb, RequestBody.fromBytes(data));

        return key; // Lưu đường dẫn tương đối
    }
}
