package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class PhieuKetToan {
    private String maPhieuKetToan;
    private LocalDateTime thoiGianLap;
    private double soTienBatDauCa;
    private double soTienKetThucCa;
    private double doanhThuThucTe;
    private double doanhThuThongKe;
    private CaLamViec caLamViec;
    private NhanVien nhanVienLap;
    private NhanVien nhanVienDuyet;

    public enum CaLamViec {
        S, C, T
    }

    public PhieuKetToan(String maPhieuKetToan, LocalDateTime thoiGianLap, double soTienBatDauCa, double soTienKetThucCa,
                        double doanhThuThucTe, double doanhThuThongKe, CaLamViec caLamViec, NhanVien nhanVienLap,
                        NhanVien nhanVienDuyet) {
        setMaPhieuKetToan(maPhieuKetToan);
        setThoiGianLap(thoiGianLap);
        setSoTienBatDauCa(soTienBatDauCa);
        setSoTienKetThucCa(soTienKetThucCa);
        setDoanhThuThucTe(doanhThuThucTe);
        setDoanhThuThongKe(doanhThuThongKe);
        setCaLamViec(caLamViec);
        setNhanVienLap(nhanVienLap);
        setNhanVienDuyet(nhanVienDuyet);
    }

    public String getMaPhieuKetToan() {
        return maPhieuKetToan;
    }

    public void setMaPhieuKetToan(String maPhieuKetToan) throws NumberFormatException {
        if (maPhieuKetToan == null || maPhieuKetToan.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu kết toán không được null hoặc rỗng");
        }
        if (maPhieuKetToan.length() != 9) {
            throw new IllegalArgumentException("Mã phiếu kết toán phải có đúng 9 ký tự");
        }
        // Kiểm tra định dạng KTddmmyyX
        if (!maPhieuKetToan.matches("^KT\\d{6}[SCT]$")) {
            throw new IllegalArgumentException("Mã phiếu kết toán phải có dạng KTddmmyyX, với X là S/C/T");
        }
        // Kiểm tra ngày ddmmyy hợp lệ
        String datePart = maPhieuKetToan.substring(2, 8); // Lấy ddmmyy
        try {
            int day = Integer.parseInt(datePart.substring(0, 2)); // dd
            int month = Integer.parseInt(datePart.substring(2, 4)); // mm
            int year = Integer.parseInt("20" + datePart.substring(4)); // yy, giả định từ 2000
            LocalDate date = LocalDate.of(year, month, day);
            LocalDate currentDate = LocalDate.now(); // 26/10/2025
            if (date.isAfter(currentDate)) {
                throw new IllegalArgumentException("Ngày trong mã phiếu không được lớn hơn ngày hiện tại");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Phần ngày ddmmyy không hợp lệ");
        }
        this.maPhieuKetToan = maPhieuKetToan;
    }

    public LocalDateTime getThoiGianLap() {
        return thoiGianLap;
    }

    public void setThoiGianLap(LocalDateTime thoiGianLap) {
        if (thoiGianLap == null) {
            throw new IllegalArgumentException("Thời gian lập không được null");
        }
        // Giả định thời gian bắt đầu ca dựa trên caLamViec (cần caLamViec đã được đặt trước)
        if (caLamViec != null) {
            LocalDateTime thoiGianBatDauCa = getThoiGianBatDauCa(caLamViec, thoiGianLap.toLocalDate());
            if (thoiGianLap.isAfter(thoiGianBatDauCa)) {
                throw new IllegalArgumentException("Thời gian lập phải có trước hoặc bằng thời gian bắt đầu ca");
            }
        }
        this.thoiGianLap = thoiGianLap;
    }

    // Phương thức hỗ trợ để lấy thời gian bắt đầu ca (giả định)
    private LocalDateTime getThoiGianBatDauCa(CaLamViec ca, LocalDate date) {
        switch (ca) {
            case S: return LocalDateTime.of(date, java.time.LocalTime.of(6, 0)); // 6:00 sáng
            case C: return LocalDateTime.of(date, java.time.LocalTime.of(14, 0)); // 2:00 chiều
            case T: return LocalDateTime.of(date, java.time.LocalTime.of(22, 0)); // 10:00 tối
            default: throw new IllegalArgumentException("Ca làm việc không hợp lệ");
        }
    }

    public double getSoTienBatDauCa() {
        return soTienBatDauCa;
    }

    public void setSoTienBatDauCa(double soTienBatDauCa) {
        if (soTienBatDauCa < 0) {
            throw new IllegalArgumentException("Số tiền bắt đầu ca không được âm");
        }
        this.soTienBatDauCa = soTienBatDauCa;
    }

    public double getSoTienKetThucCa() {
        return soTienKetThucCa;
    }

    public void setSoTienKetThucCa(double soTienKetThucCa) {
        if (soTienKetThucCa < 0) {
            throw new IllegalArgumentException("Số tiền kết thúc ca không được âm");
        }
        this.soTienKetThucCa = soTienKetThucCa;
    }

    public double getDoanhThuThucTe() {
        return doanhThuThucTe;
    }

    public void setDoanhThuThucTe(double doanhThuThucTe) {
        if (doanhThuThucTe < 0) {
            throw new IllegalArgumentException("Doanh thu thực tế không được âm");
        }
        this.doanhThuThucTe = doanhThuThucTe;
    }

    public double getDoanhThuThongKe() {
        return doanhThuThongKe;
    }

    public void setDoanhThuThongKe(double doanhThuThongKe) {
        if (doanhThuThongKe < 0) {
            throw new IllegalArgumentException("Doanh thu thống kê không được âm");
        }
        this.doanhThuThongKe = doanhThuThongKe;
    }

    public CaLamViec getCaLamViec() {
        return caLamViec;
    }

    public void setCaLamViec(CaLamViec caLamViec) {
        if (caLamViec == null) {
            throw new IllegalArgumentException("Ca làm việc không được null");
        }
        this.caLamViec = caLamViec;
        // Cập nhật lại thoiGianLap nếu cần
        if (thoiGianLap != null) {
            setThoiGianLap(thoiGianLap); // Kiểm tra lại điều kiện thời gian
        }
    }

    public NhanVien getNhanVienLap() {
        return nhanVienLap;
    }

    public void setNhanVienLap(NhanVien nhanVienLap) {
        if (nhanVienLap == null) {
            throw new IllegalArgumentException("Nhân viên lập không được null");
        }
        this.nhanVienLap = nhanVienLap;
    }

    public NhanVien getNhanVienDuyet() {
        return nhanVienDuyet;
    }

    public void setNhanVienDuyet(NhanVien nhanVienDuyet) {
        if (nhanVienDuyet == null) {
            throw new IllegalArgumentException("Nhân viên duyệt không được null");
        }
        this.nhanVienDuyet = nhanVienDuyet;
    }

    @Override
    public String toString() {
        return "PhieuKetToan [maPhieuKetToan=" + maPhieuKetToan + ", thoiGianLap=" + thoiGianLap
                + ", soTienBatDauCa=" + soTienBatDauCa + ", soTienKetThucCa=" + soTienKetThucCa
                + ", doanhThuThucTe=" + doanhThuThucTe + ", doanhThuThongKe=" + doanhThuThongKe
                + ", caLamViec=" + caLamViec + ", nhanVienLap="
                + (nhanVienLap != null ? nhanVienLap.getMaNhanVien() : "null")
                + ", nhanVienDuyet=" + (nhanVienDuyet != null ? nhanVienDuyet.getMaNhanVien() : "null") + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhieuKetToan, thoiGianLap, soTienBatDauCa, soTienKetThucCa, doanhThuThucTe,
                doanhThuThongKe, caLamViec, nhanVienLap, nhanVienDuyet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PhieuKetToan other = (PhieuKetToan) obj;
        return Objects.equals(maPhieuKetToan, other.maPhieuKetToan)
                && Objects.equals(thoiGianLap, other.thoiGianLap)
                && Double.compare(soTienBatDauCa, other.soTienBatDauCa) == 0
                && Double.compare(soTienKetThucCa, other.soTienKetThucCa) == 0
                && Double.compare(doanhThuThucTe, other.doanhThuThucTe) == 0
                && Double.compare(doanhThuThongKe, other.doanhThuThongKe) == 0
                && caLamViec == other.caLamViec
                && Objects.equals(nhanVienLap, other.nhanVienLap)
                && Objects.equals(nhanVienDuyet, other.nhanVienDuyet);
    }
}