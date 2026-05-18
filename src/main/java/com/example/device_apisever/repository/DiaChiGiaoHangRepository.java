package com.example.device_apisever.repository;

import com.example.device_apisever.entity.DiaChiGiaoHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiGiaoHangRepository extends JpaRepository<DiaChiGiaoHang, Integer> {

    /**
     * Lấy tất cả địa chỉ giao hàng của 1 user
     */
    List<DiaChiGiaoHang> findByNguoiDungIdOrderByLaMacDinhDescNgayTaoDesc(Integer nguoiDungId);

    /**
     * Reset tất cả địa chỉ mặc định của user (trước khi set mặc định mới)
     */
    @Modifying
    @Query("UPDATE DiaChiGiaoHang d SET d.laMacDinh = false WHERE d.nguoiDungId = :nguoiDungId")
    void resetMacDinh(@Param("nguoiDungId") Integer nguoiDungId);
}
