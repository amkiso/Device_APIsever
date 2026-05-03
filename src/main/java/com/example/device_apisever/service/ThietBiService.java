package com.example.device_apisever.service;

import com.example.device_apisever.dto.ThietBiDetailResponse;
import com.example.device_apisever.entity.*;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ThietBiService {

    private final ThietBiRepository thietBiRepository;
    private final LoaiThietBiRepository loaiThietBiRepository;
    private final DanhMucThietBiRepository danhMucThietBiRepository;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final TinhTrangThietBiRepository tinhTrangThietBiRepository;
    private final KhoRepository khoRepository;
    private final HinhAnhThietBiRepository hinhAnhThietBiRepository;

    public ThietBiService(ThietBiRepository thietBiRepository,
                          LoaiThietBiRepository loaiThietBiRepository,
                          DanhMucThietBiRepository danhMucThietBiRepository,
                          NhaCungCapRepository nhaCungCapRepository,
                          TinhTrangThietBiRepository tinhTrangThietBiRepository,
                          KhoRepository khoRepository,
                          HinhAnhThietBiRepository hinhAnhThietBiRepository) {
        this.thietBiRepository = thietBiRepository;
        this.loaiThietBiRepository = loaiThietBiRepository;
        this.danhMucThietBiRepository = danhMucThietBiRepository;
        this.nhaCungCapRepository = nhaCungCapRepository;
        this.tinhTrangThietBiRepository = tinhTrangThietBiRepository;
        this.khoRepository = khoRepository;
        this.hinhAnhThietBiRepository = hinhAnhThietBiRepository;
    }

    // 1. Lấy tất cả thiết bị
    public List<ThietBi> findAll() {
        return thietBiRepository.findAll();
    }

    // 2. Tìm một thiết bị theo ID
    public Optional<ThietBi> findById(Integer id) {
        return thietBiRepository.findById(id);
    }

    // 3. Thêm mới hoặc Cập nhật thiết bị
    public ThietBi save(ThietBi thietBi) {
        return thietBiRepository.save(thietBi);
    }

    // 4. Xóa thiết bị
    public void deleteById(Integer id) {
        thietBiRepository.deleteById(id);
    }

    /**
     * 5. Tra cứu toàn bộ thông tin thiết bị theo Mã Tài Sản.
     * Gộp dữ liệu từ: ThietBi, LoaiThietBi, DanhMucThietBi,
     * NhaCungCap, TinhTrangThietBi, Kho, HinhAnhThietBi.
     */
    public ThietBiDetailResponse findDetailByMaTaiSan(String maTaiSan) {
        // 1. Tìm thiết bị
        ThietBi tb = thietBiRepository.findByMaTaiSan(maTaiSan)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay thiet bi voi ma tai san: " + maTaiSan));

        // 2. Lấy thông tin loại thiết bị
        LoaiThietBi loai = loaiThietBiRepository.findById(tb.getLoaiThietBiId())
                .orElse(null);

        // 3. Lấy danh mục
        String tenDanhMuc = null;
        Integer danhMucId = null;
        if (loai != null) {
            DanhMucThietBi dm = danhMucThietBiRepository.findById(loai.getDanhMucId())
                    .orElse(null);
            if (dm != null) {
                danhMucId = dm.getDanhMucId();
                tenDanhMuc = dm.getTenDanhMuc();
            }
        }

        // 4. Lấy nhà cung cấp
        String tenNhaCungCap = null;
        Integer nhaCungCapId = null;
        if (loai != null) {
            NhaCungCap ncc = nhaCungCapRepository.findById(loai.getNhaCungCapId())
                    .orElse(null);
            if (ncc != null) {
                nhaCungCapId = ncc.getNhaCungCapId();
                tenNhaCungCap = ncc.getTenNhaCungCap();
            }
        }

        // 5. Lấy tình trạng
        TinhTrangThietBi tt = tinhTrangThietBiRepository.findById(tb.getTinhTrangId())
                .orElse(null);

        // 6. Lấy kho hiện tại
        Kho kho = khoRepository.findById(tb.getKhoHienTaiId())
                .orElse(null);

        // 7. Lấy danh sách hình ảnh
        List<HinhAnhThietBi> hinhAnhs = hinhAnhThietBiRepository.findByThietBiId(tb.getThietBiId());
        List<ThietBiDetailResponse.HinhAnhInfo> hinhAnhInfos = hinhAnhs.stream()
                .map(ha -> ThietBiDetailResponse.HinhAnhInfo.builder()
                        .hinhAnhId(ha.getHinhAnhId())
                        .urlAnh(ha.getUrlAnh())
                        .loaiAnhId(ha.getLoaiAnhId())
                        .ngayChup(ha.getNgayChup())
                        .build())
                .collect(Collectors.toList());

        // 8. Gộp tất cả vào DTO
        return ThietBiDetailResponse.builder()
                .thietBiId(tb.getThietBiId())
                .maTaiSan(tb.getMaTaiSan())
                .ngayBaoTriTiepTheo(tb.getNgayBaoTriTiepTheo())
                // Loại thiết bị
                .loaiThietBiId(loai != null ? loai.getLoaiThietBiId() : null)
                .tenLoaiThietBi(loai != null ? loai.getTenLoaiThietBi() : null)
                .thongSoKyThuat(loai != null ? loai.getThongSoKyThuat() : null)
                .giaThueThamKhao(loai != null ? loai.getGiaThueThamKhao() : null)
                .anhDaiDien(loai != null ? loai.getAnhDaiDien() : null)
                // Danh mục
                .danhMucId(danhMucId)
                .tenDanhMuc(tenDanhMuc)
                // Nhà cung cấp
                .nhaCungCapId(nhaCungCapId)
                .tenNhaCungCap(tenNhaCungCap)
                // Tình trạng
                .tinhTrangId(tt != null ? tt.getTinhTrangId() : null)
                .tenTinhTrang(tt != null ? tt.getTenTinhTrang() : null)
                // Kho
                .khoHienTaiId(kho != null ? kho.getKhoId() : null)
                .tenKho(kho != null ? kho.getTenKho() : null)
                .diaChiKho(kho != null ? kho.getDiaChi() : null)
                // Hình ảnh
                .hinhAnhs(hinhAnhInfos)
                .build();
    }
}