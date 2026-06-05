package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ApiResponse;
import com.example.device_apisever.dto.NhapKhoFormRequest;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.service.PhieuNhapService;
import com.example.device_apisever.repository.NguoiDungRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nhap-kho")
public class NhapKhoController {

    private final PhieuNhapService phieuNhapService;
    private final NguoiDungRepository nguoiDungRepository;

    public NhapKhoController(PhieuNhapService phieuNhapService, NguoiDungRepository nguoiDungRepository) {
        this.phieuNhapService = phieuNhapService;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> nhapKho(
            @RequestBody NhapKhoFormRequest request,
            Authentication authentication) {
        
        NguoiDung nguoiDung = nguoiDungRepository.findByTaiKhoan(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
                
        phieuNhapService.createPhieuNhap(request, nguoiDung.getNguoiDungId());

        return ResponseEntity.ok(ApiResponse.ok("Nhập kho thành công", null));
    }
}
