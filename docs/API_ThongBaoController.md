# Nhóm Thông Báo (ThongBaoController)
*Hệ thống đẩy thông báo (Push Notification qua FCM) và quản lý hộp thư thông báo in-app.*

---

## 1. Tạo thông báo mới (Admin)
- **Method:** `POST`
- **Endpoint:** `/api/thong-bao`
- **Mô tả:** Admin chủ động tạo thông báo gửi đến người dùng.
- **Quyền hạn (Role):** Admin

### Request Body
```json
{
  "tieuDe": "Bảo trì hệ thống",
  "noiDung": "Hệ thống sẽ bảo trì từ 12h đêm nay.",
  "nguoiNhanId": 2, 
  "guiTatCa": false
}
```

### Response
```json
{
  "status": 200,
  "message": "Tao thong bao thanh cong!",
  "data": null
}
```

---

## 2. Đăng ký FCM Token
- **Method:** `POST`
- **Endpoint:** `/api/fcm/register-token`
- **Mô tả:** Mobile App / Web gọi API này sau khi user đăng nhập để đăng ký Device Token với Firebase Cloud Messaging.
- **Quyền hạn (Role):** Mọi tài khoản

### Request Body
```json
{
  "token": "dM123_abc...",
  "deviceName": "iPhone 15 Pro"
}
```

### Response
```json
{
  "status": 200,
  "message": "Dang ky FCM token thanh cong",
  "data": null
}
```

---

## 3. Lấy danh sách thông báo của tôi
- **Method:** `GET`
- **Endpoint:** `/api/thong-bao`
- **Mô tả:** Lấy danh sách thông báo gửi đến người dùng hiện tại.
- **Quyền hạn (Role):** Mọi tài khoản

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "thongBaoId": 1,
      "tieuDe": "Hợp đồng được duyệt",
      "noiDung": "Hợp đồng HD001 của bạn đã được duyệt.",
      "daDoc": false
    }
  ]
}
```

---

## 4. Đếm số thông báo chưa đọc
- **Method:** `GET`
- **Endpoint:** `/api/thong-bao/chua-doc`
- **Mô tả:** Trả về con số để hiển thị badge chuông thông báo.
- **Quyền hạn (Role):** Mọi tài khoản

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": 3
}
```

---

## 5. Đánh dấu đã đọc
- **Method:** `PUT`
- **Endpoint:** `/api/thong-bao/{id}/da-doc`
- **Mô tả:** Đánh dấu một thông báo cụ thể là đã đọc.
- **Quyền hạn (Role):** Mọi tài khoản

### Response
```json
{
  "status": 200,
  "message": "Da danh dau doc",
  "data": null
}
```

---

## 6. Đánh dấu đã đọc tất cả
- **Method:** `PUT`
- **Endpoint:** `/api/thong-bao/da-doc-tat-ca`
- **Mô tả:** Đánh dấu toàn bộ thông báo trong hộp thư là đã đọc.
- **Quyền hạn (Role):** Mọi tài khoản

### Response
```json
{
  "status": 200,
  "message": "Da danh dau doc tat ca",
  "data": null
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | POST | `/api/thong-bao` | Tạo thông báo mới | Admin |
| 2 | POST | `/api/fcm/register-token` | Đăng ký FCM Device Token | Mọi Role |
| 3 | GET | `/api/thong-bao` | Lấy hộp thư thông báo | Mọi Role |
| 4 | GET | `/api/thong-bao/chua-doc` | Đếm số thông báo chưa đọc | Mọi Role |
| 5 | PUT | `/api/thong-bao/{id}/da-doc` | Đánh dấu 1 thông báo đã đọc | Mọi Role |
| 6 | PUT | `/api/thong-bao/da-doc-tat-ca` | Đánh dấu tất cả là đã đọc | Mọi Role |
| 7 | GET | `/api/admin/thong-bao` | Danh sách thông báo (Quản lý) | Admin |
| 8 | DELETE| `/api/admin/thong-bao/{id}` | Xóa 1 thông báo | Admin |
