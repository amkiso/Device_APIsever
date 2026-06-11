# Nhóm Kho hàng (KhoController)
*Quản lý danh sách các kho hàng trong hệ thống.*

---

## 1. Lấy danh sách kho
- **Method:** `GET`
- **Endpoint:** `/api/kho`
- **Mô tả:** Lấy toàn bộ danh sách kho hàng.
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "khoId": 1,
      "tenKho": "Kho Bình Dương",
      "diaChi": "123 QL1K, Bình Dương"
    }
  ]
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/kho` | Lấy danh sách kho | Mọi Role |
| 2 | POST | `/api/kho` | Thêm kho mới | Admin |
| 3 | PUT | `/api/kho/{id}` | Cập nhật kho | Admin |
| 4 | DELETE | `/api/kho/{id}` | Xóa kho | Admin |
