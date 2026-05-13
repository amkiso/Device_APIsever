# Quản lý Thuê Thiết Bị - API Documentation

Đây là tài liệu hướng dẫn sử dụng các Endpoint xác thực (Authentication) cho dự án Quản lý Thuê Thiết Bị. 

Tất cả các API dưới đây đều có URL cơ sở phụ thuộc vào môi trường chạy của bạn (ví dụ: `http://localhost:8080`).

---

## 🔒 1. Đăng nhập (Login)
Endpoint dùng chung cho cả **Nhân viên** và **Khách hàng**.

- **URL:** `POST /api/auth/login`
- **Yêu cầu Security:** Bỏ qua kiểm duyệt (Permit All)
- **Body Request (JSON):**
  ```json
  {
      "taiKhoan": "ten_dang_nhap_hoac_email",
      "matKhau": "123456"
  }
  ```
- **Response Thành công (200 OK):**
  Trả về Token JWT dùng để truy cập các API yêu cầu xác thực.
  ```json
  {
      "message": "Dang nhap thanh cong!",
      "data": {
          "token": "eyJhbGciOiJIUzI...",
          "nguoiDungId": 1,
          "hoTen": "Nguyễn Văn A",
          "maNguoiDung": "KH00001",
          "vaiTroId": 4,
          "tenVaiTro": "Khách hàng",
          "doiMatKhauLanDau": false
      }
  }
  ```

---

## 📝 2. Đăng ký Khách hàng (Register Flow)
Quy trình đăng ký có bảo mật chống Spam, yêu cầu xác thực OTP qua Email.

### Bước 2.1: Khởi tạo Đăng ký
Lưu tạm thông tin trên RAM Server và gửi OTP vào Email.
- **URL:** `POST /api/auth/register-init`
- **Body Request (JSON):**
  ```json
  {
      "hoTen": "Ho ten cua ban",
      "email": "your_email@host.com", 
      "matKhau": "123456",
      "soDienThoai": "your phone number",
      "loaiKhachHangId": 1, 
      "diaChi": "your address"
  }
  ```
- **Response (200 OK):** `"Vui long kiem tra email de lay ma OTP"`

### Bước 2.2: Xác nhận OTP
Gửi mã OTP (tuổi thọ 5 phút) bước 1 về lại hệ thống để tạo Tài Khoản chính thức.
- **URL:** `POST /api/auth/register-confirm`
- **Body Request (JSON):**
  ```json
  {
      "email": "your_email",
      "otp": "your OTP code in email"
  }
  ```
- **Response Thành công (200 OK):**
  Hệ thống sẽ lưu dữ liệu xuống CSDL, gửi Email chào mừng và tự động trả về `token` đăng nhập luôn.

---

## 🔑 3. Quên mật khẩu (Forgot Password Flow)
Dành cho người dùng quên mật khẩu, cần xác thực 2 bước bằng OTP.

### Bước 3.1: Nhập Email báo quên
- **URL:** `POST /api/auth/forgot-password-init`
- **Body Request (JSON):**
  ```json
  {
      "email": "hoangvu2004dl@gmail.com"
  }
  ```
- **Response (200 OK):** `"Vui long kiem tra email de lay ma OTP"`

### Bước 3.2: Nhập OTP và Mật khẩu mới
- **URL:** `POST /api/auth/forgot-password-confirm`
- **Body Request (JSON):**
  ```json
  {
      "email": "hoangvu2004dl@gmail.com",
      "otp": "654321",
      "newPassword": "Mat_khau_moi_123"
  }
  ```
- **Response Thành công (200 OK):** `"Doi mat khau thanh cong! Ban co the dang nhap bang mat khau moi."`

---

## 🔐 4. Các API cần Token (Authenticated)
Để dùng các API này, bạn phải gắn JWT token vào Headers của Request theo cấu trúc:
`Authorization: Bearer <token_cua_ban>`

### 4.1. Đổi mật khẩu
- **URL:** `POST /api/auth/doi-mat-khau`
- **Body Request (JSON):**
  ```json
  {
      "matKhauCu": "123456",
      "matKhauMoi": "Mat_khau_moi_123"
  }
  ```

### 4.2. Lấy thông tin cá nhân (Profile)
- **URL:** `GET /api/auth/me`
- **Response Thành công:** Trả về toàn bộ Object chứa thông tin `NguoiDung` (Đã ẩn mật khẩu).

---
## cập nhật 27/04/2026

## 📊 5. Dashboard - Trang chủ Admin
API cung cấp dữ liệu tổng hợp cho trang chủ Admin sau đăng nhập.

