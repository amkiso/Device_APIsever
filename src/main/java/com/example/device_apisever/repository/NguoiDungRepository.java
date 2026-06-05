package com.example.device_apisever.repository;

import com.example.device_apisever.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {

    Optional<NguoiDung> findByTaiKhoan(String taiKhoan);

    boolean existsByTaiKhoan(String taiKhoan);

    boolean existsByMaNguoiDung(String maNguoiDung);

    /**
     * Tìm mã KH lớn nhất để auto-generate mã tiếp theo (KH00001, KH00002, ...)
     */
    @Query("SELECT nd.maNguoiDung FROM NguoiDung nd WHERE nd.maNguoiDung LIKE 'KH%' ORDER BY nd.maNguoiDung DESC LIMIT 1")
    Optional<String> findMaxMaKhachHang();

    /**
     * Danh sách nhân viên (VaiTroID IN 1,2,3)
     */
    List<NguoiDung> findByVaiTroIdIn(List<Integer> vaiTroIds);

    /**
     * Danh sách nhân viên theo vai trò
     */
    List<NguoiDung> findByVaiTroId(Integer vaiTroId);

    /**
     * Danh sách nhân viên theo kho
     */
    List<NguoiDung> findByKhoId(Integer khoId);

    /**
     * Danh sách khách hàng (VaiTroID = 4)
     */
    @Query("SELECT nd FROM NguoiDung nd WHERE nd.vaiTroId = 4")
    List<NguoiDung> findAllKhachHang();

    @Query("SELECT nd.maNguoiDung FROM NguoiDung nd WHERE nd.maNguoiDung LIKE 'NV%' ORDER BY nd.maNguoiDung DESC LIMIT 1")
    Optional<String> findMaxMaNhanVien();

    @Query("SELECT n FROM NguoiDung n WHERE " +
        "(:vaiTroId IS NULL OR n.vaiTroId = :vaiTroId) AND " +
        "(:trangThaiId IS NULL OR n.trangThaiId = :trangThaiId) AND " +
        "(:loaiKhachHangId IS NULL OR n.loaiKhachHangId = :loaiKhachHangId) AND " +
        "(:keyword IS NULL OR LOWER(n.hoTen) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
        "  OR LOWER(n.email) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
        "  OR n.soDienThoai LIKE CONCAT('%',:keyword,'%') " +
        "  OR LOWER(n.maNguoiDung) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
        "ORDER BY n.ngayTao DESC")
    Page<NguoiDung> findAllWithFilters(
        @Param("vaiTroId") Integer vaiTroId,
        @Param("trangThaiId") Integer trangThaiId,
        @Param("loaiKhachHangId") Integer loaiKhachHangId,
        @Param("keyword") String keyword,
        Pageable pageable);
}
