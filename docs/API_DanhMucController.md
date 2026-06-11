# Nhóm Danh Mục (DanhMucController)
*Quản lý danh mục (Category) của thiết bị.*

---

## 1. Lấy tất cả danh mục
- **Method:** `GET`
- **Endpoint:** `/api/danh-muc`
- **Mô tả:** Trả về tất cả danh mục thiết bị trong hệ thống.
- **Quyền hạn (Role):** Mọi tài khoản

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "danhMucId": 1,
      "tenDanhMuc": "Thiết bị Xây dựng"
    }
  ]
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/danh-muc` | Lấy tất cả danh mục | Public |
| 2 | GET | `/api/danh-muc/{id}` | Lấy chi tiết danh mục | Public |
| 3 | POST | `/api/danh-muc` | Tạo danh mục mới | Admin |
| 4 | PUT | `/api/danh-muc/{id}` | Cập nhật danh mục | Admin |
| 5 | DELETE | `/api/danh-muc/{id}` | Xóa danh mục | Admin |
