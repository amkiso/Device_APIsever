# Nhóm Thống Kê (ThongKeController)
*Báo cáo chi tiết về dòng tiền và doanh thu.*

---

## 1. Thống kê dòng tiền (Doanh thu)
- **Method:** `GET`
- **Endpoint:** `/api/thong-ke/dong-tien`
- **Mô tả:** Lấy báo cáo thống kê dòng tiền vào (từ hợp đồng thuê, phí bảo trì) và dòng tiền ra (nhập thiết bị, hoàn cọc). Dữ liệu được nhóm theo tháng hoặc năm.
- **Quyền hạn (Role):** Admin

### Response
```json
{
  "tongThu": 500000000,
  "tongChi": 200000000,
  "loiNhuan": 300000000,
  "chiTietTheoThang": [
    {
      "thang": "2024-01",
      "thu": 100000000,
      "chi": 50000000
    }
  ]
}
```
*(Lưu ý: API này trả về đối tượng trực tiếp thay vì bọc trong `ApiResponse`, dựa vào code mẫu `ResponseEntity.ok(...)`)*

---

## Bảng Tổng Hợp API (API Summary Table)
| STT | Method | Endpoint | Mô tả chức năng | Phân quyền |
| :---: | :---: | :--- | :--- | :---: |
| 1 | GET | `/api/thong-ke/dong-tien` | Thống kê thu chi, lợi nhuận | Admin |
