# Nhóm Hình Ảnh (ImageController)
*Quản lý upload hình ảnh sử dụng Azure Blob Storage SAS Token.*

Hệ thống lưu trữ ảnh chia theo 3 container: `user`, `products`, và `work`.
Quy trình: 
1. Client gọi GET lấy SAS URL
2. Client PUT file ảnh lên SAS URL trực tiếp
3. Client POST về Server để lưu đường dẫn ảnh.

---

## 1. Lấy SAS Upload URL
- **Method:** `GET`
- **Endpoint:** `/api/images/{category}/get-upload-url`
- **Mô tả:** Lấy URL cho phép client tự upload ảnh lên Azure. `{category}` có thể là: `user`, `products`, `work`.
- **Quyền hạn (Role):** Mọi tài khoản đã đăng nhập

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| originalName | string | Không | Tên file gốc |
| extension | string | Không | Đuôi file (mặc định jpg) |
| contentType | string | Không | Kiểu nội dung (mặc định image/jpeg) |

### Response
```json
{
  "status": 200,
  "message": "Tao SAS URL thanh cong...",
  "data": {
    "sasUrl": "https://account.blob.../container/file.jpg?sig=...",
    "fileName": "file_uuid.jpg",
    "publicUrl": "https://account.blob.../container/file.jpg",
    "category": "user"
  }
}
```

---

## 2. Cập nhật ảnh đại diện người dùng
- **Method:** `POST`
- **Endpoint:** `/api/images/user/update-avatar`
- **Mô tả:** Sau khi upload ảnh thành công, gọi API này để hệ thống lưu Avatar mới.
- **Quyền hạn (Role):** Mọi tài khoản

### Request Body
```json
{
  "fileName": "file_uuid.jpg"
}
```

### Response
```json
{
  "status": 200,
  "message": "Cap nhat anh dai dien thanh cong!",
  "data": {
    "nguoiDungId": "1",
    "avatarUrl": "https://..."
  }
}
```

---

## 3. Cập nhật ảnh sản phẩm
- **Method:** `POST`
- **Endpoint:** `/api/images/products/thiet-bi/{thietBiId}/update-image`
- **Mô tả:** Lưu tên ảnh sản phẩm cho thiết bị cụ thể.
- **Quyền hạn (Role):** Admin

### Request Body
```json
{
  "fileName": "sanpham_img.jpg",
  "loaiAnhId": 1
}
```

---

## 4. Cập nhật ảnh nghiệp vụ
- **Method:** `POST`
- **Endpoint:** `/api/images/work/thiet-bi/{thietBiId}/update-image`
- **Mô tả:** Lưu tên ảnh công việc (khi bàn giao, bảo trì thiết bị). Bắt buộc phải có `banGiaoId` hoặc `baoTriId`.
- **Quyền hạn (Role):** NV Bàn Giao, NV Bảo Trì

### Request Body
```json
{
  "fileName": "nghiepvu_img.jpg",
  "banGiaoId": 5
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/images/{category}/get-upload-url` | Lấy link SAS Upload | Mọi Role |
| 2 | POST | `/api/images/user/update-avatar` | Cập nhật avatar | Mọi Role |
| 3 | POST | `/api/images/products/...` | Cập nhật ảnh sản phẩm | Admin |
| 4 | POST | `/api/images/work/...` | Cập nhật ảnh nghiệp vụ | NV |
| 5 | DELETE | `/api/images/{hinhAnhId}` | Xóa hình ảnh | Admin/NV |
