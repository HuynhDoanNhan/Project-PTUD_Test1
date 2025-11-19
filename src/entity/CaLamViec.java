package entity;

import java.time.LocalTime;
import java.util.Objects;

public class CaLamViec {
	private String maCa;
	private LocalTime thoiGianBatDau;
	private LocalTime thoiGianKetThuc;

	public CaLamViec(String maCa, LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc) {
		setMaCa(maCa);
		setThoiGianBatDau(thoiGianBatDau);
		setThoiGianKetThuc(thoiGianKetThuc);
	}

	public String getMaCa() {
		return maCa;
	}

	public void setMaCa(String maCa) {
		if (maCa == null || maCa.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã ca không được null hoặc rỗng");
		}
		if (maCa.length() != 16) {
			throw new IllegalArgumentException("Mã ca phải có đúng 16 ký tự");
		}
		if (!maCa.matches("^CLV[SCT]\\d{12}$")) {
			throw new IllegalArgumentException(
					"Mã ca phải bắt đầu bằng 'CLV' và theo sau bởi ký tự 'S', 'C', 'T', và 12 chữ số");
		}
		// Kiểm tra chi tiết định dạng
		String caType = maCa.substring(3, 4); // Ký tự thứ 4: S, C, T
		if (!caType.matches("[SCT]")) {
			throw new IllegalArgumentException("Ký tự ca phải là 'S' (sáng), 'C' (chiều), hoặc 'T' (tối)");
		}
		String nhanVienPart = maCa.substring(4, 6); // aa: 2 số cuối mã nhân viên
		if (!nhanVienPart.matches("\\d{2}")) {
			throw new IllegalArgumentException("Phần mã nhân viên phải là 2 chữ số");
		}
		String ngayPart = maCa.substring(6, 8); // bb: ngày
		String thangPart = maCa.substring(8, 10); // cc: tháng
		String namPart = maCa.substring(10, 12); // dd: 2 số cuối năm
		try {
			int ngay = Integer.parseInt(ngayPart);
			int thang = Integer.parseInt(thangPart);
			int nam = Integer.parseInt(namPart);
			if (ngay < 1 || ngay > 31) {
				throw new IllegalArgumentException("Ngày phải từ 01 đến 31");
			}
			if (thang < 1 || thang > 12) {
				throw new IllegalArgumentException("Tháng phải từ 01 đến 12");
			}
			// Kiểm tra năm (giả định năm hiện tại là 2025, chỉ chấp nhận 2 số cuối từ
			// 00-99)
			if (nam != 25) { // Điều chỉnh nếu cần năm khác
				throw new IllegalArgumentException("2 số cuối năm phải là '25' (năm 2025)");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Phần ngày, tháng, năm phải là số");
		}
		this.maCa = maCa;
	}

	public LocalTime getThoiGianBatDau() {
		return thoiGianBatDau;
	}

	public void setThoiGianBatDau(LocalTime thoiGianBatDau) {
		if (thoiGianBatDau == null) {
			throw new IllegalArgumentException("Thời gian bắt đầu không được null");
		}
		this.thoiGianBatDau = thoiGianBatDau;
	}

	public LocalTime getThoiGianKetThuc() {
		return thoiGianKetThuc;
	}

	public void setThoiGianKetThuc(LocalTime thoiGianKetThuc) {
		if (thoiGianKetThuc == null) {
			throw new IllegalArgumentException("Thời gian kết thúc không được null");
		}
		if (thoiGianKetThuc.isBefore(thoiGianBatDau)) {
			throw new IllegalArgumentException("Thời gian kết thúc phải lớn hơn hoặc bằng thời gian bắt đầu");
		}
		this.thoiGianKetThuc = thoiGianKetThuc;
	}

	@Override
	public String toString() {
		return "CaLamViec [maCa=" + maCa + ", thoiGianBatDau=" + thoiGianBatDau + ", thoiGianKetThuc=" + thoiGianKetThuc
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maCa, thoiGianBatDau, thoiGianKetThuc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaLamViec other = (CaLamViec) obj;
		return Objects.equals(maCa, other.maCa) && Objects.equals(thoiGianBatDau, other.thoiGianBatDau)
				&& Objects.equals(thoiGianKetThuc, other.thoiGianKetThuc);
	}
}