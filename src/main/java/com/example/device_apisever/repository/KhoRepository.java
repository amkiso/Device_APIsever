package com.example.device_apisever.repository;

import com.example.device_apisever.entity.Kho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoRepository extends JpaRepository<Kho, Integer> {
}
