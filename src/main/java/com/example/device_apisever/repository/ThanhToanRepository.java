package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
