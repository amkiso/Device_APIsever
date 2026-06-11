# Nhóm Loại Thiết Bị (LoaiThietBiController)
*Quản lý danh sách các loại thiết bị, phân loại sản phẩm.*

---

## 1. Lấy danh sách Loại thiết bị
- **Method:** `GET`
- **Endpoint:** `/api/loai-thiet-bi`
- **Mô tả:** Lấy toàn bộ loại thiết bị (có thể lọc theo danh mục).
- **Quyền hạn (Role):** Mọi tài khoản

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| danhMucId | int | Không | ID danh mục thiết bị |

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "loaiThietBiId": 1,
      "tenLoaiThietBi": "Máy Xúc",
      "danhMucId": 2
    }
  ]
}
```

---

## 2. Tìm kiếm loại thiết bị
- **Method:** `GET`
- **Endpoint:** `/api/loai-thiet-bi/search`
- **Mô tả:** Tìm kiếm loại thiết bị theo tên.

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| q | string | Có | Từ khóa tìm kiếm |

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": []
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/loai-thiet-bi` | Lấy danh sách loại thiết bị | Public |
| 2 | GET | `/api/loai-thiet-bi/{id}` | Lấy chi tiết | Public |
| 3 | GET | `/api/loai-thiet-bi/search` | Tìm kiếm | Public |
| 4 | POST | `/api/loai-thiet-bi` | Thêm mới loại thiết bị | Admin |
| 5 | PUT | `/api/loai-thiet-bi/{id}` | Cập nhật | Admin |
| 6 | DELETE | `/api/loai-thiet-bi/{id}` | Xóa loại thiết bị | Admin |
