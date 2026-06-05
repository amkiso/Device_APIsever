package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
    Page<ThongBao> findByTieuDeContainingIgnoreCaseOrderByNgayTaoDesc(String tieuDe, Pageable pageable);
    Page<ThongBao> findAllByOrderByNgayTaoDesc(Pageable pageable);
    Page<ThongBao> findByThongBaoIdInOrderByNgayTaoDesc(List<Integer> thongBaoIds, Pageable pageable);
}
