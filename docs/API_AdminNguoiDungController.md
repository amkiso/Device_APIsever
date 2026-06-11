# Nhóm Quản trị (AdminNguoiDungController)
*Quản lý danh sách người dùng, nhân viên, cấp quyền và thao tác trạng thái tài khoản cho Admin.*

---

## 1. Lấy danh sách người dùng
- **Method:** `GET`
- **Endpoint:** `/api/admin/nguoi-dung`
- **Mô tả:** Lấy danh sách người dùng có phân trang và lọc theo nhiều tiêu chí.
- **Quyền hạn (Role):** Admin

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| page | int | Không | Trang hiện tại (mặc định 0) |
| size | int | Không | Số lượng item / trang (mặc định 20) |
| vaiTroId | int | Không | Lọc theo ID vai trò |
| trangThaiId | int | Không | Lọc theo ID trạng thái |
| loaiKhachHangId| int | Không | Lọc theo loại khách hàng |
| keyword | string| Không | Tìm kiếm theo tên, email, sđt |

### Response
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "hoTen": "Nguyen Van A",
        "email": "a@gmail.com"
      }
    ],
    "totalPages": 5,
    "totalElements": 100,
    "currentPage": 0
  }
}
```

---

## 2. Xem chi tiết người dùng
- **Method:** `GET`
- **Endpoint:** `/api/admin/nguoi-dung/{id}`
- **Mô tả:** Xem chi tiết thông tin của một người dùng theo ID.
- **Quyền hạn (Role):** Admin

### Response
```json
{
  "success": true,
  "data": {
    "id": 1,
    "hoTen": "Nguyen Van A",
    "email": "a@gmail.com",
    "soDienThoai": "0987654321",
    "diaChi": "123 Đường A"
  }
}
```

---

## 3. Xem thông tin nhạy cảm (Cần mã PIN)
- **Method:** `POST`
- **Endpoint:** `/api/admin/nguoi-dung/{id}/thong-tin-nhan-cam`
- **Mô tả:** Admin cần nhập mã PIN để xem các thông tin nhạy cảm (như CCCD, Địa chỉ chi tiết) của người dùng.
- **Quyền hạn (Role):** Admin

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực của Admin |

### Request Body
```json
{
  "maPin": "123456"
}
```

### Response
```json
{
  "success": true,
  "data": {
    "cccd": "012345678901",
    "cccdNgayCap": "01/01/2020",
    "cccdNoiCap": "Cục Cảnh sát"
  }
}
```

---

## 4. Thêm mới người dùng
- **Method:** `POST`
- **Endpoint:** `/api/admin/nguoi-dung`
- **Mô tả:** Admin chủ động tạo mới tài khoản người dùng hoặc nhân viên.
- **Quyền hạn (Role):** Admin

### Request Body
```json
{
  "hoTen": "Nguyen Van B",
  "taiKhoan": "nguyenvanb",
  "matKhau": "password",
  "vaiTroId": 4,
  "khoId": null,
  "soDienThoai": "0123456789",
  "email": "b@gmail.com",
  "diaChi": "HN",
  "loaiKhachHangId": 1,
  "maSoThue": "",
  "cccd": "123456789012",
  "cccdNgayCap": "01/01/2020",
  "cccdNoiCap": "Cục CS",
  "donViCongTac": "Cty A"
}
```

### Response
```json
{
  "success": true,
  "message": "Thêm người dùng thành công",
  "data": { "id": 2, "hoTen": "Nguyen Van B" }
}
```

---

## 5. Cập nhật thông tin người dùng
- **Method:** `PUT`
- **Endpoint:** `/api/admin/nguoi-dung/{id}`
- **Mô tả:** Cập nhật thông tin tài khoản người dùng.
- **Quyền hạn (Role):** Admin

### Request Body
*(Tương tự API Thêm mới)*

### Response
```json
{
  "success": true,
  "message": "Cập nhật người dùng thành công",
  "data": { "id": 2, "hoTen": "Nguyen Van B Edited" }
}
```

---

## 6. Cập nhật trạng thái người dùng
- **Method:** `PUT`
- **Endpoint:** `/api/admin/nguoi-dung/{id}/trang-thai`
- **Mô tả:** Thay đổi trạng thái tài khoản (VD: Khóa, Mở khóa).
- **Quyền hạn (Role):** Admin

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực của Admin |

### Request Body
```json
{
  "trangThaiId": 2
}
```

### Response
```json
{
  "success": true,
  "message": "Cập nhật trạng thái thành công"
}
```

---

## 7. Xóa người dùng
- **Method:** `DELETE`
- **Endpoint:** `/api/admin/nguoi-dung/{id}`
- **Mô tả:** Xóa một tài khoản người dùng (nếu đủ điều kiện).
- **Quyền hạn (Role):** Admin

### Response
```json
{
  "success": true,
  "message": "Xóa người dùng thành công"
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/admin/nguoi-dung` | Lấy danh sách người dùng | Admin |
| 2 | GET | `/api/admin/nguoi-dung/{id}` | Xem chi tiết người dùng | Admin |
| 3 | POST | `/api/admin/nguoi-dung/{id}/thong-tin-nhan-cam` | Xem thông tin nhạy cảm (Cần PIN) | Admin |
| 4 | POST | `/api/admin/nguoi-dung` | Thêm người dùng mới | Admin |
| 5 | PUT | `/api/admin/nguoi-dung/{id}` | Cập nhật thông tin người dùng | Admin |
| 6 | PUT | `/api/admin/nguoi-dung/{id}/trang-thai` | Cập nhật trạng thái tài khoản | Admin |
| 7 | DELETE | `/api/admin/nguoi-dung/{id}` | Xóa người dùng | Admin |
