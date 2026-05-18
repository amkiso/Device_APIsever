package com.example.device_apisever.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * DTO request tạo hợp đồng mới từ checkout.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TaoHopDongRequest {

    @NotNull(message = "Địa chỉ giao hàng không được để trống")
    private Integer diaChiGiaoId;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private Integer phuongThucThanhToan;

    @NotNull(message = "Ngày bắt đầu thuê không được để trống")
    private String ngayBatDauThue;

    @NotNull(message = "Số tháng thuê không được để trống")
    @Min(value = 1, message = "Số tháng thuê tối thiểu là 1")
    private Integer soThangThue;

    private String ghiChuKhachHang;

    @NotEmpty(message = "Danh sách thiết bị không được để trống")
    private List<ThietBiThueItem> danhSachThietBi;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class ThietBiThueItem {
        @NotNull(message = "ID thiết bị không được để trống")
        private Integer thietBiId;

        @NotNull(message = "Số lượng không được để trống")
        @Min(value = 1, message = "Số lượng tối thiểu là 1")
        private Integer soLuong;
    }
}
