# Nhóm Bảo Trì (BaoTriController)
*Quản lý lịch bảo trì, báo cáo sự cố và hoàn thành bảo trì.*

---

## 1. Lấy danh sách bảo trì
- **Method:** `GET`
- **Endpoint:** `/api/bao-tri`
- **Mô tả:** Lấy danh sách các yêu cầu bảo trì, có phân trang và lọc theo trạng thái/người dùng/keyword.
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| page | int | Không | Trang hiện tại (mặc định 0) |
| size | int | Không | Số lượng item (mặc định 10) |
| trangThaiId| int | Không | Lọc theo trạng thái bảo trì |
| nguoiDungId| int | Không | Lọc theo ID người yêu cầu / kỹ thuật |
| keyword | string| Không | Từ khóa tìm kiếm |

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "content": [],
    "totalPages": 1
  }
}
```

---

## 2. Xem chi tiết bảo trì
- **Method:** `GET`
- **Endpoint:** `/api/bao-tri/{id}`
- **Mô tả:** Xem thông tin chi tiết một yêu cầu bảo trì cụ thể.
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "baoTriId": 1,
    "chiTiet": []
  }
}
```

---

## 3. Tạo yêu cầu bảo trì mới
- **Method:** `POST`
- **Endpoint:** `/api/bao-tri`
- **Mô tả:** Người dùng gửi một yêu cầu bảo trì thiết bị mới.
- **Quyền hạn (Role):** Khách hàng / Kỹ thuật

### Request Body
```json
{
  "thietBiId": 1,
  "moTaLoi": "Thiết bị không lên nguồn"
}
```

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": null
}
```

---

## 4. Xác nhận yêu cầu bảo trì
- **Method:** `PUT`
- **Endpoint:** `/api/bao-tri/{id}/xac-nhan`
- **Mô tả:** Nhân viên kỹ thuật xác nhận đã tiếp nhận yêu cầu bảo trì.
- **Quyền hạn (Role):** Kỹ thuật viên

### Response
```json
{
  "status": 200,
  "message": "Xác nhận yêu cầu thành công",
  "data": null
}
```

---

## 5. Ghi nhận sự cố
- **Method:** `POST`
- **Endpoint:** `/api/bao-tri/{id}/ghi-nhan-su-co`
- **Mô tả:** Kỹ thuật viên kiểm tra và ghi chú lại sự cố của thiết bị.
- **Quyền hạn (Role):** Kỹ thuật viên

### Request Body
```json
{
  "moTaChiTiet": "Cháy bo mạch nguồn",
  "huongGiaiQuyet": "Thay bo mới"
}
```

### Response
```json
{
  "status": 200,
  "message": "Ghi nhận sự cố thành công",
  "data": null
}
```

---

## 6. Hoàn thành bảo trì
- **Method:** `POST`
- **Endpoint:** `/api/bao-tri/{id}/hoan-thanh`
- **Mô tả:** Kỹ thuật viên cập nhật trạng thái bảo trì hoàn tất.
- **Quyền hạn (Role):** Kỹ thuật viên

### Request Body
```json
{
  "ketQua": "Đã sửa xong hoạt động tốt"
}
```

### Response
```json
{
  "status": 200,
  "message": "Hoàn thành bảo trì thành công",
  "data": null
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/bao-tri` | Danh sách bảo trì | User/Admin |
| 2 | GET | `/api/bao-tri/{id}` | Chi tiết bảo trì | User/Admin |
| 3 | GET | `/api/bao-tri/thong-ke` | Thống kê bảo trì | Admin |
| 4 | POST | `/api/bao-tri` | Tạo yêu cầu bảo trì | User/KT |
| 5 | PUT | `/api/bao-tri/{id}/xac-nhan` | Xác nhận yêu cầu | Kỹ thuật |
| 6 | POST | `/api/bao-tri/{id}/ghi-nhan-su-co`| Ghi nhận sự cố | Kỹ thuật |
| 7 | POST | `/api/bao-tri/{id}/hoan-thanh` | Hoàn tất bảo trì | Kỹ thuật |
