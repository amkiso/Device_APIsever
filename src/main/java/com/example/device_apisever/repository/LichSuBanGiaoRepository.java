package com.example.device_apisever.repository;

import com.example.device_apisever.entity.LichSuBanGiao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LichSuBanGiaoRepository extends JpaRepository<LichSuBanGiao, Integer> {

    /**
     * Lấy lịch bàn giao trong ngày hôm nay (cho phần nhắc nhở Dashboard)
     */
    @Query("SELECT bg FROM LichSuBanGiao bg " +
           "WHERE CAST(bg.ngayGiaoNhan AS date) = CAST(:today AS date) " +
           "ORDER BY bg.ngayGiaoNhan ASC")
    List<LichSuBanGiao> findBanGiaoHomNay(@Param("today") LocalDateTime today);

    List<LichSuBanGiao> findByHopDongId(Integer hopDongId);

    List<LichSuBanGiao> findByHopDongIdAndLoaiGiaoDichId(Integer hopDongId, Integer loaiGiaoDichId);

    boolean existsByNguoiDungThucHienId(Integer nguoiDungId);
}
