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

