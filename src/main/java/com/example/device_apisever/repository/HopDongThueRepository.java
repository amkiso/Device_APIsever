package com.example.device_apisever.repository;

import com.example.device_apisever.entity.HopDongThue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HopDongThueRepository extends JpaRepository<HopDongThue, Integer> {

    /**
     * Tính tổng doanh thu theo tháng/năm (HĐ đã hoàn thành, TrangThaiID=5)
     */
    @Query("SELECT COALESCE(SUM(hd.tongTienThue), 0) FROM HopDongThue hd " +
           "WHERE hd.trangThaiId = 5 " +
           "AND YEAR(hd.ngayLap) = :year AND MONTH(hd.ngayLap) = :month")
    BigDecimal findDoanhThuByMonth(@Param("year") int year, @Param("month") int month);

    /**
     * Đếm số hợp đồng đang thuê (TrangThaiID=3) sắp đến hạn trả
     */
    @Query("SELECT COUNT(hd) FROM HopDongThue hd " +
           "WHERE hd.trangThaiId = 3 " +
           "AND hd.ngayDuKienTra BETWEEN :fromDate AND :toDate")
    long countHopDongDenHan(@Param("fromDate") LocalDateTime fromDate,
                            @Param("toDate") LocalDateTime toDate);

    /**
     * Lấy danh sách hợp đồng đến hạn trả hôm nay (cho phần nhắc nhở)
     */
    @Query("SELECT hd FROM HopDongThue hd " +
           "WHERE hd.trangThaiId = 3 " +
           "AND CAST(hd.ngayDuKienTra AS date) = CAST(:today AS date)")
    List<HopDongThue> findHopDongDenHanHomNay(@Param("today") LocalDateTime today);

    /**
     * Lấy danh sách hợp đồng đang thuê sắp đến hạn (7 ngày tới)
     */
    @Query("SELECT hd FROM HopDongThue hd " +
           "WHERE hd.trangThaiId = 3 " +
           "AND hd.ngayDuKienTra BETWEEN :fromDate AND :toDate " +
           "ORDER BY hd.ngayDuKienTra ASC")
    List<HopDongThue> findHopDongSapDenHan(@Param("fromDate") LocalDateTime fromDate,
                                            @Param("toDate") LocalDateTime toDate);

    /**
     * Lấy tất cả hợp đồng của khách hàng, sắp xếp theo ngày lập mới nhất
     */
    @Query("SELECT hd FROM HopDongThue hd " +
           "WHERE hd.nguoiDungKhachId = :nguoiDungId " +
           "ORDER BY hd.ngayLap DESC")
    List<HopDongThue> findByNguoiDungKhachIdOrderByNgayLapDesc(
            @Param("nguoiDungId") Integer nguoiDungId);

    /**
     * Lấy N hợp đồng gần nhất của khách hàng (cho label trang chủ)
     * Sử dụng Pageable để giới hạn số lượng kết quả
     */
    @Query("SELECT hd FROM HopDongThue hd " +
           "WHERE hd.nguoiDungKhachId = :nguoiDungId " +
           "ORDER BY hd.ngayLap DESC")
    List<HopDongThue> findTopNByNguoiDungKhachId(
            @Param("nguoiDungId") Integer nguoiDungId,
            org.springframework.data.domain.Pageable pageable);

    /**
     * Đếm hợp đồng theo trạng thái của 1 khách hàng
     */
    @Query("SELECT COUNT(hd) FROM HopDongThue hd " +
           "WHERE hd.nguoiDungKhachId = :nguoiDungId " +
           "AND hd.trangThaiId = :trangThaiId")
    long countByNguoiDungKhachIdAndTrangThaiId(
            @Param("nguoiDungId") Integer nguoiDungId,
            @Param("trangThaiId") Integer trangThaiId);

    /**
     * Đếm tổng hợp đồng của 1 khách hàng
     */
    @Query("SELECT COUNT(hd) FROM HopDongThue hd " +
           "WHERE hd.nguoiDungKhachId = :nguoiDungId")
    long countByNguoiDungKhachId(@Param("nguoiDungId") Integer nguoiDungId);
}
