-- ============================================================
-- Migration: Cập nhật bảng GioHang
-- Thay đổi: ThietBiID → LoaiThietBiID + thêm SoLuong
-- Ngày: 2026-05-10
-- ============================================================

-- 1. Xóa constraint UNIQUE cũ (NguoiDungID, ThietBiID)
ALTER TABLE [dbo].[GioHang] DROP CONSTRAINT [UQ_GioHang];
GO

-- 2. Xóa dữ liệu cũ (nếu có) vì ThietBiID sẽ bị xóa
DELETE FROM [dbo].[GioHang];
GO

-- 3. Xóa cột ThietBiID cũ
ALTER TABLE [dbo].[GioHang] DROP COLUMN [ThietBiID];
GO

-- 4. Thêm cột LoaiThietBiID (thay cho ThietBiID)
ALTER TABLE [dbo].[GioHang] ADD [LoaiThietBiID] [int] NOT NULL;
GO

-- 5. Thêm cột SoLuong (mặc định = 1)
ALTER TABLE [dbo].[GioHang] ADD [SoLuong] [int] NOT NULL DEFAULT 1;
GO

-- 6. Tạo lại constraint UNIQUE mới (NguoiDungID, LoaiThietBiID)
ALTER TABLE [dbo].[GioHang] ADD CONSTRAINT [UQ_GioHang] UNIQUE NONCLUSTERED 
(
    [NguoiDungID] ASC,
    [LoaiThietBiID] ASC
) ON [PRIMARY];
GO

-- 7. (Tùy chọn) Thêm FK reference tới bảng LoaiThietBi
-- ALTER TABLE [dbo].[GioHang] ADD CONSTRAINT [FK_GioHang_LoaiThietBi] 
--     FOREIGN KEY ([LoaiThietBiID]) REFERENCES [dbo].[LoaiThietBi]([LoaiThietBiID]);
-- GO
