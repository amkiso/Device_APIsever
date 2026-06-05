# Quản Lý Kho Bãi - API Documentation

Các API trong nhóm này được sử dụng để lấy thông tin các kho chứa thiết bị trong hệ thống. Dữ liệu này dùng để gán thiết bị vào các kho cụ thể khi quản lý nhập/xuất thiết bị.

---

## 🏢 1. Lấy Danh Sách Tất Cả Các Kho
Lấy thông tin danh sách kho bãi (Public/Không yêu cầu phân trang, do số lượng kho thường ít).

- **URL:** `GET /api/kho`
- **Headers:** `Authorization: Bearer <token>` (Tùy cấu hình, khuyến nghị truyền Token của nhân viên/admin)
- **Response Thành công (200 OK):**
  ```json
  {
      "success": true,
      "message": "Success",
      "data": [
          {
              "khoId": 1,
              "tenKho": "Kho Trung Tâm Quận 1",
              "diaChi": "Số 1 Lê Duẩn, Quận 1, TP.HCM",
              "nguoiPhuTrach": "Nguyễn Văn Kho",
              "soDienThoai": "0987654321"
          },
          {
              "khoId": 2,
              "tenKho": "Kho Vệ Tinh Thủ Đức",
              "diaChi": "Khu công nghệ cao Thủ Đức",
              "nguoiPhuTrach": "Trần Văn Bãi",
              "soDienThoai": "0909090909"
          }
      ]
  }
  ```
