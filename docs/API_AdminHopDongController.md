# Nhóm Quản trị (AdminHopDongController)
*Quản lý hợp đồng, duyệt trạng thái và xem chữ ký hợp đồng của người dùng cho Admin.*

---

## 1. Lấy danh sách hợp đồng
- **Method:** `GET`
- **Endpoint:** `/api/admin/hop-dong`
- **Mô tả:** Lấy danh sách hợp đồng có phân trang và bộ lọc nâng cao.
- **Quyền hạn (Role):** Admin

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| page | int | Không | Trang hiện tại (mặc định 0) |
| size | int | Không | Số lượng item / trang (mặc định 15) |
| q | string | Không | Từ khóa tìm kiếm |
| startDate | string | Không | Lọc từ ngày (ISO format) |
| endDate | string | Không | Lọc đến ngày (ISO format) |
| trangThaiId | int | Không | Lọc theo trạng thái hợp đồng |
| loaiHopDongId | int | Không | Lọc theo loại hợp đồng |

### Response
```json
{
  "status": 200,
  "message": "Lấy danh sách thành công",
  "data": {
    "content": [
      {
        "id": 1,
        "maHopDong": "HD001",
        "tenKhachHang": "Nguyen Van A"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết hợp đồng
- **Method:** `GET`
- **Endpoint:** `/api/admin/hop-dong/{id}`
- **Mô tả:** Lấy thông tin chi tiết một hợp đồng.
- **Quyền hạn (Role):** Admin

### Response
```json
{
  "status": 200,
  "message": "Lấy chi tiết thành công",
  "data": {
    "id": 1,
    "maHopDong": "HD001",
    "chiTiet": []
  }
}
```

---

## 3. Cập nhật trạng thái hợp đồng
- **Method:** `PUT`
- **Endpoint:** `/api/admin/hop-dong/{id}/trang-thai`
- **Mô tả:** Cập nhật trạng thái hợp đồng (duyệt, hủy...).
- **Quyền hạn (Role):** Admin

### Request Body
```json
{
  "trangThaiId": 3,
  "lyDoHuy": "Thiếu thiết bị"
}
```

### Response
```json
{
  "status": 200,
  "message": "Cập nhật trạng thái thành công",
  "data": null
}
```

---

## 4. Lấy chữ ký điện tử khách hàng
- **Method:** `GET`
- **Endpoint:** `/api/admin/hop-dong/{id}/chu-ky`
- **Mô tả:** Lấy thông tin chữ ký xác nhận hợp đồng.
- **Quyền hạn (Role):** Admin

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực của Admin |

### Response
```json
{
  "status": 200,
  "message": "Lấy chữ ký thành công",
  "data": {
    "urlChuKy": "http://img.domain/signature.png",
    "ngayKy": "2024-01-01"
  }
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/admin/hop-dong` | Lấy danh sách hợp đồng | Admin |
| 2 | GET | `/api/admin/hop-dong/{id}` | Lấy chi tiết hợp đồng | Admin |
| 3 | PUT | `/api/admin/hop-dong/{id}/trang-thai` | Cập nhật trạng thái | Admin |
| 4 | GET | `/api/admin/hop-dong/{id}/chu-ky` | Xem chữ ký hợp đồng | Admin |
