# Nhóm Quản trị (AdminNhaCungCapController)
*Quản lý danh sách đối tác / nhà cung cấp thiết bị.*

---

## 1. Lấy danh sách nhà cung cấp
- **Method:** `GET`
- **Endpoint:** `/api/admin/nha-cung-cap`
- **Mô tả:** Lấy toàn bộ danh sách nhà cung cấp (có thể tìm kiếm theo từ khóa).
- **Quyền hạn (Role):** Admin

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| keyword | string | Không | Từ khóa tìm kiếm tên NCC |

### Response
```json
{
  "success": true,
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

## 2. Xem chi tiết nhà cung cấp
- **Method:** `GET`
- **Endpoint:** `/api/admin/nha-cung-cap/{id}`
- **Mô tả:** Lấy thông tin chi tiết một nhà cung cấp.
- **Quyền hạn (Role):** Admin

### Response
```json
{
  "success": true,
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

## 3. Thêm mới nhà cung cấp
- **Method:** `POST`
- **Endpoint:** `/api/admin/nha-cung-cap`
- **Mô tả:** Thêm một đối tác/nhà cung cấp mới.
- **Quyền hạn (Role):** Admin

### Request Body
```json
{
  "tenNhaCungCap": "Công ty TNHH B",
  "nguoiLienHe": "Trần B",
  "soDienThoai": "0987654321",
  "diaChi": "TP.HCM"
}
```

### Response
```json
{
  "success": true,
  "message": "Thêm nhà cung cấp thành công",
  "data": {
    "nhaCungCapId": 2,
    "tenNhaCungCap": "Công ty TNHH B"
  }
}
```

---

## 4. Cập nhật nhà cung cấp
- **Method:** `PUT`
- **Endpoint:** `/api/admin/nha-cung-cap/{id}`
- **Mô tả:** Chỉnh sửa thông tin nhà cung cấp.
- **Quyền hạn (Role):** Admin

### Request Body
*(Tương tự Request Body khi Thêm mới)*

### Response
```json
{
  "success": true,
  "message": "Cập nhật nhà cung cấp thành công",
  "data": {
    "nhaCungCapId": 2,
    "tenNhaCungCap": "Công ty TNHH B"
  }
}
```

---

## 5. Xóa nhà cung cấp
- **Method:** `DELETE`
- **Endpoint:** `/api/admin/nha-cung-cap/{id}`
- **Mô tả:** Xóa nhà cung cấp (nếu không có ràng buộc).
- **Quyền hạn (Role):** Admin

### Response
```json
{
  "success": true,
  "message": "Xóa nhà cung cấp thành công"
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/admin/nha-cung-cap` | Lấy danh sách NCC | Admin |
| 2 | GET | `/api/admin/nha-cung-cap/{id}` | Lấy chi tiết NCC | Admin |
| 3 | POST | `/api/admin/nha-cung-cap` | Thêm NCC mới | Admin |
| 4 | PUT | `/api/admin/nha-cung-cap/{id}` | Cập nhật NCC | Admin |
| 5 | DELETE | `/api/admin/nha-cung-cap/{id}` | Xóa NCC | Admin |
