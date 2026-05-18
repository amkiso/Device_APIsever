package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ChiTietThueThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietThueThietBiRepository extends JpaRepository<ChiTietThueThietBi, Integer> {

    /**
     * Lấy tất cả chi tiết thiết bị trong 1 hợp đồng
     */
    List<ChiTietThueThietBi> findByHopDongId(Integer hopDongId);
}
