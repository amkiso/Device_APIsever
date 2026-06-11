# Nhóm Nhà Cung Cấp (NhaCungCapController)
*Quản lý thông tin công khai về các đối tác cung cấp thiết bị.*

---

## 1. Lấy danh sách nhà cung cấp
- **Method:** `GET`
- **Endpoint:** `/api/nha-cung-cap`
- **Mô tả:** Lấy danh sách tất cả nhà cung cấp thiết bị (chỉ lấy thông tin cơ bản).
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "nhaCungCapId": 1,
      "tenNhaCungCap": "Công ty TNHH A",
      "nguoiLienHe": "Nguyễn Văn A",
      "soDienThoai": "0123456789",
      "diaChi": "Hà Nội"
    }
  ]
}
```

---

## 2. Lấy chi tiết nhà cung cấp
- **Method:** `GET`
- **Endpoint:** `/api/nha-cung-cap/{id}`
- **Mô tả:** Xem thông tin chi tiết một nhà cung cấp dựa vào ID.
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "nhaCungCapId": 1,
    "tenNhaCungCap": "Công ty TNHH A",
    "nguoiLienHe": "Nguyễn Văn A",
    "soDienThoai": "0123456789",
    "diaChi": "Hà Nội"
  }
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/nha-cung-cap` | Lấy danh sách NCC | Mọi Role |
| 2 | GET | `/api/nha-cung-cap/{id}` | Lấy chi tiết một NCC | Mọi Role |
