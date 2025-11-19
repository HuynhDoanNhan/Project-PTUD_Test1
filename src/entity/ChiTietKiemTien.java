package entity;

import java.util.Objects;

public class ChiTietKiemTien {
    private PhieuKiemTien phieuKiemTien;
    private double menhGia;
    private int soLuong;
    private double thanhTien;

    public ChiTietKiemTien(PhieuKiemTien phieuKiemTien, double menhGia, int soLuong) {
        setPhieuKiemTien(phieuKiemTien);
        setMenhGia(menhGia);
        setSoLuong(soLuong);
        // Tính thanhTien khi khởi tạo
        tinhThanhTien();
    }

    public PhieuKiemTien getPhieuKiemTien() {
        return phieuKiemTien;
    }

    public void setPhieuKiemTien(PhieuKiemTien phieuKiemTien) {
        if (phieuKiemTien == null) {
            throw new IllegalArgumentException("Phiếu kiểm tiền không được null");
        }
        this.phieuKiemTien = phieuKiemTien;
    }

    public double getMenhGia() {
        return menhGia;
    }

    public void setMenhGia(double menhGia) {
        // Danh sách các mệnh giá hợp lệ (VND)
        double[] menhGiaHopLe = {500000, 200000, 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100};
        boolean hopLe = false;
        for (double mg : menhGiaHopLe) {
            if (mg == menhGia) {
                hopLe = true;
                break;
            }
        }
        if (!hopLe) {
            throw new IllegalArgumentException("Mệnh giá phải là một trong các giá trị: 500000, 200000, 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100 VND");
        }
        this.menhGia = menhGia;
        // Cập nhật thanhTien khi menhGia thay đổi
        tinhThanhTien();
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
        // Cập nhật thanhTien khi soLuong thay đổi
        tinhThanhTien();
    }

    public double getThanhTien() {
        return thanhTien;
    }

    // Loại bỏ setThanhTien vì thanhTien được tính tự động
    private void tinhThanhTien() {
        this.thanhTien = menhGia * soLuong;
    }

    @Override
    public String toString() {
        return "ChiTietKiemTien [phieuKiemTien=" + (phieuKiemTien != null ? phieuKiemTien.getMaPhieuKiemTien() : "null")
                + ", menhGia=" + menhGia + " VND, soLuong=" + soLuong + ", thanhTien=" + thanhTien + " VND]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(phieuKiemTien, menhGia, soLuong, thanhTien);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChiTietKiemTien other = (ChiTietKiemTien) obj;
        return Objects.equals(phieuKiemTien, other.phieuKiemTien)
                && Double.compare(menhGia, other.menhGia) == 0
                && soLuong == other.soLuong
                && Double.compare(thanhTien, other.thanhTien) == 0;
    }
}