package entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Pattern;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String cccd;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private LocalDate ngayBatDauLamViec;
    private LocalDate ngayKetThucLamViec;
    private String vaiTro;
    private boolean trangThaiNhanVien;

    public NhanVien(String maNhanVien, String hoTen, String cccd, String soDienThoai, LocalDate ngaySinh,
                    LocalDate ngayBatDauLamViec, LocalDate ngayKetThucLamViec, String vaiTro, boolean trangThaiNhanVien) {
        setMaNhanVien(maNhanVien);
        setHoTen(hoTen);
        setCccd(cccd);
        setSoDienThoai(soDienThoai);
        setNgaySinh(ngaySinh);
        setNgayBatDauLamViec(ngayBatDauLamViec);
        setNgayKetThucLamViec(ngayKetThucLamViec);
        setVaiTro(vaiTro);
        setTrangThaiNhanVien(trangThaiNhanVien);
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được null hoặc rỗng");
        }
        if (maNhanVien.length() != 11) {
            throw new IllegalArgumentException("Mã nhân viên phải có đúng 11 ký tự");
        }
        if (!maNhanVien.matches("^NV\\d{9}$")) {
            throw new IllegalArgumentException("Mã nhân viên phải bắt đầu bằng 'NV' và theo sau bởi 9 chữ số");
        }

        try {
            String namSinhPart = maNhanVien.substring(2, 4); // 2 ký tự năm sinh
            int namSinh = Integer.parseInt("20" + namSinhPart); // Giả định năm sinh từ 2000 trở lên
            int namHienTai = LocalDate.now().getYear(); // 2025
            if (namSinh > namHienTai || namSinh < 1900) {
                throw new IllegalArgumentException("Năm sinh không hợp lệ");
            }
            int seqNumber = Integer.parseInt(maNhanVien.substring(6)); // 3 ký tự cuối
            if (seqNumber < 1 || seqNumber > 999) {
                throw new IllegalArgumentException("Số thứ tự phải nằm trong khoảng từ 001 đến 999");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Phần năm sinh và số thứ tự phải là số");
        }
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được null hoặc rỗng");
        }
        // Kiểm tra không chứa ký tự đặc biệt hoặc số
        if (Pattern.compile("[0-9#*?!]").matcher(hoTen).find()) {
            throw new IllegalArgumentException("Họ tên không được chứa số hoặc ký hiệu đặc biệt");
        }
        // Kiểm tra ít nhất 2 từ, viết hoa chữ cái đầu, có khoảng trắng
        String[] words = hoTen.trim().split("\\s+");
        if (words.length < 2) {
            throw new IllegalArgumentException("Họ tên phải có ít nhất 2 từ");
        }
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        this.hoTen = formattedName.toString().trim();
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) {
            throw new IllegalArgumentException("CCCD không được null hoặc rỗng");
        }
        if (cccd.length() != 12) {
            throw new IllegalArgumentException("CCCD phải có đúng 12 ký tự");
        }
        if (!cccd.matches("^\\d{12}$")) {
            throw new IllegalArgumentException("CCCD phải là 12 chữ số");
        }
        // Kiểm tra 3 ký tự đầu (mã tỉnh)
        String maTinh = cccd.substring(0, 3);
        if (!maTinh.matches("^(001|079)$")) { // Ví dụ: 001 (Hà Nội), 079 (TP HCM), có thể mở rộng
            throw new IllegalArgumentException("Mã tỉnh không hợp lệ (ví dụ: 001, 079)");
        }
        // Kiểm tra ký tự giới tính (vị trí 3)
        String gioiTinh = cccd.substring(3, 4);
        if (!gioiTinh.matches("[01]")) {
            throw new IllegalArgumentException("Mã giới tính phải là 0 (nam) hoặc 1 (nữ)");
        }
        // Kiểm tra năm sinh (vị trí 4-5)
        String namSinhPart = cccd.substring(4, 6);
        try {
            int namSinh = Integer.parseInt("20" + namSinhPart); // Giả định từ 2000
            int namHienTai = LocalDate.now().getYear(); // 2025
            if (namSinh < 1900 || namSinh > namHienTai) {
                throw new IllegalArgumentException("Năm sinh không hợp lệ");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Phần năm sinh phải là số");
        }
        this.cccd = cccd;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được null hoặc rỗng");
        }
        if (soDienThoai.length() != 10) {
            throw new IllegalArgumentException("Số điện thoại phải có đúng 10 ký tự");
        }
        if (!soDienThoai.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số");
        }
        // Kiểm tra 3 ký tự đầu (đầu số nhà mạng)
        String dauSo = soDienThoai.substring(0, 3);
        if (!dauSo.matches("^(070|091)$")) { // Ví dụ: 070 (MobiFone), 091 (Vinaphone), có thể mở rộng
            throw new IllegalArgumentException("Đầu số không hợp lệ (ví dụ: 070, 091)");
        }
        this.soDienThoai = soDienThoai;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        if (ngaySinh == null) {
            throw new IllegalArgumentException("Ngày sinh không được null");
        }
        LocalDate ngayHienTai = LocalDate.now(); // 26/10/2025
        int tuoi = Period.between(ngaySinh, ngayHienTai).getYears();
        if (tuoi < 18) {
            throw new IllegalArgumentException("Nhân viên phải từ 18 tuổi trở lên");
        }
        this.ngaySinh = ngaySinh;
    }

    public LocalDate getNgayBatDauLamViec() {
        return ngayBatDauLamViec;
    }

    public void setNgayBatDauLamViec(LocalDate ngayBatDauLamViec) {
        if (ngayBatDauLamViec == null) {
            throw new IllegalArgumentException("Ngày bắt đầu làm việc không được null");
        }
        if (ngayBatDauLamViec.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày bắt đầu làm việc không được lớn hơn ngày hiện tại");
        }
        if (ngaySinh != null && !ngayBatDauLamViec.isAfter(ngaySinh.plusYears(18))) {
            throw new IllegalArgumentException("Ngày bắt đầu làm việc phải lớn hơn ngày sinh + 18 năm");
        }
        this.ngayBatDauLamViec = ngayBatDauLamViec;
    }

    public LocalDate getNgayKetThucLamViec() {
        return ngayKetThucLamViec;
    }

    public void setNgayKetThucLamViec(LocalDate ngayKetThucLamViec) {
        if (ngayKetThucLamViec != null && ngayBatDauLamViec != null && ngayKetThucLamViec.isBefore(ngayBatDauLamViec)) {
            throw new IllegalArgumentException("Ngày kết thúc làm việc phải lớn hơn hoặc bằng ngày bắt đầu làm việc");
        }
        this.ngayKetThucLamViec = ngayKetThucLamViec;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        if (vaiTro == null || vaiTro.trim().isEmpty()) {
            throw new IllegalArgumentException("Vai trò không được null hoặc rỗng");
        }
        if (!vaiTro.matches("^(Nhân viên|Quản lý)$")) {
            throw new IllegalArgumentException("Vai trò phải là 'Nhân viên' hoặc 'Quản lý'");
        }
        this.vaiTro = vaiTro;
    }

    public boolean getTrangThaiNhanVien() {
        return trangThaiNhanVien;
    }

    public void setTrangThaiNhanVien(boolean trangThaiNhanVien) {
        this.trangThaiNhanVien = trangThaiNhanVien;
    }

    @Override
    public String toString() {
        return "NhanVien [maNhanVien=" + maNhanVien + ", hoTen=" + hoTen + ", cccd=" + cccd
                + ", soDienThoai=" + soDienThoai + ", ngaySinh=" + ngaySinh
                + ", ngayBatDauLamViec=" + ngayBatDauLamViec + ", ngayKetThucLamViec=" + ngayKetThucLamViec
                + ", vaiTro=" + vaiTro + ", trangThaiNhanVien="
                + (trangThaiNhanVien ? "đang làm" : "không còn làm") + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNhanVien, hoTen, cccd, soDienThoai, ngaySinh, ngayBatDauLamViec, ngayKetThucLamViec, vaiTro, trangThaiNhanVien);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NhanVien other = (NhanVien) obj;
        return Objects.equals(maNhanVien, other.maNhanVien) && Objects.equals(hoTen, other.hoTen)
                && Objects.equals(cccd, other.cccd) && Objects.equals(soDienThoai, other.soDienThoai)
                && Objects.equals(ngaySinh, other.ngaySinh)
                && Objects.equals(ngayBatDauLamViec, other.ngayBatDauLamViec)
                && Objects.equals(ngayKetThucLamViec, other.ngayKetThucLamViec)
                && Objects.equals(vaiTro, other.vaiTro) && trangThaiNhanVien == other.trangThaiNhanVien;
    }
}