# Nhóm Hợp đồng (HopDongController)
*Quản lý hợp đồng của người dùng (Khách hàng): tạo hợp đồng, ký kết, thanh toán, hủy hợp đồng, hỗ trợ.*

---

## 1. Tạo hợp đồng mới
- **Method:** `POST`
- **Endpoint:** `/api/hop-dong/tao`
- **Mô tả:** Chuyển giỏ hàng thành hợp đồng (đặt hàng).
- **Quyền hạn (Role):** Khách hàng

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực |

### Request Body
```json
{
  "diaChiGiaoId": 1,
  "ghiChu": "Giao trong giờ hành chính",
  "danhSachThietBi": [
    { "loaiThietBiId": 5, "soLuong": 2 }
  ]
}
```

### Response
```json
{
  "status": 201,
  "message": "Tạo hợp đồng thành công",
  "data": {
    "hopDongId": 1,
    "maHopDong": "HD001"
  }
}
```

---

## 2. Ký hợp đồng điện tử
- **Method:** `POST`
- **Endpoint:** `/api/hop-dong/{id}/ky-ket`
- **Mô tả:** Khách hàng xác nhận và ký hợp đồng bằng mã PIN.
- **Quyền hạn (Role):** Khách hàng

### Request Body
```json
{
  "fileName": "signature_123.png",
  "maPin": "123456"
}
```

### Response
```json
{
  "status": 200,
  "message": "Ký hợp đồng thành công",
  "data": null
}
```

---

## 3. Xác nhận thanh toán (Callback)
- **Method:** `POST`
- **Endpoint:** `/api/hop-dong/{id}/xac-nhan-thanh-toan`
- **Mô tả:** API nhận callback khi khách hàng thanh toán cọc thành công.
- **Quyền hạn (Role):** Hệ thống / Public callback

### Request Body
```json
{
  "maGiaoDich": "VNPT123456",
  "soTien": 5000000,
  "trangThai": "SUCCESS"
}
```

---

## 4. Danh sách hợp đồng của tôi
- **Method:** `GET`
- **Endpoint:** `/api/hop-dong/cua-toi`
- **Mô tả:** Lấy danh sách toàn bộ hợp đồng của khách hàng đang đăng nhập.
- **Quyền hạn (Role):** Khách hàng

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "hopDongId": 1,
      "maHopDong": "HD001",
      "trangThai": "Đã ký"
    }
  ]
}
```

---

## 5. Danh sách hợp đồng gần nhất
- **Method:** `GET`
- **Endpoint:** `/api/hop-dong/gan-nhat`
- **Mô tả:** Dùng cho hiển thị trên Dashboard khách hàng.
- **Quyền hạn (Role):** Khách hàng

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| limit | int | Không | Số lượng tối đa (mặc định 5) |

---

## 6. Hủy hợp đồng
- **Method:** `POST`
- **Endpoint:** `/api/hop-dong/{id}/huy`
- **Mô tả:** Khách hàng chủ động hủy hợp đồng khi hợp đồng đang ở trạng thái Chờ xác nhận.
- **Quyền hạn (Role):** Khách hàng

### Request Body
```json
{
  "lyDoHuy": "Không còn nhu cầu"
}
```

### Response
```json
{
  "status": 200,
  "message": "Hủy hợp đồng thành công"
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | POST | `/api/hop-dong/tao` | Tạo hợp đồng mới | Khách hàng |
| 2 | POST | `/api/hop-dong/{id}/ky-ket` | Ký hợp đồng bằng mã PIN | Khách hàng |
| 3 | POST | `/api/hop-dong/{id}/xac-nhan-thanh-toan` | Callback xác nhận thanh toán | Callback |
| 4 | GET | `/api/hop-dong/cua-toi` | Lấy tất cả hợp đồng của mình | Khách hàng |
| 5 | GET | `/api/hop-dong/gan-nhat` | Lấy N hợp đồng gần nhất | Khách hàng |
| 6 | GET | `/api/hop-dong/{id}/chi-tiet` | Xem chi tiết hợp đồng | Khách hàng |
| 7 | GET | `/api/hop-dong/don-hang-count` | Đếm số lượng đơn theo trạng thái | Khách hàng |
| 8 | POST | `/api/hop-dong/{id}/huy` | Khách hàng hủy hợp đồng | Khách hàng |
| 9 | POST | `/api/hop-dong/{id}/yeu-cau-ho-tro` | Gửi yêu cầu hỗ trợ/bảo trì | Khách hàng |
| 10| POST | `/api/hop-dong/{id}/thanh-toan-demo` | Giao dịch thanh toán demo | Khách hàng |
