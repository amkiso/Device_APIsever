package com.example.device_apisever.repository;

import com.example.device_apisever.entity.LichSuBaoTri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LichSuBaoTriRepository extends JpaRepository<LichSuBaoTri, Integer> {

    /**
     * Đếm số phiếu bảo trì đang thực hiện (TrangThaiID=1: Đang thực hiện)
     */
    @Query("SELECT COUNT(bt) FROM LichSuBaoTri bt WHERE bt.trangThaiId = 1")
    long countDangBaoTri();

    /**
     * Lấy danh sách phiếu bảo trì được lên lịch hôm nay
     */
    @Query("SELECT bt FROM LichSuBaoTri bt " +
           "WHERE bt.trangThaiId = 1 " +
           "AND CAST(bt.ngayThucHien AS date) = CAST(:today AS date)")
    List<LichSuBaoTri> findBaoTriHomNay(@Param("today") LocalDateTime today);

    /**
     * Lấy lịch sử bảo trì theo thiết bị (mới nhất trước)
     */
    List<LichSuBaoTri> findByThietBiIdOrderByNgayThucHienDesc(Integer thietBiId);

    /**
     * Kiểm tra xem thiết bị có lịch sử bảo trì không
     */
    boolean existsByThietBiId(Integer thietBiId);
}
