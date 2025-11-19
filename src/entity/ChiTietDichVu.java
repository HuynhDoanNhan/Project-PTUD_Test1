package entity;

import java.util.Objects;

public class ChiTietDichVu {
	private PhieuDatPhong phieuDatPhong;
	private KhachHang khachHang;
	private Phong phong;
	private DichVu dichVu;
	private int soLuong;
	private double thanhTien;

	public ChiTietDichVu(PhieuDatPhong phieuDatPhong, KhachHang khachHang, Phong phong, DichVu dichVu, int soLuong,
			double thanhTien) {
		setPhieuDatPhong(phieuDatPhong);
		setKhachHang(khachHang);
		setPhong(phong);
		setDichVu(dichVu);
		setSoLuong(soLuong);
		setThanhTien(thanhTien);
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

	public DichVu getDichVu() {
		return dichVu;
	}

	public void setDichVu(DichVu dichVu) {
		if (dichVu == null) {
			throw new IllegalArgumentException("Dịch vụ không được null");
		}
		this.dichVu = dichVu;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		if (soLuong <= 0) {
			throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
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
		return Objects.hash(phieuDatPhong, khachHang, phong, dichVu, soLuong, thanhTien);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChiTietDichVu other = (ChiTietDichVu) obj;
		return Objects.equals(phieuDatPhong, other.phieuDatPhong) && Objects.equals(khachHang, other.khachHang)
				&& Objects.equals(phong, other.phong) && Objects.equals(dichVu, other.dichVu)
				&& soLuong == other.soLuong && Double.compare(thanhTien, other.thanhTien) == 0;
	}
}