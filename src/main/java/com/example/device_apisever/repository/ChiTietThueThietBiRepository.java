package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ChiTietThueThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietThueThietBiRepository extends JpaRepository<ChiTietThueThietBi, Integer> {

    /**
     * Lấy tất cả chi tiết thiết bị trong 1 hợp đồng
     */
    List<ChiTietThueThietBi> findByHopDongId(Integer hopDongId);

    /**
     * Đếm số thiết bị trong 1 hợp đồng
     */
    long countByHopDongId(Integer hopDongId);

    /**
     * Lấy danh sách hopDongId mà thiết bị đã tham gia
     */
    @Query("SELECT DISTINCT c.hopDongId FROM ChiTietThueThietBi c WHERE c.thietBiId = :thietBiId")
    List<Integer> findHopDongIdsByThietBiId(@Param("thietBiId") Integer thietBiId);
}