- **URL:** `GET /api/dashboard`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "data": {
          "doanhThuThangNay": 150000000.00,
          "doanhThuThangTruoc": 120000000.00,
          "tiLeTangTruong": 25.0,
          "soThietBiDangBaoTri": 3,
          "soHopDongDenHan": 5,
          "nhacNhoHomNay": [
              {
                  "loai": "HOP_DONG",
                  "tieuDe": "Hợp đồng #12 đến hạn trả",
                  "moTa": "Khách hàng: Nguyễn Văn A - Địa điểm: 123 ABC",
                  "referenceId": 12
              },
              {
                  "loai": "BAO_TRI",
                  "tieuDe": "Thiết bị TB001 cần bảo trì",
                  "moTa": "Đã đến ngày bảo trì định kỳ",
                  "referenceId": 1
              }
          ]
      }
  }
  ```

---

## 🔍 6. Tra cứu Thiết bị theo Mã
- **URL:** `GET /api/thiet-bi/tra-cuu/{maTaiSan}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `GET /api/thiet-bi/tra-cuu/TB001`
- **Response:** Trả về toàn bộ thông tin thiết bị bao gồm: loại thiết bị, danh mục, nhà cung cấp, tình trạng, kho hiện tại, danh sách hình ảnh.

---

## 🔔 7. Thông báo (Notification System)
Hệ thống thông báo hỗ trợ: gửi cho tất cả, gửi theo nhóm vai trò, gửi cho cá nhân. Kết hợp Firebase Push Notification.

### 7.1. Tạo thông báo mới (Admin)
- **URL:** `POST /api/thong-bao`
- **Headers:** `Authorization: Bearer <token>`
- **Body (gửi tất cả):**
  ```json
  {
      "tieuDe": "Thông báo bảo trì hệ thống",
      "noiDung": "Hệ thống sẽ bảo trì từ 22h-23h ngày 28/04",
      "loaiThongBao": 1
  }
  ```
- **Body (gửi theo vai trò, ví dụ: KTV - vaiTroId=3):**
  ```json
  {
      "tieuDe": "Lịch bảo trì tuần tới",
      "noiDung": "Vui lòng kiểm tra lịch bảo trì",
      "loaiThongBao": 2,
      "vaiTroNhanId": 3
  }
  ```
- **Body (gửi cá nhân):**
  ```json
  {
      "tieuDe": "Hợp đồng của bạn đã được duyệt",
      "noiDung": "Hợp đồng #15 đã được duyệt thành công",
      "loaiThongBao": 3,
      "nguoiDungNhanId": 5
  }
  ```

### 7.2. Lấy danh sách thông báo
- **URL:** `GET /api/thong-bao`
- **Headers:** `Authorization: Bearer <token>`

### 7.3. Đếm thông báo chưa đọc (badge chuông 🔔)
- **URL:** `GET /api/thong-bao/chua-doc`
- **Headers:** `Authorization: Bearer <token>`
- **Response:** `{ "data": 3 }`

### 7.4. Đánh dấu đã đọc
- **URL:** `PUT /api/thong-bao/{id}/da-doc`
- **Headers:** `Authorization: Bearer <token>`

### 7.5. Đăng ký FCM Token (Client gọi sau khi login)
- **URL:** `POST /api/fcm/register-token`
- **Headers:** `Authorization: Bearer <token>`
- **Body:**
  ```json
  {
      "token": "fcm_device_token_here",
      "deviceName": "Samsung Galaxy S24"
  }
  ```

---
## cập nhật 02/05/2026

## 🖼️ 8. Upload Ảnh (Azure Blob Storage + SAS Token + Multi-Container)

Hệ thống upload ảnh sử dụng kiến trúc **cấp phát SAS Token** (Shared Access Signature) kết hợp **Multi-Container** để tối ưu băng thông và quản lý ảnh có tổ chức.

### Cấu trúc Container trên Azure

| Container | Mục đích | Ví dụ |
|-----------|----------|-------|
| `user` | Ảnh cá nhân | Ảnh đại diện (avatar) |
| `products` | Ảnh sản phẩm/thiết bị | Ảnh thiết bị cho cataloge |
| `work` | Ảnh nghiệp vụ | Giao nhận, bàn giao, hiện trường, bảo trì |

**Luồng hoạt động:**
```
┌──────────┐  1. GET /{category}/get-upload-url  ┌──────────┐
│  Mobile  │ ───────────────────────────────────► │  Server  │
│   App    │ ◄─────────────────────────────────── │ (Spring) │
│          │  2. Trả về sasUrl+fileName+category  │          │
│          │                                      └──────────┘
│          │  3. HTTP PUT sasUrl (file)            ┌──────────┐
│          │ ───────────────────────────────────► │  Azure   │
│          │ ◄─────────────────────────────────── │  Blob    │
│          │  4. Upload thành công                 │ Storage  │
│          │                                      └──────────┘
│          │  5. POST /{category}/confirm-upload   ┌──────────┐
│          │ ───────────────────────────────────► │  Server  │
│          │ ◄─────────────────────────────────── │  (lưu DB)│
└──────────┘  6. Xác nhận lưu thành công           └──────────┘
```

