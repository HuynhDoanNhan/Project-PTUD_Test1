package entity;

import java.time.LocalDate;
import java.util.Objects;

public class PhieuKiemTien {
	private String maPhieuKiemTien;
	private double tongTien;
	private NhanVien nhanVienGiamSat;
	private NhanVien nhanVienKiemTien;
	private PhieuKetToan phieuKetToan;

	public PhieuKiemTien(String maPhieuKiemTien, double tongTien, NhanVien nhanVienGiamSat, NhanVien nhanVienKiemTien,
			PhieuKetToan phieuKetToan) {
		setMaPhieuKiemTien(maPhieuKiemTien);
		setTongTien(tongTien);
		setNhanVienGiamSat(nhanVienGiamSat);
		setNhanVienKiemTien(nhanVienKiemTien);
		setPhieuKetToan(phieuKetToan);
	}

	public String getMaPhieuKiemTien() {
		return maPhieuKiemTien;
	}

	public void setMaPhieuKiemTien(String maPhieuKiemTien) throws NumberFormatException {
		if (maPhieuKiemTien == null || maPhieuKiemTien.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã phiếu kiểm tiền không được null hoặc rỗng");
		}
		if (maPhieuKiemTien.length() != 11) {
			throw new IllegalArgumentException("Mã phiếu kiểm tiền phải có đúng 11 ký tự");
		}
		// Kiểm tra định dạng PKTddmmyyXZZ
		if (!maPhieuKiemTien.matches("^PKT\\d{6}[SCT][BDKT]$")) {
			throw new IllegalArgumentException(
					"Mã phiếu kiểm tiền phải có dạng PKTddmmyyXZZ, với X là S/C/T và ZZ là BD/KT");
		}
		// Kiểm tra ngày ddmmyy hợp lệ
		String datePart = maPhieuKiemTien.substring(3, 9); // Lấy ddmmyy
		try {
			int day = Integer.parseInt(datePart.substring(0, 2)); // dd
			int month = Integer.parseInt(datePart.substring(2, 4)); // mm
			int year = Integer.parseInt("20" + datePart.substring(4)); // yy, giả định từ 2000
			LocalDate date = LocalDate.of(year, month, day);
			LocalDate currentDate = LocalDate.now(); // 26/10/2025
			if (date.isAfter(currentDate)) {
				throw new IllegalArgumentException("Ngày trong mã phiếu không được lớn hơn ngày hiện tại");
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Phần ngày ddmmyy không hợp lệ");
		}
		this.maPhieuKiemTien = maPhieuKiemTien;
	}

	public double getTongTien() {
		return tongTien;
	}

	public void setTongTien(double tongTien) {
		if (tongTien < 0) {
			throw new IllegalArgumentException("Tổng tiền không được âm");
		}
		this.tongTien = tongTien;
	}

	public NhanVien getNhanVienGiamSat() {
		return nhanVienGiamSat;
	}

	public void setNhanVienGiamSat(NhanVien nhanVienGiamSat) {
		if (nhanVienGiamSat == null) {
			throw new IllegalArgumentException("Nhân viên giám sát không được null");
		}
		this.nhanVienGiamSat = nhanVienGiamSat;
	}

	public NhanVien getNhanVienKiemTien() {
		return nhanVienKiemTien;
	}

	public void setNhanVienKiemTien(NhanVien nhanVienKiemTien) {
		if (nhanVienKiemTien == null) {
			throw new IllegalArgumentException("Nhân viên kiểm tiền không được null");
		}
		this.nhanVienKiemTien = nhanVienKiemTien;
	}

	public PhieuKetToan getPhieuKetToan() {
		return phieuKetToan;
	}

	public void setPhieuKetToan(PhieuKetToan phieuKetToan) {
		if (phieuKetToan == null) {
			throw new IllegalArgumentException("Phiếu kết toán không được null");
		}
		this.phieuKetToan = phieuKetToan;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maPhieuKiemTien, tongTien, nhanVienGiamSat, nhanVienKiemTien, phieuKetToan);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhieuKiemTien other = (PhieuKiemTien) obj;
		return Objects.equals(maPhieuKiemTien, other.maPhieuKiemTien) && Double.compare(tongTien, other.tongTien) == 0
				&& Objects.equals(nhanVienGiamSat, other.nhanVienGiamSat)
				&& Objects.equals(nhanVienKiemTien, other.nhanVienKiemTien)
				&& Objects.equals(phieuKetToan, other.phieuKetToan);
	}
}