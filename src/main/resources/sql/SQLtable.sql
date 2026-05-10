/****** Object:  Database [QuanLyThietBi]    Script Date: 5/10/2026 2:43:43 PM ******/
CREATE DATABASE [QuanLyThietBi]  (EDITION = 'Standard', SERVICE_OBJECTIVE = 'S0', MAXSIZE = 250 GB) WITH CATALOG_COLLATION = SQL_Latin1_General_CP1_CI_AS, LEDGER = OFF;
GO
ALTER DATABASE [QuanLyThietBi] SET COMPATIBILITY_LEVEL = 160
GO
ALTER DATABASE [QuanLyThietBi] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET ARITHABORT OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QuanLyThietBi] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QuanLyThietBi] SET READ_COMMITTED_SNAPSHOT ON 
GO
ALTER DATABASE [QuanLyThietBi] SET  MULTI_USER 
GO
ALTER DATABASE [QuanLyThietBi] SET AUTOMATIC_INDEX_COMPACTION = OFF 
GO
ALTER DATABASE [QuanLyThietBi] SET ENCRYPTION ON
GO
ALTER DATABASE [QuanLyThietBi] SET QUERY_STORE = ON
GO
ALTER DATABASE [QuanLyThietBi] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
/****** Object:  Table [dbo].[LoaiThietBi]    Script Date: 5/10/2026 2:43:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiThietBi](
	[LoaiThietBiID] [int] IDENTITY(1,1) NOT NULL,
	[DanhMucID] [int] NOT NULL,
	[NhaCungCapID] [int] NOT NULL,
	[TenLoaiThietBi] [nvarchar](150) NOT NULL,
	[ThongSoKyThuat] [nvarchar](max) NULL,
	[GiaThueThamKhao] [decimal](18, 2) NOT NULL,
	[AnhDaiDien] [varchar](500) NULL,
 CONSTRAINT [PK_LoaiThietBi] PRIMARY KEY CLUSTERED 
(
	[LoaiThietBiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TinhTrangThietBi]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TinhTrangThietBi](
	[TinhTrangID] [tinyint] NOT NULL,
	[TenTinhTrang] [nvarchar](50) NOT NULL,
	[MoTa] [nvarchar](255) NULL,
 CONSTRAINT [PK_TinhTrangThietBi] PRIMARY KEY CLUSTERED 
(
	[TinhTrangID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ThietBi]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ThietBi](
	[ThietBiID] [int] IDENTITY(1,1) NOT NULL,
	[LoaiThietBiID] [int] NOT NULL,
	[MaTaiSan] [varchar](50) NOT NULL,
	[TinhTrangID] [tinyint] NOT NULL,
	[KhoHienTaiID] [int] NOT NULL,
	[NgayBaoTriTiepTheo] [datetime] NULL,
 CONSTRAINT [PK_ThietBi] PRIMARY KEY CLUSTERED 
(
	[ThietBiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ChiTietThue]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietThue](
	[HopDongID] [int] NOT NULL,
	[ThietBiID] [int] NOT NULL,
	[GiaThueThucTe] [decimal](18, 2) NOT NULL,
 CONSTRAINT [PK_ChiTietThue] PRIMARY KEY CLUSTERED 
(
	[HopDongID] ASC,
	[ThietBiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Kho]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Kho](
	[KhoID] [int] IDENTITY(1,1) NOT NULL,
	[TenKho] [nvarchar](200) NOT NULL,
	[DiaChi] [nvarchar](500) NULL,
	[NguoiPhuTrach] [nvarchar](100) NULL,
	[SoDienThoai] [varchar](20) NULL,
 CONSTRAINT [PK_Kho] PRIMARY KEY CLUSTERED 
(
	[KhoID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[v_ChiTietHopDong]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- View thiết bị trong từng hợp đồng kèm thông tin chi tiết
--   Dùng cho màn hình chi tiết hợp đồng (cả online lẫn offline cache)
CREATE VIEW [dbo].[v_ChiTietHopDong] AS
SELECT
    ctt.HopDongID,
    tb.ThietBiID,
    tb.MaTaiSan,
    ltb.TenLoaiThietBi,
    ltb.ThongSoKyThuat,
    ltb.AnhDaiDien,
    ctt.GiaThueThucTe,
    tt.TenTinhTrang     AS TinhTrangHienTai,
    k.TenKho            AS KhoHienTai
FROM  ChiTietThue       ctt
JOIN  ThietBi           tb  ON tb.ThietBiID       = ctt.ThietBiID
JOIN  LoaiThietBi       ltb ON ltb.LoaiThietBiID  = tb.LoaiThietBiID
JOIN  TinhTrangThietBi  tt  ON tt.TinhTrangID      = tb.TinhTrangID
JOIN  Kho               k   ON k.KhoID             = tb.KhoHienTaiID;
GO
/****** Object:  Table [dbo].[NguoiDung]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NguoiDung](
	[NguoiDungID] [int] IDENTITY(1,1) NOT NULL,
	[MaNguoiDung] [varchar](20) NOT NULL,
	[HoTen] [nvarchar](150) NOT NULL,
	[TaiKhoan] [varchar](100) NOT NULL,
	[MatKhau] [varchar](255) NOT NULL,
	[VaiTroID] [tinyint] NOT NULL,
	[KhoID] [int] NULL,
	[SoDienThoai] [varchar](20) NULL,
	[Email] [varchar](100) NULL,
	[DiaChi] [nvarchar](255) NULL,
	[LoaiKhachHangID] [tinyint] NULL,
	[MaSoThue] [varchar](50) NULL,
	[TrangThaiID] [tinyint] NOT NULL,
	[NgayTao] [datetime] NOT NULL,
	[LanDangNhapCuoi] [datetime] NULL,
	[DoiMatKhauLanDau] [bit] NOT NULL,
 CONSTRAINT [PK_NguoiDung] PRIMARY KEY CLUSTERED 
(
	[NguoiDungID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TrangThaiHopDong]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TrangThaiHopDong](
	[TrangThaiID] [tinyint] NOT NULL,
	[TenTrangThai] [nvarchar](60) NOT NULL,
 CONSTRAINT [PK_TrangThaiHopDong] PRIMARY KEY CLUSTERED 
(
	[TrangThaiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HopDongThue]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HopDongThue](
	[HopDongID] [int] IDENTITY(1,1) NOT NULL,
	[NguoiDungKhachID] [int] NOT NULL,
	[NguoiDungTaoID] [int] NOT NULL,
	[NgayLap] [datetime] NOT NULL,
	[NgayBatDauThue] [datetime] NOT NULL,
	[NgayDuKienTra] [datetime] NOT NULL,
	[NgayTraThucTe] [datetime] NULL,
	[DiaDiemGiao] [nvarchar](500) NOT NULL,
	[TienCoc] [decimal](18, 2) NOT NULL,
	[TongTienThue] [decimal](18, 2) NOT NULL,
	[PhiBoiThuong] [decimal](18, 2) NOT NULL,
	[TrangThaiID] [tinyint] NOT NULL,
	[NguonTao] [tinyint] NOT NULL,
	[GhiChu] [nvarchar](500) NULL,
 CONSTRAINT [PK_HopDongThue] PRIMARY KEY CLUSTERED 
(
	[HopDongID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[v_HopDongTongQuan]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- View tổng quan hợp đồng kèm thông tin khách hàng
CREATE VIEW [dbo].[v_HopDongTongQuan] AS
SELECT
    hd.HopDongID,
    hd.NgayLap,
    hd.NgayBatDauThue,
    hd.NgayDuKienTra,
    hd.NgayTraThucTe,
    hd.DiaDiemGiao,
    hd.TienCoc,
    hd.TongTienThue,
    hd.PhiBoiThuong,
    hd.TongTienThue + hd.PhiBoiThuong - hd.TienCoc AS SoTienThanhToan,
    hd.NguonTao,
    tt.TenTrangThai AS TrangThai,
    kh.NguoiDungID  AS KhachHangID,
    kh.HoTen        AS TenKhachHang,
    kh.SoDienThoai  AS DienThoaiKH,
    kh.Email        AS EmailKH,
    nd.HoTen        AS NguoiTao
FROM  HopDongThue       hd
JOIN  NguoiDung         kh ON kh.NguoiDungID = hd.NguoiDungKhachID
JOIN  NguoiDung         nd ON nd.NguoiDungID = hd.NguoiDungTaoID
JOIN  TrangThaiHopDong  tt ON tt.TrangThaiID = hd.TrangThaiID;
GO
/****** Object:  Table [dbo].[LichSuBaoTri]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LichSuBaoTri](
	[BaoTriID] [int] IDENTITY(1,1) NOT NULL,
	[ThietBiID] [int] NOT NULL,
	[NguoiDungBaoTriID] [int] NOT NULL,
	[HopDongID] [int] NULL,
	[NgayThucHien] [datetime] NOT NULL,
	[LoaiBaoTriID] [tinyint] NOT NULL,
	[NoiDungBaoTri] [nvarchar](max) NOT NULL,
	[ChiPhi] [decimal](18, 2) NOT NULL,
	[TrangThaiID] [tinyint] NOT NULL,
	[TinhVaoBoiThuong] [bit] NOT NULL,
	[GhiChu] [nvarchar](500) NULL,
 CONSTRAINT [PK_LichSuBaoTri] PRIMARY KEY CLUSTERED 
(
	[BaoTriID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  View [dbo].[v_ThanhLyHopDong]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- View bảng thanh lý cuối kỳ
CREATE VIEW [dbo].[v_ThanhLyHopDong] AS
SELECT
    hd.HopDongID,
    hd.TongTienThue,
    hd.PhiBoiThuong,
    hd.TienCoc,
    hd.TongTienThue + hd.PhiBoiThuong - hd.TienCoc AS SoTienConLai,
    CASE
        WHEN hd.TongTienThue + hd.PhiBoiThuong - hd.TienCoc > 0
        THEN N'Khách hàng cần thanh toán thêm'
        WHEN hd.TongTienThue + hd.PhiBoiThuong - hd.TienCoc < 0
        THEN N'Hoàn tiền cho khách hàng'
        ELSE N'Không cần thanh toán thêm'
    END AS GhiChuThanhLy,
    -- Chi tiết bồi thường
    (
        SELECT COUNT(*) FROM LichSuBaoTri lsbt
        WHERE lsbt.HopDongID = hd.HopDongID AND lsbt.TinhVaoBoiThuong = 1
    ) AS SoPhieuBoiThuong
FROM HopDongThue hd;
GO
/****** Object:  Table [dbo].[DanhMucThietBi]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DanhMucThietBi](
	[DanhMucID] [int] IDENTITY(1,1) NOT NULL,
	[TenDanhMuc] [nvarchar](100) NOT NULL,
 CONSTRAINT [PK_DanhMucThietBi] PRIMARY KEY CLUSTERED 
(
	[DanhMucID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[v_ThietBiSanSang]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- ============================================================================
-- NHÓM 13: VIEW TIỆN ÍCH
-- ============================================================================

-- View danh sách thiết bị sẵn sàng cho thuê  (dùng cho màn hình KH duyệt danh mục)
CREATE VIEW [dbo].[v_ThietBiSanSang] AS
SELECT
    tb.ThietBiID,
    tb.MaTaiSan,
    ltb.TenLoaiThietBi,
    ltb.ThongSoKyThuat,
    ltb.GiaThueThamKhao,
    ltb.AnhDaiDien,
    dm.TenDanhMuc,
    k.TenKho,
    k.DiaChi    AS DiaChiKho
FROM  ThietBi       tb
JOIN  LoaiThietBi   ltb ON ltb.LoaiThietBiID = tb.LoaiThietBiID
JOIN  DanhMucThietBi dm  ON dm.DanhMucID     = ltb.DanhMucID
JOIN  Kho           k   ON k.KhoID           = tb.KhoHienTaiID
WHERE tb.TinhTrangID = 1;   -- chỉ "Sẵn sàng"
GO
/****** Object:  Table [dbo].[ChiTietPhieuNhap]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietPhieuNhap](
	[PhieuNhapID] [int] NOT NULL,
	[LoaiThietBiID] [int] NOT NULL,
	[SoLuong] [int] NOT NULL,
	[DonGiaNhap] [decimal](18, 2) NOT NULL,
 CONSTRAINT [PK_ChiTietPhieuNhap] PRIMARY KEY CLUSTERED 
(
	[PhieuNhapID] ASC,
	[LoaiThietBiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DieuKhoanHopDong]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DieuKhoanHopDong](
	[DieuKhoanID] [int] IDENTITY(1,1) NOT NULL,
	[TieuDe] [nvarchar](200) NOT NULL,
	[NoiDung] [nvarchar](max) NOT NULL,
	[PhienBan] [varchar](20) NOT NULL,
	[NgayHieuLuc] [date] NOT NULL,
	[IsActive] [bit] NOT NULL,
 CONSTRAINT [PK_DieuKhoanHopDong] PRIMARY KEY CLUSTERED 
(
	[DieuKhoanID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[FCMToken]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[FCMToken](
	[TokenID] [int] IDENTITY(1,1) NOT NULL,
	[NguoiDungID] [int] NOT NULL,
	[Token] [varchar](500) NOT NULL,
	[DeviceName] [nvarchar](100) NULL,
	[NgayCapNhat] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[TokenID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GioHang]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GioHang](
	[GioHangID] [int] IDENTITY(1,1) NOT NULL,
	[NguoiDungID] [int] NOT NULL,
	[LoaiThietBiID] [int] NOT NULL,
	[SoLuong] [int] NOT NULL,
	[NgayThem] [datetime] NOT NULL,
 CONSTRAINT [PK_GioHang] PRIMARY KEY CLUSTERED 
(
	[GioHangID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HinhAnhThietBi]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HinhAnhThietBi](
	[HinhAnhID] [int] IDENTITY(1,1) NOT NULL,
	[ThietBiID] [int] NOT NULL,
	[NguoiDungChupID] [int] NOT NULL,
	[UrlAnh] [varchar](500) NOT NULL,
	[LoaiAnhID] [tinyint] NOT NULL,
	[NgayChup] [datetime] NOT NULL,
	[BanGiaoID] [int] NULL,
	[BaoTriID] [int] NULL,
 CONSTRAINT [PK_HinhAnhThietBi] PRIMARY KEY CLUSTERED 
(
	[HinhAnhID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LichSuBanGiao]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LichSuBanGiao](
	[BanGiaoID] [int] IDENTITY(1,1) NOT NULL,
	[HopDongID] [int] NULL,
	[ThietBiID] [int] NOT NULL,
	[TuKhoID] [int] NOT NULL,
	[DenKhoID] [int] NULL,
	[NguoiDungThucHienID] [int] NOT NULL,
	[NgayGiaoNhan] [datetime] NOT NULL,
	[NguoiNhan] [nvarchar](100) NULL,
	[HinhAnhXacNhan] [varchar](500) NULL,
	[LoaiGiaoDichID] [tinyint] NOT NULL,
	[MucDanhGiaID] [tinyint] NULL,
	[GhiChu_TinhTrang] [nvarchar](500) NULL,
 CONSTRAINT [PK_LichSuBanGiao] PRIMARY KEY CLUSTERED 
(
	[BanGiaoID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoaiAnh]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiAnh](
	[LoaiAnhID] [tinyint] NOT NULL,
	[TenLoai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_LoaiAnh] PRIMARY KEY CLUSTERED 
(
	[LoaiAnhID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoaiBaoTri]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiBaoTri](
	[LoaiBaoTriID] [tinyint] NOT NULL,
	[TenLoai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_LoaiBaoTri] PRIMARY KEY CLUSTERED 
(
	[LoaiBaoTriID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoaiGiaoDich]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiGiaoDich](
	[LoaiGiaoDichID] [tinyint] NOT NULL,
	[TenLoai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_LoaiGiaoDich] PRIMARY KEY CLUSTERED 
(
	[LoaiGiaoDichID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoaiKhachHang]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiKhachHang](
	[LoaiID] [tinyint] NOT NULL,
	[TenLoai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_LoaiKhachHang] PRIMARY KEY CLUSTERED 
(
	[LoaiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MucDanhGia]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MucDanhGia](
	[MucDanhGiaID] [tinyint] NOT NULL,
	[TenMuc] [nvarchar](60) NOT NULL,
	[TinhVaoBoiThuong] [bit] NOT NULL,
	[MoTa] [nvarchar](255) NULL,
 CONSTRAINT [PK_MucDanhGia] PRIMARY KEY CLUSTERED 
(
	[MucDanhGiaID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NhaCungCap]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhaCungCap](
	[NhaCungCapID] [int] IDENTITY(1,1) NOT NULL,
	[TenNhaCungCap] [nvarchar](150) NOT NULL,
	[NguoiLienHe] [nvarchar](100) NULL,
	[SoDienThoai] [varchar](20) NULL,
	[DiaChi] [nvarchar](255) NULL,
 CONSTRAINT [PK_NhaCungCap] PRIMARY KEY CLUSTERED 
(
	[NhaCungCapID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PhieuNhap]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PhieuNhap](
	[PhieuNhapID] [int] IDENTITY(1,1) NOT NULL,
	[NhaCungCapID] [int] NOT NULL,
	[NguoiDungNhapID] [int] NOT NULL,
	[NgayNhap] [datetime] NOT NULL,
	[TongTienNhap] [decimal](18, 2) NOT NULL,
	[GhiChu] [nvarchar](500) NULL,
 CONSTRAINT [PK_PhieuNhap] PRIMARY KEY CLUSTERED 
(
	[PhieuNhapID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ThongBao]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ThongBao](
	[ThongBaoID] [int] IDENTITY(1,1) NOT NULL,
	[TieuDe] [nvarchar](200) NOT NULL,
	[NoiDung] [nvarchar](max) NOT NULL,
	[LoaiThongBao] [tinyint] NOT NULL,
	[NguoiGuiID] [int] NOT NULL,
	[VaiTroNhanID] [tinyint] NULL,
	[NgayTao] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ThongBaoID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ThongBaoNguoiDung]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ThongBaoNguoiDung](
	[ThongBaoNguoiDungID] [int] IDENTITY(1,1) NOT NULL,
	[ThongBaoID] [int] NOT NULL,
	[NguoiDungID] [int] NOT NULL,
	[DaDoc] [bit] NOT NULL,
	[NgayDoc] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ThongBaoNguoiDungID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TrangThaiBaoTri]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TrangThaiBaoTri](
	[TrangThaiID] [tinyint] NOT NULL,
	[TenTrangThai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_TrangThaiBaoTri] PRIMARY KEY CLUSTERED 
(
	[TrangThaiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TrangThaiNguoiDung]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TrangThaiNguoiDung](
	[TrangThaiID] [tinyint] NOT NULL,
	[TenTrangThai] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_TrangThaiNguoiDung] PRIMARY KEY CLUSTERED 
(
	[TrangThaiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[VaiTro]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[VaiTro](
	[VaiTroID] [tinyint] NOT NULL,
	[TenVaiTro] [nvarchar](50) NOT NULL,
	[MoTa] [nvarchar](255) NULL,
 CONSTRAINT [PK_VaiTro] PRIMARY KEY CLUSTERED 
(
	[VaiTroID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[XacNhanDieuKhoan]    Script Date: 5/10/2026 2:43:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[XacNhanDieuKhoan](
	[HopDongID] [int] NOT NULL,
	[DieuKhoanID] [int] NOT NULL,
	[ThoiGianXacNhan] [datetime] NOT NULL,
	[DiaChiIP] [varchar](50) NULL,
 CONSTRAINT [PK_XacNhanDieuKhoan] PRIMARY KEY CLUSTERED 
(
	[HopDongID] ASC,
	[DieuKhoanID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[DanhMucThietBi] ON 

INSERT [dbo].[DanhMucThietBi] ([DanhMucID], [TenDanhMuc]) VALUES (1, N'Y tế')
INSERT [dbo].[DanhMucThietBi] ([DanhMucID], [TenDanhMuc]) VALUES (2, N'Giáo dục')
INSERT [dbo].[DanhMucThietBi] ([DanhMucID], [TenDanhMuc]) VALUES (3, N'Sự kiện')
INSERT [dbo].[DanhMucThietBi] ([DanhMucID], [TenDanhMuc]) VALUES (4, N'Công trình')
INSERT [dbo].[DanhMucThietBi] ([DanhMucID], [TenDanhMuc]) VALUES (5, N'Công nghiệp')
INSERT [dbo].[DanhMucThietBi] ([DanhMucID], [TenDanhMuc]) VALUES (6, N'Văn Phòng')
INSERT [dbo].[DanhMucThietBi] ([DanhMucID], [TenDanhMuc]) VALUES (7, N'Cứu hộ')
SET IDENTITY_INSERT [dbo].[DanhMucThietBi] OFF
GO
SET IDENTITY_INSERT [dbo].[DieuKhoanHopDong] ON 

INSERT [dbo].[DieuKhoanHopDong] ([DieuKhoanID], [TieuDe], [NoiDung], [PhienBan], [NgayHieuLuc], [IsActive]) VALUES (1, N'Điều khoản thuê thiết bị v1.0', N'## 1. Bồi thường thiệt hại
- Hao mòn tự nhiên: bên cho thuê chịu.
- Hư hỏng do sử dụng sai: bên thuê chịu chi phí sửa chữa thực tế.
- Mất thiết bị: bên thuê bồi thường 100% giá trị tài sản.

## 2. Bảo trì trong thời gian thuê
- Lỗi kỹ thuật không do sử dụng sai: bên cho thuê xử lý.
- Khách hàng không tự ý sửa chữa; phải thông báo ngay khi phát sinh sự cố.

## 3. Thanh toán và tiền cọc
- Tiền cọc khấu trừ vào số tiền thanh toán cuối kỳ.
- Thanh toán cuối kỳ = Tổng tiền thuê + Phí bồi thường − Tiền cọc.
- Thời hạn thanh toán: 3 ngày làm việc kể từ khi xuất hoá đơn thanh lý.', N'1.0', CAST(N'2026-04-21' AS Date), 1)
SET IDENTITY_INSERT [dbo].[DieuKhoanHopDong] OFF
GO
SET IDENTITY_INSERT [dbo].[Kho] ON 

INSERT [dbo].[Kho] ([KhoID], [TenKho], [DiaChi], [NguoiPhuTrach], [SoDienThoai]) VALUES (1, N'Kho Tổng TP.HCM', N'Quận 12, TP.HCM', N'Nguyễn Văn A', N'028-1234-5678')
INSERT [dbo].[Kho] ([KhoID], [TenKho], [DiaChi], [NguoiPhuTrach], [SoDienThoai]) VALUES (2, N'Chi nhánh Đà Nẵng', N'Hải Châu, Đà Nẵng', N'Trần Thị B', N'0236-1234-5678')
SET IDENTITY_INSERT [dbo].[Kho] OFF
GO
INSERT [dbo].[LoaiAnh] ([LoaiAnhID], [TenLoai]) VALUES (1, N'Nhập kho')
INSERT [dbo].[LoaiAnh] ([LoaiAnhID], [TenLoai]) VALUES (2, N'Xuất kho')
INSERT [dbo].[LoaiAnh] ([LoaiAnhID], [TenLoai]) VALUES (3, N'Biên bản bảo trì')
INSERT [dbo].[LoaiAnh] ([LoaiAnhID], [TenLoai]) VALUES (4, N'Ghi nhận sự cố')
INSERT [dbo].[LoaiAnh] ([LoaiAnhID], [TenLoai]) VALUES (5, N'Chữ ký xác nhận')
GO
INSERT [dbo].[LoaiBaoTri] ([LoaiBaoTriID], [TenLoai]) VALUES (1, N'Bảo trì định kỳ')
INSERT [dbo].[LoaiBaoTri] ([LoaiBaoTriID], [TenLoai]) VALUES (2, N'Sửa chữa sự cố')
INSERT [dbo].[LoaiBaoTri] ([LoaiBaoTriID], [TenLoai]) VALUES (3, N'Nâng cấp / cải tiến')
GO
INSERT [dbo].[LoaiGiaoDich] ([LoaiGiaoDichID], [TenLoai]) VALUES (1, N'Xuất cho thuê')
INSERT [dbo].[LoaiGiaoDich] ([LoaiGiaoDichID], [TenLoai]) VALUES (2, N'Thu hồi từ khách')
INSERT [dbo].[LoaiGiaoDich] ([LoaiGiaoDichID], [TenLoai]) VALUES (3, N'Chuyển kho nội bộ')
GO
INSERT [dbo].[LoaiKhachHang] ([LoaiID], [TenLoai]) VALUES (1, N'Cá nhân')
INSERT [dbo].[LoaiKhachHang] ([LoaiID], [TenLoai]) VALUES (2, N'Doanh nghiệp')
GO
SET IDENTITY_INSERT [dbo].[LoaiThietBi] ON 

INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (1, 1, 1, N'Máy tạo oxy Yuwell 5 Lít', N'{"luu_luong_L_phut":5,"nong_do_oxy":"93%","trong_luong_kg":15,"dien_ap":"220V"}', CAST(150000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085122.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (2, 1, 2, N'Giường bệnh nhân 2 tay quay y tế', N'{"chuc_nang":"Nâng lưng, nâng chân","tai_trong_kg":200,"kich_thuoc":"2000x900x500mm"}', CAST(200000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085156.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (3, 2, 1, N'Máy chiếu tương tác thông minh Sony', N'{"do_sang":"3500 lumens","do_phan_giai":"Full HD 1080p","tuoi_tho_bong_gio":10000}', CAST(350000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085243.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (4, 3, 2, N'Loa kéo công suất lớn JBL PartyBox', N'{"cong_suat_W":800,"thoi_gian_pin_h":12,"ket_noi":"Bluetooth, USB, AUX"}', CAST(400000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085313.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (5, 3, 1, N'Hệ thống đèn LED sân khấu Par 54', N'{"loai_bong":"LED 3W","so_luong_bong":54,"che_do_chay":"Tự động, cảm biến âm thanh"}', CAST(120000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085340.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (6, 4, 2, N'Máy trộn bê tông quả lê 350L', N'{"dung_tich_L":350,"dong_co":"Motor 3kW 1 pha","trong_luong_kg":150}', CAST(250000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085408.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (7, 4, 1, N'Khung giàn giáo chữ H 1.7m (Bộ 42kg)', N'{"chieu_cao_m":1.7,"do_day_ong_mm":2,"tai_trong_chuan_kg":2000}', CAST(45000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085443.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (8, 5, 2, N'Xe nâng tay Pallet Niuli 2.5 Tấn', N'{"tai_trong_nang_kg":2500,"chieu_cao_nang_cao_nhat_mm":200,"chieu_dai_cang_mm":1150}', CAST(100000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085504.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (9, 6, 1, N'Máy in đa năng HP LaserJet Pro', N'{"chuc_nang":"In, Copy, Scan 2 mặt","toc_do_trang_phut":38,"ket_noi":"LAN, Wifi"}', CAST(150000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085531.png')
INSERT [dbo].[LoaiThietBi] ([LoaiThietBiID], [DanhMucID], [NhaCungCapID], [TenLoaiThietBi], [ThongSoKyThuat], [GiaThueThamKhao], [AnhDaiDien]) VALUES (10, 7, 2, N'Bộ dụng cụ banh cắt thủy lực cứu hộ', N'{"luc_cat_toi_da_kN":350,"luc_banh_toi_da_kN":120,"trong_luong_kg":18.5}', CAST(600000.00 AS Decimal(18, 2)), N'Screenshot 2026-05-04 085616.png')
SET IDENTITY_INSERT [dbo].[LoaiThietBi] OFF
GO
INSERT [dbo].[MucDanhGia] ([MucDanhGiaID], [TenMuc], [TinhVaoBoiThuong], [MoTa]) VALUES (1, N'Tốt', 0, N'Hao mòn tự nhiên bình thường, không tính phí.')
INSERT [dbo].[MucDanhGia] ([MucDanhGiaID], [TenMuc], [TinhVaoBoiThuong], [MoTa]) VALUES (2, N'Hao mòn thông thường', 0, N'Trầy xước nhẹ trong mức chấp nhận, không tính phí.')
INSERT [dbo].[MucDanhGia] ([MucDanhGiaID], [TenMuc], [TinhVaoBoiThuong], [MoTa]) VALUES (3, N'Hư hỏng do sử dụng', 1, N'Phát sinh phiếu bảo trì, chi phí tính vào bồi thường.')
INSERT [dbo].[MucDanhGia] ([MucDanhGiaID], [TenMuc], [TinhVaoBoiThuong], [MoTa]) VALUES (4, N'Mất thiết bị', 1, N'100% giá trị tài sản; thiết bị bị thanh lý khỏi hệ thống.')
GO
SET IDENTITY_INSERT [dbo].[NguoiDung] ON 

INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (1, N'NV01', N'Nguyễn Văn Admin', N'admin', N'$2a$10$gXMH2dj6H/gie0hUeiu8C.8XQPQoovn8ffYjapDy5NIFr4H0f92Iu', 1, NULL, NULL, NULL, NULL, NULL, NULL, 1, CAST(N'2026-04-21T08:46:16.043' AS DateTime), CAST(N'2026-05-08T14:29:50.970' AS DateTime), 0)
INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (2, N'NV02', N'Trần Thủ Kho HCM', N'kho_hcm', N'$2b$12$placeholder', 2, 1, NULL, NULL, NULL, NULL, NULL, 1, CAST(N'2026-04-21T08:46:16.043' AS DateTime), NULL, 1)
INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (3, N'NV03', N'Lê Kỹ Thuật HCM', N'kythuat1', N'$2b$12$placeholder', 3, 1, NULL, NULL, NULL, NULL, NULL, 1, CAST(N'2026-04-21T08:46:16.043' AS DateTime), NULL, 1)
INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (4, N'NV04', N'Phạm Thủ Kho ĐN', N'kho_dn', N'$2b$12$placeholder', 2, 2, NULL, NULL, NULL, NULL, NULL, 1, CAST(N'2026-04-21T08:46:16.043' AS DateTime), NULL, 1)
INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (5, N'NV05', N'Hoàng Kỹ Thuật ĐN', N'kythuat2', N'$2b$12$placeholder', 3, 2, NULL, NULL, NULL, NULL, NULL, 1, CAST(N'2026-04-21T08:46:16.043' AS DateTime), NULL, 1)
INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (6, N'KH00001', N'Nguyễn Thị Khách', N'khachhang1@email.com', N'$2b$12$placeholder', 4, NULL, N'0901234567', N'khachhang1@email.com', N'Quận 1, TP.HCM', 1, NULL, 1, CAST(N'2026-04-21T08:46:16.047' AS DateTime), NULL, 0)
INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (7, N'KH00002', N'Công ty TNHH ABC', N'abc.company@email.com', N'$2b$12$placeholder', 4, NULL, N'0287654321', N'abc.company@email.com', N'Bình Dương', 2, N'0123456789', 1, CAST(N'2026-04-21T08:46:16.047' AS DateTime), NULL, 0)
INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (8, N'KH00003', N'Lô Hoàng Vũ', N'bao295770@gmail.com', N'$2a$10$gXMH2dj6H/gie0hUeiu8C.8XQPQoovn8ffYjapDy5NIFr4H0f92Iu', 4, NULL, N'0385489501', N'bao295770@gmail.com', N'74/6 Núi Thành, Tân Bình, Tp.HCM', 1, NULL, 1, CAST(N'2026-04-21T12:47:01.873' AS DateTime), CAST(N'2026-05-10T14:21:03.947' AS DateTime), 0)
INSERT [dbo].[NguoiDung] ([NguoiDungID], [MaNguoiDung], [HoTen], [TaiKhoan], [MatKhau], [VaiTroID], [KhoID], [SoDienThoai], [Email], [DiaChi], [LoaiKhachHangID], [MaSoThue], [TrangThaiID], [NgayTao], [LanDangNhapCuoi], [DoiMatKhauLanDau]) VALUES (9, N'KH00004', N'Lô Hoàng Vũ', N'hoangvu2004dl@gmail.com', N'$2a$10$g7kpcg.6Q0sGKRrh9gNU0eIBxhW33fdaKiUt0q6XraOnaT2Yvh9ui', 4, NULL, N'0385489591', N'hoangvu2004dl@gmail.com', N'74/6 Núi Thành, Tân Bình, Tp.HCM', 1, NULL, 1, CAST(N'2026-04-21T14:03:01.080' AS DateTime), NULL, 0)
SET IDENTITY_INSERT [dbo].[NguoiDung] OFF
GO
SET IDENTITY_INSERT [dbo].[NhaCungCap] ON 

INSERT [dbo].[NhaCungCap] ([NhaCungCapID], [TenNhaCungCap], [NguoiLienHe], [SoDienThoai], [DiaChi]) VALUES (1, N'Công ty TNHH Honda Việt Nam', N'Phòng kinh doanh', N'024-3768-5968', N'Hà Nội')
INSERT [dbo].[NhaCungCap] ([NhaCungCapID], [TenNhaCungCap], [NguoiLienHe], [SoDienThoai], [DiaChi]) VALUES (2, N'Công ty CP Máy Xây Dựng', N'Nguyễn Văn C', N'028-3855-1234', N'TP.HCM')
SET IDENTITY_INSERT [dbo].[NhaCungCap] OFF
GO
INSERT [dbo].[TinhTrangThietBi] ([TinhTrangID], [TenTinhTrang], [MoTa]) VALUES (1, N'Sẵn sàng', N'Đang ở kho, có thể cho thuê — hiển thị trên app KH')
INSERT [dbo].[TinhTrangThietBi] ([TinhTrangID], [TenTinhTrang], [MoTa]) VALUES (2, N'Đang cho thuê', N'Khách hàng đang giữ ngoài hiện trường')
INSERT [dbo].[TinhTrangThietBi] ([TinhTrangID], [TenTinhTrang], [MoTa]) VALUES (3, N'Đang bảo trì', N'Đang sửa chữa, không thể cho thuê')
INSERT [dbo].[TinhTrangThietBi] ([TinhTrangID], [TenTinhTrang], [MoTa]) VALUES (4, N'Thanh lý / Hỏng', N'Loại khỏi vòng quay cho thuê')
GO
INSERT [dbo].[TrangThaiBaoTri] ([TrangThaiID], [TenTrangThai]) VALUES (1, N'Đang thực hiện')
INSERT [dbo].[TrangThaiBaoTri] ([TrangThaiID], [TenTrangThai]) VALUES (2, N'Hoàn thành')
INSERT [dbo].[TrangThaiBaoTri] ([TrangThaiID], [TenTrangThai]) VALUES (3, N'Chờ phụ tùng')
GO
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (1, N'Chờ duyệt')
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (2, N'Chờ giao')
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (3, N'Đang thuê')
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (4, N'Chờ thanh toán')
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (5, N'Đã hoàn thành')
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (6, N'Đã từ chối')
INSERT [dbo].[TrangThaiHopDong] ([TrangThaiID], [TenTrangThai]) VALUES (7, N'Đã hủy')
GO
INSERT [dbo].[TrangThaiNguoiDung] ([TrangThaiID], [TenTrangThai]) VALUES (1, N'Đang hoạt động')
INSERT [dbo].[TrangThaiNguoiDung] ([TrangThaiID], [TenTrangThai]) VALUES (2, N'Tạm khóa')
INSERT [dbo].[TrangThaiNguoiDung] ([TrangThaiID], [TenTrangThai]) VALUES (3, N'Đã vô hiệu')
GO
INSERT [dbo].[VaiTro] ([VaiTroID], [TenVaiTro], [MoTa]) VALUES (1, N'Admin', N'Quản trị viên — toàn quyền hệ thống')
INSERT [dbo].[VaiTro] ([VaiTroID], [TenVaiTro], [MoTa]) VALUES (2, N'Thủ kho', N'Quản lý kho, nhập xuất thiết bị, duyệt hợp đồng')
INSERT [dbo].[VaiTro] ([VaiTroID], [TenVaiTro], [MoTa]) VALUES (3, N'Kỹ thuật viên', N'Giao nhận hiện trường, bảo trì và sự cố')
INSERT [dbo].[VaiTro] ([VaiTroID], [TenVaiTro], [MoTa]) VALUES (4, N'Khách hàng', N'Tự đặt thuê qua app, xem hợp đồng cá nhân')
GO
/****** Object:  Index [UQ_GioHang]    Script Date: 5/10/2026 2:44:33 PM ******/
ALTER TABLE [dbo].[GioHang] ADD  CONSTRAINT [UQ_GioHang] UNIQUE NONCLUSTERED 
(
	[NguoiDungID] ASC,
	[LoaiThietBiID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ_NguoiDung_Ma]    Script Date: 5/10/2026 2:44:33 PM ******/
ALTER TABLE [dbo].[NguoiDung] ADD  CONSTRAINT [UQ_NguoiDung_Ma] UNIQUE NONCLUSTERED 
(
	[MaNguoiDung] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ_NguoiDung_TaiKhoan]    Script Date: 5/10/2026 2:44:33 PM ******/
ALTER TABLE [dbo].[NguoiDung] ADD  CONSTRAINT [UQ_NguoiDung_TaiKhoan] UNIQUE NONCLUSTERED 
(
	[TaiKhoan] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ_ThietBi_Ma]    Script Date: 5/10/2026 2:44:33 PM ******/
ALTER TABLE [dbo].[ThietBi] ADD  CONSTRAINT [UQ_ThietBi_Ma] UNIQUE NONCLUSTERED 
(
	[MaTaiSan] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UQ_TBND]    Script Date: 5/10/2026 2:44:33 PM ******/
ALTER TABLE [dbo].[ThongBaoNguoiDung] ADD  CONSTRAINT [UQ_TBND] UNIQUE NONCLUSTERED 
(
	[ThongBaoID] ASC,
	[NguoiDungID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[DieuKhoanHopDong] ADD  CONSTRAINT [DF_DieuKhoan_Active]  DEFAULT ((1)) FOR [IsActive]
GO
ALTER TABLE [dbo].[FCMToken] ADD  DEFAULT (getdate()) FOR [NgayCapNhat]
GO
ALTER TABLE [dbo].[GioHang] ADD  CONSTRAINT [DF_GioHang_Ngay]  DEFAULT (getdate()) FOR [NgayThem]
GO
ALTER TABLE [dbo].[HinhAnhThietBi] ADD  CONSTRAINT [DF_HinhAnh_Ngay]  DEFAULT (getdate()) FOR [NgayChup]
GO
ALTER TABLE [dbo].[HopDongThue] ADD  CONSTRAINT [DF_HopDong_NgayLap]  DEFAULT (getdate()) FOR [NgayLap]
GO
ALTER TABLE [dbo].[HopDongThue] ADD  CONSTRAINT [DF_HopDong_Coc]  DEFAULT ((0)) FOR [TienCoc]
GO
ALTER TABLE [dbo].[HopDongThue] ADD  CONSTRAINT [DF_HopDong_Tong]  DEFAULT ((0)) FOR [TongTienThue]
GO
ALTER TABLE [dbo].[HopDongThue] ADD  CONSTRAINT [DF_HopDong_BoiThuong]  DEFAULT ((0)) FOR [PhiBoiThuong]
GO
ALTER TABLE [dbo].[HopDongThue] ADD  CONSTRAINT [DF_HopDong_TrangThai]  DEFAULT ((1)) FOR [TrangThaiID]
GO
ALTER TABLE [dbo].[HopDongThue] ADD  CONSTRAINT [DF_HopDong_Nguon]  DEFAULT ((2)) FOR [NguonTao]
GO
ALTER TABLE [dbo].[LichSuBanGiao] ADD  CONSTRAINT [DF_LSBG_Ngay]  DEFAULT (getdate()) FOR [NgayGiaoNhan]
GO
ALTER TABLE [dbo].[LichSuBaoTri] ADD  CONSTRAINT [DF_LSBT_Ngay]  DEFAULT (getdate()) FOR [NgayThucHien]
GO
ALTER TABLE [dbo].[LichSuBaoTri] ADD  CONSTRAINT [DF_LSBT_ChiPhi]  DEFAULT ((0)) FOR [ChiPhi]
GO
ALTER TABLE [dbo].[LichSuBaoTri] ADD  CONSTRAINT [DF_LSBT_TrangThai]  DEFAULT ((1)) FOR [TrangThaiID]
GO
ALTER TABLE [dbo].[LichSuBaoTri] ADD  CONSTRAINT [DF_LSBT_BoiThuong]  DEFAULT ((0)) FOR [TinhVaoBoiThuong]
GO
ALTER TABLE [dbo].[LoaiThietBi] ADD  CONSTRAINT [DF_LoaiTB_Gia]  DEFAULT ((0)) FOR [GiaThueThamKhao]
GO
ALTER TABLE [dbo].[MucDanhGia] ADD  DEFAULT ((0)) FOR [TinhVaoBoiThuong]
GO
ALTER TABLE [dbo].[NguoiDung] ADD  CONSTRAINT [DF_NguoiDung_TrangThai]  DEFAULT ((1)) FOR [TrangThaiID]
GO
ALTER TABLE [dbo].[NguoiDung] ADD  CONSTRAINT [DF_NguoiDung_NgayTao]  DEFAULT (getdate()) FOR [NgayTao]
GO
ALTER TABLE [dbo].[NguoiDung] ADD  CONSTRAINT [DF_NguoiDung_DoiMK]  DEFAULT ((0)) FOR [DoiMatKhauLanDau]
GO
ALTER TABLE [dbo].[PhieuNhap] ADD  CONSTRAINT [DF_PhieuNhap_Ngay]  DEFAULT (getdate()) FOR [NgayNhap]
GO
ALTER TABLE [dbo].[ThietBi] ADD  CONSTRAINT [DF_ThietBi_TinhTrang]  DEFAULT ((1)) FOR [TinhTrangID]
GO
ALTER TABLE [dbo].[ThongBao] ADD  DEFAULT ((1)) FOR [LoaiThongBao]
GO
ALTER TABLE [dbo].[ThongBao] ADD  DEFAULT (getdate()) FOR [NgayTao]
GO
ALTER TABLE [dbo].[ThongBaoNguoiDung] ADD  DEFAULT ((0)) FOR [DaDoc]
GO
ALTER TABLE [dbo].[XacNhanDieuKhoan] ADD  CONSTRAINT [DF_XacNhan_Thoigian]  DEFAULT (getdate()) FOR [ThoiGianXacNhan]
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK_CTPN_LoaiThietBi] FOREIGN KEY([LoaiThietBiID])
REFERENCES [dbo].[LoaiThietBi] ([LoaiThietBiID])
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap] CHECK CONSTRAINT [FK_CTPN_LoaiThietBi]
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK_CTPN_PhieuNhap] FOREIGN KEY([PhieuNhapID])
REFERENCES [dbo].[PhieuNhap] ([PhieuNhapID])
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap] CHECK CONSTRAINT [FK_CTPN_PhieuNhap]
GO
ALTER TABLE [dbo].[ChiTietThue]  WITH CHECK ADD  CONSTRAINT [FK_CTT_HopDong] FOREIGN KEY([HopDongID])
REFERENCES [dbo].[HopDongThue] ([HopDongID])
GO
ALTER TABLE [dbo].[ChiTietThue] CHECK CONSTRAINT [FK_CTT_HopDong]
GO
ALTER TABLE [dbo].[ChiTietThue]  WITH CHECK ADD  CONSTRAINT [FK_CTT_ThietBi] FOREIGN KEY([ThietBiID])
REFERENCES [dbo].[ThietBi] ([ThietBiID])
GO
ALTER TABLE [dbo].[ChiTietThue] CHECK CONSTRAINT [FK_CTT_ThietBi]
GO
ALTER TABLE [dbo].[FCMToken]  WITH CHECK ADD  CONSTRAINT [FK_FCM_NguoiDung] FOREIGN KEY([NguoiDungID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[FCMToken] CHECK CONSTRAINT [FK_FCM_NguoiDung]
GO
ALTER TABLE [dbo].[GioHang]  WITH CHECK ADD  CONSTRAINT [FK_GH_NguoiDung] FOREIGN KEY([NguoiDungID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[GioHang] CHECK CONSTRAINT [FK_GH_NguoiDung]
GO
ALTER TABLE [dbo].[GioHang]  WITH CHECK ADD  CONSTRAINT [FK_GH_ThietBi] FOREIGN KEY([ThietBiID])
REFERENCES [dbo].[ThietBi] ([ThietBiID])
GO
ALTER TABLE [dbo].[GioHang] CHECK CONSTRAINT [FK_GH_ThietBi]
GO
ALTER TABLE [dbo].[HinhAnhThietBi]  WITH CHECK ADD  CONSTRAINT [FK_HA_BanGiao] FOREIGN KEY([BanGiaoID])
REFERENCES [dbo].[LichSuBanGiao] ([BanGiaoID])
GO
ALTER TABLE [dbo].[HinhAnhThietBi] CHECK CONSTRAINT [FK_HA_BanGiao]
GO
ALTER TABLE [dbo].[HinhAnhThietBi]  WITH CHECK ADD  CONSTRAINT [FK_HA_BaoTri] FOREIGN KEY([BaoTriID])
REFERENCES [dbo].[LichSuBaoTri] ([BaoTriID])
GO
ALTER TABLE [dbo].[HinhAnhThietBi] CHECK CONSTRAINT [FK_HA_BaoTri]
GO
ALTER TABLE [dbo].[HinhAnhThietBi]  WITH CHECK ADD  CONSTRAINT [FK_HA_LoaiAnh] FOREIGN KEY([LoaiAnhID])
REFERENCES [dbo].[LoaiAnh] ([LoaiAnhID])
GO
ALTER TABLE [dbo].[HinhAnhThietBi] CHECK CONSTRAINT [FK_HA_LoaiAnh]
GO
ALTER TABLE [dbo].[HinhAnhThietBi]  WITH CHECK ADD  CONSTRAINT [FK_HA_NguoiDung] FOREIGN KEY([NguoiDungChupID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[HinhAnhThietBi] CHECK CONSTRAINT [FK_HA_NguoiDung]
GO
ALTER TABLE [dbo].[HinhAnhThietBi]  WITH CHECK ADD  CONSTRAINT [FK_HA_ThietBi] FOREIGN KEY([ThietBiID])
REFERENCES [dbo].[ThietBi] ([ThietBiID])
GO
ALTER TABLE [dbo].[HinhAnhThietBi] CHECK CONSTRAINT [FK_HA_ThietBi]
GO
ALTER TABLE [dbo].[HopDongThue]  WITH CHECK ADD  CONSTRAINT [FK_HD_KhachHang] FOREIGN KEY([NguoiDungKhachID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[HopDongThue] CHECK CONSTRAINT [FK_HD_KhachHang]
GO
ALTER TABLE [dbo].[HopDongThue]  WITH CHECK ADD  CONSTRAINT [FK_HD_NguoiTao] FOREIGN KEY([NguoiDungTaoID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[HopDongThue] CHECK CONSTRAINT [FK_HD_NguoiTao]
GO
ALTER TABLE [dbo].[HopDongThue]  WITH CHECK ADD  CONSTRAINT [FK_HD_TrangThai] FOREIGN KEY([TrangThaiID])
REFERENCES [dbo].[TrangThaiHopDong] ([TrangThaiID])
GO
ALTER TABLE [dbo].[HopDongThue] CHECK CONSTRAINT [FK_HD_TrangThai]
GO
ALTER TABLE [dbo].[LichSuBanGiao]  WITH CHECK ADD  CONSTRAINT [FK_LSBG_DenKho] FOREIGN KEY([DenKhoID])
REFERENCES [dbo].[Kho] ([KhoID])
GO
ALTER TABLE [dbo].[LichSuBanGiao] CHECK CONSTRAINT [FK_LSBG_DenKho]
GO
ALTER TABLE [dbo].[LichSuBanGiao]  WITH CHECK ADD  CONSTRAINT [FK_LSBG_HopDong] FOREIGN KEY([HopDongID])
REFERENCES [dbo].[HopDongThue] ([HopDongID])
GO
ALTER TABLE [dbo].[LichSuBanGiao] CHECK CONSTRAINT [FK_LSBG_HopDong]
GO
ALTER TABLE [dbo].[LichSuBanGiao]  WITH CHECK ADD  CONSTRAINT [FK_LSBG_LoaiGiaoDich] FOREIGN KEY([LoaiGiaoDichID])
REFERENCES [dbo].[LoaiGiaoDich] ([LoaiGiaoDichID])
GO
ALTER TABLE [dbo].[LichSuBanGiao] CHECK CONSTRAINT [FK_LSBG_LoaiGiaoDich]
GO
ALTER TABLE [dbo].[LichSuBanGiao]  WITH CHECK ADD  CONSTRAINT [FK_LSBG_MucDanhGia] FOREIGN KEY([MucDanhGiaID])
REFERENCES [dbo].[MucDanhGia] ([MucDanhGiaID])
GO
ALTER TABLE [dbo].[LichSuBanGiao] CHECK CONSTRAINT [FK_LSBG_MucDanhGia]
GO
ALTER TABLE [dbo].[LichSuBanGiao]  WITH CHECK ADD  CONSTRAINT [FK_LSBG_NguoiDung] FOREIGN KEY([NguoiDungThucHienID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[LichSuBanGiao] CHECK CONSTRAINT [FK_LSBG_NguoiDung]
GO
ALTER TABLE [dbo].[LichSuBanGiao]  WITH CHECK ADD  CONSTRAINT [FK_LSBG_ThietBi] FOREIGN KEY([ThietBiID])
REFERENCES [dbo].[ThietBi] ([ThietBiID])
GO
ALTER TABLE [dbo].[LichSuBanGiao] CHECK CONSTRAINT [FK_LSBG_ThietBi]
GO
ALTER TABLE [dbo].[LichSuBanGiao]  WITH CHECK ADD  CONSTRAINT [FK_LSBG_TuKho] FOREIGN KEY([TuKhoID])
REFERENCES [dbo].[Kho] ([KhoID])
GO
ALTER TABLE [dbo].[LichSuBanGiao] CHECK CONSTRAINT [FK_LSBG_TuKho]
GO
ALTER TABLE [dbo].[LichSuBaoTri]  WITH CHECK ADD  CONSTRAINT [FK_LSBT_HopDong] FOREIGN KEY([HopDongID])
REFERENCES [dbo].[HopDongThue] ([HopDongID])
GO
ALTER TABLE [dbo].[LichSuBaoTri] CHECK CONSTRAINT [FK_LSBT_HopDong]
GO
ALTER TABLE [dbo].[LichSuBaoTri]  WITH CHECK ADD  CONSTRAINT [FK_LSBT_LoaiBaoTri] FOREIGN KEY([LoaiBaoTriID])
REFERENCES [dbo].[LoaiBaoTri] ([LoaiBaoTriID])
GO
ALTER TABLE [dbo].[LichSuBaoTri] CHECK CONSTRAINT [FK_LSBT_LoaiBaoTri]
GO
ALTER TABLE [dbo].[LichSuBaoTri]  WITH CHECK ADD  CONSTRAINT [FK_LSBT_NguoiDung] FOREIGN KEY([NguoiDungBaoTriID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[LichSuBaoTri] CHECK CONSTRAINT [FK_LSBT_NguoiDung]
GO
ALTER TABLE [dbo].[LichSuBaoTri]  WITH CHECK ADD  CONSTRAINT [FK_LSBT_ThietBi] FOREIGN KEY([ThietBiID])
REFERENCES [dbo].[ThietBi] ([ThietBiID])
GO
ALTER TABLE [dbo].[LichSuBaoTri] CHECK CONSTRAINT [FK_LSBT_ThietBi]
GO
ALTER TABLE [dbo].[LichSuBaoTri]  WITH CHECK ADD  CONSTRAINT [FK_LSBT_TrangThai] FOREIGN KEY([TrangThaiID])
REFERENCES [dbo].[TrangThaiBaoTri] ([TrangThaiID])
GO
ALTER TABLE [dbo].[LichSuBaoTri] CHECK CONSTRAINT [FK_LSBT_TrangThai]
GO
ALTER TABLE [dbo].[LoaiThietBi]  WITH CHECK ADD  CONSTRAINT [FK_LTB_DanhMuc] FOREIGN KEY([DanhMucID])
REFERENCES [dbo].[DanhMucThietBi] ([DanhMucID])
GO
ALTER TABLE [dbo].[LoaiThietBi] CHECK CONSTRAINT [FK_LTB_DanhMuc]
GO
ALTER TABLE [dbo].[LoaiThietBi]  WITH CHECK ADD  CONSTRAINT [FK_LTB_NhaCungCap] FOREIGN KEY([NhaCungCapID])
REFERENCES [dbo].[NhaCungCap] ([NhaCungCapID])
GO
ALTER TABLE [dbo].[LoaiThietBi] CHECK CONSTRAINT [FK_LTB_NhaCungCap]
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD  CONSTRAINT [FK_ND_Kho] FOREIGN KEY([KhoID])
REFERENCES [dbo].[Kho] ([KhoID])
GO
ALTER TABLE [dbo].[NguoiDung] CHECK CONSTRAINT [FK_ND_Kho]
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD  CONSTRAINT [FK_ND_LoaiKH] FOREIGN KEY([LoaiKhachHangID])
REFERENCES [dbo].[LoaiKhachHang] ([LoaiID])
GO
ALTER TABLE [dbo].[NguoiDung] CHECK CONSTRAINT [FK_ND_LoaiKH]
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD  CONSTRAINT [FK_ND_TrangThai] FOREIGN KEY([TrangThaiID])
REFERENCES [dbo].[TrangThaiNguoiDung] ([TrangThaiID])
GO
ALTER TABLE [dbo].[NguoiDung] CHECK CONSTRAINT [FK_ND_TrangThai]
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD  CONSTRAINT [FK_ND_VaiTro] FOREIGN KEY([VaiTroID])
REFERENCES [dbo].[VaiTro] ([VaiTroID])
GO
ALTER TABLE [dbo].[NguoiDung] CHECK CONSTRAINT [FK_ND_VaiTro]
GO
ALTER TABLE [dbo].[PhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK_PN_NguoiDung] FOREIGN KEY([NguoiDungNhapID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[PhieuNhap] CHECK CONSTRAINT [FK_PN_NguoiDung]
GO
ALTER TABLE [dbo].[PhieuNhap]  WITH CHECK ADD  CONSTRAINT [FK_PN_NhaCungCap] FOREIGN KEY([NhaCungCapID])
REFERENCES [dbo].[NhaCungCap] ([NhaCungCapID])
GO
ALTER TABLE [dbo].[PhieuNhap] CHECK CONSTRAINT [FK_PN_NhaCungCap]
GO
ALTER TABLE [dbo].[ThietBi]  WITH CHECK ADD  CONSTRAINT [FK_TB_Kho] FOREIGN KEY([KhoHienTaiID])
REFERENCES [dbo].[Kho] ([KhoID])
GO
ALTER TABLE [dbo].[ThietBi] CHECK CONSTRAINT [FK_TB_Kho]
GO
ALTER TABLE [dbo].[ThietBi]  WITH CHECK ADD  CONSTRAINT [FK_TB_LoaiThietBi] FOREIGN KEY([LoaiThietBiID])
REFERENCES [dbo].[LoaiThietBi] ([LoaiThietBiID])
GO
ALTER TABLE [dbo].[ThietBi] CHECK CONSTRAINT [FK_TB_LoaiThietBi]
GO
ALTER TABLE [dbo].[ThietBi]  WITH CHECK ADD  CONSTRAINT [FK_TB_TinhTrang] FOREIGN KEY([TinhTrangID])
REFERENCES [dbo].[TinhTrangThietBi] ([TinhTrangID])
GO
ALTER TABLE [dbo].[ThietBi] CHECK CONSTRAINT [FK_TB_TinhTrang]
GO
ALTER TABLE [dbo].[ThongBao]  WITH CHECK ADD  CONSTRAINT [FK_TB_NguoiGui] FOREIGN KEY([NguoiGuiID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[ThongBao] CHECK CONSTRAINT [FK_TB_NguoiGui]
GO
ALTER TABLE [dbo].[ThongBaoNguoiDung]  WITH CHECK ADD  CONSTRAINT [FK_TBND_NguoiDung] FOREIGN KEY([NguoiDungID])
REFERENCES [dbo].[NguoiDung] ([NguoiDungID])
GO
ALTER TABLE [dbo].[ThongBaoNguoiDung] CHECK CONSTRAINT [FK_TBND_NguoiDung]
GO
ALTER TABLE [dbo].[ThongBaoNguoiDung]  WITH CHECK ADD  CONSTRAINT [FK_TBND_ThongBao] FOREIGN KEY([ThongBaoID])
REFERENCES [dbo].[ThongBao] ([ThongBaoID])
GO
ALTER TABLE [dbo].[ThongBaoNguoiDung] CHECK CONSTRAINT [FK_TBND_ThongBao]
GO
ALTER TABLE [dbo].[XacNhanDieuKhoan]  WITH CHECK ADD  CONSTRAINT [FK_XNDK_DieuKhoan] FOREIGN KEY([DieuKhoanID])
REFERENCES [dbo].[DieuKhoanHopDong] ([DieuKhoanID])
GO
ALTER TABLE [dbo].[XacNhanDieuKhoan] CHECK CONSTRAINT [FK_XNDK_DieuKhoan]
GO
ALTER TABLE [dbo].[XacNhanDieuKhoan]  WITH CHECK ADD  CONSTRAINT [FK_XNDK_HopDong] FOREIGN KEY([HopDongID])
REFERENCES [dbo].[HopDongThue] ([HopDongID])
GO
ALTER TABLE [dbo].[XacNhanDieuKhoan] CHECK CONSTRAINT [FK_XNDK_HopDong]
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap]  WITH CHECK ADD  CONSTRAINT [CK_CTPN_SoLuong] CHECK  (([SoLuong]>(0)))
GO
ALTER TABLE [dbo].[ChiTietPhieuNhap] CHECK CONSTRAINT [CK_CTPN_SoLuong]
GO
ALTER TABLE [dbo].[HopDongThue]  WITH CHECK ADD  CONSTRAINT [CK_HopDong_NgayTra] CHECK  (([NgayDuKienTra]>[NgayBatDauThue]))
GO
ALTER TABLE [dbo].[HopDongThue] CHECK CONSTRAINT [CK_HopDong_NgayTra]
GO
ALTER TABLE [dbo].[HopDongThue]  WITH CHECK ADD  CONSTRAINT [CK_HopDong_NguonTao] CHECK  (([NguonTao]=(2) OR [NguonTao]=(1)))
GO
ALTER TABLE [dbo].[HopDongThue] CHECK CONSTRAINT [CK_HopDong_NguonTao]
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD  CONSTRAINT [CK_NguoiDung_Kho] CHECK  ((([VaiTroID]=(3) OR [VaiTroID]=(2)) AND [KhoID] IS NOT NULL OR ([VaiTroID]=(4) OR [VaiTroID]=(1)) AND [KhoID] IS NULL))
GO
ALTER TABLE [dbo].[NguoiDung] CHECK CONSTRAINT [CK_NguoiDung_Kho]
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD  CONSTRAINT [CK_NguoiDung_LoaiKH] CHECK  (([VaiTroID]=(4) OR [LoaiKhachHangID] IS NULL))
GO
ALTER TABLE [dbo].[NguoiDung] CHECK CONSTRAINT [CK_NguoiDung_LoaiKH]
GO
ALTER DATABASE [QuanLyThietBi] SET  READ_WRITE 
GO
