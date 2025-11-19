package entity;

import java.util.Objects;

public class LoaiGiuong {
	private String maLoaiGiuong;
	private String tenLoaiGiuong;
	private double giaGiuong;

	public LoaiGiuong(String maLoaiGiuong, double giaGiuong) {
		setMaLoaiGiuong(maLoaiGiuong);
		setGiaGiuong(giaGiuong);
		setTenLoaiGiuongBasedOnMaLoaiGiuong();
	}

	public String getMaLoaiGiuong() {
		return maLoaiGiuong;
	}

	public void setMaLoaiGiuong(String maLoaiGiuong) {
		if (maLoaiGiuong == null || maLoaiGiuong.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã loại giường không được null hoặc rỗng");
		}
		if (!maLoaiGiuong.matches("^(SG|DB|QN|KG)$")) {
			throw new IllegalArgumentException("Mã loại giường phải là một trong các giá trị: SG, DB, QN, KG");
		}
		this.maLoaiGiuong = maLoaiGiuong;
	}

	public String getTenLoaiGiuong() {
		return tenLoaiGiuong;
	}

	private void setTenLoaiGiuongBasedOnMaLoaiGiuong() {
		switch (maLoaiGiuong) {
		case "SG":
			this.tenLoaiGiuong = "Single Bed (Giường đơn)";
			break;
		case "DB":
			this.tenLoaiGiuong = "Double Bed (Giường đôi nhỏ)";
			break;
		case "QN":
			this.tenLoaiGiuong = "Queen Size Bed (Giường đôi lớn)";
			break;
		case "KG":
			this.tenLoaiGiuong = "King Size Bed (Giường cỡ lớn)";
			break;
		default:
			throw new IllegalStateException("Mã loại giường không hợp lệ");
		}
	}

	// Phương thức setTenLoaiGiuong chỉ cho phép cập nhật nếu khớp với maLoaiGiuong
	public void setTenLoaiGiuong(String tenLoaiGiuong) {
		if (tenLoaiGiuong == null || tenLoaiGiuong.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên loại giường không được null hoặc rỗng");
		}
		String expectedTen = "";
		switch (maLoaiGiuong) {
		case "SG":
			expectedTen = "Single Bed (Giường đơn)";
			break;
		case "DB":
			expectedTen = "Double Bed (Giường đôi nhỏ)";
			break;
		case "QN":
			expectedTen = "Queen Size Bed (Giường đôi lớn)";
			break;
		case "KG":
			expectedTen = "King Size Bed (Giường cỡ lớn)";
			break;
		}
		if (!tenLoaiGiuong.equals(expectedTen)) {
			throw new IllegalArgumentException("Tên loại giường không khớp với mã loại giường");
		}
		this.tenLoaiGiuong = tenLoaiGiuong;
	}

	public double getGiaGiuong() {
		return giaGiuong;
	}

	public void setGiaGiuong(double giaGiuong) {
		if (giaGiuong < 0) {
			throw new IllegalArgumentException("Giá giường không được âm");
		}
		this.giaGiuong = giaGiuong;
	}

	@Override
	public String toString() {
		return "LoaiGiuong [maLoaiGiuong=" + maLoaiGiuong + ", tenLoaiGiuong=" + tenLoaiGiuong + ", giaGiuong="
				+ giaGiuong + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maLoaiGiuong, tenLoaiGiuong, giaGiuong);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoaiGiuong other = (LoaiGiuong) obj;
		return Objects.equals(maLoaiGiuong, other.maLoaiGiuong) && Objects.equals(tenLoaiGiuong, other.tenLoaiGiuong)
				&& Double.compare(giaGiuong, other.giaGiuong) == 0;
	}
}