# Hợp Đồng (Khách Hàng) - API Documentation

Các API trong nhóm này được sử dụng bởi **Khách hàng** để thực hiện các nghiệp vụ liên quan đến hợp đồng thuê thiết bị (Tạo đơn, Ký kết, Thanh toán, Hủy, Yêu cầu hỗ trợ).

Tất cả các API dưới đây đều yêu cầu xác thực bằng JWT Token ở Header:
`Authorization: Bearer <token>`

---

## 🛒 1. Tạo Hợp Đồng Mới (Từ Giỏ Hàng)
Chuyển các thiết bị từ giỏ hàng sang thành hợp đồng mới (Trạng thái: Chờ xác nhận).

- **URL:** `POST /api/hop-dong/tao`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "diaChiGiaoId": 1,
      "phuongThucThanhToan": 1,
      "ngayBatDauThue": "2026-06-05",
      "soThangThue": 6,
      "ghiChuKhachHang": "Vui lòng giao giờ hành chính",
      "danhSachThietBi": [
          {
              "thietBiId": 5,
              "soLuong": 2
          }
      ]
  }
  ```
- **Response Thành công (201 Created):**
  ```json
  {
      "success": true,
      "message": "Tạo hợp đồng thành công",
      "data": {
          "hopDongId": 15,
          "maHopDong": "HD-2026-00015",
          "trangThai": "Chờ xác nhận",
          "chiTietThietBi": [
              {
                  "tenThietBi": "Máy phát điện 50KVA",
                  "soSerial": "SN-12345",
                  "tinhTrangBanGiao": "Mới 95%",
                  "mucDichSuDung": "Công trường",
                  "giaTriMay": 50000000.00,
                  "giaThueThang": 5000000.00,
                  "ngayKiemDinh": "2026-01-10"
              }
          ],
          "chiPhi": {
              "tongTienThue": 60000000.00,
              "tienCoc": 30000000.00,
              "thueVAT": 6000000.00,
              "phiTreHanPhanTram": 5.00,
              "soNgayTreHanMoiKy": 7,
              "soNgayViPhamChamDut": 15,
              "phiVeSinhChuyenSau": 200000.00,
              "khauHaoHaoMonNam": null,
              "phiGianDoanPhanTram": 3.00
          }
      }
  }
  ```

---

## ✍️ 2. Ký Hợp Đồng Điện Tử
Xác nhận hợp đồng sau khi đã đọc điều khoản bằng cách gửi tên file ảnh chữ ký (sau khi tải lên R2 qua SAS URL) và mã PIN.

- **URL:** `POST /api/hop-dong/{id}/ky-ket`
- **Headers:** `Authorization: Bearer <token>`, `Content-Type: application/json`
- **Body Request (JSON):**
  ```json
  {
      "fileName": "uuid-file-name.png",
      "maPin": "123456"
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Ký hợp đồng thành công",
      "data": {
          "hopDongId": 15,
          "trangThai": "Chờ TT cọc",
          "ngayKy": "2026-06-04T10:00:00",
          "urlThanhToan": null
      }
  }
  ```

---

## 💳 3. Thanh Toán Demo
Giả lập thanh toán cọc hoặc thanh toán nợ thành công (Chuyển trạng thái mà không cần qua cổng thực tế).

- **URL:** `POST /api/hop-dong/{id}/thanh-toan-demo`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Thanh toán thành công",
      "data": {
          "hopDongId": 15,
          "trangThai": "Chờ nhận thiết bị",
          "maGiaoDich": "DEMO-1688888888888"
      }
  }
  ```

---

## 🏦 4. Xác Nhận Thanh Toán (Callback Gateway)
Callback từ cổng thanh toán thực tế (Ví dụ: MoMo/ZaloPay).

- **URL:** `POST /api/hop-dong/{id}/xac-nhan-thanh-toan`
- **Body Request (JSON):**
  ```json
  {
      "maGiaoDich": "MOMO_TID_9999",
      "soTien": 30000000.00,
      "trangThai": 1,
      "phuongThuc": 2
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Xác nhận thanh toán thành công",
      "data": {
          "hopDongId": 15,
          "trangThai": "Chờ nhận thiết bị",
          "maGiaoDich": "MOMO_TID_9999"
      }
  }
  ```

---

## 📋 5. Lấy Danh Sách Hợp Đồng Của Tôi
Lấy toàn bộ lịch sử đơn hàng/hợp đồng của Khách hàng đang đăng nhập.

