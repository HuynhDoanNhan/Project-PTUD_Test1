package entity;

import java.util.Objects;

public class LoaiPhong {
	private String maLoaiPhong;
	private String tenLoaiPhong;
	private double giaCoBan;
	private double phanTramDatCoc;

	public LoaiPhong(String maLoaiPhong, double giaCoBan) {
		setMaLoaiPhong(maLoaiPhong);
		setGiaCoBan(giaCoBan);
		setTenLoaiPhongBasedOnMaLoaiPhong();
		setPhanTramDatCocBasedOnMaLoaiPhong();
	}

	public String getMaLoaiPhong() {
		return maLoaiPhong;
	}

	public void setMaLoaiPhong(String maLoaiPhong) {
		if (maLoaiPhong == null || maLoaiPhong.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã loại phòng không được null hoặc rỗng");
		}
		if (!maLoaiPhong.matches("^(STD|SUP|DLX|SUT)$")) {
			throw new IllegalArgumentException("Mã loại phòng phải là một trong các giá trị: STD, SUP, DLX, SUT");
		}
		this.maLoaiPhong = maLoaiPhong;
	}

	public String getTenLoaiPhong() {
		return tenLoaiPhong;
	}

	private void setTenLoaiPhongBasedOnMaLoaiPhong() {
		switch (maLoaiPhong) {
		case "STD":
			this.tenLoaiPhong = "phòng Standard";
			break;
		case "SUP":
			this.tenLoaiPhong = "phòng Superior";
			break;
		case "DLX":
			this.tenLoaiPhong = "phòng Deluxe";
			break;
		case "SUT":
			this.tenLoaiPhong = "phòng Suite";
			break;
		default:
			throw new IllegalStateException("Mã loại phòng không hợp lệ");
		}
	}

	public void setTenLoaiPhong(String tenLoaiPhong) {
		if (tenLoaiPhong == null || tenLoaiPhong.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên loại phòng không được null hoặc rỗng");
		}
		String expectedTen = "";
		switch (maLoaiPhong) {
		case "STD":
			expectedTen = "phòng Standard";
			break;
		case "SUP":
			expectedTen = "phòng Superior";
			break;
		case "DLX":
			expectedTen = "phòng Deluxe";
			break;
		case "SUT":
			expectedTen = "phòng Suite";
			break;
		}
		if (!tenLoaiPhong.equals(expectedTen)) {
			throw new IllegalArgumentException("Tên loại phòng không khớp với mã loại phòng");
		}
		this.tenLoaiPhong = tenLoaiPhong;
	}

	public double getGiaCoBan() {
		return giaCoBan;
	}

	public void setGiaCoBan(double giaCoBan) {
		if (giaCoBan < 0) {
			throw new IllegalArgumentException("Giá cơ bản không được âm");
		}
		this.giaCoBan = giaCoBan;
	}

	public double getPhanTramDatCoc() {
		return phanTramDatCoc;
	}

	private void setPhanTramDatCocBasedOnMaLoaiPhong() {
		switch (maLoaiPhong) {
		case "STD":
			this.phanTramDatCoc = 30.0;
			break;
		case "SUP":
			this.phanTramDatCoc = 35.0;
			break;
		case "DLX":
			this.phanTramDatCoc = 40.0;
			break;
		case "SUT":
			this.phanTramDatCoc = 50.0;
			break;
		default:
			throw new IllegalStateException("Mã loại phòng không hợp lệ");
		}
	}

	// Phương thức setPhanTramDatCoc chỉ cho phép cập nhật nếu khớp với maLoaiPhong
	public void setPhanTramDatCoc(double phanTramDatCoc) {
		if (phanTramDatCoc < 0 || phanTramDatCoc > 100) {
			throw new IllegalArgumentException("Phần trăm đặt cọc phải từ 0 đến 100");
		}
		double expectedPercent = 0.0;
		switch (maLoaiPhong) {
		case "STD":
			expectedPercent = 30.0;
			break;
		case "SUP":
			expectedPercent = 35.0;
			break;
		case "DLX":
			expectedPercent = 40.0;
			break;
		case "SUT":
			expectedPercent = 50.0;
			break;
		}
		if (Math.abs(phanTramDatCoc - expectedPercent) > 0.01) { 
			throw new IllegalArgumentException("Phần trăm đặt cọc không khớp với mã loại phòng");
		}
		this.phanTramDatCoc = phanTramDatCoc;
	}

	@Override
	public String toString() {
		return "LoaiPhong [maLoaiPhong=" + maLoaiPhong + ", tenLoaiPhong=" + tenLoaiPhong + ", giaCoBan=" + giaCoBan
				+ ", phanTramDatCoc=" + phanTramDatCoc + "%]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maLoaiPhong, tenLoaiPhong, giaCoBan, phanTramDatCoc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoaiPhong other = (LoaiPhong) obj;
		return Objects.equals(maLoaiPhong, other.maLoaiPhong) && Objects.equals(tenLoaiPhong, other.tenLoaiPhong)
				&& Double.compare(giaCoBan, other.giaCoBan) == 0
				&& Double.compare(phanTramDatCoc, other.phanTramDatCoc) == 0;
	}
}