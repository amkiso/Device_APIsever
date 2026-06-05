package com.example.device_apisever.service;

import com.example.device_apisever.entity.NhaCungCap;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.LoaiThietBiRepository;
import com.example.device_apisever.repository.NhaCungCapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminNhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;
    private final LoaiThietBiRepository loaiThietBiRepository;

    public AdminNhaCungCapService(NhaCungCapRepository nhaCungCapRepository,
                                  LoaiThietBiRepository loaiThietBiRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
        this.loaiThietBiRepository = loaiThietBiRepository;
    }

    public List<NhaCungCap> getAll(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return nhaCungCapRepository.searchByKeyword(keyword.trim());
        }
        return nhaCungCapRepository.findAll();
    }

    public NhaCungCap getDetail(Integer id) {
        return nhaCungCapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhà cung cấp"));
    }

    public NhaCungCap create(NhaCungCap request) {
        if (request.getTenNhaCungCap() == null || request.getTenNhaCungCap().trim().isEmpty()) {
            throw new BusinessException("Tên nhà cung cấp không được để trống");
        }
        NhaCungCap ncc = new NhaCungCap();
        ncc.setTenNhaCungCap(request.getTenNhaCungCap().trim());
        ncc.setNguoiLienHe(request.getNguoiLienHe());
        ncc.setSoDienThoai(request.getSoDienThoai());
        ncc.setDiaChi(request.getDiaChi());
        return nhaCungCapRepository.save(ncc);
    }

    public NhaCungCap update(Integer id, NhaCungCap request) {
        NhaCungCap ncc = getDetail(id);
        if (request.getTenNhaCungCap() == null || request.getTenNhaCungCap().trim().isEmpty()) {
            throw new BusinessException("Tên nhà cung cấp không được để trống");
        }
        ncc.setTenNhaCungCap(request.getTenNhaCungCap().trim());
        ncc.setNguoiLienHe(request.getNguoiLienHe());
        ncc.setSoDienThoai(request.getSoDienThoai());
        ncc.setDiaChi(request.getDiaChi());
        return nhaCungCapRepository.save(ncc);
    }

    public void delete(Integer id) {
        NhaCungCap ncc = getDetail(id);
        if (loaiThietBiRepository.existsByNhaCungCapId(id)) {
            throw new BusinessException("Không thể xóa nhà cung cấp đã có loại thiết bị liên kết");
        }
        nhaCungCapRepository.delete(ncc);
    }
}
