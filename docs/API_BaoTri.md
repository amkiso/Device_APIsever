# Quản Lý Bảo Trì (Nhân Viên/Admin) - API Documentation

Các API trong nhóm này được sử dụng bởi **Kỹ thuật viên / Nhân viên / Admin** để lên lịch và ghi nhận thông tin bảo trì, sự cố thiết bị.

Tất cả các API dưới đây đều yêu cầu xác thực bằng JWT Token ở Header:
`Authorization: Bearer <token>`
Và yêu cầu người dùng phải có quyền là Kỹ thuật viên, Nhân viên, hoặc Admin.

---

## 📋 1. Lấy Danh Sách Lịch Bảo Trì
Lấy danh sách các lịch bảo trì, có phân trang và có thể lọc theo trạng thái, người dùng bảo trì, hoặc từ khóa (mã thiết bị).

- **URL:** `GET /api/bao-tri`
- **Headers:** `Authorization: Bearer <token>`
- **Query Params (Tùy chọn):**
  | Param | Kiểu | Mặc định | Mô tả |
  |-------|------|----------|-------|
  | `page` | int | `0` | Số thứ tự trang (bắt đầu từ 0) |
  | `size` | int | `10` | Số lượng lịch bảo trì trên 1 trang |
  | `trangThaiId` | int | null | Lọc theo trạng thái bảo trì |
  | `nguoiDungId` | int | null | Lọc theo ID nhân viên kỹ thuật thực hiện |
  | `keyword` | string | null | Lọc theo mã thiết bị hoặc nội dung |

- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": {
          "content": [
              {
                  "baoTriId": 1,
                  "thietBiId": 15,
                  "maThietBi": "TB-001",
                  "tenLoaiThietBi": "Máy phát điện 50KVA",
                  "nguoiDungBaoTriId": 3,
                  "tenNguoiDungBaoTri": "KTV Nguyễn Văn B",
                  "hopDongId": 12,
                  "ngayThucHien": "2026-06-15T08:00:00",
                  "loaiBaoTriId": 1,
                  "tenLoaiBaoTri": "Bảo trì định kỳ",
                  "noiDungBaoTri": "Thay dầu máy",
                  "chiPhi": 500000.00,
                  "trangThaiId": 2,
                  "tenTrangThai": "Đang thực hiện",
                  "tinhVaoBoiThuong": false,
                  "ghiChu": null,
                  "ngayHoanThanh": null
              }
          ],
          "pageable": { ... },
          "totalElements": 20,
          "totalPages": 2,
          "last": false,
          "size": 10,
          "number": 0,
          "first": true,
          "empty": false
      }
  }
  ```

---

## 🔍 2. Xem Chi Tiết Lịch Bảo Trì
Lấy toàn bộ thông tin chi tiết của 1 phiên bảo trì, bao gồm cả danh sách hình ảnh ghi nhận sự cố hoặc hình ảnh lúc bàn giao/hoàn thành.

- **URL:** `GET /api/bao-tri/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `GET /api/bao-tri/1`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": {
          "baoTriId": 1,
          "thietBiId": 15,
          "maThietBi": "TB-001",
          ... (như trong list ở trên),
          "anhGhiNhanSuCo": [
              {
                  "hinhAnhId": 101,
                  "urlAnh": "https://<account_id>.r2.cloudflarestorage.com/quanlythietbi/work/anh-su-co-1.jpg"
              }
          ],
          "anhBanGiao": [
              {
                  "hinhAnhId": 102,
                  "urlAnh": "https://<account_id>.r2.cloudflarestorage.com/quanlythietbi/work/anh-hoan-thanh.jpg"
              }
          ]
      }
  }
  ```

---

## 📊 3. Thống Kê Chi Phí Bảo Trì (Chỉ Admin)
Lấy báo cáo tổng hợp chi phí bảo trì phân loại theo các nhóm: định kỳ, cải tiến, sự cố, lỗi khách hàng (bồi thường).

- **URL:** `GET /api/bao-tri/thong-ke`
- **Headers:** `Authorization: Bearer <token>`
- **Yêu cầu Security:** Role = `ADMIN`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": {
          "tongChiPhi": 15000000.00,
          "chiPhiDinhKy": 5000000.00,
          "chiPhiCaiTien": 2000000.00,
          "chiPhiLoiKhongBoiThuong": 3000000.00,
          "chiPhiSuCoCoBoiThuong": 5000000.00
      }
  }
  ```

---

## ➕ 4. Tạo Lịch Bảo Trì Mới
Tạo một yêu cầu/lịch trình bảo trì mới cho một thiết bị cụ thể. Trạng thái khởi tạo sẽ tự động được set (ví dụ: Chờ xác nhận/Chờ thực hiện).

- **URL:** `POST /api/bao-tri`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "thietBiId": 15,
      "hopDongId": 12,           // Tùy chọn, nếu bảo trì trong lúc đang cho thuê
      "loaiBaoTriId": 1,         // 1: Định kỳ, 2: Sự cố
      "noiDungBaoTri": "Kiểm tra động cơ trước khi cho thuê",
      "ngayThucHien": "2026-06-10T09:00:00",
      "tinhVaoBoiThuong": false
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": {
          "baoTriId": 2,
          "thietBiId": 15,
          ...
      }
  }
  ```

---

## ✔️ 5. Kỹ Thuật Viên Xác Nhận Yêu Cầu
Kỹ thuật viên xác nhận tiếp nhận công việc bảo trì (Chuyển trạng thái sang Đang thực hiện).

- **URL:** `PUT /api/bao-tri/{id}/xac-nhan`
- **Headers:** `Authorization: Bearer <token>`
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Xác nhận yêu cầu thành công",
      "data": { ... }
  }
  ```

---

## 📸 6. Ghi Nhận Hình Ảnh Sự Cố
Trong quá trình bảo trì, Kỹ thuật viên upload hình ảnh báo cáo tình trạng (ví dụ thiết bị hỏng hóc). 
*Lưu ý: URL ảnh truyền vào là các tên file hoặc URL có được sau khi đã upload lên R2 qua SAS Link của container `work`.*

- **URL:** `POST /api/bao-tri/{id}/ghi-nhan-su-co`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "imageUrls": [
          "uuid-su-co-1.jpg",
          "uuid-su-co-2.jpg"
      ]
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Ghi nhận sự cố thành công",
      "data": { ... }
  }
  ```

---

## 🏁 7. Hoàn Thành Bảo Trì
Cập nhật trạng thái hoàn tất, nhập chi phí thực tế phát sinh và upload hình ảnh sau khi đã sửa chữa xong.

- **URL:** `POST /api/bao-tri/{id}/hoan-thanh`
- **Headers:** `Authorization: Bearer <token>`
- **Body Request (JSON):**
  ```json
  {
      "noiDungBaoTri": "Đã thay dầu và vệ sinh bugi",
      "chiPhi": 350000.00,
      "ghiChu": "Thiết bị hoạt động ổn định",
      "tinhVaoBoiThuong": false,
      "imageUrls": [
          "uuid-hoan-thanh-1.jpg"
      ]
  }
  ```
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Hoàn thành bảo trì thành công",
      "data": { ... }
  }
  ```
