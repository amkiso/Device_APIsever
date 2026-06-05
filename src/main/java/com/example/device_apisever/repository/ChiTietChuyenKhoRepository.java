package com.example.device_apisever.repository;

import com.example.device_apisever.entity.ChiTietChuyenKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietChuyenKhoRepository extends JpaRepository<ChiTietChuyenKho, Integer> {
    List<ChiTietChuyenKho> findByLenhChuyenKhoId(Integer lenhChuyenKhoId);
}
