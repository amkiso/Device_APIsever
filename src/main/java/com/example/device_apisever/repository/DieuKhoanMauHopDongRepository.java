package com.example.device_apisever.repository;

import com.example.device_apisever.entity.DieuKhoanMauHopDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DieuKhoanMauHopDongRepository extends JpaRepository<DieuKhoanMauHopDong, Integer> {

    /**
     * Lấy tất cả điều khoản đang hoạt động, sắp xếp theo số điều
     */
    List<DieuKhoanMauHopDong> findByDangHoatDongTrueOrderBySoDieuAsc();
}
