package com.example.device_apisever.repository;

import com.example.device_apisever.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Integer> {

    /**
     * Lấy tất cả item trong giỏ hàng của 1 user, sắp xếp mới nhất lên đầu.
     */
    List<GioHang> findByNguoiDungIdOrderByNgayThemDesc(Integer nguoiDungId);

    /**
     * Tìm item theo user + loại thiết bị (kiểm tra trùng).
     */
    Optional<GioHang> findByNguoiDungIdAndLoaiThietBiId(Integer nguoiDungId, Integer loaiThietBiId);

    /**
     * Đếm tổng số items trong giỏ hàng của 1 user.
     */
    @Query("SELECT COALESCE(SUM(g.soLuong), 0) FROM GioHang g WHERE g.nguoiDungId = :nguoiDungId")
    Integer countTotalItems(@Param("nguoiDungId") Integer nguoiDungId);

    /**
     * Xóa tất cả items trong giỏ hàng của 1 user (sau khi tạo hợp đồng).
     */
    void deleteByNguoiDungId(Integer nguoiDungId);
}
