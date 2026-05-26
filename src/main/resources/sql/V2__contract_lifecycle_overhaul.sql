-- ============================================================
-- MIGRATION: Contract Lifecycle Overhaul
-- Version: 2.0
-- Date: 2026-05-26
-- Description: 
--   - Thêm bảng LoaiHopDong, CauHinhHopDong
--   - Thêm cột mới cho HopDongThue (phân loại, hỏa tốc, hạn TT, lý do hủy)
--   - Đổi ChuKyDienTu.DuLieuChuKy (varbinary) → TenFileChuKy (nvarchar)
--   - Cập nhật trạng thái hợp đồng lên 12 trạng thái
-- ============================================================

USE [QuanLyThietBi];
GO

-- ─────────────────────────────────────────────────────
-- 1. BẢNG LOẠI HỢP ĐỒNG
-- ─────────────────────────────────────────────────────
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'LoaiHopDong')
BEGIN
    CREATE TABLE [dbo].[LoaiHopDong] (
        [LoaiHopDongID] INT NOT NULL PRIMARY KEY,
        [TenLoai] NVARCHAR(50) NOT NULL,
        [MoTa] NVARCHAR(255) NULL
    );

    INSERT INTO [dbo].[LoaiHopDong] VALUES (1, N'Cá nhân', N'Hợp đồng thuê cho khách hàng cá nhân');
    INSERT INTO [dbo].[LoaiHopDong] VALUES (2, N'Doanh nghiệp', N'Hợp đồng thuê cho doanh nghiệp/tổ chức');
    INSERT INTO [dbo].[LoaiHopDong] VALUES (3, N'Hỏa tốc', N'Hợp đồng xử lý gấp (phí +10%)');
    PRINT N'✅ Tạo bảng LoaiHopDong thành công';
END
ELSE
    PRINT N'⏭️ Bảng LoaiHopDong đã tồn tại, bỏ qua';
GO

-- ─────────────────────────────────────────────────────
-- 2. BẢNG CẤU HÌNH HỢP ĐỒNG
-- ─────────────────────────────────────────────────────
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'CauHinhHopDong')
BEGIN
    CREATE TABLE [dbo].[CauHinhHopDong] (
        [CauHinhID] INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        [MaCauHinh] NVARCHAR(50) NOT NULL UNIQUE,
        [GiaTri] DECIMAL(18,2) NOT NULL,
        [MoTa] NVARCHAR(255) NULL,
        [NgayCapNhat] DATETIME2 NOT NULL DEFAULT GETDATE()
    );

    INSERT INTO [dbo].[CauHinhHopDong] ([MaCauHinh], [GiaTri], [MoTa]) 
    VALUES (N'PHI_HOA_TOC', 10.00, N'Phí hỏa tốc (%) cộng thêm vào tổng tiền thuê');
    
    INSERT INTO [dbo].[CauHinhHopDong] ([MaCauHinh], [GiaTri], [MoTa]) 
    VALUES (N'HAN_THANH_TOAN_NGAY', 2.00, N'Số ngày trước ngày bắt đầu thuê cần thanh toán cọc');
    
    PRINT N'✅ Tạo bảng CauHinhHopDong thành công';
END
ELSE
    PRINT N'⏭️ Bảng CauHinhHopDong đã tồn tại, bỏ qua';
GO

-- ─────────────────────────────────────────────────────
-- 3. THÊM CỘT MỚI CHO HopDongThue
-- ─────────────────────────────────────────────────────

-- LoaiHopDongID
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'HopDongThue' AND COLUMN_NAME = 'LoaiHopDongID')
BEGIN
    ALTER TABLE [dbo].[HopDongThue] ADD [LoaiHopDongID] INT NULL;
    PRINT N'✅ Thêm cột LoaiHopDongID';
END
GO

-- LaHoaToc
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'HopDongThue' AND COLUMN_NAME = 'LaHoaToc')
BEGIN
    ALTER TABLE [dbo].[HopDongThue] ADD [LaHoaToc] BIT NOT NULL DEFAULT 0;
    PRINT N'✅ Thêm cột LaHoaToc';
END
GO

