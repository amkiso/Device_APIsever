package com.example.device_apisever.dto.baotri;

import lombok.Data;
import java.util.List;

@Data
public class GhiNhanSuCoReq {
    private List<String> imageUrls; // List of image URLs
}
