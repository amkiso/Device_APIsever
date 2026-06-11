# Nhóm Điều Khoản Mẫu (DieuKhoanMauController)
*Lấy thông tin các điều khoản mẫu để hiển thị khi người dùng tạo/ký hợp đồng.*

---

## 1. Lấy tất cả điều khoản mẫu
- **Method:** `GET`
- **Endpoint:** `/api/dieu-khoan-mau`
- **Mô tả:** Trả về danh sách nội dung các điều khoản, chính sách pháp lý để hiển thị cho Khách hàng đọc trước khi ký hợp đồng.
- **Quyền hạn (Role):** Mọi tài khoản

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "tieuDe": "Điều khoản đền bù",
      "noiDung": "Khách hàng phải bồi thường 100% giá trị thiết bị nếu làm hỏng."
    },
    {
      "id": 2,
      "tieuDe": "Trách nhiệm giao nhận",
      "noiDung": "Bên A có trách nhiệm giao hàng đúng hạn..."
    }
  ]
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/dieu-khoan-mau` | Lấy danh sách các điều khoản mẫu | Public |
