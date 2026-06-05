package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ChiTietPhieuNhap;
import com.example.device_apisever.entity.ChiTietPhieuNhapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietPhieuNhapRepository extends JpaRepository<ChiTietPhieuNhap, ChiTietPhieuNhapId> {
}
