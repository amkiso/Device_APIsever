package com.example.device_apisever.controller;

import com.example.device_apisever.dto.ThongKeDongTienDto;
import com.example.device_apisever.service.ThongKeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/thong-ke")
@RequiredArgsConstructor
public class ThongKeController {

    private final ThongKeService thongKeService;

    @GetMapping("/dong-tien")
    public ResponseEntity<ThongKeDongTienDto> getThongKeDongTien() {
        return ResponseEntity.ok(thongKeService.getThongKeDongTien());
    }
}