---

### 8.1. Lấy SAS URL để upload ảnh (chung cho cả 3 container)
Gọi trước khi upload để lấy link upload có thời hạn. Thay `{category}` bằng `user`, `products`, hoặc `work`.
- **URL:** `GET /api/images/{category}/get-upload-url`
- **Headers:** `Authorization: Bearer <token>`
- **Path Variable:** `category` = `user` | `products` | `work`
- **Query Params (tùy chọn):** `?extension=jpg` (mặc định: jpg)
- **Ví dụ:**
  - `GET /api/images/user/get-upload-url` — lấy link upload avatar
  - `GET /api/images/products/get-upload-url?extension=png` — lấy link upload ảnh sản phẩm
  - `GET /api/images/work/get-upload-url` — lấy link upload ảnh nghiệp vụ
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Tao SAS URL thanh cong cho container 'products'. URL co hieu luc trong 5 phut.",
      "data": {
          "sasUrl": "https://mediaserverproject.blob.core.windows.net/products/uuid-file.jpg?sv=...&se=...&sp=w&sig=...",
          "fileName": "a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
          "publicUrl": "https://mediaserverproject.blob.core.windows.net/products/a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
          "category": "products"
      }
  }
  ```
- **Ghi chú SAS Token:**
  - Quyền: Chỉ GHI (Write) — không thể đọc hay xóa file khác
  - Thời hạn: 5 phút

---

### 8.2. Client upload file lên Azure (không qua server)
Sau khi nhận `sasUrl`, client gọi trực tiếp lên Azure:
- **Method:** `PUT`
- **URL:** Giá trị `sasUrl` nhận được ở bước 8.1
- **Headers:**
  ```
  x-ms-blob-type: BlockBlob
  Content-Type: image/jpeg
  ```
- **Body:** File binary (ảnh)

---

### 8.3. Xác nhận upload — Container "user" (Ảnh đại diện)
- **URL:** `POST /api/images/user/update-avatar`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):**
  ```json
  {
      "fileName": "a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg"
  }
  ```
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Cap nhat anh dai dien thanh cong!",
      "data": {
          "nguoiDungId": "1",
          "avatarUrl": "https://mediaserverproject.blob.core.windows.net/user/a1b2c3d4-...jpg"
      }
  }
  ```

---

### 8.4. Xác nhận upload — Container "products" (Ảnh sản phẩm)
- **URL:** `POST /api/images/products/thiet-bi/{thietBiId}/update-image`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):**
  ```json
  {
      "fileName": "a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
      "loaiAnhId": 1
  }
  ```
  | Trường | Bắt buộc | Mô tả |
  |--------|----------|-------|
  | fileName | ✅ | Tên file UUID nhận từ bước 8.1 |
  | loaiAnhId | ❌ | Loại ảnh (mặc định: 1 = Ảnh sản phẩm) |
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Luu anh san pham thanh cong!",
      "data": {
          "hinhAnhId": 10,
          "thietBiId": 5,
          "nguoiDungChupId": 1,
          "urlAnh": "https://mediaserverproject.blob.core.windows.net/products/a1b2c3d4-...jpg",
          "loaiAnhId": 1,
          "ngayChup": "2026-05-02T23:00:00"
      }
  }
  ```

---

### 8.5. Xác nhận upload — Container "work" (Ảnh nghiệp vụ)
- **URL:** `POST /api/images/work/thiet-bi/{thietBiId}/update-image`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):**
  ```json
  {
      "fileName": "a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
      "loaiAnhId": 2,
      "banGiaoId": 5,
      "baoTriId": null
  }
  ```
  | Trường | Bắt buộc | Mô tả |
  |--------|----------|-------|
  | fileName | ✅ | Tên file UUID nhận từ bước 8.1 |
  | loaiAnhId | ❌ | Loại ảnh (mặc định: 2 = Biên bản) |
  | banGiaoId | ⚠️ | ID bàn giao (phải có 1 trong 2: banGiaoId hoặc baoTriId) |
  | baoTriId | ⚠️ | ID bảo trì (phải có 1 trong 2: banGiaoId hoặc baoTriId) |
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Luu anh nghiep vu thanh cong!",
      "data": {
          "hinhAnhId": 11,
          "thietBiId": 5,
          "nguoiDungChupId": 1,
          "urlAnh": "https://mediaserverproject.blob.core.windows.net/work/a1b2c3d4-...jpg",
          "loaiAnhId": 2,
          "ngayChup": "2026-05-02T23:00:00",
          "banGiaoId": 5,
          "baoTriId": null
      }
  }
  ```

---

