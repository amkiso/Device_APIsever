package com.example.device_apisever.repository;

import com.example.device_apisever.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
