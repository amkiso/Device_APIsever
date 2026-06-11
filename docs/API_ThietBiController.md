# Nhóm Thiết bị (ThietBiController)
*Quản lý danh sách thiết bị, tạo mã QR, tra cứu trạng thái và vô hiệu hóa thiết bị.*

---

## 1. Lấy danh sách thiết bị
- **Method:** `GET`
- **Endpoint:** `/api/thiet-bi`
- **Mô tả:** Lấy danh sách thiết bị. Có thể lọc theo Loại thiết bị hoặc Kho hiện tại.
- **Quyền hạn (Role):** Mọi tài khoản

### Request Parameter
| Key | Type | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| loaiThietBiId | int | Không | Lọc theo ID loại thiết bị |
| khoHienTaiId | int | Không | Lọc theo ID kho hiện tại |

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "thietBiId": 1,
      "maTaiSan": "TB001",
      "tinhTrangId": 1,
      "tenKho": "Kho Tổng",
      "tenTinhTrang": "Sẵn sàng"
    }
  ]
}
```

---

## 2. Tra cứu thiết bị theo Mã Tài Sản
- **Method:** `GET`
- **Endpoint:** `/api/thiet-bi/tra-cuu/{maTaiSan}`
- **Mô tả:** Tra cứu toàn bộ thông tin thiết bị (kể cả quét QR code ra mã).
- **Quyền hạn (Role):** Mọi tài khoản

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "thietBiId": 1,
    "maTaiSan": "TB001",
    "loaiThietBi": "Máy chiếu"
  }
}
```

---

## 3. Tạo/Cập nhật mã QR cho thiết bị
- **Method:** `GET`
- **Endpoint:** `/api/thiet-bi/{id}/qr-code`
- **Mô tả:** Lấy hoặc tự động tạo mã QR cho thiết bị.
- **Quyền hạn (Role):** Admin / Quản lý thiết bị

### Response
```json
{
  "status": 200,
  "message": "Lay QR code thanh cong",
  "data": {
    "thietBiId": 1,
    "maTaiSan": "TB001",
    "qrContent": "DEVICE:TB001",
    "qrCodeUrl": "http://img.domain/qr/TB001.png"
  }
}
```

---

## 4. Thêm thiết bị mới
- **Method:** `POST`
- **Endpoint:** `/api/thiet-bi`
- **Mô tả:** Tạo mới một thiết bị vào hệ thống.
- **Quyền hạn (Role):** Admin / Quản lý thiết bị

### Request Body
```json
{
  "maTaiSan": "TB002",
  "loaiThietBiId": 5,
  "khoHienTaiId": 1,
  "tinhTrangId": 1
}
```

### Response
```json
{
  "status": 200,
  "message": "Tao thiet bi thanh cong",
  "data": { "thietBiId": 2 }
}
```

---

## 5. Vô hiệu hóa thiết bị
- **Method:** `PUT`
- **Endpoint:** `/api/thiet-bi/{id}/vo-hieu-hoa`
- **Mô tả:** Chuyển trạng thái thiết bị sang vô hiệu hóa (nếu không thể xóa).
- **Quyền hạn (Role):** Admin

### Response
```json
{
  "status": 200,
  "message": "Da vo hieu hoa thiet bi",
  "data": null
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/thiet-bi` | Lấy danh sách thiết bị | Mọi Role |
| 2 | GET | `/api/thiet-bi/tra-cuu/{maTaiSan}` | Tra cứu theo Mã Tài Sản | Mọi Role |
| 3 | GET | `/api/thiet-bi/kho/{khoId}` | Lấy thiết bị trong kho | Mọi Role |
| 4 | GET | `/api/thiet-bi/{id}/qr-code` | Lấy mã QR code | Admin/NV |
| 5 | POST | `/api/thiet-bi` | Thêm thiết bị mới | Admin |
| 6 | PUT | `/api/thiet-bi/{id}` | Cập nhật thiết bị | Admin |
| 7 | DELETE | `/api/thiet-bi/{id}` | Xóa thiết bị | Admin |
| 8 | PUT | `/api/thiet-bi/{id}/vo-hieu-hoa` | Vô hiệu hóa thiết bị | Admin |
| 9 | GET | `/api/thiet-bi/{id}/hop-dong` | Xem hợp đồng của thiết bị | Admin |
| 10| GET | `/api/thiet-bi/{id}/lich-su-bao-tri` | Lịch sử bảo trì | Mọi Role |
| 11| PUT | `/api/thiet-bi/{id}/cap-nhat-tinh-trang` | Cập nhật tình trạng | Mọi Role |
