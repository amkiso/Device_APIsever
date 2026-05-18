package com.example.device_apisever.service;

import com.example.device_apisever.dto.DieuKhoanMauResponse;
import com.example.device_apisever.repository.DieuKhoanMauHopDongRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DieuKhoanMauService {

    private final DieuKhoanMauHopDongRepository repository;

    public DieuKhoanMauService(DieuKhoanMauHopDongRepository repository) {
        this.repository = repository;
    }

    public List<DieuKhoanMauResponse> getAll() {
        return repository.findByDangHoatDongTrueOrderBySoDieuAsc()
                .stream()
                .map(dk -> DieuKhoanMauResponse.builder()
                        .soDieu(dk.getSoDieu())
                        .tieuDe(dk.getTieuDe())
                        .noiDung(dk.getNoiDung())
                        .build())
                .collect(Collectors.toList());
    }
}
