package entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

public class HoaDon {
	private String maHoaDon;
	private LocalDateTime ngayNhanPhong;
	private LocalDateTime ngayTraPhong;
	private double phiBoSung;
	private double tongTien;
	private boolean trangThai;
	private String ghiChu;
	private PhieuDatPhong phieuDatPhong;

	public HoaDon(String maHoaDon, double phiBoSung, double tongTien, boolean trangThai, String ghiChu,
			PhieuDatPhong phieuDatPhong) {
		setMaHoaDon(maHoaDon);
		setPhiBoSung(phiBoSung);
		setTongTien(tongTien);
		setTrangThai(trangThai);
		setGhiChu(ghiChu);
		setPhieuDatPhong(phieuDatPhong);
		if (phieuDatPhong != null) {
			setNgayNhanPhong(phieuDatPhong.getNgayNhan());
			setNgayTraPhong(phieuDatPhong.getNgayTra());
		}
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã hóa đơn không được null hoặc rỗng");
		}
		if (maHoaDon.length() != 13) {
			throw new IllegalArgumentException("Mã hóa đơn phải có đúng 13 ký tự");
		}
		if (!maHoaDon.startsWith("HD")) {
			throw new IllegalArgumentException("Mã hóa đơn phải bắt đầu bằng 'HD'");
		}
		String maPhieuPart = maHoaDon.substring(2, 12);
		if (!maPhieuPart.matches("^PD\\d{10}$")) {
			throw new IllegalArgumentException("Phần mã phiếu phải bắt đầu bằng 'PD' và theo sau bởi 10 chữ số");
		}
		if (!maHoaDon.substring(12, 14).equals("KC")) {
			throw new IllegalArgumentException("Mã hóa đơn phải chứa 'KC' sau mã phiếu");
		}
		String datePart = maHoaDon.substring(14, 20);
		if (!datePart.matches("^\\d{6}$")) {
			throw new IllegalArgumentException("Phần ngày phải là 6 chữ số (VD: 250926)");
		}
		String seqPart = maHoaDon.substring(20);
		try {
			int seqNumber = Integer.parseInt(seqPart);
			if (seqNumber < 1 || seqNumber > 999) {
				throw new IllegalArgumentException("Số thứ tự phải nằm trong khoảng từ 001 đến 999");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Phần số thứ tự phải là 3 chữ số");
		}
		this.maHoaDon = maHoaDon;
	}

	public LocalDateTime getNgayNhanPhong() {
		return ngayNhanPhong;
	}

	public void setNgayNhanPhong(LocalDateTime ngayNhanPhong) {
		if (ngayNhanPhong == null) {
			throw new IllegalArgumentException("Ngày nhận phòng không được null");
		}
		this.ngayNhanPhong = ngayNhanPhong;
	}

	public LocalDateTime getNgayTraPhong() {
		return ngayTraPhong;
	}

	public void setNgayTraPhong(LocalDateTime ngayTraPhong) {
		if (ngayTraPhong == null) {
			throw new IllegalArgumentException("Ngày trả phòng không được null");
		}
		if (ngayTraPhong.isBefore(ngayNhanPhong)) {
			throw new IllegalArgumentException("Ngày trả phòng phải lớn hơn hoặc bằng ngày nhận phòng");
		}
		this.ngayTraPhong = ngayTraPhong;
	}

	public double getPhiBoSung() {
		return phiBoSung;
	}

	public void setPhiBoSung(double phiBoSung) {
		if (phiBoSung < 0) {
			throw new IllegalArgumentException("Phí bổ sung không được âm");
		}
		this.phiBoSung = phiBoSung;
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

	public boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		if (ghiChu != null) {
			if (Pattern.compile("[#*?!]").matcher(ghiChu).find()) {
				throw new IllegalArgumentException("Ghi chú không được chứa ký hiệu đặc biệt như #, *, ?, !");
			}
			this.ghiChu = ghiChu.trim();
		} else {
			this.ghiChu = "";
		}
	}

	public PhieuDatPhong getPhieuDatPhong() {
		return phieuDatPhong;
	}

	public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
		if (phieuDatPhong == null) {
			throw new IllegalArgumentException("Phiếu đặt phòng không được null");
		}
		this.phieuDatPhong = phieuDatPhong;
		setNgayNhanPhong(phieuDatPhong.getNgayNhan());
		setNgayTraPhong(phieuDatPhong.getNgayTra());
	}

	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", ngayNhanPhong=" + ngayNhanPhong + ", ngayTraPhong=" + ngayTraPhong
				+ ", phiBoSung=" + phiBoSung + ", tongTien=" + tongTien + ", trangThai="
				+ (trangThai ? "Đã thanh toán" : "Chờ thanh toán") + ", ghiChu=" + ghiChu + ", phieuDatPhong="
				+ (phieuDatPhong != null ? phieuDatPhong.getMaPhieuDat() : "null") + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maHoaDon, ngayNhanPhong, ngayTraPhong, phiBoSung, tongTien, trangThai, ghiChu,
				phieuDatPhong);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHoaDon, other.maHoaDon) && Objects.equals(ngayNhanPhong, other.ngayNhanPhong)
				&& Objects.equals(ngayTraPhong, other.ngayTraPhong) && Double.compare(phiBoSung, other.phiBoSung) == 0
				&& Double.compare(tongTien, other.tongTien) == 0 && trangThai == other.trangThai
				&& Objects.equals(ghiChu, other.ghiChu) && Objects.equals(phieuDatPhong, other.phieuDatPhong);
	}
}