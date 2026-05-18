-- ============================================================================
-- MIGRATION: Checkout & E-Contract Module
-- Date: 2026-05-16
-- Description: Thêm bảng và cột mới cho module thanh toán, hợp đồng điện tử
-- ============================================================================

-- 1. Cập nhật bảng NguoiDung — Thêm thông tin CCCD cho hợp đồng
ALTER TABLE [dbo].[NguoiDung]
    ADD [CCCD]            VARCHAR(20) NULL,
        [CccdNgayCap]     DATETIME NULL,
        [CccdNoiCap]      NVARCHAR(200) NULL,
        [DonViCongTac]    NVARCHAR(200) NULL;
GO

-- 2. Bảng DiaChiGiaoHang — Quản lý nhiều địa chỉ giao hàng
CREATE TABLE [dbo].[DiaChiGiaoHang](
    [DiaChiID]       INT IDENTITY(1,1) NOT NULL,
    [NguoiDungID]    INT NOT NULL,
    [TenNguoiNhan]   NVARCHAR(150) NOT NULL,
    [SoDienThoai]    VARCHAR(20) NOT NULL,
    [TinhThanhPho]   NVARCHAR(100) NOT NULL,
    [PhuongXa]       NVARCHAR(100) NOT NULL,
    [DiaChiChiTiet]  NVARCHAR(500) NOT NULL,
    [DonVi]          NVARCHAR(200) NULL,
    [LoaiDiaChi]     TINYINT NOT NULL DEFAULT 2,
    [LaMacDinh]      BIT NOT NULL DEFAULT 0,
    [NgayTao]        DATETIME NOT NULL DEFAULT GETDATE(),
    [NgayCapNhat]    DATETIME NULL,
    CONSTRAINT [PK_DiaChiGiaoHang] PRIMARY KEY ([DiaChiID]),
    CONSTRAINT [FK_DCGH_NguoiDung] FOREIGN KEY ([NguoiDungID])
        REFERENCES [dbo].[NguoiDung]([NguoiDungID])
);
GO

-- 3. Bảng ThanhToan — Lịch sử giao dịch thanh toán
CREATE TABLE [dbo].[ThanhToan](
    [ThanhToanID]       INT IDENTITY(1,1) NOT NULL,
    [HopDongID]         INT NOT NULL,
    [PhuongThuc]        TINYINT NOT NULL,
    [LoaiThanhToan]     TINYINT NOT NULL DEFAULT 1,
    [MaGiaoDich]        VARCHAR(100) NULL,
    [SoTien]            DECIMAL(18,2) NOT NULL,
    [TrangThai]         TINYINT NOT NULL DEFAULT 0,
    [NgayThanhToan]     DATETIME NULL,
    [NgayTao]           DATETIME NOT NULL DEFAULT GETDATE(),
    [GhiChu]            NVARCHAR(500) NULL,
    CONSTRAINT [PK_ThanhToan] PRIMARY KEY ([ThanhToanID]),
    CONSTRAINT [FK_TT_HopDong] FOREIGN KEY ([HopDongID])
        REFERENCES [dbo].[HopDongThue]([HopDongID])
);
GO

-- 4. Bảng ChuKyDienTu — Chữ ký số khách hàng
CREATE TABLE [dbo].[ChuKyDienTu](
    [ChuKyID]         INT IDENTITY(1,1) NOT NULL,
    [HopDongID]       INT NOT NULL,
    [NguoiDungID]     INT NOT NULL,
    [DuLieuChuKy]     VARBINARY(MAX) NOT NULL,
    [MaPinHash]       VARCHAR(255) NOT NULL,
    [NgayKy]          DATETIME NOT NULL DEFAULT GETDATE(),
    [IpAddress]       VARCHAR(45) NULL,
    [ThietBiKy]       NVARCHAR(200) NULL,
    CONSTRAINT [PK_ChuKyDienTu] PRIMARY KEY ([ChuKyID]),
    CONSTRAINT [FK_CKDT_HopDong] FOREIGN KEY ([HopDongID])
        REFERENCES [dbo].[HopDongThue]([HopDongID]),
    CONSTRAINT [FK_CKDT_NguoiDung] FOREIGN KEY ([NguoiDungID])
        REFERENCES [dbo].[NguoiDung]([NguoiDungID])
);
GO

-- 5. Bảng DieuKhoanMauHopDong — Mẫu điều khoản hợp đồng
CREATE TABLE [dbo].[DieuKhoanMauHopDong](
    [DieuKhoanID]     INT IDENTITY(1,1) NOT NULL,
    [SoDieu]          INT NOT NULL,
    [TieuDe]          NVARCHAR(200) NOT NULL,
    [NoiDung]         NVARCHAR(MAX) NOT NULL,
    [PhienBan]        INT NOT NULL DEFAULT 1,
    [DangHoatDong]    BIT NOT NULL DEFAULT 1,
    [NgayTao]         DATETIME NOT NULL DEFAULT GETDATE(),
    [NgayCapNhat]     DATETIME NULL,
    CONSTRAINT [PK_DieuKhoanMauHD] PRIMARY KEY ([DieuKhoanID])
);
GO

-- 6. Cập nhật bảng ThietBi — Thêm thông tin cho hợp đồng
ALTER TABLE [dbo].[ThietBi]
    ADD [SoSerial]          VARCHAR(50) NULL,
        [MucDichSuDung]     NVARCHAR(500) NULL,
        [GiaTriMay]         DECIMAL(18,2) NULL,
        [NgayKiemDinh]      DATETIME NULL,
        [TinhTrangBanGiao]  NVARCHAR(1000) NULL;
GO

