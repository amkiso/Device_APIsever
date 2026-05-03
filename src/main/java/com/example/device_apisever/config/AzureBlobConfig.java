package com.example.device_apisever.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình kết nối Azure Blob Storage.
 * <p>
 * Đọc Connection String từ application.properties và tạo Bean {@link BlobServiceClient}
 * để các Service khác có thể inject và sử dụng.
 */
@Configuration
public class AzureBlobConfig {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    /**
     * Khởi tạo BlobServiceClient — đối tượng gốc để thao tác với Azure Blob Storage.
     * Bean này là singleton, chỉ tạo một lần khi ứng dụng khởi động.
     */
    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }
}
