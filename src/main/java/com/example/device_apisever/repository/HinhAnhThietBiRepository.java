package com.example.device_apisever.repository;

import com.example.device_apisever.entity.HinhAnhThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhThietBiRepository extends JpaRepository<HinhAnhThietBi, Integer> {

    /**
     * Lấy tất cả hình ảnh theo ThietBiID
     */
    List<HinhAnhThietBi> findByThietBiId(Integer thietBiId);
    List<HinhAnhThietBi> findByBaoTriId(Integer baoTriId);
}