### 8.6. Xóa ảnh (tự phát hiện container)
Xóa ảnh khỏi cả Azure Blob Storage và Database. Server tự phát hiện container từ URL.
- **URL:** `DELETE /api/images/{hinhAnhId}`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Da xoa anh ID: 10"
  }
  ```

---
## cập nhật 08/05/2026

## 📂 9. Danh mục Thiết bị (CRUD)
Quản lý danh mục thiết bị (ví dụ: "Thiết bị điện", "Thiết bị cơ khí"...).

> **Phân quyền:**
> - `GET` — Tất cả user đã đăng nhập
> - `POST`, `PUT`, `DELETE` — Chỉ **ADMIN** và **THỦ KHO**

### 9.1. Lấy tất cả danh mục
- **URL:** `GET /api/danh-muc`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "danhMucId": 1,
              "tenDanhMuc": "Thiết bị điện"
          },
          {
              "danhMucId": 2,
              "tenDanhMuc": "Thiết bị cơ khí"
          }
      ]
  }
  ```

### 9.2. Lấy 1 danh mục theo ID
- **URL:** `GET /api/danh-muc/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `GET /api/danh-muc/1`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": {
          "danhMucId": 1,
          "tenDanhMuc": "Thiết bị điện"
      }
  }
  ```

### 9.3. Tạo danh mục mới (Admin, Thủ kho)
- **URL:** `POST /api/danh-muc`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):**
  ```json
  {
      "tenDanhMuc": "Thiết bị đo lường"
  }
  ```
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Tạo danh mục thành công",
      "data": {
          "danhMucId": 3,
          "tenDanhMuc": "Thiết bị đo lường"
      }
  }
  ```

### 9.4. Cập nhật danh mục (Admin, Thủ kho)
- **URL:** `PUT /api/danh-muc/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):**
  ```json
  {
      "tenDanhMuc": "Thiết bị điện tử"
  }
  ```
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Cập nhật danh mục thành công",
      "data": {
          "danhMucId": 1,
          "tenDanhMuc": "Thiết bị điện tử"
      }
  }
  ```

### 9.5. Xóa danh mục (Admin, Thủ kho)
- **URL:** `DELETE /api/danh-muc/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đã xóa danh mục ID: 1"
  }
  ```

---

## 🏷️ 10. Loại Thiết bị (CRUD + Tìm kiếm)
Quản lý loại thiết bị cụ thể trong mỗi danh mục (ví dụ: "Máy phát điện 50KVA", "Máy khoan bê tông"...).

> **Phân quyền:**
> - `GET` — Tất cả user đã đăng nhập
> - `POST`, `PUT`, `DELETE` — Chỉ **ADMIN** và **THỦ KHO**

### 10.1. Lấy tất cả loại thiết bị (có thể lọc theo danh mục)
- **URL:** `GET /api/loai-thiet-bi`
- **URL (lọc theo danh mục):** `GET /api/loai-thiet-bi?danhMucId={id}`
- **Headers:** `Authorization: Bearer <token>`
- **Query Params (tùy chọn):**

  | Param | Bắt buộc | Mô tả |
  |-------|----------|-------|
  | danhMucId | ❌ | Lọc loại thiết bị theo danh mục. Nếu không truyền → lấy tất cả |

- **Ví dụ:** `GET /api/loai-thiet-bi?danhMucId=1`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "loaiThietBiId": 1,
              "danhMucId": 1,
              "nhaCungCapId": 2,
              "tenLoaiThietBi": "Máy phát điện 50KVA",
              "thongSoKyThuat": "Công suất: 50KVA, Nhiên liệu: Diesel",
              "giaThueThamKhao": 5000000.00,
              "anhDaiDien": "https://mediaserverproject.blob.core.windows.net/products/abc.jpg"
          }
      ]
  }
  ```

### 10.2. Lấy chi tiết 1 loại thiết bị
- **URL:** `GET /api/loai-thiet-bi/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `GET /api/loai-thiet-bi/1`

### 10.3. Tìm kiếm loại thiết bị theo tên
- **URL:** `GET /api/loai-thiet-bi/search?q={keyword}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `GET /api/loai-thiet-bi/search?q=máy phát`
- **Response (200 OK):** Trả về danh sách `LoaiThietBi` có tên chứa keyword.

