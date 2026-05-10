package com.example.device_apisever.service;

import com.example.device_apisever.entity.DanhMucThietBi;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.DanhMucThietBiRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanhMucService {

    private final DanhMucThietBiRepository danhMucRepository;

    public DanhMucService(DanhMucThietBiRepository danhMucRepository) {
        this.danhMucRepository = danhMucRepository;
    }

    /**
     * Lấy tất cả danh mục thiết bị
     */
    public List<DanhMucThietBi> findAll() {
        return danhMucRepository.findAll();
    }

    /**
     * Tìm danh mục theo ID
     */
    public DanhMucThietBi findById(Integer id) {
        return danhMucRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy danh mục với ID: " + id));
    }

    /**
     * Tạo danh mục mới
     */
    public DanhMucThietBi create(DanhMucThietBi danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    /**
     * Cập nhật danh mục
     */
    public DanhMucThietBi update(Integer id, DanhMucThietBi danhMucRequest) {
        DanhMucThietBi existing = danhMucRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy danh mục với ID: " + id));
        existing.setTenDanhMuc(danhMucRequest.getTenDanhMuc());
        return danhMucRepository.save(existing);
    }

    /**
     * Xóa danh mục theo ID
     */
    public void deleteById(Integer id) {
        if (!danhMucRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Không tìm thấy danh mục với ID: " + id);
        }
        danhMucRepository.deleteById(id);
    }
}
