package com.example.device_apisever.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Service xử lý nghiệp vụ liên quan đến Azure Blob Storage.
 * <p>
 * Kiến trúc SAS Token với Multi-Container:
 * - Server KHÔNG nhận file upload trực tiếp → tiết kiệm băng thông.
 * - Server sinh SAS URL có thời hạn để client tự upload lên Azure.
 * - Ảnh được phân loại vào 3 container riêng biệt:
 *   + "user"     → Ảnh cá nhân (avatar, ảnh đại diện)
 *   + "products" → Ảnh sản phẩm/thiết bị
 *   + "work"     → Ảnh nghiệp vụ (giao nhận, bàn giao, hiện trường, bảo trì)
 */
@Service
public class AzureStorageService {

    private final BlobServiceClient blobServiceClient;

    /** Mapping tên category → tên container thực trên Azure */
    private final Map<String, String> containerMap;

    public AzureStorageService(BlobServiceClient blobServiceClient,
                               @Value("${azure.storage.container.user}") String userContainer,
                               @Value("${azure.storage.container.products}") String productsContainer,
                               @Value("${azure.storage.container.work}") String workContainer) {
        this.blobServiceClient = blobServiceClient;
        this.containerMap = Map.of(
                "user", userContainer,
                "products", productsContainer,
                "work", workContainer
        );
    }

    // ========================================================================================
    // PHÂN GIẢI CONTAINER
    // ========================================================================================

    /**
     * Phân giải tên category (user/products/work) thành tên container thực trên Azure.
     *
     * @param category loại ảnh: "user", "products", hoặc "work"
     * @return tên container trên Azure
     * @throws IllegalArgumentException nếu category không hợp lệ
     */
    public String resolveContainerName(String category) {
        String container = containerMap.get(category.toLowerCase());
        if (container == null) {
            throw new IllegalArgumentException(
                    "Category khong hop le: '" + category + "'. Chi chap nhan: user, products, work");
        }
        return container;
    }

    /**
     * Trả về danh sách các category hợp lệ và container tương ứng.
     */
    public Map<String, String> getAvailableCategories() {
        return containerMap;
    }

    // ========================================================================================
    // SINH UUID FILE NAME
    // ========================================================================================

    /**
     * Sinh tên file duy nhất bằng UUID để tránh trùng lặp.
     * Format: "uuid-random.jpg"
     *
     * @param extension đuôi file (ví dụ: "jpg", "png")
     * @return tên file duy nhất
     */
    public String generateFileName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    // ========================================================================================
    // SAS TOKEN — UPLOAD
    // ========================================================================================

    /**
     * Tạo SAS URL cho phép client upload (PUT) trực tiếp lên Azure Blob.
     * <p>
     * Chi tiết SAS Token được tạo:
     * - Quyền: Chỉ GHI (Write) — client không thể đọc hay xóa blob khác.
     * - Thời hạn: 5 phút kể từ lúc tạo.
     * - URL trả về có dạng: https://{account}.blob.core.windows.net/{container}/{blob}?{sas_params}
     *
     * @param category loại ảnh: "user", "products", hoặc "work"
     * @param fileName tên file (blob) trên Azure
     * @return đường link SAS URL đầy đủ, client dùng HTTP PUT để upload
     */
    public String generateSasUploadUrl(String category, String fileName) {
        String containerName = resolveContainerName(category);

        // 1. Lấy reference tới container và blob cụ thể
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // 2. Cấu hình quyền SAS: chỉ cho phép WRITE (ghi)
        BlobSasPermission permissions = new BlobSasPermission().setWritePermission(true);

        // 3. Cấu hình thời hạn SAS: hết hạn sau 5 phút
        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(
                OffsetDateTime.now().plusMinutes(5),  // Expiry time
                permissions                           // Write-only permission
        );

        // 4. Sinh SAS token string
        String sasToken = blobClient.generateSas(sasValues);

        // 5. Ghép URL đầy đủ: blob URL + "?" + SAS token
        return blobClient.getBlobUrl() + "?" + sasToken;
    }

    // ========================================================================================
    // PUBLIC URL — ĐỌC ẢNH
    // ========================================================================================

    /**
     * Trả về URL công khai (public) của blob — dùng để hiển thị ảnh trên app.
     * <p>
     * Lưu ý: Container phải được cấu hình public access level = "Blob"
     * trên Azure Portal để URL này truy cập được mà không cần SAS.
     *
     * @param category loại ảnh: "user", "products", hoặc "work"
     * @param fileName tên file trên Azure
     * @return URL công khai của blob
     */
    public String getPublicUrl(String category, String fileName) {
        String containerName = resolveContainerName(category);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        return blobClient.getBlobUrl();
    }

    // ========================================================================================
    // XÓA BLOB
    // ========================================================================================

    /**
     * Xóa blob trên Azure (dùng khi cần xóa ảnh cũ).
     *
     * @param category loại ảnh: "user", "products", hoặc "work"
     * @param fileName tên file cần xóa
     */
    public void deleteBlob(String category, String fileName) {
        String containerName = resolveContainerName(category);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        if (blobClient.exists()) {
            blobClient.delete();
        }
    }

    /**
     * Tự động phát hiện category từ URL đầy đủ của blob.
     * Ví dụ: "https://mediaserverproject.blob.core.windows.net/products/abc.jpg" → "products"
     *
     * @param blobUrl URL đầy đủ của blob
     * @return tên category, hoặc null nếu không khớp container nào
     */
    public String detectCategoryFromUrl(String blobUrl) {
        for (Map.Entry<String, String> entry : containerMap.entrySet()) {
            if (blobUrl.contains("/" + entry.getValue() + "/")) {
                return entry.getKey();
            }
        }
        return null;
    }
}
