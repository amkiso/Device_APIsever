package com.example.device_apisever.repository;

import com.example.device_apisever.entity.LichSuBaoTri;
import com.example.device_apisever.dto.baotri.BaoTriDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LichSuBaoTriRepository extends JpaRepository<LichSuBaoTri, Integer> {

    @Query("SELECT new com.example.device_apisever.dto.baotri.BaoTriDto(" +
           "b.baoTriId, b.thietBiId, tb.maTaiSan, ltb.tenLoaiThietBi, " +
           "b.nguoiDungBaoTriId, nd.hoTen, (SELECT MAX(ct.hopDongId) FROM ChiTietThueThietBi ct WHERE ct.thietBiId = b.thietBiId), b.ngayThucHien, " +
           "b.loaiBaoTriId, lb.tenLoai, b.noiDungBaoTri, b.chiPhi, " +
           "b.trangThaiId, ttb.tenTrangThai, b.tinhVaoBoiThuong, b.ghiChu, b.ngayHoanThanh) " +
           "FROM LichSuBaoTri b " +
           "LEFT JOIN ThietBi tb ON b.thietBiId = tb.thietBiId " +
           "LEFT JOIN LoaiThietBi ltb ON tb.loaiThietBiId = ltb.loaiThietBiId " +
           "LEFT JOIN NguoiDung nd ON b.nguoiDungBaoTriId = nd.nguoiDungId " +
           "LEFT JOIN LoaiBaoTri lb ON b.loaiBaoTriId = lb.loaiBaoTriId " +
           "LEFT JOIN TrangThaiBaoTri ttb ON b.trangThaiId = ttb.trangThaiId " +
           "WHERE (:trangThaiId IS NULL OR b.trangThaiId = :trangThaiId) " +
           "AND (:nguoiDungId IS NULL OR b.nguoiDungBaoTriId = :nguoiDungId) " +
           "AND (:keyword IS NULL OR LOWER(tb.maTaiSan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR CAST((SELECT MAX(ct.hopDongId) FROM ChiTietThueThietBi ct WHERE ct.thietBiId = b.thietBiId) AS string) LIKE CONCAT('%', :keyword, '%')) " +
           "ORDER BY b.ngayThucHien DESC")
    Page<BaoTriDto> searchBaoTri(
            @Param("trangThaiId") Integer trangThaiId,
            @Param("nguoiDungId") Integer nguoiDungId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT new com.example.device_apisever.dto.baotri.BaoTriDto(" +
           "b.baoTriId, b.thietBiId, tb.maTaiSan, ltb.tenLoaiThietBi, " +
           "b.nguoiDungBaoTriId, nd.hoTen, (SELECT MAX(ct.hopDongId) FROM ChiTietThueThietBi ct WHERE ct.thietBiId = b.thietBiId), b.ngayThucHien, " +
           "b.loaiBaoTriId, lb.tenLoai, b.noiDungBaoTri, b.chiPhi, " +
           "b.trangThaiId, ttb.tenTrangThai, b.tinhVaoBoiThuong, b.ghiChu, b.ngayHoanThanh) " +
           "FROM LichSuBaoTri b " +
           "LEFT JOIN ThietBi tb ON b.thietBiId = tb.thietBiId " +
           "LEFT JOIN LoaiThietBi ltb ON tb.loaiThietBiId = ltb.loaiThietBiId " +
           "LEFT JOIN NguoiDung nd ON b.nguoiDungBaoTriId = nd.nguoiDungId " +
           "LEFT JOIN LoaiBaoTri lb ON b.loaiBaoTriId = lb.loaiBaoTriId " +
           "LEFT JOIN TrangThaiBaoTri ttb ON b.trangThaiId = ttb.trangThaiId " +
           "WHERE b.baoTriId = :baoTriId")
    Optional<BaoTriDto> findDtoById(@Param("baoTriId") Integer baoTriId);

    @Query("SELECT SUM(b.chiPhi) FROM LichSuBaoTri b")
    Optional<BigDecimal> sumAllChiPhi();

    @Query("SELECT SUM(b.chiPhi) FROM LichSuBaoTri b WHERE b.loaiBaoTriId = 1")
    Optional<BigDecimal> sumChiPhiDinhKy();

    @Query("SELECT SUM(b.chiPhi) FROM LichSuBaoTri b WHERE b.loaiBaoTriId = 3")
    Optional<BigDecimal> sumChiPhiCaiTien();

    @Query("SELECT SUM(b.chiPhi) FROM LichSuBaoTri b WHERE EXISTS (SELECT 1 FROM ChiTietThueThietBi ct WHERE ct.thietBiId = b.thietBiId) AND b.tinhVaoBoiThuong = false")
    Optional<BigDecimal> sumChiPhiLoiKhongBoiThuong();

    @Query("SELECT SUM(b.chiPhi) FROM LichSuBaoTri b WHERE b.loaiBaoTriId = 2 AND b.tinhVaoBoiThuong = true")
    Optional<BigDecimal> sumChiPhiSuCoCoBoiThuong();

    // Khôi phục các hàm cũ
    @Query("SELECT b FROM LichSuBaoTri b WHERE b.thietBiId IN (SELECT ct.thietBiId FROM ChiTietThueThietBi ct WHERE ct.hopDongId = :hopDongId)")
    java.util.List<LichSuBaoTri> findByHopDongId(@Param("hopDongId") Integer hopDongId);
    
    @Query("SELECT CASE WHEN COUNT(ct) > 0 THEN true ELSE false END FROM ChiTietThueThietBi ct WHERE ct.thietBiId = :thietBiId AND (ct.ngayTra IS NULL OR ct.ngayTra > CURRENT_TIMESTAMP)")
    boolean isThietBiDangChoThue(@Param("thietBiId") Integer thietBiId);
    
    boolean existsByThietBiId(Integer thietBiId);
    boolean existsByThietBiIdAndTrangThaiIdIn(Integer thietBiId, java.util.List<Integer> trangThaiIds);
    java.util.List<LichSuBaoTri> findByThietBiIdOrderByNgayThucHienDesc(Integer thietBiId);

    @Query("SELECT COALESCE(SUM(b.chiPhi), 0) FROM LichSuBaoTri b WHERE b.trangThaiId = 2 AND b.tinhVaoBoiThuong = false")
    BigDecimal sumChiPhiBaoTri();

    long countByTrangThaiId(Integer trangThaiId);
    long countByNguoiDungBaoTriIdAndTrangThaiId(Integer nguoiDungBaoTriId, Integer trangThaiId);
    long countByNguoiDungBaoTriIdAndTrangThaiIdIn(Integer nguoiDungBaoTriId, java.util.List<Integer> trangThaiIds);

    @Query("SELECT b FROM LichSuBaoTri b WHERE (b.nguoiDungBaoTriId IS NULL OR b.nguoiDungBaoTriId = :nguoiDungId) AND CAST(b.ngayThucHien AS date) = CAST(:today AS date)")
    java.util.List<LichSuBaoTri> findLichBaoTriHomNayChoKtv(@Param("nguoiDungId") Integer nguoiDungId, @Param("today") java.time.LocalDateTime today);
}
