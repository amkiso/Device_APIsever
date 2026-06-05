# Quản Lý Địa Chỉ Giao Hàng - API Documentation

Các API trong nhóm này được sử dụng bởi **Khách hàng** để quản lý danh sách địa chỉ giao hàng của mình. Khi tạo hợp đồng thuê thiết bị, khách hàng sẽ chọn 1 trong các địa chỉ này để giao nhận.

Tất cả các API dưới đây đều yêu cầu xác thực bằng JWT Token ở Header:
`Authorization: Bearer <token>`

---

## 📍 1. Lấy Danh Sách Địa Chỉ Của Tôi
Lấy toàn bộ địa chỉ giao hàng do khách hàng đang đăng nhập tạo ra.

- **URL:** `GET /api/dia-chi`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "diaChiId": 1,
              "tenNguoiNhan": "Nguyễn Văn A",
              "soDienThoai": "0901234567",
              "tinhThanhPho": "TP Hồ Chí Minh",
              "phuongXa": "Phường Bến Nghé",
              "diaChiChiTiet": "Tòa nhà Bitexco, Số 2 Hải Triều",
              "donVi": "Tầng 50",
              "loaiDiaChi": 1,
              "laMacDinh": true
          }
      ]
  }
  ```

---

## ➕ 2. Thêm Mới Địa Chỉ
Thêm một địa chỉ giao hàng mới vào sổ địa chỉ.

- **URL:** `POST /api/dia-chi`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "tenNguoiNhan": "Nguyễn Văn A",
      "soDienThoai": "0901234567",
      "tinhThanhPho": "TP Hồ Chí Minh",
      "phuongXa": "Phường Tân Định",
      "diaChiChiTiet": "123 Đường Hai Bà Trưng",
      "donVi": "Công ty TNHH ABC",
      "loaiDiaChi": 2,
      "laMacDinh": false
  }
  ```
  *(loaiDiaChi: có thể là 1 - Nhà riêng, 2 - Công ty)*
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Tạo địa chỉ thành công",
      "data": { ... }
  }
  ```

---

## ✏️ 3. Cập Nhật Địa Chỉ
Chỉnh sửa thông tin của một địa chỉ đã có. Nếu set `laMacDinh: true`, hệ thống sẽ tự động tắt cờ mặc định của các địa chỉ khác.

- **URL:** `PUT /api/dia-chi/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):** *(Tương tự API Thêm mới)*
  ```json
  {
      "tenNguoiNhan": "Nguyễn Văn A",
      "soDienThoai": "0988888888",
      "tinhThanhPho": "TP Hồ Chí Minh",
      "phuongXa": "Phường Tân Định",
      "diaChiChiTiet": "123 Đường Hai Bà Trưng",
      "donVi": "Công ty TNHH ABC",
      "loaiDiaChi": 2,
      "laMacDinh": true
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Cập nhật địa chỉ thành công",
      "data": { ... }
  }
  ```

---

## ❌ 4. Xóa Địa Chỉ
Xóa một địa chỉ khỏi sổ địa chỉ của khách hàng.

- **URL:** `DELETE /api/dia-chi/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đã xóa địa chỉ",
      "data": null
  }
  ```
