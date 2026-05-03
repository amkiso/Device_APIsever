package com.example.device_apisever.repository;

import com.example.device_apisever.entity.LoaiThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiThietBiRepository extends JpaRepository<LoaiThietBi, Integer> {
}
