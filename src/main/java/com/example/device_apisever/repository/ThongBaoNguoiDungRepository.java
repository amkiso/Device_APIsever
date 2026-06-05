package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ThongBaoNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThongBaoNguoiDungRepository extends JpaRepository<ThongBaoNguoiDung, Integer> {

    /**
     * Lấy danh sách thông báo của 1 user, sắp xếp mới nhất trước
     */
    @Query("SELECT tbnd FROM ThongBaoNguoiDung tbnd " +
           "WHERE tbnd.nguoiDungId = :nguoiDungId " +
           "ORDER BY tbnd.thongBaoId DESC")
    List<ThongBaoNguoiDung> findByNguoiDungIdOrderByThongBaoIdDesc(@Param("nguoiDungId") Integer nguoiDungId);

    /**
     * Đếm số thông báo chưa đọc của 1 user
     */
    long countByNguoiDungIdAndDaDoc(Integer nguoiDungId, Boolean daDoc);

    /**
     * Tìm bản ghi cụ thể (để đánh dấu đã đọc)
     */
    Optional<ThongBaoNguoiDung> findByThongBaoIdAndNguoiDungId(Integer thongBaoId, Integer nguoiDungId);

    void deleteByThongBaoId(Integer thongBaoId);

    long countByThongBaoId(Integer thongBaoId);

    long countByThongBaoIdAndDaDoc(Integer thongBaoId, Boolean daDoc);

    @Query("SELECT tbnd.thongBaoId FROM ThongBaoNguoiDung tbnd WHERE tbnd.nguoiDungId = :nguoiDungId")
    List<Integer> findThongBaoIdsByNguoiDungId(@Param("nguoiDungId") Integer nguoiDungId);
}