-- PhiHoaToc
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'HopDongThue' AND COLUMN_NAME = 'PhiHoaToc')
BEGIN
    ALTER TABLE [dbo].[HopDongThue] ADD [PhiHoaToc] DECIMAL(18,2) NULL DEFAULT 0;
    PRINT N'✅ Thêm cột PhiHoaToc';
END
GO

-- HanThanhToan
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'HopDongThue' AND COLUMN_NAME = 'HanThanhToan')
BEGIN
    ALTER TABLE [dbo].[HopDongThue] ADD [HanThanhToan] DATETIME2 NULL;
    PRINT N'✅ Thêm cột HanThanhToan';
END
GO

-- LyDoHuy
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'HopDongThue' AND COLUMN_NAME = 'LyDoHuy')
BEGIN
    ALTER TABLE [dbo].[HopDongThue] ADD [LyDoHuy] NVARCHAR(500) NULL;
    PRINT N'✅ Thêm cột LyDoHuy';
END
GO

-- FK: LoaiHopDongID → LoaiHopDong
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_HopDongThue_LoaiHopDong')
BEGIN
    ALTER TABLE [dbo].[HopDongThue] 
    ADD CONSTRAINT FK_HopDongThue_LoaiHopDong 
    FOREIGN KEY ([LoaiHopDongID]) REFERENCES [dbo].[LoaiHopDong]([LoaiHopDongID]);
    PRINT N'✅ Thêm FK LoaiHopDongID';
END
GO

-- ─────────────────────────────────────────────────────
-- 4. SỬA ChuKyDienTu: byte[] → tên file Azure
-- ─────────────────────────────────────────────────────

-- Thêm cột mới TenFileChuKy
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'ChuKyDienTu' AND COLUMN_NAME = 'TenFileChuKy')
BEGIN
    ALTER TABLE [dbo].[ChuKyDienTu] ADD [TenFileChuKy] NVARCHAR(255) NULL;
    PRINT N'✅ Thêm cột TenFileChuKy';
END
GO

-- Xóa cột cũ DuLieuChuKy (nếu đã chuyển dữ liệu hoặc chưa có dữ liệu thực)
-- ⚠️ CẢNH BÁO: Chỉ chạy khi đã backup dữ liệu hoặc đây là môi trường dev
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'ChuKyDienTu' AND COLUMN_NAME = 'DuLieuChuKy')
BEGIN
    ALTER TABLE [dbo].[ChuKyDienTu] DROP COLUMN [DuLieuChuKy];
    PRINT N'✅ Xóa cột DuLieuChuKy (byte[])';
END
GO

-- Đặt NOT NULL cho TenFileChuKy sau khi xóa cột cũ
-- (Chỉ chạy nếu bảng rỗng hoặc đã migrate dữ liệu)
-- ALTER TABLE [dbo].[ChuKyDienTu] ALTER COLUMN [TenFileChuKy] NVARCHAR(255) NOT NULL;
-- GO

-- ─────────────────────────────────────────────────────
-- 5. CẬP NHẬT TRẠNG THÁI HỢP ĐỒNG (12 trạng thái)
-- ─────────────────────────────────────────────────────

-- Xóa dữ liệu cũ và thêm 12 trạng thái mới
DELETE FROM [dbo].[TrangThaiHopDong];
GO

INSERT INTO [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES
(1,  N'Chờ xác nhận'),
(2,  N'Đã xác nhận - Chờ thanh toán cọc'),
(3,  N'Chờ nhận thiết bị'),
(4,  N'Đang cho thuê'),
(5,  N'Vi phạm - Chấm dứt hợp đồng'),
(6,  N'Quá hạn thanh toán'),
(7,  N'Đã hủy bởi khách hàng'),
(8,  N'Đã thu hồi thiết bị'),
(9,  N'Đang kiểm tra thiết bị'),
(10, N'Chờ thanh toán nợ'),
(11, N'Đã hủy bởi nhân viên'),
(12, N'Hoàn tất');
GO

PRINT N'✅ Cập nhật 12 trạng thái hợp đồng';
GO

PRINT N'';
PRINT N'══════════════════════════════════════════════════';
PRINT N'  MIGRATION HOÀN TẤT — Contract Lifecycle v2.0';
PRINT N'══════════════════════════════════════════════════';
GO