-- 7. Cập nhật bảng HopDongThue — Mở rộng cho thanh toán online
ALTER TABLE [dbo].[HopDongThue]
    ADD [DiaChiGiaoID]          INT NULL,
        [PhuongThucThanhToan]   TINYINT NULL,
        [ThueVAT]               DECIMAL(18,2) NOT NULL DEFAULT 0,
        [PhiTreHanPhanTram]     DECIMAL(5,2) NOT NULL DEFAULT 3.0,
        [SoNgayTreHanMoiKy]     INT NOT NULL DEFAULT 3,
        [SoNgayViPhamChamDut]   INT NOT NULL DEFAULT 15,
        [PhiVeSinhChuyenSau]    DECIMAL(18,2) NOT NULL DEFAULT 1000000,
        [KhauHaoHaoMonNam]      DECIMAL(18,2) NULL,
        [PhiGianDoanPhanTram]   DECIMAL(5,2) NOT NULL DEFAULT 50.0,
        [GhiChuKhachHang]      NVARCHAR(500) NULL,
        [NgayKyDienTu]          DATETIME NULL,
        [MaPinXacNhan]          VARCHAR(255) NULL;
GO

ALTER TABLE [dbo].[HopDongThue]
    ADD CONSTRAINT [FK_HD_DiaChi] FOREIGN KEY ([DiaChiGiaoID])
        REFERENCES [dbo].[DiaChiGiaoHang]([DiaChiID]);
GO

-- 8. Bảng ChiTietThueThietBi — Chi tiết thiết bị trong hợp đồng
CREATE TABLE [dbo].[ChiTietThueThietBi](
    [ChiTietID]       INT IDENTITY(1,1) NOT NULL,
    [HopDongID]       INT NOT NULL,
    [ThietBiID]       INT NOT NULL,
    [GiaThueThang]    DECIMAL(18,2) NOT NULL,
    [GiaTriMay]       DECIMAL(18,2) NOT NULL,
    [TinhTrangGiao]   NVARCHAR(1000) NULL,
    [TinhTrangTra]    NVARCHAR(1000) NULL,
    [NgayGiao]        DATETIME NULL,
    [NgayTra]         DATETIME NULL,
    CONSTRAINT [PK_ChiTietThueThietBi] PRIMARY KEY ([ChiTietID]),
    CONSTRAINT [FK_CTTB_HopDong] FOREIGN KEY ([HopDongID])
        REFERENCES [dbo].[HopDongThue]([HopDongID]),
    CONSTRAINT [FK_CTTB_ThietBi] FOREIGN KEY ([ThietBiID])
        REFERENCES [dbo].[ThietBi]([ThietBiID])
);
GO

-- 9. Cập nhật trạng thái hợp đồng mới
DELETE FROM [dbo].[TrangThaiHopDong];
GO

INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (1, N'Chờ ký kết');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (2, N'Đã ký - Chờ thanh toán');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (3, N'Đã thanh toán - Chờ phê duyệt');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (4, N'Đã phê duyệt - Chờ giao hàng');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (5, N'Đang cho thuê');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (6, N'Quá hạn thanh toán');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (7, N'Vi phạm - Chấm dứt');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (8, N'Đã trả thiết bị');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (9, N'Đã hoàn tất');
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (10, N'Đã hủy');
GO

-- 10. Dữ liệu mẫu điều khoản hợp đồng
INSERT [dbo].[DieuKhoanMauHopDong] ([SoDieu], [TieuDe], [NoiDung]) VALUES 
(1, N'THÔNG TIN THIẾT BỊ THUÊ', N'Bên A đồng ý cho Bên B thuê thiết bị với thông tin chi tiết như bảng danh sách thiết bị đính kèm hợp đồng này.');
INSERT [dbo].[DieuKhoanMauHopDong] ([SoDieu], [TieuDe], [NoiDung]) VALUES 
(2, N'CHI PHÍ, THỜI GIAN THUÊ VÀ THANH TOÁN', N'Đơn giá thuê, thời hạn thuê và phương thức thanh toán theo thỏa thuận giữa hai bên, được ghi nhận tại thời điểm ký kết hợp đồng.');
INSERT [dbo].[DieuKhoanMauHopDong] ([SoDieu], [TieuDe], [NoiDung]) VALUES 
(3, N'QUYỀN VÀ NGHĨA VỤ CỦA BÊN A', N'Bên A có trách nhiệm giao thiết bị đúng chất lượng, đúng hạn. Hỗ trợ kỹ thuật trong suốt thời gian thuê.');
INSERT [dbo].[DieuKhoanMauHopDong] ([SoDieu], [TieuDe], [NoiDung]) VALUES 
(4, N'QUYỀN VÀ NGHĨA VỤ CỦA BÊN B', N'Bên B có trách nhiệm sử dụng thiết bị đúng mục đích, bảo quản cẩn thận. Thanh toán đúng hạn theo hợp đồng.');
INSERT [dbo].[DieuKhoanMauHopDong] ([SoDieu], [TieuDe], [NoiDung]) VALUES 
(5, N'BỒI THƯỜNG THIỆT HẠI VÀ PHẠT VI PHẠM', N'Hao mòn tự nhiên: Bên A chịu. Hư hỏng do sử dụng sai: Bên B chịu chi phí sửa chữa thực tế. Mất thiết bị: Bên B bồi thường 100% giá trị tài sản.');
INSERT [dbo].[DieuKhoanMauHopDong] ([SoDieu], [TieuDe], [NoiDung]) VALUES 
(6, N'ĐIỀU KHOẢN CHUNG', N'Hợp đồng có hiệu lực từ ngày ký. Mọi tranh chấp được giải quyết trên tinh thần hợp tác. Trường hợp không thỏa thuận được, hai bên đưa ra cơ quan có thẩm quyền giải quyết.');
GO
