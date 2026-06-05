package com.example.device_apisever.service;

import com.example.device_apisever.dto.ThongBaoRequest;
import com.example.device_apisever.dto.bangiao.*;
import com.example.device_apisever.entity.*;
import com.example.device_apisever.repository.*;
import com.example.device_apisever.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BanGiaoService {

    private final HopDongThueRepository hopDongThueRepository;
    private final ChiTietThueThietBiRepository chiTietThueThietBiRepository;
    private final LichSuBanGiaoRepository lichSuBanGiaoRepository;
    private final HinhAnhThietBiRepository hinhAnhThietBiRepository;
    private final ThietBiRepository thietBiRepository;
    private final S3StorageService s3StorageService;
    private final ThongBaoService thongBaoService;

    public BanGiaoService(HopDongThueRepository hopDongThueRepository,
                          ChiTietThueThietBiRepository chiTietThueThietBiRepository,
                          LichSuBanGiaoRepository lichSuBanGiaoRepository,
                          HinhAnhThietBiRepository hinhAnhThietBiRepository,
                          ThietBiRepository thietBiRepository,
                          S3StorageService s3StorageService,
                          ThongBaoService thongBaoService) {
        this.hopDongThueRepository = hopDongThueRepository;
        this.chiTietThueThietBiRepository = chiTietThueThietBiRepository;
        this.lichSuBanGiaoRepository = lichSuBanGiaoRepository;
        this.hinhAnhThietBiRepository = hinhAnhThietBiRepository;
        this.thietBiRepository = thietBiRepository;
        this.s3StorageService = s3StorageService;
        this.thongBaoService = thongBaoService;
    }

    @Transactional
    public void banGiaoThietBi(Integer hopDongId, BanGiaoHopDongRequest request, Integer nguoiThucHienId) {
        // 1. Lấy hợp đồng, validate trạng thái = 3
        HopDongThue hd = hopDongThueRepository.findById(hopDongId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hợp đồng"));
        if (hd.getTrangThaiId() != 3) {
            throw new IllegalArgumentException("Hợp đồng không ở trạng thái 'Chờ nhận thiết bị'");
        }

        // 2. Lấy danh sách chi tiết thuê của hợp đồng
        List<ChiTietThueThietBi> chiTiets = chiTietThueThietBiRepository.findByHopDongId(hopDongId);

        // 3. Xử lý từng thiết bị trong request
        for (BanGiaoThietBiRequest tbReq : request.getDanhSachBanGiao()) {
            // Validate thiết bị thuộc hợp đồng
            ChiTietThueThietBi chiTiet = chiTiets.stream()
                .filter(ct -> ct.getThietBiId().equals(tbReq.getThietBiId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "Thiết bị " + tbReq.getThietBiId() + " không thuộc hợp đồng"));

            // Validate chưa bàn giao
            if (chiTiet.getNgayGiao() != null) {
                throw new IllegalArgumentException("Thiết bị " + tbReq.getThietBiId() + " đã được bàn giao");
            }

            // Lấy thông tin thiết bị
            ThietBi tb = thietBiRepository.findById(tbReq.getThietBiId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị"));

            // 3a. Tạo bản ghi LichSuBanGiao
            LichSuBanGiao banGiao = LichSuBanGiao.builder()
                .hopDongId(hopDongId)
                .thietBiId(tbReq.getThietBiId())
                .tuKhoId(tb.getKhoHienTaiId())  // Kho xuất = kho hiện tại của thiết bị
                .denKhoId(null)                  // NULL = giao ra ngoài cho khách
                .nguoiDungThucHienId(nguoiThucHienId)
                .ngayGiaoNhan(LocalDateTime.now())
                .nguoiNhan(tbReq.getNguoiNhan())
                .hinhAnhXacNhan(tbReq.getSignatureFileName()) // Lưu tên file chữ ký
                .loaiGiaoDichId(1)               // 1 = Xuất cho thuê
                .ghiChuTinhTrang(tbReq.getGhiChuTinhTrang())
                .build();
            LichSuBanGiao saved = lichSuBanGiaoRepository.save(banGiao);

            // 3b. Lưu ảnh hiện trạng vào HinhAnhThietBi
            if (tbReq.getImageFileNames() != null) {
                for (String fileName : tbReq.getImageFileNames()) {
                    HinhAnhThietBi ha = HinhAnhThietBi.builder()
                        .thietBiId(tbReq.getThietBiId())
                        .nguoiDungChupId(nguoiThucHienId)
                        .urlAnh(fileName)   // Chỉ lưu tên file (API trả về link khi đọc)
                        .loaiAnhId(3)       // 3 = Bàn giao thiết bị
                        .banGiaoId(saved.getBanGiaoId())
                        .build();
                    hinhAnhThietBiRepository.save(ha);
                }
            }

            // 3c. Cập nhật ChiTietThueThietBi
            chiTiet.setTinhTrangGiao(tbReq.getGhiChuTinhTrang());
            chiTiet.setNgayGiao(LocalDateTime.now());
            chiTietThueThietBiRepository.save(chiTiet);

            // 3d. Cập nhật trạng thái thiết bị = 2 (Đang cho thuê)
            tb.setTinhTrangId(2);
            thietBiRepository.save(tb);
        }

        // 4. Kiểm tra nếu TẤT CẢ thiết bị đã bàn giao → chuyển HĐ sang trạng thái 4
        boolean allDelivered = chiTiets.stream().allMatch(ct -> ct.getNgayGiao() != null);
        if (allDelivered) {
            hd.setTrangThaiId(4); // 4 = Đang cho thuê
            hopDongThueRepository.save(hd);

            // Gửi thông báo cho khách hàng
            ThongBaoRequest notiReq = new ThongBaoRequest();
            notiReq.setTieuDe("Bàn giao thiết bị thành công");
            notiReq.setNoiDung("Hợp đồng #" + hd.getHopDongId() + " đã được bàn giao đầy đủ thiết bị và bắt đầu tính thời gian thuê.");
            notiReq.setLoaiThongBao(3);
            notiReq.setNguoiDungNhanId(hd.getNguoiDungKhachId());
            thongBaoService.taoThongBao(notiReq, nguoiThucHienId);
        }
    }

    @Transactional
    public void thuHoiThietBi(Integer hopDongId, ThuHoiHopDongRequest request, Integer nguoiThucHienId) {
        // 1. Lấy hợp đồng, validate trạng thái = 4 hoặc 8
        HopDongThue hd = hopDongThueRepository.findById(hopDongId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hợp đồng"));
        if (hd.getTrangThaiId() != 4 && hd.getTrangThaiId() != 8) {
            throw new IllegalArgumentException("Hợp đồng không ở trạng thái phù hợp để thu hồi");
        }

        List<ChiTietThueThietBi> chiTiets = chiTietThueThietBiRepository.findByHopDongId(hopDongId);

        // 2. Xử lý từng thiết bị
        for (ThuHoiThietBiRequest tbReq : request.getDanhSachThuHoi()) {
            // Validate số lượng ảnh: 2-6
            if (tbReq.getImageFileNames() == null || tbReq.getImageFileNames().size() < 2) {
                throw new IllegalArgumentException("Thiết bị " + tbReq.getThietBiId() + " phải có ít nhất 2 ảnh");
            }
            if (tbReq.getImageFileNames().size() > 6) {
                throw new IllegalArgumentException("Thiết bị " + tbReq.getThietBiId() + " chỉ được tối đa 6 ảnh");
            }

            ChiTietThueThietBi chiTiet = chiTiets.stream()
                .filter(ct -> ct.getThietBiId().equals(tbReq.getThietBiId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Thiết bị không thuộc hợp đồng"));

            ThietBi tb = thietBiRepository.findById(tbReq.getThietBiId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị"));

            // 2a. Tạo bản ghi LichSuBanGiao (loại 2 = thu hồi)
            LichSuBanGiao banGiao = LichSuBanGiao.builder()
                .hopDongId(hopDongId)
                .thietBiId(tbReq.getThietBiId())
                .tuKhoId(tb.getKhoHienTaiId())
                .denKhoId(tb.getKhoHienTaiId()) // Thu về kho hiện tại
                .nguoiDungThucHienId(nguoiThucHienId)
                .ngayGiaoNhan(LocalDateTime.now())
                .loaiGiaoDichId(2)              // 2 = Thu hồi từ khách
                .mucDanhGiaId(tbReq.getMucDanhGiaId())
                .ghiChuTinhTrang(tbReq.getGhiChuTinhTrang())
                .build();
            LichSuBanGiao saved = lichSuBanGiaoRepository.save(banGiao);

            // 2b. Lưu ảnh
            for (String fileName : tbReq.getImageFileNames()) {
                HinhAnhThietBi ha = HinhAnhThietBi.builder()
                    .thietBiId(tbReq.getThietBiId())
                    .nguoiDungChupId(nguoiThucHienId)
                    .urlAnh(fileName)
                    .loaiAnhId(6)              // 6 = Thu hồi thiết bị
                    .banGiaoId(saved.getBanGiaoId())
                    .build();
                hinhAnhThietBiRepository.save(ha);
            }

            // 2c. Cập nhật ChiTietThueThietBi
            chiTiet.setTinhTrangTra(tbReq.getGhiChuTinhTrang());
            chiTiet.setNgayTra(LocalDateTime.now());
            chiTietThueThietBiRepository.save(chiTiet);

            // 2d. Cập nhật trạng thái thiết bị
            if (tbReq.getMucDanhGiaId() != null && tbReq.getMucDanhGiaId() == 4) {
                tb.setTinhTrangId(4); // Thanh lý
            } else if (tbReq.getMucDanhGiaId() != null && tbReq.getMucDanhGiaId() == 3) {
                tb.setTinhTrangId(3); // Đang bảo trì
            } else {
                tb.setTinhTrangId(1); // Sẵn sàng cho thuê lại
            }
            thietBiRepository.save(tb);
        }

        // 3. Chuyển hợp đồng sang trạng thái 9 (Đang kiểm tra hư hại)
        hd.setTrangThaiId(9);
        hd.setNgayTraThucTe(LocalDateTime.now());
        hopDongThueRepository.save(hd);
    }
}
