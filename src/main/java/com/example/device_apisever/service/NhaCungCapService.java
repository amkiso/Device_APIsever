package com.example.device_apisever.service;

import com.example.device_apisever.entity.NhaCungCap;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.NhaCungCapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;

    public NhaCungCapService(NhaCungCapRepository nhaCungCapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    /**
     * Lấy tất cả nhà cung cấp
     */
    public List<NhaCungCap> findAll() {
        return nhaCungCapRepository.findAll();
    }

    /**
     * Tìm nhà cung cấp theo ID
     */
    public NhaCungCap findById(Integer id) {
        return nhaCungCapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy nhà cung cấp với ID: " + id));
    }
}
