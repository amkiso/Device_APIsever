package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThietBiRepository extends JpaRepository<ThietBi, Integer> {

    /**
     * Tìm thiết bị theo Mã tài sản (unique)
     */
    Optional<ThietBi> findByMaTaiSan(String maTaiSan);

    /**
     * Đếm số thiết bị theo tình trạng (VD: TinhTrangID=3 là đang bảo trì)
     */
    long countByTinhTrangId(Integer tinhTrangId);

    /**
     * Lấy thiết bị cần bảo trì hôm nay
     */
    @Query("SELECT tb FROM ThietBi tb " +
           "WHERE CAST(tb.ngayBaoTriTiepTheo AS date) = CAST(:today AS date)")
    List<ThietBi> findThietBiCanBaoTriHomNay(@Param("today") LocalDateTime today);

    /**
     * Lọc thiết bị theo loại thiết bị
     */
    List<ThietBi> findByLoaiThietBiId(Integer loaiThietBiId);

    /**
     * Đếm số lượng thiết bị theo loại để sinh mã
     */
    long countByLoaiThietBiId(Integer loaiThietBiId);

    /**
     * Đếm số lượng thiết bị theo loại và tình trạng
     */
    long countByLoaiThietBiIdAndTinhTrangId(Integer loaiThietBiId, Integer tinhTrangId);

    /**
     * Đếm số thiết bị thuộc một kho cụ thể
     */
    long countByKhoHienTaiId(Integer khoId);

    /**
     * Đếm số thiết bị thuộc một kho theo tình trạng
     */
    long countByKhoHienTaiIdAndTinhTrangId(Integer khoId, Integer tinhTrangId);

    List<ThietBi> findByKhoHienTaiId(Integer khoHienTaiId);
    List<ThietBi> findByLoaiThietBiIdAndKhoHienTaiId(Integer loaiThietBiId, Integer khoHienTaiId);
}
