package com.example.device_apisever.repository;

import com.example.device_apisever.entity.LoaiThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiThietBiRepository extends JpaRepository<LoaiThietBi, Integer> {

    /**
     * Lọc loại thiết bị theo danh mục
     */
    List<LoaiThietBi> findByDanhMucId(Integer danhMucId);

    /**
     * Tìm kiếm loại thiết bị theo tên (LIKE %keyword%)
     */
    List<LoaiThietBi> findByTenLoaiThietBiContainingIgnoreCase(String keyword);
}
