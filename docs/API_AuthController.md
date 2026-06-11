# Nhóm Tài khoản (AuthController)
*Quản lý các chức năng xác thực, đăng nhập, đăng ký và quản lý tài khoản cho tất cả các vai trò.*

---

## 1. Đăng nhập hệ thống
- **Method:** `POST`
- **Endpoint:** `/api/auth/login`
- **Mô tả:** API dùng để xác thực người dùng và lấy token (dành cho cả Khách hàng và Nhân viên).
- **Quyền hạn (Role):** Public

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |

### Request Body
```json
{
  "taiKhoan": "admin",
  "matKhau": "password123"
}
```

### Response

**Thành công (200 OK)**
```json
{
  "status": 200,
  "message": "Dang nhap thanh cong!",
  "data": {
    "token": "eyJhbGciOiJIUzI...",
    "nguoiDungId": 1,
    "hoTen": "Nguyen Van A",
    "maNguoiDung": "NV001",
    "vaiTroId": 1,
    "tenVaiTro": "ADMIN",
    "khoId": null,
    "doiMatKhauLanDau": false,
    "avt": "avatar.jpg",
    "hasPin": true
  }
}
```

**Thất bại (400 Bad Request / 401 Unauthorized)**
```json
{
  "status": 401,
  "message": "Sai tài khoản hoặc mật khẩu"
}
```

---

## 2. Khởi tạo đăng ký (Bước 1)
- **Method:** `POST`
- **Endpoint:** `/api/auth/register-init`
- **Mô tả:** Gửi thông tin đăng ký, hệ thống sẽ sinh mã OTP gửi về Email.
- **Quyền hạn (Role):** Public

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |

### Request Body
```json
{
  "hoTen": "Nguyen Van A",
  "email": "nguyenvana@gmail.com",
  "matKhau": "password123",
  "soDienThoai": "0987654321",
  "diaChi": "123 Đường A, Quận B, TP C",
  "maSoThue": "0123456789"
}
```

### Response
```json
{
  "status": 200,
  "message": "Vui long kiem tra email de lay ma OTP",
  "data": null
}
```

---

## 3. Xác nhận đăng ký (Bước 2)
- **Method:** `POST`
- **Endpoint:** `/api/auth/register-confirm`
- **Mô tả:** Nhận OTP từ Email để xác nhận và lưu thông tin vào cơ sở dữ liệu.
- **Quyền hạn (Role):** Public

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |

### Request Body
```json
{
  "email": "nguyenvana@gmail.com",
  "otp": "123456"
}
```

### Response
```json
{
  "status": 200,
  "message": "Dang ky thanh cong!",
  "data": {
    "token": "eyJhbGciOiJI...",
    "nguoiDungId": 2,
    "hoTen": "Nguyen Van A",
    "maNguoiDung": "KH001",
    "vaiTroId": 4,
    "tenVaiTro": "KHACH_HANG",
    "khoId": null,
    "doiMatKhauLanDau": false,
    "avt": null,
    "hasPin": false
  }
}
```

---

## 4. Khởi tạo quên mật khẩu (Bước 1)
- **Method:** `POST`
- **Endpoint:** `/api/auth/forgot-password-init`
- **Mô tả:** Gửi yêu cầu quên mật khẩu, hệ thống sinh mã OTP gửi qua Email.
- **Quyền hạn (Role):** Public

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |

### Request Body
```json
{
  "email": "nguyenvana@gmail.com"
}
```

### Response
```json
{
  "status": 200,
  "message": "Vui long kiem tra email de lay ma OTP",
  "data": null
}
```

---

## 5. Xác nhận quên mật khẩu (Bước 2)
- **Method:** `POST`
- **Endpoint:** `/api/auth/forgot-password-confirm`
- **Mô tả:** Nhập mã OTP và đổi mật khẩu mới.
- **Quyền hạn (Role):** Public

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |

### Request Body
```json
{
  "email": "nguyenvana@gmail.com",
  "otp": "123456",
  "newPassword": "newpassword123"
}
```

### Response
```json
{
  "status": 200,
  "message": "Doi mat khau thanh cong! Ban co the dang nhap bang mat khau moi.",
  "data": null
}
```

---

## 6. Kiểm tra Email Reset Mật Khẩu
- **Method:** `POST`
- **Endpoint:** `/api/auth/check-email-reset`
- **Mô tả:** Kiểm tra email. Nếu là khách hàng, cho phép tự reset. Nếu là nhân viên, trả về SĐT admin để liên hệ.
- **Quyền hạn (Role):** Public

### Request Body
```json
{
  "email": "nhanvien@gmail.com"
}
```

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "adminPhone": "0123456789",
    "adminName": "Admin He Thong"
  }
}
```

---

## 7. Đổi mật khẩu
- **Method:** `POST`
- **Endpoint:** `/api/auth/doi-mat-khau`
- **Mô tả:** Đổi mật khẩu cho tài khoản đang đăng nhập (áp dụng cho tất cả vai trò).
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |
| Authorization | Bearer <token> | Có | Token xác thực đăng nhập |

### Request Body
```json
{
  "matKhauCu": "oldpassword123",
  "matKhauMoi": "newpassword123"
}
```

### Response
```json
{
  "status": 200,
  "message": "Doi mat khau thanh cong!",
  "data": null
}
```

---

## 8. Lấy thông tin cá nhân (Profile)
- **Method:** `GET`
- **Endpoint:** `/api/auth/me`
- **Mô tả:** Lấy thông tin người dùng đang đăng nhập.
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực đăng nhập |

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "taiKhoan": "admin",
    "hoTen": "Nguyen Van A",
    "email": "admin@gmail.com",
    "soDienThoai": "0987654321",
    "maNguoiDung": "NV001"
    // Các thông tin khác...
  }
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
*Bảng tóm tắt nhanh các API có trong AuthController.*

| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | POST | `/api/auth/login` | Đăng nhập hệ thống | Public |
| 2 | POST | `/api/auth/register-init` | Khởi tạo đăng ký (Bước 1) | Public |
| 3 | POST | `/api/auth/register-confirm` | Xác nhận đăng ký (Bước 2) | Public |
| 4 | POST | `/api/auth/forgot-password-init` | Khởi tạo quên mật khẩu (Bước 1) | Public |
| 5 | POST | `/api/auth/forgot-password-confirm` | Xác nhận quên mật khẩu (Bước 2) | Public |
| 6 | POST | `/api/auth/check-email-reset` | Kiểm tra quyền reset pass qua email | Public |
| 7 | POST | `/api/auth/doi-mat-khau` | Đổi mật khẩu | Mọi Role |
| 8 | GET | `/api/auth/me` | Lấy thông tin cá nhân | Mọi Role |