- **URL:** `GET /api/hop-dong/cua-toi`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "hopDongId": 15,
              "maHopDong": "HD-2026-00015",
              "trangThaiId": 1,
              "trangThai": "Chờ xác nhận",
              "ngayLap": "2026-06-04T09:00:00",
              "ngayBatDauThue": "2026-06-05T00:00:00",
              "ngayDuKienTra": "2026-12-05T00:00:00",
              "tongTienThue": 60000000.00,
              "tienCoc": 30000000.00,
              "soThietBi": 2,
              "diaDiemGiao": "123 Đường ABC, Quận 1",
              "laHoaToc": false,
              "loaiHopDong": "Cá nhân",
              "hanThanhToan": "2026-06-03T00:00:00"
          }
      ]
  }
  ```

---

## 🔄 6. Lấy N Hợp Đồng Gần Nhất
Lấy danh sách rút gọn các hợp đồng gần đây (Thường dùng cho Slider/Carousel ở màn hình Home).

- **URL:** `GET /api/hop-dong/gan-nhat?limit=5`
- **Headers:** `Authorization: Bearer <token>`
- **Query Params:** `limit` (int, mặc định: 5)
- **Response:** (Cấu trúc tương tự API `5. Của Tôi`).

---

## 🧾 7. Xem Chi Tiết Hợp Đồng
Xem chi tiết đầy đủ thông tin hợp đồng đã tạo (Bao gồm thông tin Khách hàng, chi phí, danh sách thiết bị).

- **URL:** `GET /api/hop-dong/{id}/chi-tiet`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": {
          "hopDongId": 15,
          "maHopDong": "HD-2026-00015",
          "trangThaiId": 1,
          "trangThai": "Chờ xác nhận",
          "ngayLap": "2026-06-04T09:00:00",
          "soThangThue": 6,
          "laHoaToc": false,
          "loaiHopDong": "Cá nhân",
          "phiHoaToc": 0.00,
          "khachHang": {
              "hoTen": "Nguyễn Văn A",
              "email": "nguyenvana@gmail.com",
              "soDienThoai": "0901234567",
              "diaChi": "123 Đường ABC",
              "cccd": "012345678901",
              "cccdNgayCap": "2020-01-01",
              "cccdNoiCap": "Cục CSQLHC",
              "donViCongTac": "Công ty TNHH ABC"
          },
          "chiTietThietBi": [ ... ],
          "chiPhi": { ... }
      }
  }
  ```

---

## 🔔 8. Lấy Số Lượng Đơn Hàng Theo Trạng Thái
Đếm số lượng đơn hàng theo các trạng thái chính để hiển thị Badge Notification (chấm đỏ) ở tab Hồ sơ cá nhân.

- **URL:** `GET /api/hop-dong/don-hang-count`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": {
          "choXetDuyet": 1,
          "canThanhToan": 0,
          "choGiaoHang": 2,
          "dangThue": 1,
          "tongDonHang": 4
      }
  }
  ```

---

## ❌ 9. Khách Hàng Tự Hủy Hợp Đồng
Hủy hợp đồng khi hợp đồng vẫn đang ở trạng thái **Chờ xác nhận (Trạng thái 1)**.

- **URL:** `POST /api/hop-dong/{id}/huy`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON - Tùy chọn):**
  ```json
  {
      "lyDoHuy": "Tôi không còn nhu cầu thuê nữa"
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Hủy hợp đồng thành công"
  }
  ```

---

## 🛠️ 10. Gửi Yêu Cầu Hỗ Trợ / Bảo Trì
Khách hàng gửi yêu cầu khi cần Admin/Kỹ thuật viên can thiệp vào thiết bị (Sửa chữa, bảo trì, hoặc thắc mắc chung). Yêu cầu sẽ bắn Push Notification đến toàn bộ nhân viên.

- **URL:** `POST /api/hop-dong/{id}/yeu-cau-ho-tro`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "noiDung": "Thiết bị A đang phát ra tiếng kêu lạ, cần kỹ thuật xuống kiểm tra.",
      "loaiYeuCau": 2
  }
  ```
  *(loaiYeuCau: `1` = Hỗ trợ chung, `2` = Yêu cầu bảo trì)*
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đã gửi yêu cầu hỗ trợ"
  }
  ```
