# Người Dùng (User Profile) - API Documentation

Các API trong nhóm này dùng để quản lý thông tin hồ sơ và bảo mật của chính người dùng đang đăng nhập (như việc thiết lập và đổi mã PIN để ký hợp đồng).

Tất cả các API dưới đây đều yêu cầu xác thực bằng JWT Token ở Header:
`Authorization: Bearer <token>`

---

## 🔐 1. Thiết Lập Mã PIN Lần Đầu
Sử dụng khi khách hàng mới đăng ký tài khoản và cần tạo mã PIN (gồm 6 chữ số) để sử dụng cho chức năng ký tên điện tử sau này. 
Chỉ gọi được nếu khách hàng **chưa** từng có mã PIN.

- **URL:** `POST /api/user/setup-pin`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "newPin": "123456"
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Thiết lập mã PIN thành công",
      "data": null
  }
  ```
- **Response Lỗi (400 Bad Request):** (Khi đã có PIN)
  ```json
  {
      "success": false,
      "message": "Mã PIN đã được thiết lập trước đó",
      "data": null
  }
  ```

---

## 🔑 2. Đổi Mã PIN
Sử dụng khi khách hàng muốn thay đổi mã PIN hiện tại sang mã PIN mới. Cần cung cấp mã PIN cũ để xác thực.

- **URL:** `PUT /api/user/change-pin`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "oldPin": "123456",
      "newPin": "654321"
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đổi mã PIN thành công",
      "data": null
  }
  ```
- **Response Lỗi (400 Bad Request):** (Sai mã PIN cũ)
  ```json
  {
      "success": false,
      "message": "Mã PIN hiện tại không chính xác",
      "data": null
  }
  ```
