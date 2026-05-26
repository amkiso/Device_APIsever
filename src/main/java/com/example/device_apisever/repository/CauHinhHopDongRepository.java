package com.example.device_apisever.repository;

import com.example.device_apisever.entity.CauHinhHopDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CauHinhHopDongRepository extends JpaRepository<CauHinhHopDong, Integer> {
    Optional<CauHinhHopDong> findByMaCauHinh(String maCauHinh);
}