### 10.4. Tạo loại thiết bị mới (Admin, Thủ kho)
- **URL:** `POST /api/loai-thiet-bi`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):**
  ```json
  {
      "danhMucId": 1,
      "nhaCungCapId": 2,
      "tenLoaiThietBi": "Máy phát điện 100KVA",
      "thongSoKyThuat": "Công suất: 100KVA, Nhiên liệu: Diesel",
      "giaThueThamKhao": 8000000.00,
      "anhDaiDien": "https://mediaserverproject.blob.core.windows.net/products/xyz.jpg"
  }
  ```
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Tạo loại thiết bị thành công",
      "data": {
          "loaiThietBiId": 5,
          "danhMucId": 1,
          "nhaCungCapId": 2,
          "tenLoaiThietBi": "Máy phát điện 100KVA",
          "thongSoKyThuat": "Công suất: 100KVA, Nhiên liệu: Diesel",
          "giaThueThamKhao": 8000000.00,
          "anhDaiDien": "https://mediaserverproject.blob.core.windows.net/products/xyz.jpg"
      }
  }
  ```

### 10.5. Cập nhật loại thiết bị (Admin, Thủ kho)
- **URL:** `PUT /api/loai-thiet-bi/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):** Tương tự body tạo mới (10.4), truyền các trường cần cập nhật.

### 10.6. Xóa loại thiết bị (Admin, Thủ kho)
- **URL:** `DELETE /api/loai-thiet-bi/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đã xóa loại thiết bị ID: 5"
  }
  ```

---

## 🏭 11. Nhà Cung Cấp
Tra cứu thông tin nhà cung cấp thiết bị.

> **Phân quyền:** `GET` — Tất cả user đã đăng nhập

### 11.1. Lấy tất cả nhà cung cấp
- **URL:** `GET /api/nha-cung-cap`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "nhaCungCapId": 1,
              "tenNhaCungCap": "Công ty TNHH ABC",
              "nguoiLienHe": "Nguyễn Văn A",
              "soDienThoai": "0901234567",
              "diaChi": "123 Đường ABC, Q.1, TP.HCM"
          },
          {
              "nhaCungCapId": 2,
              "tenNhaCungCap": "Công ty CP XYZ",
              "nguoiLienHe": "Trần Thị B",
              "soDienThoai": "0987654321",
              "diaChi": "456 Đường XYZ, Q.7, TP.HCM"
          }
      ]
  }
  ```

### 11.2. Lấy 1 nhà cung cấp theo ID
- **URL:** `GET /api/nha-cung-cap/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Ví dụ:** `GET /api/nha-cung-cap/1`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": {
          "nhaCungCapId": 1,
          "tenNhaCungCap": "Công ty TNHH ABC",
          "nguoiLienHe": "Nguyễn Văn A",
          "soDienThoai": "0901234567",
          "diaChi": "123 Đường ABC, Q.1, TP.HCM"
      }
  }
  ```

---

## 🖥️ 12. Thiết bị (CRUD mở rộng)
Ngoài API tra cứu (mục 6), hệ thống còn có các endpoint quản lý thiết bị.

> **Phân quyền:**
> - `GET` — Tất cả user đã đăng nhập
> - `POST`, `PUT`, `DELETE` — Chỉ **ADMIN** và **THỦ KHO**

### 12.1. Lấy danh sách thiết bị (có thể lọc theo loại)
- **URL:** `GET /api/thiet-bi`
- **URL (lọc theo loại):** `GET /api/thiet-bi?loaiThietBiId={id}`
- **Headers:** `Authorization: Bearer <token>`
- **Query Params (tùy chọn):**

  | Param | Bắt buộc | Mô tả |
  |-------|----------|-------|
  | loaiThietBiId | ❌ | Lọc thiết bị theo loại. Nếu không truyền → lấy tất cả (raw entity) |

- **Ví dụ:** `GET /api/thiet-bi?loaiThietBiId=1`
- **Response khi có `loaiThietBiId` (200 OK):** Trả về DTO gộp dữ liệu từ ThietBi + TinhTrang + Kho
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "thietBiId": 1,
              "maTaiSan": "MK-001",
              "tinhTrangId": 1,
              "tenTinhTrang": "Sẵn sàng",
              "khoHienTaiId": 1,
              "tenKho": "Kho A",
              "ngayBaoTriTiepTheo": "2026-06-15T00:00:00"
          }
      ]
  }
  ```
- **Response khi không có `loaiThietBiId` (200 OK):** Trả về raw entity
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "thietBiId": 1,
              "loaiThietBiId": 1,
              "maTaiSan": "MK-001",
              "tinhTrangId": 1,
              "khoHienTaiId": 1,
              "ngayBaoTriTiepTheo": "2026-06-15T00:00:00"
          }
      ]
  }
  ```

### 12.2. Tạo thiết bị mới (Admin, Thủ kho)
- **URL:** `POST /api/thiet-bi`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):**
  ```json
  {
      "loaiThietBiId": 1,
      "maTaiSan": "MK-005",
      "tinhTrangId": 1,
      "khoHienTaiId": 1,
      "ngayBaoTriTiepTheo": "2026-07-01T00:00:00"
  }
  ```
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Tao thiet bi thanh cong",
      "data": {
          "thietBiId": 5,
          "loaiThietBiId": 1,
          "maTaiSan": "MK-005",
          "tinhTrangId": 1,
          "khoHienTaiId": 1,
          "ngayBaoTriTiepTheo": "2026-07-01T00:00:00"
      }
  }
  ```

