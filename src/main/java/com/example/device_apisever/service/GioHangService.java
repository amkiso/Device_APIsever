package com.example.device_apisever.service;

import com.example.device_apisever.dto.GioHangRequest;
import com.example.device_apisever.dto.GioHangResponse;
import com.example.device_apisever.dto.GioHangUpdateRequest;
import com.example.device_apisever.entity.GioHang;
import com.example.device_apisever.entity.LoaiThietBi;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.GioHangRepository;
import com.example.device_apisever.repository.LoaiThietBiRepository;
import com.example.device_apisever.repository.NguoiDungRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Lazy;

@Service
public class GioHangService {

    private final GioHangRepository gioHangRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final LoaiThietBiRepository loaiThietBiRepository;
    private final LoaiThietBiService loaiThietBiService;

    public GioHangService(GioHangRepository gioHangRepository,
                          NguoiDungRepository nguoiDungRepository,
                          LoaiThietBiRepository loaiThietBiRepository,
                          @Lazy LoaiThietBiService loaiThietBiService) {
        this.gioHangRepository = gioHangRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.loaiThietBiRepository = loaiThietBiRepository;
        this.loaiThietBiService = loaiThietBiService;
    }

    // ─── Helper: lấy NguoiDungID từ taiKhoan (JWT subject) ───
    private Integer resolveNguoiDungId(String taiKhoan) {
        NguoiDung nd = nguoiDungRepository.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        return nd.getNguoiDungId();
    }

    // ─── Helper: map entity → response DTO ───
    private GioHangResponse toResponse(GioHang gh) {
        LoaiThietBi ltb = loaiThietBiRepository.findById(gh.getLoaiThietBiId()).orElse(null);

        String tenLoai = ltb != null ? ltb.getTenLoaiThietBi() : "Không xác định";
        // Chuyển đổi filename → full Azure URL
        String anhDaiDien = ltb != null
                ? loaiThietBiService.getFullImageUrl(ltb.getAnhDaiDien())
                : null;
        BigDecimal gia = ltb != null ? ltb.getGiaThueThamKhao() : BigDecimal.ZERO;
        BigDecimal thanhTien = gia.multiply(BigDecimal.valueOf(gh.getSoLuong()));

        return GioHangResponse.builder()
                .gioHangId(gh.getGioHangId())
                .loaiThietBiId(gh.getLoaiThietBiId())
                .tenLoaiThietBi(tenLoai)
                .anhDaiDien(anhDaiDien)
                .giaThueThamKhao(gia)
                .soLuong(gh.getSoLuong())
                .thanhTien(thanhTien)
                .ngayThem(gh.getNgayThem())
                .build();
    }

    /**
     * GET /api/gio-hang — Lấy danh sách giỏ hàng của user hiện tại.
     */
    public List<GioHangResponse> getByUser(String taiKhoan) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);
        List<GioHang> items = gioHangRepository.findByNguoiDungIdOrderByNgayThemDesc(nguoiDungId);
        return items.stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * POST /api/gio-hang — Thêm item vào giỏ hàng.
     * Nếu loại thiết bị đã có trong giỏ → cộng dồn số lượng.
     */
    @Transactional
    public GioHangResponse addItem(String taiKhoan, GioHangRequest request) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);

        // Kiểm tra loại thiết bị tồn tại
        if (!loaiThietBiRepository.existsById(request.getLoaiThietBiId())) {
            throw new ResourceNotFoundException("Loại thiết bị ID " + request.getLoaiThietBiId() + " không tồn tại");
        }

        // Nếu đã có trong giỏ → cộng dồn
        GioHang gh = gioHangRepository
                .findByNguoiDungIdAndLoaiThietBiId(nguoiDungId, request.getLoaiThietBiId())
                .map(existing -> {
                    existing.setSoLuong(existing.getSoLuong() + request.getSoLuong());
                    return existing;
                })
                .orElseGet(() -> GioHang.builder()
                        .nguoiDungId(nguoiDungId)
                        .loaiThietBiId(request.getLoaiThietBiId())
                        .soLuong(request.getSoLuong())
                        .build());

        gioHangRepository.save(gh);
        return toResponse(gh);
    }

    /**
     * PUT /api/gio-hang/{id} — Cập nhật số lượng.
     */
    @Transactional
    public GioHangResponse updateQuantity(String taiKhoan, Integer gioHangId, GioHangUpdateRequest request) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);

        GioHang gh = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy item giỏ hàng ID: " + gioHangId));

        // Đảm bảo item thuộc về user hiện tại
        if (!gh.getNguoiDungId().equals(nguoiDungId)) {
            throw new BusinessException("Bạn không có quyền cập nhật item này");
        }

        gh.setSoLuong(request.getSoLuong());
        gioHangRepository.save(gh);
        return toResponse(gh);
    }

    /**
     * DELETE /api/gio-hang/{id} — Xóa item khỏi giỏ hàng.
     */
    @Transactional
    public void deleteItem(String taiKhoan, Integer gioHangId) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);

        GioHang gh = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy item giỏ hàng ID: " + gioHangId));

        // Đảm bảo item thuộc về user hiện tại
        if (!gh.getNguoiDungId().equals(nguoiDungId)) {
            throw new BusinessException("Bạn không có quyền xóa item này");
        }

        gioHangRepository.delete(gh);
    }

    /**
     * GET /api/gio-hang/count — Đếm tổng items (cho badge).
     */
    public Integer countItems(String taiKhoan) {
        Integer nguoiDungId = resolveNguoiDungId(taiKhoan);
        return gioHangRepository.countTotalItems(nguoiDungId);
    }
}
