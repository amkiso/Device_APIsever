package com.example.device_apisever.repository;

import com.example.device_apisever.entity.DanhMucThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhMucThietBiRepository extends JpaRepository<DanhMucThietBi, Integer> {
}
