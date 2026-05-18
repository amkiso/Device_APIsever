package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ChuKyDienTu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChuKyDienTuRepository extends JpaRepository<ChuKyDienTu, Integer> {

    Optional<ChuKyDienTu> findByHopDongId(Integer hopDongId);
}
