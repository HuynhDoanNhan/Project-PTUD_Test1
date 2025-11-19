package entity;

import java.util.Objects;
import java.util.regex.Pattern;

public class DichVu {
    private String maDichVu;
    private String tenDichVu;
    private String moTa;
    private String donViTinh;
    private double donGia;
    private int soLuong;
    private boolean trangThaiDichVu;

    public DichVu(String maDichVu, String tenDichVu, String moTa, String donViTinh, double donGia, int soLuong, boolean trangThaiDichVu) {
        setMaDichVu(maDichVu);
        setTenDichVu(tenDichVu);
        setMoTa(moTa);
        setDonViTinh(donViTinh);
        setDonGia(donGia);
        setSoLuong(soLuong);
        setTrangThaiDichVu(trangThaiDichVu);
    }

    public String getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(String maDichVu) {
        if (maDichVu == null || maDichVu.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã dịch vụ không được null hoặc rỗng");
        }
        if (maDichVu.length() != 7) {
            throw new IllegalArgumentException("Mã dịch vụ phải có đúng 7 ký tự");
        }
        if (!maDichVu.matches("^DV\\w{2}\\d{3}$")) {
            throw new IllegalArgumentException("Mã dịch vụ phải bắt đầu bằng 'DV', theo sau bởi 2 ký tự viết tắt và 3 số tăng dần");
        }
        String seqPart = maDichVu.substring(4);
        try {
            int seqNumber = Integer.parseInt(seqPart);
            if (seqNumber < 1 || seqNumber > 999) {
                throw new IllegalArgumentException("Số thứ tự phải nằm trong khoảng từ 001 đến 999");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Phần số thứ tự phải là 3 chữ số");
        }
        this.maDichVu = maDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        if (tenDichVu == null || tenDichVu.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên dịch vụ không được null hoặc rỗng");
        }
        // Kiểm tra không chứa ký tự đặc biệt hoặc số
        if (Pattern.compile("[0-9#*?!]").matcher(tenDichVu).find()) {
            throw new IllegalArgumentException("Tên dịch vụ không được chứa số hoặc ký hiệu đặc biệt");
        }
        // Kiểm tra có khoảng trắng và viết hoa chữ cái đầu
        String[] words = tenDichVu.trim().split("\\s+");
        if (words.length < 1) {
            throw new IllegalArgumentException("Tên dịch vụ phải có ít nhất 1 từ");
        }
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        this.tenDichVu = formattedName.toString().trim();
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        if (moTa == null || moTa.trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả không được null hoặc rỗng");
        }
        this.moTa = moTa.trim();
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        if (donViTinh == null || donViTinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Đơn vị tính không được null hoặc rỗng");
        }
        this.donViTinh = donViTinh.trim();
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá không được âm");
        }
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
    }

    public boolean getTrangThaiDichVu() {
        return trangThaiDichVu;
    }

    public void setTrangThaiDichVu(boolean trangThaiDichVu) {
        this.trangThaiDichVu = trangThaiDichVu;
    }

    @Override
    public String toString() {
        return "DichVu [maDichVu=" + maDichVu + ", tenDichVu=" + tenDichVu + ", moTa=" + moTa
                + ", donViTinh=" + donViTinh + ", donGia=" + donGia + ", soLuong=" + soLuong
                + ", trangThaiDichVu=" + (trangThaiDichVu ? "sẵn sàng" : "chưa sẵn sàng") + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDichVu, tenDichVu, moTa, donViTinh, donGia, soLuong, trangThaiDichVu);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DichVu other = (DichVu) obj;
        return Objects.equals(maDichVu, other.maDichVu) && Objects.equals(tenDichVu, other.tenDichVu)
                && Objects.equals(moTa, other.moTa) && Objects.equals(donViTinh, other.donViTinh)
                && Double.compare(donGia, other.donGia) == 0 && soLuong == other.soLuong
                && trangThaiDichVu == other.trangThaiDichVu;
    }
}