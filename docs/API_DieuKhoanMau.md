# Điều Khoản Mẫu Hợp Đồng - API Documentation

API này trả về danh sách các điều khoản mẫu (template) đã được thiết lập sẵn trong hệ thống để hiển thị lên giao diện Ký hợp đồng cho khách hàng đọc trước khi ký.

API này **có thể gọi công khai (Public)** hoặc truyền Token (tùy thuộc vào cấu hình bảo mật, tuy nhiên dữ liệu chỉ mang tính chất đọc).

---

## 📜 1. Lấy Toàn Bộ Điều Khoản Mẫu
Lấy danh sách các điều khoản được đánh số thứ tự dùng cho hợp đồng thuê.

- **URL:** `GET /api/dieu-khoan-mau`
- **Headers:** `Authorization: Bearer <token>` (Tùy chọn)
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "soDieu": 1,
              "tieuDe": "Mục đích thuê và thời hạn",
              "noiDung": "Bên A đồng ý cho Bên B thuê thiết bị..."
          },
          {
              "soDieu": 2,
              "tieuDe": "Giá thuê và phương thức thanh toán",
              "noiDung": "Giá thuê được tính theo tháng..."
          },
          {
              "soDieu": 3,
              "tieuDe": "Quyền và nghĩa vụ của Bên A",
              "noiDung": "Bên A có nghĩa vụ bàn giao thiết bị đúng hạn..."
          }
      ]
  }
  ```
