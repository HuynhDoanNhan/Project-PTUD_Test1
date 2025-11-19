package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class KhachHang {
	private String maKhachHang;
	private String hoTen;
	private String cccd;
	private LocalDate ngaySinh;
	private String soDienThoai;
	private int diemTichLuy;
	private String capBac;

	// Hằng số cho cấp bậc
	private static final String DONG = "Dong";
	private static final String BAC = "Bac";
	private static final String VANG = "Vang";
	private static final String KIM_CUONG = "KimCuong";

	// Hằng số cho quy đổi điểm và mức giảm giá
	private static final double TI_LE_QUY_DOI = 100000;
	private static final double GIAM_GIA_DONG = 0.01;
	private static final double GIAM_GIA_BAC = 0.02;
	private static final double GIAM_GIA_VANG = 0.05;
	private static final double GIAM_GIA_KIM_CUONG = 0.10;
	private static final int DIEM_DONG = 100;
	private static final int DIEM_BAC = 200;
	private static final int DIEM_VANG = 500;
	private static final int DIEM_KIM_CUONG = 1000;

	public KhachHang(String maKhachHang, String hoTen, String cccd, LocalDate ngaySinh, String soDienThoai,
			int diemTichLuy) {
		setMaKhachHang(maKhachHang);
		setHoTen(hoTen);
		setCccd(cccd);
		setNgaySinh(ngaySinh);
		setSoDienThoai(soDienThoai);
		setDiemTichLuy(diemTichLuy);
	}

	public String getMaKhachHang() {
		return maKhachHang;
	}

	public void setMaKhachHang(String maKhachHang) {
		if (maKhachHang == null || maKhachHang.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã khách hàng không được null hoặc rỗng");
		}
		if (!maKhachHang.matches("^KH[0-9]{4,}$")) {
			throw new IllegalArgumentException("Mã khách hàng phải bắt đầu bằng 'KH' và theo sau bởi ít nhất 4 chữ số");
		}
		this.maKhachHang = maKhachHang;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		if (hoTen == null || hoTen.trim().isEmpty()) {
			throw new IllegalArgumentException("Họ tên không được null hoặc rỗng");
		}
		if (!hoTen.matches("^[\\p{L}\\s]+$")) {
			throw new IllegalArgumentException("Họ tên chỉ được chứa chữ cái và khoảng trắng");
		}
		this.hoTen = hoTen;
	}

	public String getCccd() {
		return cccd;
	}

	public void setCccd(String cccd) {
		if (cccd == null || cccd.trim().isEmpty()) {
			throw new IllegalArgumentException("CCCD không được null hoặc rỗng");
		}
		if (!cccd.matches("^[0-9]{12}$")) {
			throw new IllegalArgumentException("CCCD phải có đúng 12 chữ số");
		}
		this.cccd = cccd;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		if (ngaySinh == null) {
			throw new IllegalArgumentException("Ngày sinh không được null");
		}
		if (ngaySinh.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Ngày sinh không được lớn hơn ngày hiện tại");
		}
		this.ngaySinh = ngaySinh;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
			throw new IllegalArgumentException("Số điện thoại không được null hoặc rỗng");
		}
		if (!soDienThoai.matches("^(09|03|08)[0-9]{8}$")) {
			throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số, bắt đầu bằng 09, 03 hoặc 08");
		}
		this.soDienThoai = soDienThoai;
	}

	public int getDiemTichLuy() {
		return diemTichLuy;
	}

	public void setDiemTichLuy(int diemTichLuy) {
		if (diemTichLuy < 0) {
			throw new IllegalArgumentException("Điểm tích lũy không được âm");
		}
		this.diemTichLuy = diemTichLuy;
		updateCapBac();
	}

	public String getCapBac() {
		return capBac;
	}

	// Phương thức private để cập nhật cấp bậc dựa trên điểm tích lũy
	private void updateCapBac() {
		if (diemTichLuy >= DIEM_KIM_CUONG) {
			this.capBac = KIM_CUONG;
		} else if (diemTichLuy >= DIEM_VANG) {
			this.capBac = VANG;
		} else if (diemTichLuy >= DIEM_BAC) {
			this.capBac = BAC;
		} else if (diemTichLuy >= DIEM_DONG) {
			this.capBac = DONG;
		} else {
			this.capBac = "Khong";
		}
	}

	// Tính mức giảm giá dựa trên cấp bậc và tổng tiền
	public double tinhMucGiamGia(double tongTien) {
		if (tongTien < 0) {
			throw new IllegalArgumentException("Tổng tiền không được âm");
		}
		double tiLeGiamGia = switch (capBac) {
		case KIM_CUONG -> GIAM_GIA_KIM_CUONG;
		case VANG -> GIAM_GIA_VANG;
		case BAC -> GIAM_GIA_BAC;
		case DONG -> GIAM_GIA_DONG;
		default -> 0.0;
		};
		return tiLeGiamGia * tongTien;
	}

	// Cập nhật điểm tích lũy dựa trên số tiền chi tiêu
	public void capNhatDiemTichLuy(double soTienChiTieu) {
		if (soTienChiTieu < 0) {
			throw new IllegalArgumentException("Số tiền chi tiêu không được âm");
		}
		int diemMoi = (int) (soTienChiTieu / TI_LE_QUY_DOI);
		setDiemTichLuy(this.diemTichLuy + diemMoi); // Cập nhật điểm và tự động cập nhật cấp bậc
	}

	@Override
	public String toString() {
		return "KhachHang [maKhachHang=" + maKhachHang + ", hoTen=" + hoTen + ", cccd=" + cccd + ", ngaySinh="
				+ ngaySinh + ", soDienThoai=" + soDienThoai + ", diemTichLuy=" + diemTichLuy + ", capBac=" + capBac
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(capBac, cccd, diemTichLuy, hoTen, maKhachHang, ngaySinh, soDienThoai);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(capBac, other.capBac) && Objects.equals(cccd, other.cccd)
				&& diemTichLuy == other.diemTichLuy && Objects.equals(hoTen, other.hoTen)
				&& Objects.equals(maKhachHang, other.maKhachHang) && Objects.equals(ngaySinh, other.ngaySinh)
				&& Objects.equals(soDienThoai, other.soDienThoai);
	}
}