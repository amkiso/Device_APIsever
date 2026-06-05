package com.example.device_apisever.service;

import com.example.device_apisever.entity.Kho;
import com.example.device_apisever.repository.KhoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class KhoService {
    private final KhoRepository khoRepository;

    public KhoService(KhoRepository khoRepository) {
        this.khoRepository = khoRepository;
    }

    public List<Kho> getAllKho() {
        return khoRepository.findAll();
    }

    @Transactional
    public Kho createKho(Kho request) {
        Kho kho = new Kho();
        kho.setTenKho(request.getTenKho());
        kho.setDiaChi(request.getDiaChi());
        kho.setNguoiPhuTrach(request.getNguoiPhuTrach());
        kho.setSoDienThoai(request.getSoDienThoai());
        kho.setQuanLyId(request.getQuanLyId());
        return khoRepository.save(kho);
    }

    @Transactional
    public Kho updateKho(Integer id, Kho request) {
        Kho kho = khoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kho"));
        kho.setTenKho(request.getTenKho());
        kho.setDiaChi(request.getDiaChi());
        kho.setNguoiPhuTrach(request.getNguoiPhuTrach());
        kho.setSoDienThoai(request.getSoDienThoai());
        kho.setQuanLyId(request.getQuanLyId());
        return khoRepository.save(kho);
    }

    @Transactional
    public void deleteKho(Integer id) {
        if (!khoRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy kho");
        }
        khoRepository.deleteById(id);
    }
}
