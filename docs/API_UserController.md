# Nhóm Người dùng (UserController)
*Quản lý các chức năng liên quan đến tài khoản cá nhân nâng cao (ví dụ: Mã PIN bảo mật).*

---

## 1. Thiết lập mã PIN
- **Method:** `POST`
- **Endpoint:** `/api/user/setup-pin`
- **Mô tả:** Thiết lập mã PIN lần đầu cho người dùng. Nếu người dùng đã có mã PIN, API sẽ trả về lỗi.
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |
| Authorization | Bearer <token> | Có | Token xác thực đăng nhập |

### Request Body
```json
{
  "newPin": "123456"
}
```

### Response

**Thành công (200 OK)**
```json
{
  "status": 200,
  "message": "Thiết lập mã PIN thành công",
  "data": null
}
```

**Thất bại (400 Bad Request)**
```json
{
  "status": 400,
  "message": "Mã PIN đã được thiết lập trước đó",
  "data": null
}
```

---

## 2. Thay đổi mã PIN
- **Method:** `PUT`
- **Endpoint:** `/api/user/change-pin`
- **Mô tả:** Đổi mã PIN đang sử dụng sang mã PIN mới. Cần cung cấp mã PIN cũ để xác nhận.
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |
| Authorization | Bearer <token> | Có | Token xác thực đăng nhập |

### Request Body
```json
{
  "oldPin": "123456",
  "newPin": "654321"
}
```

### Response

**Thành công (200 OK)**
```json
{
  "status": 200,
  "message": "Đổi mã PIN thành công",
  "data": null
}
```

**Thất bại (400 Bad Request)**
```json
{
  "status": 400,
  "message": "Mã PIN hiện tại không chính xác",
  "data": null
}
```

---

## 3. Xác thực mã PIN
- **Method:** `POST`
- **Endpoint:** `/api/user/verify-pin`
- **Mô tả:** Xác nhận xem mã PIN người dùng nhập có đúng với mã PIN hiện tại hay không (Dùng trước khi thực hiện các giao dịch nhạy cảm).
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Content-Type | application/json | Có | Định dạng dữ liệu |
| Authorization | Bearer <token> | Có | Token xác thực đăng nhập |

### Request Body
```json
{
  "pin": "123456"
}
```

### Response

**Thành công (200 OK)**
```json
{
  "status": 200,
  "message": "Mã PIN hợp lệ",
  "data": null
}
```

**Thất bại (400 Bad Request)**
```json
{
  "status": 400,
  "message": "Mã PIN không chính xác",
  "data": null
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
*Bảng tóm tắt nhanh các API có trong UserController.*

| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | POST | `/api/user/setup-pin` | Thiết lập mã PIN lần đầu | Mọi Role |
| 2 | PUT | `/api/user/change-pin` | Đổi mã PIN | Mọi Role |
| 3 | POST | `/api/user/verify-pin` | Xác thực mã PIN | Mọi Role |
