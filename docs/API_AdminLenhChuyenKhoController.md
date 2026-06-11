# Nhóm Quản trị (AdminLenhChuyenKhoController)
*Quản lý, tạo và duyệt các lệnh luân chuyển thiết bị giữa các kho.*

---

## 1. Lấy danh sách lệnh chuyển kho
- **Method:** `GET`
- **Endpoint:** `/api/admin/lenh-chuyen-kho`
- **Mô tả:** Lấy danh sách lệnh chuyển kho với phân trang và lọc theo trạng thái, kho, người thực hiện.
- **Quyền hạn (Role):** Admin, Quản lý Kho

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| page | int | Không | Trang hiện tại (mặc định 0) |
| size | int | Không | Số lượng item / trang (mặc định 20) |
| trangThai | int | Không | Lọc theo ID trạng thái duyệt |
| khoId | int | Không | Lọc lệnh liên quan đến kho cụ thể |
| nguoiThucHienId| int | Không | Lọc theo nhân viên thực hiện |

### Response
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "maLenh": "LCK001",
        "tuKho": "Kho A",
        "denKho": "Kho B",
        "trangThai": 1
      }
    ],
    "totalPages": 2,
    "totalElements": 30,
    "currentPage": 0
  }
}
```

---

## 2. Xem chi tiết lệnh chuyển kho
- **Method:** `GET`
- **Endpoint:** `/api/admin/lenh-chuyen-kho/{id}`
- **Mô tả:** Lấy chi tiết thông tin lệnh và danh sách thiết bị cần luân chuyển.
- **Quyền hạn (Role):** Admin, Quản lý Kho

### Response
```json
{
  "success": true,
  "data": {
    "id": 1,
    "maLenh": "LCK001",
    "tuKho": "Kho A",
    "denKho": "Kho B",
    "chiTiet": [
      {
        "thietBiId": 101,
        "tenThietBi": "Máy X"
      }
    ]
  }
}
```

---

## 3. Tạo lệnh chuyển kho
- **Method:** `POST`
- **Endpoint:** `/api/admin/lenh-chuyen-kho`
- **Mô tả:** Tạo mới một lệnh yêu cầu xuất thiết bị từ kho này sang kho khác.
- **Quyền hạn (Role):** Admin, Quản lý Kho

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực (Người tạo lệnh) |

### Request Body
```json
{
  "tuKhoId": 1,
  "denKhoId": 2,
  "nguoiThucHienId": 5,
  "ghiChu": "Chuyển gấp",
  "chiTiet": [
    {
      "thietBiId": 101,
      "ghiChu": "Hàng dễ vỡ"
    }
  ]
}
```

### Response
```json
{
  "success": true,
  "message": "Tạo lệnh chuyển kho thành công",
  "data": { "id": 2 }
}
```

---

## 4. Duyệt lệnh chuyển kho
- **Method:** `PUT`
- **Endpoint:** `/api/admin/lenh-chuyen-kho/{id}/duyet`
- **Mô tả:** Duyệt thay đổi trạng thái của lệnh chuyển kho (Đang vận chuyển, Hoàn tất, Hủy).
- **Quyền hạn (Role):** Admin, Quản lý Kho

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực của người duyệt |

### Request Body
```json
{
  "trangThai": 2,
  "ghiChu": "Bắt đầu vận chuyển"
}
```

### Response
```json
{
  "success": true,
  "message": "Cập nhật lệnh chuyển kho thành công"
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/admin/lenh-chuyen-kho` | Lấy danh sách lệnh chuyển kho | Admin |
| 2 | GET | `/api/admin/lenh-chuyen-kho/{id}` | Lấy chi tiết lệnh chuyển | Admin |
| 3 | POST | `/api/admin/lenh-chuyen-kho` | Tạo lệnh chuyển kho mới | Admin |
| 4 | PUT | `/api/admin/lenh-chuyen-kho/{id}/duyet`| Duyệt/Cập nhật lệnh chuyển | Admin |
