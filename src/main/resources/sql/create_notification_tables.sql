-- ============================================================================
-- SCRIPT TẠO 3 BẢNG MỚI CHO HỆ THỐNG THÔNG BÁO
-- Chạy trong SQL Server Management Studio (SSMS)
-- Database: QuanLyThueThietBi
-- ============================================================================

USE [QuanLyThueThietBi]
GO

-- ========== Bảng 1: ThongBao ==========
-- Lưu nội dung thông báo. LoaiThongBao phân loại đối tượng nhận.
CREATE TABLE [dbo].[ThongBao] (
    [ThongBaoID]    INT IDENTITY(1,1) PRIMARY KEY,
    [TieuDe]        NVARCHAR(200)   NOT NULL,
    [NoiDung]       NVARCHAR(MAX)   NOT NULL,
    [LoaiThongBao]  TINYINT         NOT NULL DEFAULT 1,  -- 1=Tất cả, 2=Theo vai trò, 3=Cá nhân
    [NguoiGuiID]    INT             NOT NULL,
    [VaiTroNhanID]  TINYINT         NULL,                 -- Dùng khi LoaiThongBao=2
    [NgayTao]       DATETIME        NOT NULL DEFAULT GETDATE(),
    CONSTRAINT [FK_TB_NguoiGui] FOREIGN KEY ([NguoiGuiID]) REFERENCES [dbo].[NguoiDung]([NguoiDungID])
);
GO

-- ========== Bảng 2: ThongBaoNguoiDung ==========
-- Bảng trung gian: theo dõi trạng thái đã đọc/chưa đọc cho từng user.
CREATE TABLE [dbo].[ThongBaoNguoiDung] (
    [ThongBaoNguoiDungID]  INT IDENTITY(1,1) PRIMARY KEY,
    [ThongBaoID]           INT   NOT NULL,
    [NguoiDungID]          INT   NOT NULL,
    [DaDoc]                BIT   NOT NULL DEFAULT 0,
    [NgayDoc]              DATETIME NULL,
    CONSTRAINT [FK_TBND_ThongBao]  FOREIGN KEY ([ThongBaoID])  REFERENCES [dbo].[ThongBao]([ThongBaoID]),
    CONSTRAINT [FK_TBND_NguoiDung] FOREIGN KEY ([NguoiDungID]) REFERENCES [dbo].[NguoiDung]([NguoiDungID]),
    CONSTRAINT [UQ_TBND] UNIQUE ([ThongBaoID], [NguoiDungID])
);
GO

-- ========== Bảng 3: FCMToken ==========
-- Lưu Firebase Cloud Messaging device token của người dùng.
-- Mỗi user có thể đăng nhập nhiều thiết bị -> nhiều token.
CREATE TABLE [dbo].[FCMToken] (
    [TokenID]      INT IDENTITY(1,1) PRIMARY KEY,
    [NguoiDungID]  INT           NOT NULL,
    [Token]        VARCHAR(500)  NOT NULL,
    [DeviceName]   NVARCHAR(100) NULL,
    [NgayCapNhat]  DATETIME      NOT NULL DEFAULT GETDATE(),
    CONSTRAINT [FK_FCM_NguoiDung] FOREIGN KEY ([NguoiDungID]) REFERENCES [dbo].[NguoiDung]([NguoiDungID])
);
GO

PRINT N'Đã tạo thành công 3 bảng: ThongBao, ThongBaoNguoiDung, FCMToken';
GO
