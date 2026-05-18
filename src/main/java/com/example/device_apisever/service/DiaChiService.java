package com.example.device_apisever.service;

import com.example.device_apisever.dto.DiaChiRequest;
import com.example.device_apisever.dto.DiaChiResponse;
import com.example.device_apisever.entity.DiaChiGiaoHang;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.DiaChiGiaoHangRepository;
import com.example.device_apisever.repository.NguoiDungRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaChiService {

    private final DiaChiGiaoHangRepository diaChiRepository;
    private final NguoiDungRepository nguoiDungRepository;

    public DiaChiService(DiaChiGiaoHangRepository diaChiRepository,
                         NguoiDungRepository nguoiDungRepository) {
        this.diaChiRepository = diaChiRepository;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    private Integer resolveNguoiDungId(String taiKhoan) {
        NguoiDung nd = nguoiDungRepository.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        return nd.getNguoiDungId();
    }

    private DiaChiResponse toResponse(DiaChiGiaoHang dc) {
        return DiaChiResponse.builder()
                .diaChiId(dc.getDiaChiId())
                .tenNguoiNhan(dc.getTenNguoiNhan())
                .soDienThoai(dc.getSoDienThoai())
                .tinhThanhPho(dc.getTinhThanhPho())
                .phuongXa(dc.getPhuongXa())
                .diaChiChiTiet(dc.getDiaChiChiTiet())
                .donVi(dc.getDonVi())
                .loaiDiaChi(dc.getLoaiDiaChi())
                .laMacDinh(dc.getLaMacDinh())
                .build();
    }

    public List<DiaChiResponse> getByUser(String taiKhoan) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);
        return diaChiRepository.findByNguoiDungIdOrderByLaMacDinhDescNgayTaoDesc(nguoiDungId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public DiaChiResponse create(String taiKhoan, DiaChiRequest request) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);
        if (Boolean.TRUE.equals(request.getLaMacDinh())) {
            diaChiRepository.resetMacDinh(nguoiDungId);
        }
        DiaChiGiaoHang dc = DiaChiGiaoHang.builder()
                .nguoiDungId(nguoiDungId)
                .tenNguoiNhan(request.getTenNguoiNhan())
                .soDienThoai(request.getSoDienThoai())
                .tinhThanhPho(request.getTinhThanhPho())
                .phuongXa(request.getPhuongXa())
                .diaChiChiTiet(request.getDiaChiChiTiet())
                .donVi(request.getDonVi())
                .loaiDiaChi(request.getLoaiDiaChi())
                .laMacDinh(request.getLaMacDinh() != null ? request.getLaMacDinh() : false)
                .build();
        diaChiRepository.save(dc);
        return toResponse(dc);
    }

    @Transactional
    public DiaChiResponse update(String taiKhoan, Integer diaChiId, DiaChiRequest request) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);
        DiaChiGiaoHang dc = diaChiRepository.findById(diaChiId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ ID: " + diaChiId));
        if (!dc.getNguoiDungId().equals(nguoiDungId)) {
            throw new BusinessException("Bạn không có quyền cập nhật địa chỉ này");
        }
        if (Boolean.TRUE.equals(request.getLaMacDinh())) {
            diaChiRepository.resetMacDinh(nguoiDungId);
        }
        dc.setTenNguoiNhan(request.getTenNguoiNhan());
        dc.setSoDienThoai(request.getSoDienThoai());
        dc.setTinhThanhPho(request.getTinhThanhPho());
        dc.setPhuongXa(request.getPhuongXa());
        dc.setDiaChiChiTiet(request.getDiaChiChiTiet());
        dc.setDonVi(request.getDonVi());
        dc.setLoaiDiaChi(request.getLoaiDiaChi());
        dc.setLaMacDinh(request.getLaMacDinh() != null ? request.getLaMacDinh() : dc.getLaMacDinh());
        diaChiRepository.save(dc);
        return toResponse(dc);
    }

    @Transactional
    public void delete(String taiKhoan, Integer diaChiId) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);
        DiaChiGiaoHang dc = diaChiRepository.findById(diaChiId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ ID: " + diaChiId));
        if (!dc.getNguoiDungId().equals(nguoiDungId)) {
            throw new BusinessException("Bạn không có quyền xóa địa chỉ này");
        }
        diaChiRepository.delete(dc);
    }
}
