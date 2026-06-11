# Nhóm Địa Chỉ (DiaChiController)
*Quản lý danh bạ địa chỉ nhận hàng/bảo trì của khách hàng.*

---

## 1. Lấy danh sách địa chỉ
- **Method:** `GET`
- **Endpoint:** `/api/dia-chi`
- **Mô tả:** Lấy danh sách các địa chỉ đã lưu của tài khoản hiện tại.
- **Quyền hạn (Role):** Khách hàng

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "tenNguoiNhan": "Nguyen Van A",
      "soDienThoai": "0987654321",
      "tinhThanhPho": "Hà Nội",
      "phuongXa": "Quận Cầu Giấy",
      "diaChiChiTiet": "Số nhà 123",
      "donVi": "Công ty TNHH A"
    }
  ]
}
```

---

## 2. Thêm địa chỉ mới
- **Method:** `POST`
- **Endpoint:** `/api/dia-chi`
- **Mô tả:** Lưu một địa chỉ nhận hàng mới.
- **Quyền hạn (Role):** Khách hàng

### Request Body
```json
{
  "tenNguoiNhan": "Nguyen Van B",
  "soDienThoai": "0123456789",
  "tinhThanhPho": "TP.HCM",
  "phuongXa": "Quận 1",
  "diaChiChiTiet": "Tòa nhà X",
  "donVi": "Cá nhân"
}
```

### Response
```json
{
  "status": 200,
  "message": "Tạo địa chỉ thành công",
  "data": {
    "id": 2
  }
}
```

---

## 3. Cập nhật địa chỉ
- **Method:** `PUT`
- **Endpoint:** `/api/dia-chi/{id}`
- **Mô tả:** Sửa thông tin của một địa chỉ.
- **Quyền hạn (Role):** Khách hàng

### Request Body
*(Giống API thêm địa chỉ)*

### Response
```json
{
  "status": 200,
  "message": "Cập nhật địa chỉ thành công",
  "data": null
}
```

---

## 4. Xóa địa chỉ
- **Method:** `DELETE`
- **Endpoint:** `/api/dia-chi/{id}`
- **Mô tả:** Xóa một địa chỉ khỏi danh bạ.
- **Quyền hạn (Role):** Khách hàng

### Response
```json
{
  "status": 200,
  "message": "Đã xóa địa chỉ",
  "data": null
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/dia-chi` | Lấy danh sách địa chỉ | Khách hàng |
| 2 | POST | `/api/dia-chi` | Tạo địa chỉ mới | Khách hàng |
| 3 | PUT | `/api/dia-chi/{id}` | Cập nhật địa chỉ | Khách hàng |
| 4 | DELETE | `/api/dia-chi/{id}` | Xóa địa chỉ | Khách hàng |