### 12.3. Xóa thiết bị (Admin, Thủ kho)
- **URL:** `DELETE /api/thiet-bi/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Da xoa thiet bi ID: 5"
  }
  ```

---

## 🛒 13. Giỏ hàng (Cart API)
Quản lý giỏ hàng tạm cho Khách hàng. Sau khi tạo hợp đồng thuê thành công, giỏ hàng sẽ được xóa.

> **Phân quyền:** Tất cả endpoints — Chỉ **KHÁCH HÀNG** (`VaiTroID = 4`)
>
> **Xác thực:** Tất cả endpoints yêu cầu JWT Token trong Header: `Authorization: Bearer <token>`
>
> **Lưu ý:** Nếu thêm trùng `loaiThietBiId` đã có trong giỏ → hệ thống tự cộng dồn số lượng.

### 13.1. Lấy danh sách giỏ hàng
Lấy tất cả items trong giỏ hàng của user đang đăng nhập (xác định qua JWT).
- **URL:** `GET /api/gio-hang`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "gioHangId": 1,
              "loaiThietBiId": 3,
              "tenLoaiThietBi": "Máy phát điện 50KVA",
              "anhDaiDien": "https://mediaserverproject.blob.core.windows.net/products/abc.jpg",
              "giaThueThamKhao": 5000000.00,
              "soLuong": 2,
              "thanhTien": 10000000.00,
              "ngayThem": "2026-05-08T15:30:00"
          },
          {
              "gioHangId": 2,
              "loaiThietBiId": 7,
              "tenLoaiThietBi": "Máy khoan bê tông",
              "anhDaiDien": "https://mediaserverproject.blob.core.windows.net/products/xyz.jpg",
              "giaThueThamKhao": 3000000.00,
              "soLuong": 1,
              "thanhTien": 3000000.00,
              "ngayThem": "2026-05-08T14:00:00"
          }
      ]
  }
  ```

### 13.2. Thêm item vào giỏ hàng
Thêm loại thiết bị vào giỏ. Nếu loại thiết bị đã tồn tại trong giỏ → **cộng dồn** số lượng.
- **URL:** `POST /api/gio-hang`
- **Headers:** `Authorization: Bearer <token>`
- **Body (JSON):**
  ```json
  {
      "loaiThietBiId": 3,
      "soLuong": 1
  }
  ```
  | Trường | Bắt buộc | Validate | Mô tả |
  |--------|----------|---------|-------|
  | loaiThietBiId | ✅ | NOT NULL | ID loại thiết bị muốn thuê |
  | soLuong | ✅ | >= 1 | Số lượng cần thuê |

- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đã thêm vào giỏ hàng",
      "data": {
          "gioHangId": 1,
          "loaiThietBiId": 3,
          "tenLoaiThietBi": "Máy phát điện 50KVA",
          "anhDaiDien": "https://mediaserverproject.blob.core.windows.net/products/abc.jpg",
          "giaThueThamKhao": 5000000.00,
          "soLuong": 1,
          "thanhTien": 5000000.00,
          "ngayThem": "2026-05-08T15:30:00"
      }
  }
  ```
- **Lỗi (404):** Nếu `loaiThietBiId` không tồn tại: `"Loại thiết bị ID 999 không tồn tại"`

### 13.3. Cập nhật số lượng
Thay đổi số lượng của 1 item trong giỏ hàng.
- **URL:** `PUT /api/gio-hang/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Path Variable:** `id` = GioHangID
- **Body (JSON):**
  ```json
  {
      "soLuong": 3
  }
  ```
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Cập nhật số lượng thành công",
      "data": {
          "gioHangId": 1,
          "loaiThietBiId": 3,
          "tenLoaiThietBi": "Máy phát điện 50KVA",
          "anhDaiDien": "https://mediaserverproject.blob.core.windows.net/products/abc.jpg",
          "giaThueThamKhao": 5000000.00,
          "soLuong": 3,
          "thanhTien": 15000000.00,
          "ngayThem": "2026-05-08T15:30:00"
      }
  }
  ```
- **Lỗi (403):** Nếu item không thuộc user hiện tại: `"Bạn không có quyền cập nhật item này"`
- **Lỗi (404):** Nếu `id` không tồn tại: `"Không tìm thấy item giỏ hàng ID: 99"`

### 13.4. Xóa item khỏi giỏ hàng
- **URL:** `DELETE /api/gio-hang/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Path Variable:** `id` = GioHangID
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Đã xóa khỏi giỏ hàng"
  }
  ```
- **Lỗi (403):** Nếu item không thuộc user hiện tại: `"Bạn không có quyền xóa item này"`

