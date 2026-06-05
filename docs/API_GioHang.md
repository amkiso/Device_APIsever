# Giỏ Hàng (Khách Hàng) - API Documentation

Các API trong nhóm này được sử dụng bởi **Khách hàng** để quản lý các thiết bị dự định thuê trước khi tiến hành lên đơn (Tạo hợp đồng).

Tất cả các API dưới đây đều yêu cầu xác thực bằng JWT Token ở Header:
`Authorization: Bearer <token>`

---

## 🛒 1. Lấy Danh Sách Giỏ Hàng
Lấy toàn bộ các mặt hàng đang có trong giỏ hàng của người dùng hiện tại.

- **URL:** `GET /api/gio-hang`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "gioHangId": 1,
              "loaiThietBiId": 5,
              "tenLoaiThietBi": "Máy phát điện 50KVA",
              "anhDaiDien": "https://<account_id>.r2.cloudflarestorage.com/quanlythietbi/public/may-phat-dien.jpg",
              "giaThueThamKhao": 5000000.00,
              "soLuong": 2,
              "thanhTien": 10000000.00,
              "ngayThem": "2026-06-04T10:00:00"
          }
      ]
  }
  ```

---

## ➕ 2. Thêm Thiết Bị Vào Giỏ Hàng
Khách hàng chọn "Thêm vào giỏ hàng" từ màn hình chi tiết sản phẩm.

- **URL:** `POST /api/gio-hang`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "loaiThietBiId": 5,
      "soLuong": 1
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đã thêm vào giỏ hàng",
      "data": { ... }
  }
  ```

---

## 🔄 3. Cập Nhật Số Lượng Trong Giỏ Hàng
Thay đổi số lượng thuê của một thiết bị đang có sẵn trong giỏ.

- **URL:** `PUT /api/gio-hang/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `PUT /api/gio-hang/1` *(1 là gioHangId)*
- **Body Request (JSON):**
  ```json
  {
      "soLuong": 3
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Cập nhật số lượng thành công",
      "data": {
          "gioHangId": 1,
          "loaiThietBiId": 5,
          "soLuong": 3,
          "thanhTien": 15000000.00,
          ...
      }
  }
  ```

---

## ❌ 4. Xóa Thiết Bị Khỏi Giỏ Hàng
Khách hàng chọn xóa hoặc gạt bỏ một thiết bị khỏi giỏ hàng.

- **URL:** `DELETE /api/gio-hang/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `DELETE /api/gio-hang/1`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đã xóa khỏi giỏ hàng",
      "data": null
  }
  ```

---

## 🔴 5. Đếm Số Lượng Trong Giỏ Hàng (Badge Count)
Sử dụng để hiển thị số lượng (chấm đỏ) trên biểu tượng giỏ hàng ở thanh điều hướng (Navigation bar).

- **URL:** `GET /api/gio-hang/count`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": 4
  }
  ```
