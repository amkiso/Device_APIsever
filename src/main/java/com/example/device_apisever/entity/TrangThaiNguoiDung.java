package com.example.device_apisever.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Trạng thái tài khoản người dùng.
 * 1 = Đang hoạt động, 2 = Tạm khóa, 3 = Đã vô hiệu
 */
@Entity
@Table(name = "TrangThaiNguoiDung")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrangThaiNguoiDung {

    @Id
    @Column(name = "TrangThaiID")
    private Integer trangThaiId;

    @Column(name = "TenTrangThai", nullable = false, length = 50)
    private String tenTrangThai;
}
