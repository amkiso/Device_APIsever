package com.example.device_apisever.dto;

import lombok.*;

/**
 * Response cho API kiểm tra email khi quên mật khẩu.
 * Trả về loại tài khoản (khách hàng hay nhân viên) và thông tin liên hệ admin nếu cần.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CheckEmailResponse {
    /** true nếu email thuộc khách hàng (vaiTroId=4), false nếu nhân viên */
    private boolean isCustomer;
    /** Vai trò ID (1=Admin, 2=Thủ kho, 3=KTV, 4=KH) */
    private Integer vaiTroId;
    /** Thông tin liên hệ admin (SĐT) — chỉ có khi isCustomer=false */
    private String adminPhone;
    /** Tên admin — chỉ có khi isCustomer=false */
    private String adminName;
}
