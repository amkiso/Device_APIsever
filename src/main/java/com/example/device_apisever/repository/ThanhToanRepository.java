package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {

    /**
     * Lấy tất cả thanh toán của 1 hợp đồng
     */
    List<ThanhToan> findByHopDongIdOrderByNgayTaoDesc(Integer hopDongId);

    /**
     * Tìm thanh toán theo mã giao dịch
     */
    Optional<ThanhToan> findByMaGiaoDich(String maGiaoDich);

    /**
     * Tính tổng số tiền theo loại thanh toán (chỉ tính giao dịch thành công)
     */
    @Query("SELECT COALESCE(SUM(t.soTien), 0) FROM ThanhToan t WHERE t.loaiThanhToan = :loaiThanhToan AND t.trangThai = 1")
    BigDecimal sumSoTienByLoaiThanhToanAndSuccess(@Param("loaiThanhToan") Integer loaiThanhToan);

    /**
     * Lấy 10 giao dịch gần nhất
     */
    List<ThanhToan> findTop10ByOrderByNgayTaoDesc();
}
