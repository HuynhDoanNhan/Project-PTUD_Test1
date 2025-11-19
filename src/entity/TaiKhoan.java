package entity;

import java.util.Objects;
import java.util.regex.Pattern;

public class TaiKhoan {
    private String taiKhoan;
    private String matKhau;
    private boolean trangThai;
    private NhanVien nhanVien; // Quan hệ với NhanVien

    public TaiKhoan(NhanVien nhanVien, String matKhau, boolean trangThai) {
        setNhanVien(nhanVien);
        setMatKhau(matKhau);
        setTrangThai(trangThai);
        // Gán taiKhoan từ maNhanVien của NhanVien
        this.taiKhoan = nhanVien.getMaNhanVien();
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("Nhân viên không được null");
        }
        this.nhanVien = nhanVien;
        // Cập nhật taiKhoan khi nhanVien thay đổi
        this.taiKhoan = nhanVien.getMaNhanVien();
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        if (matKhau == null || matKhau.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được null hoặc rỗng");
        }
        if (matKhau.length() > 25) {
            throw new IllegalArgumentException("Mật khẩu không được vượt quá 25 ký tự");
        }
        // Kiểm tra ít nhất 1 số, 1 chữ cái in hoa, 1 ký tự đặc biệt
        if (!Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*])[0-9A-Za-z!@#$%^&*]{1,25}$").matcher(matKhau).matches()) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 1 số, 1 chữ cái in hoa, và 1 ký tự đặc biệt");
        }
        this.matKhau = matKhau;
    }

    public boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    @Override
    public String toString() {
        return "TaiKhoan [taiKhoan=" + taiKhoan + ", matKhau=" + matKhau
                + ", trangThai=" + (trangThai ? "hoạt động" : "không hoạt động")
                + ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : "null") + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(taiKhoan, matKhau, trangThai, nhanVien);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaiKhoan other = (TaiKhoan) obj;
        return Objects.equals(taiKhoan, other.taiKhoan) && Objects.equals(matKhau, other.matKhau)
                && trangThai == other.trangThai && Objects.equals(nhanVien, other.nhanVien);
    }
}