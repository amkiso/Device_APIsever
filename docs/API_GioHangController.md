# Nhóm Giỏ Hàng (GioHangController)
*Cho phép khách hàng thêm, sửa, xóa các thiết bị dự định thuê/mua vào giỏ hàng cá nhân.*

---

## 1. Lấy danh sách giỏ hàng
- **Method:** `GET`
- **Endpoint:** `/api/gio-hang`
- **Mô tả:** Lấy danh sách thiết bị có trong giỏ hàng của tài khoản đang đăng nhập.
- **Quyền hạn (Role):** Khách hàng

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": [
    {
      "gioHangId": 1,
      "loaiThietBiId": 5,
      "tenLoaiThietBi": "Máy chiếu Sony",
      "soLuong": 2,
      "anhDaiDien": "may-chieu.jpg"
    }
  ]
}
```

---

## 2. Thêm thiết bị vào giỏ hàng
- **Method:** `POST`
- **Endpoint:** `/api/gio-hang`
- **Mô tả:** Thêm một loại thiết bị mới vào giỏ hàng (nếu đã có thì tăng số lượng).
- **Quyền hạn (Role):** Khách hàng

### Request Body
```json
{
  "loaiThietBiId": 5,
  "soLuong": 1
}
```

### Response
```json
{
  "status": 200,
  "message": "Đã thêm vào giỏ hàng",
  "data": {
    "gioHangId": 1
  }
}
```

---

## 3. Cập nhật số lượng thiết bị
- **Method:** `PUT`
- **Endpoint:** `/api/gio-hang/{id}`
- **Mô tả:** Chỉnh sửa số lượng của một sản phẩm trong giỏ hàng.
- **Quyền hạn (Role):** Khách hàng

### Request Body
```json
{
  "soLuong": 3
}
```

### Response
```json
{
  "status": 200,
  "message": "Cập nhật số lượng thành công",
  "data": null
}
```

---

## 4. Xóa thiết bị khỏi giỏ hàng
- **Method:** `DELETE`
- **Endpoint:** `/api/gio-hang/{id}`
- **Mô tả:** Loại bỏ hoàn toàn một thiết bị khỏi giỏ hàng.
- **Quyền hạn (Role):** Khách hàng

### Response
```json
{
  "status": 200,
  "message": "Đã xóa khỏi giỏ hàng",
  "data": null
}
```

---

## 5. Đếm số lượng sản phẩm
- **Method:** `GET`
- **Endpoint:** `/api/gio-hang/count`
- **Mô tả:** Đếm tổng số lượng thiết bị trong giỏ (hiển thị trên badge giỏ hàng góc màn hình).
- **Quyền hạn (Role):** Khách hàng

### Response
```json
{
  "status": 200,
  "message": "Success",
  "data": 5
}
```

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/gio-hang` | Lấy danh sách giỏ hàng | Khách hàng |
| 2 | POST | `/api/gio-hang` | Thêm vào giỏ hàng | Khách hàng |
| 3 | PUT | `/api/gio-hang/{id}` | Cập nhật số lượng | Khách hàng |
| 4 | DELETE | `/api/gio-hang/{id}` | Xóa khỏi giỏ hàng | Khách hàng |
| 5 | GET | `/api/gio-hang/count` | Đếm số lượng sản phẩm trong giỏ | Khách hàng |
