# Nhóm Chữ Ký (SignatureController)
*Quản lý việc tạo link bảo mật (SAS Token) để Upload và Đọc hình ảnh chữ ký điện tử.*

---

## 1. Lấy URL Upload Chữ Ký (SAS Token)
- **Method:** `GET`
- **Endpoint:** `/api/signatures/contract/{hopDongId}/get-upload-url`
- **Mô tả:** Lấy đường dẫn an toàn (SAS URL) để client tải ảnh chữ ký trực tiếp lên Azure Blob Storage. Chỉ hỗ trợ file `.png` hoặc `.jpg`.
- **Quyền hạn (Role):** Admin, Khách hàng sở hữu hợp đồng

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| extension | string | Không | Đuôi file (mặc định "png") |

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực đăng nhập |

### Response
```json
{
  "status": 200,
  "message": "Tao SAS URL upload chu ky thanh cong.",
  "data": {
    "sasUrl": "https://account.blob.../sign/file.png?sig=...",
    "fileName": "file_uuid.png",
    "publicUrl": null,
    "category": "sign"
  }
}
```

---

## 2. Lấy URL Xem Chữ Ký (SAS Token)
- **Method:** `GET`
- **Endpoint:** `/api/signatures/contract/{hopDongId}/read-url`
- **Mô tả:** Chữ ký lưu trên Private Bucket nên cần cấp một đường link tạm thời (có giới hạn thời gian) để xem.
- **Quyền hạn (Role):** Admin, Khách hàng sở hữu hợp đồng

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| fileName | string | Có | Tên file đã upload |

### Response
```json
{
  "status": 200,
  "message": "Tao SAS URL doc chu ky thanh cong.",
  "data": {
    "readUrl": "https://account.blob.../sign/file.png?sig=..."
  }
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/signatures/contract/{hopDongId}/get-upload-url` | Lấy URL (SAS) upload ảnh chữ ký | Admin/User |
| 2 | GET | `/api/signatures/contract/{hopDongId}/read-url` | Lấy URL (SAS) xem ảnh chữ ký | Admin/User |
