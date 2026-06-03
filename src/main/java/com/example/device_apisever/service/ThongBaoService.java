package com.example.device_apisever.service;

import com.example.device_apisever.dto.ThongBaoRequest;
import com.example.device_apisever.dto.ThongBaoResponse;
import com.example.device_apisever.entity.NguoiDung;
import com.example.device_apisever.entity.ThongBao;
import com.example.device_apisever.entity.ThongBaoNguoiDung;
import com.example.device_apisever.exception.BusinessException;
import com.example.device_apisever.exception.ResourceNotFoundException;
import com.example.device_apisever.repository.NguoiDungRepository;
import com.example.device_apisever.repository.ThongBaoNguoiDungRepository;
import com.example.device_apisever.repository.ThongBaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThongBaoService {

    private final ThongBaoRepository thongBaoRepository;
    private final ThongBaoNguoiDungRepository thongBaoNguoiDungRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final FCMService fcmService;

    public ThongBaoService(ThongBaoRepository thongBaoRepository,
                           ThongBaoNguoiDungRepository thongBaoNguoiDungRepository,
                           NguoiDungRepository nguoiDungRepository,
                           FCMService fcmService) {
        this.thongBaoRepository = thongBaoRepository;
        this.thongBaoNguoiDungRepository = thongBaoNguoiDungRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.fcmService = fcmService;
    }

    /**
     * Tạo thông báo mới và phân phối tới user tương ứng.
     * Đồng thời gửi push notification qua Firebase.
     *
     * @param request   nội dung thông báo
     * @param nguoiGuiId ID của người gửi (Admin)
     */
    @Transactional
    public ThongBao taoThongBao(ThongBaoRequest request, Integer nguoiGuiId) {
        // 1. Lưu nội dung thông báo
        ThongBao thongBao = ThongBao.builder()
                .tieuDe(request.getTieuDe())
                .noiDung(request.getNoiDung())
                .loaiThongBao(request.getLoaiThongBao())
                .nguoiGuiId(nguoiGuiId)
                .vaiTroNhanId(request.getVaiTroNhanId())
                .build();
        thongBaoRepository.save(thongBao);

        // 2. Xác định danh sách người nhận
        List<NguoiDung> danhSachNhan = new ArrayList<>();

        switch (request.getLoaiThongBao()) {
            case 1: // Tất cả user
                danhSachNhan = nguoiDungRepository.findAll();
                break;

            case 2: // Theo nhóm vai trò
                if (request.getVaiTroNhanId() == null) {
                    throw new BusinessException("Loai thong bao theo vai tro phai co VaiTroNhanID");
                }
                danhSachNhan = nguoiDungRepository.findByVaiTroId(request.getVaiTroNhanId());
                break;

            case 3: // Cá nhân
                if (request.getNguoiDungNhanId() == null) {
                    throw new BusinessException("Loai thong bao ca nhan phai co NguoiDungNhanID");
                }
                NguoiDung nguoiNhan = nguoiDungRepository.findById(request.getNguoiDungNhanId())
                        .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung nhan"));
                danhSachNhan.add(nguoiNhan);
                break;

            default:
                throw new BusinessException("Loai thong bao khong hop le");
        }

        // 3. Insert vào bảng ThongBaoNguoiDung (trạng thái chưa đọc)
        List<Integer> nguoiDungIds = new ArrayList<>();
        for (NguoiDung nd : danhSachNhan) {
            ThongBaoNguoiDung tbnd = ThongBaoNguoiDung.builder()
                    .thongBaoId(thongBao.getThongBaoId())
                    .nguoiDungId(nd.getNguoiDungId())
                    .daDoc(false)
                    .build();
            thongBaoNguoiDungRepository.save(tbnd);
            nguoiDungIds.add(nd.getNguoiDungId());
        }

        // 4. Gửi push notification qua Firebase (bất đồng bộ)
        System.out.println("==== THONG BAO LOG: Tạo xong nội dung DB, chuẩn bị gửi FCM tới " + nguoiDungIds.size() + " người dùng. ====");
        fcmService.sendToUsers(nguoiDungIds, request.getTieuDe(), request.getNoiDung());

        return thongBao;
    }

    /**
     * Lấy danh sách thông báo của 1 user (sắp xếp mới nhất trước).
     */
    public List<ThongBaoResponse> layDanhSachThongBao(Integer nguoiDungId) {
        List<ThongBaoNguoiDung> danhSach = thongBaoNguoiDungRepository
                .findByNguoiDungIdOrderByThongBaoIdDesc(nguoiDungId);

        return danhSach.stream().map(tbnd -> {
            ThongBao tb = thongBaoRepository.findById(tbnd.getThongBaoId()).orElse(null);
            if (tb == null) return null;

            String nguoiGui = nguoiDungRepository.findById(tb.getNguoiGuiId())
                    .map(NguoiDung::getHoTen)
                    .orElse("Hệ thống");

            return ThongBaoResponse.builder()
                    .thongBaoId(tb.getThongBaoId())
                    .tieuDe(tb.getTieuDe())
                    .noiDung(tb.getNoiDung())
                    .loaiThongBao(tb.getLoaiThongBao())
                    .nguoiGui(nguoiGui)
                    .ngayTao(tb.getNgayTao())
                    .daDoc(tbnd.getDaDoc())
                    .ngayDoc(tbnd.getNgayDoc())
                    .build();
        }).filter(r -> r != null).collect(Collectors.toList());
    }

    /**
     * Đếm số thông báo chưa đọc (cho badge chuông).
     */
    public long demChuaDoc(Integer nguoiDungId) {
        return thongBaoNguoiDungRepository.countByNguoiDungIdAndDaDoc(nguoiDungId, false);
    }

    /**
     * Đánh dấu 1 thông báo là đã đọc.
     */
    @Transactional
    public void danhDaDoc(Integer thongBaoId, Integer nguoiDungId) {
        ThongBaoNguoiDung tbnd = thongBaoNguoiDungRepository
                .findByThongBaoIdAndNguoiDungId(thongBaoId, nguoiDungId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thong bao"));

        tbnd.setDaDoc(true);
        tbnd.setNgayDoc(LocalDateTime.now());
        thongBaoNguoiDungRepository.save(tbnd);
    }

    /**
     * Đánh dấu tất cả thông báo của 1 user là đã đọc.
     */
    @Transactional
    public void danhDaDocTatCa(Integer nguoiDungId) {
        List<ThongBaoNguoiDung> danhSachChuaDoc = thongBaoNguoiDungRepository
                .findByNguoiDungIdOrderByThongBaoIdDesc(nguoiDungId).stream()
                .filter(tb -> !tb.getDaDoc())
                .collect(Collectors.toList());

        for (ThongBaoNguoiDung tbnd : danhSachChuaDoc) {
            tbnd.setDaDoc(true);
            tbnd.setNgayDoc(LocalDateTime.now());
        }
        
        thongBaoNguoiDungRepository.saveAll(danhSachChuaDoc);
    }
}
