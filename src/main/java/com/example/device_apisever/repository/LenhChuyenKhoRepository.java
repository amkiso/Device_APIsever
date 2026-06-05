package com.example.device_apisever.repository;

import com.example.device_apisever.entity.LenhChuyenKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LenhChuyenKhoRepository extends JpaRepository<LenhChuyenKho, Integer> {

    @Query("SELECT l FROM LenhChuyenKho l WHERE " +
           "(:trangThai IS NULL OR l.trangThai = :trangThai) AND " +
           "(:khoId IS NULL OR l.tuKhoId = :khoId OR l.denKhoId = :khoId) AND " +
           "(:nguoiThucHienId IS NULL OR l.nguoiThucHienId = :nguoiThucHienId) " +
           "ORDER BY l.ngayTao DESC")
    Page<LenhChuyenKho> findAllWithFilters(
            @Param("trangThai") Integer trangThai,
            @Param("khoId") Integer khoId,
            @Param("nguoiThucHienId") Integer nguoiThucHienId,
            Pageable pageable);
}
