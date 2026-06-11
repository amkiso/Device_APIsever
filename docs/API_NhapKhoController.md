# Nhóm Nhập Kho (NhapKhoController)
*Quản lý việc tạo phiếu nhập thiết bị vào kho.*

---

## 1. Tạo phiếu nhập kho
- **Method:** `POST`
- **Endpoint:** `/api/nhap-kho`
- **Mô tả:** Nhân viên kho hoặc Admin thực hiện nhập hàng vào kho.
- **Quyền hạn (Role):** Admin / Nhân viên Kho

### Request Header
| Key | Value | Bắt buộc | Mô tả |
| :--- | :--- | :---: | :--- |
| Authorization | Bearer <token> | Có | Token xác thực |

### Request Body
```json
{
  "khoId": 1,
  "nhaCungCapId": 2,
  "ghiChu": "Nhập hàng đợt 1",
  "chiTietNhapKho": [
    {
      "loaiThietBiId": 5,
      "soLuong": 10,
      "donGia": 1500000
    }
  ]
}
```

### Response
```json
{
  "status": 200,
  "message": "Nhập kho thành công",
  "data": null
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | POST | `/api/nhap-kho` | Tạo phiếu nhập kho | Admin/NV Kho |
