package com.example.device_apisever.service;

import com.example.device_apisever.dto.baotri.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBaoTriService {
    Page<BaoTriDto> getDanhSachBaoTri(Integer trangThaiId, Integer nguoiDungId, String keyword, Pageable pageable);
    BaoTriDetailDto getChiTietBaoTri(Integer baoTriId);
    BaoTriThongKeDto getThongKeBaoTri();
    BaoTriDto createBaoTri(BaoTriCreateReq req, Integer createdBy);
    
    BaoTriDto xacNhanYeuCau(Integer baoTriId, Integer technicianId);
    BaoTriDto ghiNhanSuCo(Integer baoTriId, GhiNhanSuCoReq req, Integer technicianId);
    BaoTriDto hoanThanhBaoTri(Integer baoTriId, HoanThanhBaoTriReq req, Integer technicianId);
}
