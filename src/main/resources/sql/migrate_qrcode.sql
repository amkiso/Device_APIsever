-- =====================================================
-- Migration: Thêm cột QrCodeUrl vào bảng ThietBi
-- Ngày: 2026-05-13
-- Mô tả: Lưu đường dẫn ảnh QR code cho mỗi thiết bị
-- =====================================================

ALTER TABLE ThietBi
ADD QrCodeUrl NVARCHAR(500) NULL;

GO
