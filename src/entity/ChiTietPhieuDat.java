package entity;

import java.util.Objects;

public class ChiTietPhieuDat {
    private PhieuDatPhong phieuDatPhong;
    private KhachHang khachHang;
    private Phong phong;
    private int soNguoi;
    private String trangThai;

    public ChiTietPhieuDat(PhieuDatPhong phieuDatPhong, KhachHang khachHang, Phong phong, int soNguoi, String trangThai) {
        setPhieuDatPhong(phieuDatPhong);
        setKhachHang(khachHang);
        setPhong(phong);
        setSoNguoi(soNguoi);
        setTrangThai(trangThai);
    }

    public PhieuDatPhong getPhieuDatPhong() {
        return phieuDatPhong;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        if (phieuDatPhong == null) {
            throw new IllegalArgumentException("Phiếu đặt phòng không được null");
        }
        this.phieuDatPhong = phieuDatPhong;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        if (khachHang == null) {
            throw new IllegalArgumentException("Khách hàng không được null");
        }
        this.khachHang = khachHang;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        if (phong == null) {
            throw new IllegalArgumentException("Phòng không được null");
        }
        this.phong = phong;
    }

    public int getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(int soNguoi) {
        if (soNguoi <= 0) {
            throw new IllegalArgumentException("Số người phải lớn hơn 0");
        }
        this.soNguoi = soNguoi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được null hoặc rỗng");
        }
        if (!trangThai.matches("^(DaDat|DaHuy|DaThanhToan)$")) {
            throw new IllegalArgumentException("Trạng thái phải là 'DaDat', 'DaHuy', hoặc 'DaThanhToan'");
        }
        this.trangThai = trangThai;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phieuDatPhong, khachHang, phong, soNguoi, trangThai);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChiTietPhieuDat other = (ChiTietPhieuDat) obj;
        return Objects.equals(phieuDatPhong, other.phieuDatPhong) && Objects.equals(khachHang, other.khachHang)
                && Objects.equals(phong, other.phong) && soNguoi == other.soNguoi
                && Objects.equals(trangThai, other.trangThai);
    }
}