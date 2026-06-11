# Nhóm Bàn Giao & Thu Hồi (BanGiaoController)
*Quản lý việc bàn giao thiết bị từ công ty cho khách hàng, cũng như thu hồi thiết bị sau khi kết thúc hợp đồng.*

Lưu ý: Controller này nằm trong thư mục Admin nhưng phục vụ mục đích bàn giao (`/api/admin/hop-dong/{hopDongId}/ban-giao`)

---

## 1. Bàn giao thiết bị cho khách hàng
- **Method:** `POST`
- **Endpoint:** `/api/admin/hop-dong/{hopDongId}/ban-giao`
- **Mô tả:** Nhân viên kỹ thuật bàn giao thiết bị cho khách hàng sau khi kiểm tra mã thiết bị, và lưu chữ ký điện tử biên bản bàn giao.
- **Quyền hạn (Role):** Admin, Kỹ thuật viên

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực đăng nhập |

### Request Body
```json
{
  "danhSachThietBi": [
    { "thietBiId": 101, "ghiChu": "Mới 100%" }
  ],
  "chuKyUrl": "signature_123.png",
  "ghiChuChung": "Khách hàng hài lòng"
}
```

### Response
```json
{
  "status": 200,
  "message": "Bàn giao thiết bị thành công",
  "data": null
}
```

---

## 2. Lấy URL Upload chữ ký bàn giao (SAS Token)
- **Method:** `GET`
- **Endpoint:** `/api/admin/hop-dong/{hopDongId}/ban-giao/signature/upload-url`
- **Mô tả:** Lấy URL SAS để upload ảnh chữ ký của khách hàng ký nhận khi bàn giao thiết bị.
- **Quyền hạn (Role):** Admin, Kỹ thuật viên

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| extension | string | Không | Đuôi ảnh (mặc định "png") |

### Response
```json
{
  "status": 200,
  "message": "Tạo SAS URL chữ ký bàn giao thành công",
  "data": {
    "sasUrl": "https://...",
    "fileName": "file_uuid.png",
    "publicUrl": null,
    "category": "sign"
  }
}
```

---

## 3. Thu hồi toàn bộ thiết bị
- **Method:** `POST`
- **Endpoint:** `/api/admin/hop-dong/{hopDongId}/thu-hoi`
- **Mô tả:** Kỹ thuật viên đến thu hồi thiết bị khi hết hạn hợp đồng. Đổi trạng thái thiết bị và cập nhật kho.
- **Quyền hạn (Role):** Admin, Kỹ thuật viên

### Request Body
```json
{
  "khoThuHoiId": 1,
  "ghiChu": "Thu hồi đủ thiết bị",
  "danhSachThietBi": [
    { "thietBiId": 101, "tinhTrangId": 1 }
  ]
}
```

### Response
```json
{
  "status": 200,
  "message": "Thu hồi thiết bị thành công",
  "data": null
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | POST | `/api/admin/hop-dong/{id}/ban-giao` | Bàn giao thiết bị | Admin/Kỹ thuật |
| 2 | GET | `/api/admin/hop-dong/{id}/ban-giao/signature/...`| SAS link chữ ký bàn giao | Admin/Kỹ thuật |
| 3 | POST | `/api/admin/hop-dong/{id}/thu-hoi` | Thu hồi toàn bộ thiết bị | Admin/Kỹ thuật |