### 13.5. Đếm tổng items (Badge)
Đếm tổng số lượng thiết bị trong giỏ (SUM số lượng), dùng cho badge icon giỏ hàng.
- **URL:** `GET /api/gio-hang/count`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": 5
  }
  ```
  > `data` = tổng `SUM(soLuong)` của tất cả items. Trả về `0` nếu giỏ trống.

---
## cập nhật 13/05/2026

## 📱 14. QR Code Thiết bị

API tạo và quản lý QR code cho từng thiết bị. QR code chứa mã định danh thiết bị theo format `DEVICE:<maTaiSan>`, được lưu dạng ảnh PNG trên server.

> **Phân quyền:** `GET` — Tất cả user đã đăng nhập
>
> **Lưu ý:** Ảnh QR chỉ được tạo 1 lần. Các lần gọi tiếp theo sẽ trả về ảnh đã tạo trước đó.

### Flow hoạt động

```
┌──────────┐  1. GET /api/thiet-bi/{id}/qr-code   ┌──────────┐
│  Client  │ ────────────────────────────────────► │  Server  │
│  (App)   │                                       │ (Spring) │
│          │  2. Kiểm tra JWT + quyền truy cập     │          │
│          │  3. Tìm thiết bị theo ID               │          │
│          │  4. Kiểm tra QR đã tồn tại?            │          │
│          │     ┌─ CÓ  → trả URL ảnh cũ           │          │
│          │     └─ CHƯA → tạo QR mới              │          │
│          │        → lưu PNG vào /uploads/qrcode/  │          │
│          │        → cập nhật DB (QrCodeUrl)        │          │
│          │ ◄──────────────────────────────────── │          │
│          │  5. Trả về JSON chứa qrCodeUrl         │          │
└──────────┘                                       └──────────┘
```

### 14.1. Lấy / Tạo QR code cho thiết bị
- **URL:** `GET /api/thiet-bi/{id}/qr-code`
- **Headers:** `Authorization: Bearer <token>`
- **Path Variable:** `id` = ThietBiID
- **Ví dụ:** `GET /api/thiet-bi/5/qr-code`
- **Response (200 OK) — Lần đầu (tạo mới):**
  ```json
  {
      "success": true,
      "message": "Lay QR code thanh cong",
      "data": {
          "thietBiId": 5,
          "maTaiSan": "TB001",
          "qrContent": "DEVICE:TB001",
          "qrCodeUrl": "/uploads/qrcode/TB001.png"
      }
  }
  ```
- **Response (200 OK) — Lần sau (đã có):**
  ```json
  {
      "success": true,
      "message": "Lay QR code thanh cong",
      "data": {
          "thietBiId": 5,
          "maTaiSan": "TB001",
          "qrContent": "DEVICE:TB001",
          "qrCodeUrl": "/uploads/qrcode/TB001.png"
      }
  }
  ```
- **Lỗi (404):** Nếu thiết bị không tồn tại: `"Khong tim thay thiet bi voi ID: 999"`
- **Lỗi (400):** Nếu lỗi tạo QR: `"Khong the tao QR code: ..."`

### 14.2. Truy cập ảnh QR trực tiếp (Static File)
Sau khi nhận `qrCodeUrl`, client có thể truy cập ảnh QR trực tiếp qua URL:
- **URL:** `GET {baseUrl}/uploads/qrcode/{maTaiSan}.png`
- **Ví dụ:** `GET http://localhost:8080/uploads/qrcode/TB001.png`
- **Yêu cầu Security:** Không cần Token (Public)
- **Response:** Ảnh PNG

### QR Content Format
| Thành phần | Giá trị | Ví dụ |
|------------|---------|-------|
| Prefix | `DEVICE:` | Cố định |
| Mã tài sản | `maTaiSan` của thiết bị | `TB001` |
| **Full content** | `DEVICE:<maTaiSan>` | `DEVICE:TB001` |

### Thông số kỹ thuật QR
| Thuộc tính | Giá trị |
|------------|---------|
| Kích thước | 400 x 400 px |
| Format | PNG |
| Error Correction | Level H (30%) |
| Character Set | UTF-8 |
| Margin | 2 modules |
| Thư mục lưu trữ | `uploads/qrcode/` |

### Migration Database
Trước khi sử dụng, cần chạy script SQL để thêm cột `QrCodeUrl` vào bảng `ThietBi`:
```sql
ALTER TABLE ThietBi
ADD QrCodeUrl NVARCHAR(500) NULL;
```
> File migration: `src/main/resources/sql/migrate_qrcode.sql`

---

## 📋 Tổng hợp tất cả Endpoints

