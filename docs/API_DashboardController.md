# Nhóm Dashboard (DashboardController)
*Cung cấp các con số thống kê tổng hợp dùng cho trang chủ (Dashboard) của từng vai trò (Admin, Thủ kho, Kỹ thuật viên).*

---

## 1. Dashboard dành cho Admin
- **Method:** `GET`
- **Endpoint:** `/api/dashboard`
- **Mô tả:** Lấy các chỉ số thống kê tổng quan toàn hệ thống: Doanh thu, số lượng hợp đồng mới, thiết bị đang cho thuê, thiết bị cần bảo trì, ...
- **Quyền hạn (Role):** Admin

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "tongDoanhThu": 150000000,
    "soHopDongMoi": 10,
    "thietBiDangChoThue": 120,
    "thietBiCanBaoTri": 5
  }
}
```

---

## 2. Dashboard dành cho Thủ Kho
- **Method:** `GET`
- **Endpoint:** `/api/dashboard/thu-kho`
- **Mô tả:** Lấy dữ liệu thống kê riêng cho kho mà Thủ Kho đang quản lý (số phiếu nhập chờ duyệt, số lệnh xuất kho, lượng thiết bị tồn).
- **Quyền hạn (Role):** Thủ Kho

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "phieuNhapChoDuyet": 2,
    "lenhXuatChoXuLy": 5,
    "tongThietBiTrongKho": 500
  }
}
```

---

## 3. Dashboard dành cho Kỹ thuật viên
- **Method:** `GET`
- **Endpoint:** `/api/dashboard/ktv`
- **Mô tả:** Lấy dữ liệu thống kê cá nhân cho KTV (số lịch bảo trì hôm nay, số lịch chưa hoàn thành, thiết bị cần bàn giao).
- **Quyền hạn (Role):** Kỹ thuật viên

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "lichBaoTriHomNay": 3,
    "lichBanGiaoChuaXuLy": 1,
    "tongSoGioLamViec": 40
  }
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/dashboard` | Thống kê tổng quan | Admin |
| 2 | GET | `/api/dashboard/thu-kho` | Thống kê theo Kho | Thủ Kho |
| 3 | GET | `/api/dashboard/ktv` | Thống kê việc KTV | Kỹ Thuật Viên |
