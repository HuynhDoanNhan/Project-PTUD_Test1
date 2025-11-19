package entity;

import java.util.Objects;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private KhachHang khachHang;
    private DichVu dichVu;
    private Phong phong;
    private NhanVien nhanVien;
    private int soLuong;
    private double thanhTien;

    public ChiTietHoaDon(HoaDon hoaDon, KhachHang khachHang, DichVu dichVu, Phong phong, NhanVien nhanVien,
                         int soLuong, double thanhTien) {
        setHoaDon(hoaDon);
        setKhachHang(khachHang);
        setDichVu(dichVu);
        setPhong(phong);
        setNhanVien(nhanVien);
        setSoLuong(soLuong);
        setThanhTien(thanhTien);
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không được null");
        }
        this.hoaDon = hoaDon;
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

    public DichVu getDichVu() {
        return dichVu;
    }

    public void setDichVu(DichVu dichVu) {
        if (dichVu == null) {
            throw new IllegalArgumentException("Dịch vụ không được null");
        }
        this.dichVu = dichVu;
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

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("Nhân viên không được null");
        }
        this.nhanVien = nhanVien;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        if (thanhTien < 0) {
            throw new IllegalArgumentException("Thành tiền không được âm");
        }
        this.thanhTien = thanhTien;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hoaDon, khachHang, dichVu, phong, nhanVien, soLuong, thanhTien);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChiTietHoaDon other = (ChiTietHoaDon) obj;
        return Objects.equals(hoaDon, other.hoaDon) && Objects.equals(khachHang, other.khachHang)
                && Objects.equals(dichVu, other.dichVu) && Objects.equals(phong, other.phong)
                && Objects.equals(nhanVien, other.nhanVien) && soLuong == other.soLuong
                && Double.compare(thanhTien, other.thanhTien) == 0;
    }
}