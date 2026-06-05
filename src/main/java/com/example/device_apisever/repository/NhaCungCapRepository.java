package com.example.device_apisever.repository;

import com.example.device_apisever.entity.NhaCungCap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {
    @Query("SELECT n FROM NhaCungCap n WHERE LOWER(n.tenNhaCungCap) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<NhaCungCap> searchByKeyword(@Param("keyword") String keyword);
}
