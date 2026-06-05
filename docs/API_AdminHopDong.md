# Quản Lý Hợp Đồng (Dành Cho Admin) - API Documentation

Các API trong nhóm này được sử dụng bởi **Nhân viên / Admin** để quản lý hợp đồng thuê thiết bị của toàn hệ thống.

Tất cả các API dưới đây đều yêu cầu xác thực bằng JWT Token ở Header:
`Authorization: Bearer <token>`
Và yêu cầu người dùng phải có quyền (Role) là Admin hoặc Nhân viên quản lý.

---

## 📋 1. Lấy Danh Sách Hợp Đồng
API hỗ trợ lấy danh sách hợp đồng có phân trang và có thể lọc theo nhiều tiêu chí.

- **URL:** `GET /api/admin/hop-dong`
- **Headers:** `Authorization: Bearer <token>`
- **Query Params (Tùy chọn):**
  | Param | Kiểu | Mặc định | Mô tả |
  |-------|------|----------|-------|
  | `page` | int | `0` | Số thứ tự trang (bắt đầu từ 0) |
  | `size` | int | `15` | Số lượng hợp đồng trên 1 trang |
  | `q` | string | null | Từ khóa tìm kiếm (Mã hợp đồng, tên khách hàng, số điện thoại) |
  | `startDate`| datetime | null | Lọc từ ngày (Định dạng ISO: `2026-06-01T00:00:00`) |
  | `endDate` | datetime | null | Lọc đến ngày (Định dạng ISO: `2026-06-30T23:59:59`) |
  | `trangThaiId` | int | null | Lọc theo ID trạng thái hợp đồng |
  | `loaiHopDongId`| int | null | Lọc theo phân loại hợp đồng |

- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Lấy danh sách thành công",
      "data": {
          "content": [
              {
                  "hopDongId": 12,
                  "maHopDong": "HD-2026-00012",
                  "tenKhachHang": "Nguyễn Văn A",
                  "soDienThoai": "0901234567",
                  "loaiHopDongId": 1,
                  "laHoaToc": false,
                  "tongTienThue": 1500000.00,
                  "tienCoc": 750000.00,
                  "trangThaiId": 1,
                  "tenTrangThai": "Chờ xác nhận",
                  "ngayLap": "2026-06-03T08:30:00",
                  "ngayBatDauThue": "2026-06-05T00:00:00",
                  "ngayDuKienTra": "2026-07-05T00:00:00",
                  "ngayTraThucTe": null
              }
          ],
          "pageable": { ... },
          "totalElements": 50,
          "totalPages": 4,
          "last": false,
          "size": 15,
          "number": 0,
          "first": true,
          "empty": false
      }
  }
  ```

---

## 🔍 2. Xem Chi Tiết Hợp Đồng
Lấy toàn bộ thông tin chi tiết của 1 hợp đồng bao gồm thiết bị, chi phí, lịch sử bảo trì.

- **URL:** `GET /api/admin/hop-dong/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `GET /api/admin/hop-dong/12`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Lấy chi tiết thành công",
      "data": {
          "hopDongId": 12,
          "maHopDong": "HD-2026-00012",
          "loaiHopDongId": 1,
          "laHoaToc": false,
          "phiHoaToc": 0.00,
          "trangThaiId": 1,
          "tenTrangThai": "Chờ xác nhận",
          "ngayLap": "2026-06-03T08:30:00",
          "ngayBatDauThue": "2026-06-05T00:00:00",
          "ngayDuKienTra": "2026-07-05T00:00:00",
          "ngayTraThucTe": null,
          "diaDiemGiao": "123 Đường ABC, Phường 1, Quận 1, TP.HCM",
          "ghiChuKhachHang": "Giao giờ hành chính",
          "lyDoHuy": null,
          "khachHang": {
              "nguoiDungId": 5,
              "hoTen": "Nguyễn Văn A",
              "email": "nguyenvana@gmail.com",
              "soDienThoai": "0901234567",
              "cccd": "012345678901"
          },
          "chiPhi": {
              "tongTienThue": 1500000.00,
              "tienCoc": 750000.00,
              "thueVat": 150000.00,
              "phiTreHanPhanTram": 5.00,
              "soNgayTreHanMoiKy": 7,
              "soNgayViPhamChamDut": 15,
              "phiVeSinhChuyenSau": 200000.00,
              "phiGianDoanPhanTram": 3.00
          },
          "danhSachThietBi": [ ... ],
          "lichSuBaoTri": [ ... ]
      }
  }
  ```

---

## 📝 3. Cập Nhật Trạng Thái Hợp Đồng
Admin thực hiện phê duyệt, giao thiết bị, hoặc hủy hợp đồng bằng cách cập nhật trạng thái mới.

- **URL:** `PUT /api/admin/hop-dong/{id}/trang-thai`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "trangThaiId": 2,
      "lyDoHuy": "Thiếu linh kiện" // Tùy chọn, chỉ truyền khi trangThaiId là trạng thái Hủy
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Cập nhật trạng thái thành công"
  }
  ```

---

## 🖋️ 4. Xem Chữ Ký Điện Tử Của Hợp Đồng
Lấy link để hiển thị chữ ký khách hàng đã ký trong hợp đồng. Phục vụ cho Admin đối chiếu.

- **URL:** `GET /api/admin/hop-dong/{id}/chu-ky`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Lấy chữ ký thành công",
      "data": {
          "chuKyId": 1,
          "hopDongId": 12,
          "nguoiDungId": 5,
          "tenNguoiDung": "Nguyễn Văn A",
          "urlChuKy": "https://<account_id>.r2.cloudflarestorage.com/sign/sign/uuid-file.png?...",
          "ngayKy": "2026-06-03T10:05:00",
          "ipAddress": "192.168.1.10",
          "thietBiKy": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)..."
      }
  }
  ```
