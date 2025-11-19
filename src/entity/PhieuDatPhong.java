package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class PhieuDatPhong {
    private String maPhieuDat;
    private LocalDateTime ngayDat;
    private LocalDateTime ngayNhan;
    private LocalDateTime ngayTra;
    private double tongCoc;
    private double tongTien;
    private String ghiChu;

    public PhieuDatPhong(String maPhieuDat, LocalDateTime ngayDat, LocalDateTime ngayNhan, LocalDateTime ngayTra, 
                         double tongCoc, double tongTien, String ghiChu) {
        setMaPhieuDat(maPhieuDat);
        setNgayDat(ngayDat);
        setNgayNhan(ngayNhan);
        setNgayTra(ngayTra);
        setTongCoc(tongCoc);
        setTongTien(tongTien);
        setGhiChu(ghiChu);
    }

    public String getMaPhieuDat() {
        return maPhieuDat;
    }

    public void setMaPhieuDat(String maPhieuDat) {
        if (maPhieuDat == null || maPhieuDat.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu đặt không được null hoặc rỗng");
        }
        if (maPhieuDat.length() != 12) {
            throw new IllegalArgumentException("Mã phiếu đặt phải có đúng 12 ký tự");
        }
        if (!maPhieuDat.matches("^PD\\d{10}$")) {
            throw new IllegalArgumentException("Mã phiếu đặt phải bắt đầu bằng 'PD' và theo sau bởi 10 chữ số");
        }
        // Kiểm tra 6 ký tự tiếp theo là định dạng năm/tháng/ngày (VD: 250922)
        String datePart = maPhieuDat.substring(2, 8);
        if (!datePart.matches("^\\d{2}(\\d{2}){2}$")) {
            throw new IllegalArgumentException("Phần năm/tháng/ngày phải là 6 chữ số (VD: 250922)");
        }
        // Kiểm tra 4 ký tự cuối là số tăng dần (0001-9999)
        String seqPart = maPhieuDat.substring(8, 12);
        try {
            int seqNumber = Integer.parseInt(seqPart);
            if (seqNumber < 1 || seqNumber > 9999) {
                throw new IllegalArgumentException("Số thứ tự phải nằm trong khoảng từ 0001 đến 9999");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Phần số thứ tự phải là 4 chữ số");
        }
        this.maPhieuDat = maPhieuDat;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        if (ngayDat == null) {
            throw new IllegalArgumentException("Ngày đặt không được null");
        }
        if (ngayDat.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Ngày đặt không được lớn hơn thời điểm hiện tại");
        }
        this.ngayDat = ngayDat;
    }

    public LocalDateTime getNgayNhan() {
        return ngayNhan;
    }

    public void setNgayNhan(LocalDateTime ngayNhan) {
        if (ngayNhan == null) {
            throw new IllegalArgumentException("Ngày nhận không được null");
        }
        if (ngayNhan.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Ngày nhận không được nhỏ hơn thời điểm hiện tại");
        }
        this.ngayNhan = ngayNhan;
    }

    public LocalDateTime getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDateTime ngayTra) {
        if (ngayTra == null) {
            throw new IllegalArgumentException("Ngày trả không được null");
        }
        if (ngayTra.isBefore(ngayNhan)) {
            throw new IllegalArgumentException("Ngày trả phải lớn hơn hoặc bằng ngày nhận");
        }
        this.ngayTra = ngayTra;
    }

    public double getTongCoc() {
        return tongCoc;
    }

    public void setTongCoc(double tongCoc) {
        if (tongCoc < 0) {
            throw new IllegalArgumentException("Tổng cọc không được âm");
        }
        this.tongCoc = tongCoc;
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

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        if (ghiChu == null) {
            this.ghiChu = ""; 
        } else {
            this.ghiChu = ghiChu.trim();
        }
    }

    @Override
    public String toString() {
        return "PhieuDatPhong [maPhieuDat=" + maPhieuDat + ", ngayDat=" + ngayDat + ", ngayNhan=" + ngayNhan
                + ", ngayTra=" + ngayTra + ", tongCoc=" + tongCoc + ", tongTien=" + tongTien + ", ghiChu=" + ghiChu + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhieuDat, ngayDat, ngayNhan, ngayTra, tongCoc, tongTien, ghiChu);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PhieuDatPhong other = (PhieuDatPhong) obj;
        return Objects.equals(maPhieuDat, other.maPhieuDat) && Objects.equals(ngayDat, other.ngayDat)
                && Objects.equals(ngayNhan, other.ngayNhan) && Objects.equals(ngayTra, other.ngayTra)
                && Double.compare(tongCoc, other.tongCoc) == 0 && Double.compare(tongTien, other.tongTien) == 0
                && Objects.equals(ghiChu, other.ghiChu);
    }
}