| # | Method | Endpoint | Mô tả | Quyền |
|---|--------|----------|-------|-------|
| 1 | POST | `/api/auth/login` | Đăng nhập | Public |
| 2 | POST | `/api/auth/register-init` | Đăng ký - Bước 1 (gửi OTP) | Public |
| 3 | POST | `/api/auth/register-confirm` | Đăng ký - Bước 2 (xác nhận OTP) | Public |
| 4 | POST | `/api/auth/forgot-password-init` | Quên MK - Bước 1 | Public |
| 5 | POST | `/api/auth/forgot-password-confirm` | Quên MK - Bước 2 | Public |
| 6 | POST | `/api/auth/doi-mat-khau` | Đổi mật khẩu | Authenticated |
| 7 | GET | `/api/auth/me` | Lấy thông tin cá nhân | Authenticated |
| 8 | GET | `/api/dashboard` | Dashboard Admin | Authenticated |
| 9 | GET | `/api/danh-muc` | Lấy tất cả danh mục | Authenticated |
| 10 | GET | `/api/danh-muc/{id}` | Lấy 1 danh mục | Authenticated |
| 11 | POST | `/api/danh-muc` | Tạo danh mục | ADMIN, THỦ KHO |
| 12 | PUT | `/api/danh-muc/{id}` | Cập nhật danh mục | ADMIN, THỦ KHO |
| 13 | DELETE | `/api/danh-muc/{id}` | Xóa danh mục | ADMIN, THỦ KHO |
| 14 | GET | `/api/loai-thiet-bi` | Lấy tất cả loại TB | Authenticated |
| 15 | GET | `/api/loai-thiet-bi/{id}` | Lấy chi tiết loại TB | Authenticated |
| 16 | GET | `/api/loai-thiet-bi/search?q=` | Tìm kiếm loại TB | Authenticated |
| 17 | POST | `/api/loai-thiet-bi` | Tạo loại TB | ADMIN, THỦ KHO |
| 18 | PUT | `/api/loai-thiet-bi/{id}` | Cập nhật loại TB | ADMIN, THỦ KHO |
| 19 | DELETE | `/api/loai-thiet-bi/{id}` | Xóa loại TB | ADMIN, THỦ KHO |
| 20 | GET | `/api/nha-cung-cap` | Lấy tất cả NCC | Authenticated |
| 21 | GET | `/api/nha-cung-cap/{id}` | Lấy 1 NCC | Authenticated |
| 22 | GET | `/api/thiet-bi` | Lấy danh sách thiết bị | Authenticated |
| 23 | GET | `/api/thiet-bi/tra-cuu/{maTaiSan}` | Tra cứu thiết bị theo mã | Authenticated |
| 24 | GET | `/api/thiet-bi/{id}/qr-code` | Lấy/Tạo QR code thiết bị | Authenticated |
| 25 | POST | `/api/thiet-bi` | Tạo thiết bị | ADMIN, THỦ KHO |
| 26 | DELETE | `/api/thiet-bi/{id}` | Xóa thiết bị | ADMIN, THỦ KHO |
| 27 | POST | `/api/thong-bao` | Tạo thông báo | Authenticated |
| 28 | GET | `/api/thong-bao` | Lấy DS thông báo | Authenticated |
| 29 | GET | `/api/thong-bao/chua-doc` | Đếm thông báo chưa đọc | Authenticated |
| 30 | PUT | `/api/thong-bao/{id}/da-doc` | Đánh dấu đã đọc | Authenticated |
| 31 | POST | `/api/fcm/register-token` | Đăng ký FCM Token | Authenticated |
| 32 | GET | `/api/images/{category}/get-upload-url` | Lấy SAS URL upload ảnh | ADMIN, THỦ KHO |
| 33 | POST | `/api/images/user/update-avatar` | Cập nhật avatar | ADMIN, THỦ KHO |
| 34 | POST | `/api/images/products/thiet-bi/{id}/update-image` | Lưu ảnh sản phẩm | ADMIN, THỦ KHO |
| 35 | POST | `/api/images/work/thiet-bi/{id}/update-image` | Lưu ảnh nghiệp vụ | ADMIN, THỦ KHO |
| 36 | DELETE | `/api/images/{hinhAnhId}` | Xóa ảnh | ADMIN, THỦ KHO |
| 37 | GET | `/api/gio-hang` | Lấy DS giỏ hàng | KHÁCH HÀNG |
| 38 | POST | `/api/gio-hang` | Thêm item vào giỏ | KHÁCH HÀNG |
| 39 | PUT | `/api/gio-hang/{id}` | Cập nhật số lượng | KHÁCH HÀNG |
| 40 | DELETE | `/api/gio-hang/{id}` | Xóa item khỏi giỏ | KHÁCH HÀNG |
| 41 | GET | `/api/gio-hang/count` | Đếm tổng items (badge) | KHÁCH HÀNG |
| — | GET | `/uploads/qrcode/{file}.png` | Truy cập ảnh QR (static) | Public |
