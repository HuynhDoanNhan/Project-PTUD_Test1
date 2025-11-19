package entity;

import java.util.Objects;

public class Phong {
	private String maPhong;
	private LoaiPhong loaiPhong;
	private LoaiGiuong loaiGiuong;
	private int soNguoi;
	private double giaPhong;
	private String trangThai;

	public Phong(String maPhong, LoaiPhong loaiPhong, LoaiGiuong loaiGiuong, int soNguoi, double giaPhong,
			String trangThai) {
		setMaPhong(maPhong);
		setLoaiPhong(loaiPhong);
		setLoaiGiuong(loaiGiuong);
		setSoNguoi(soNguoi);
		setGiaPhong(giaPhong);
		setTrangThai(trangThai);
	}

	public String getMaPhong() {
		return maPhong;
	}

	public void setMaPhong(String maPhong) {
		if (maPhong == null || maPhong.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã phòng không được null hoặc rỗng");
		}
		if (maPhong.length() != 4) {
			throw new IllegalArgumentException("Mã phòng phải có đúng 4 ký tự");
		}
		if (!maPhong.matches("^P[1-9]\\d{2}$")) {
			throw new IllegalArgumentException(
					"Mã phòng phải bắt đầu bằng 'P', ký tự tiếp theo là số tầng (1-9), và 2 ký tự cuối là số tự tăng (00-99)");
		}
		// Kiểm tra ký tự thứ hai là số tầng hợp lệ (1-9)
		int tang = Character.getNumericValue(maPhong.charAt(1));
		if (tang < 1 || tang > 9) {
			throw new IllegalArgumentException("Số tầng phải từ 1 đến 9");
		}
		// Kiểm tra 2 ký tự cuối là số từ 00 đến 99
		String roomNumberPart = maPhong.substring(2, 4);
		try {
			int roomNumber = Integer.parseInt(roomNumberPart);
			if (roomNumber < 0 || roomNumber > 99) {
				throw new IllegalArgumentException("Số phòng phải từ 00 đến 99");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("2 ký tự cuối của mã phòng phải là số");
		}
		this.maPhong = maPhong;
	}

	public LoaiPhong getLoaiPhong() {
		return loaiPhong;
	}

	public void setLoaiPhong(LoaiPhong loaiPhong) {
		if (loaiPhong == null) {
			throw new IllegalArgumentException("Loại phòng không được null");
		}
		this.loaiPhong = loaiPhong;
	}

	public LoaiGiuong getLoaiGiuong() {
		return loaiGiuong;
	}

	public void setLoaiGiuong(LoaiGiuong loaiGiuong) {
		if (loaiGiuong == null) {
			throw new IllegalArgumentException("Loại giường không được null");
		}
		this.loaiGiuong = loaiGiuong;
	}

	public int getSoNguoi() {
		return soNguoi;
	}

	public void setSoNguoi(int soNguoi) {
		if (soNguoi < 0) {
			throw new IllegalArgumentException("Số người không được âm");
		}
		this.soNguoi = soNguoi;
	}

	public double getGiaPhong() {
		return giaPhong;
	}

	public void setGiaPhong(double giaPhong) {
		if (giaPhong < 0) {
			throw new IllegalArgumentException("Giá phòng không được âm");
		}
		this.giaPhong = giaPhong;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		if (trangThai == null || trangThai.trim().isEmpty()) {
			throw new IllegalArgumentException("Trạng thái không được null hoặc rỗng");
		}
		if (!trangThai.matches("^(Trong|DaDat|DangSua)$")) {
			throw new IllegalArgumentException("Trạng thái phải là 'Trong', 'DaDat', hoặc 'DangSua'");
		}
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "Phong [maPhong=" + maPhong + ", loaiPhong=" + (loaiPhong != null ? loaiPhong.getMaLoaiPhong() : "null")
				+ ", loaiGiuong=" + (loaiGiuong != null ? loaiGiuong.getMaLoaiGiuong() : "null") + ", soNguoi="
				+ soNguoi + ", giaPhong=" + giaPhong + ", trangThai=" + trangThai + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maPhong, loaiPhong, loaiGiuong, soNguoi, giaPhong, trangThai);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phong other = (Phong) obj;
		return Objects.equals(maPhong, other.maPhong) && Objects.equals(loaiPhong, other.loaiPhong)
				&& Objects.equals(loaiGiuong, other.loaiGiuong) && soNguoi == other.soNguoi
				&& Double.compare(giaPhong, other.giaPhong) == 0 && Objects.equals(trangThai, other.trangThai);
	}
}