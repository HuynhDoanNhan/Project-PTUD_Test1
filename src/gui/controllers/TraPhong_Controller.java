package gui.controllers;

import bus.QuanLyHoaDon_Business;
import bus.QuanLyPhieuDat_Business;
import dto.*;
import com.jfoenix.controls.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import utils.ExportFile;
import utils.ShowDialog;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

import utils.NumberFormat;

import static utils.ExportFile.printPdf;

public class TraPhong_Controller implements Initializable {
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton btnGoiYTien1;

    @FXML
    private JFXButton btnGoiYTien2;

    @FXML
    private JFXButton btnGoiYTien3;

    @FXML
    private JFXButton btnHoanThanh;

    @FXML
    private JFXButton btnSearchMaHD;

    @FXML
    private Label lbCCCD;

    @FXML
    private Label lbHoVaTen;

    @FXML
    private Label lbMaKH;

    @FXML
    private Label lbNgaySinh;


    @FXML
    private Label lbPhiBoSung;

    @FXML
    private Label lbSDT;

    @FXML
    private TextField lbSoTIenKhachTra;

    @FXML
    private Label lbSoTienCanTT;

    @FXML
    private Label lbSoTienDaTT;

    @FXML
    private Label lbThoiGianLapHD;
    
    @FXML
    private Label lbThoiGianBatDauO;

    @FXML
    private Label lbThongTinHD;

    @FXML
    private Label lbTongTien;

    @FXML
    private TextField searchBarSDTOrCCCD;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea ghiChu;

    private CaLamViec caLamViec;
    private VBox tbChiTiet;
    private HoaDon hoaDon;

    public void setCaLamViec(CaLamViec caLamViec) {
        this.caLamViec = caLamViec;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() ->{
            tbChiTiet = new VBox(10);
            scrollPane.setContent(tbChiTiet);
            scrollPane.setStyle("-fx-background:white; -fx-background-color: white");
            btnSearchMaHD.setOnAction(event -> {
                String text = searchBarSDTOrCCCD.getText();
                if (text.isEmpty()) {
                    ShowDialog.showDialog(stackPane, "Lỗi", "Vui lòng nhập số điện thoại hay cccd", "OK");
                } else {
                    hoaDon = QuanLyHoaDon_Business.getHoaDonHienTaiTheoSDTOrCCCD(text);
                    if (hoaDon == null) {
                        ShowDialog.showDialog(stackPane, "Lỗi", "Không tìm thấy hóa đơn", "OK");
                        return;
                    }
                    if (!hoaDon.isTrangThai()) {
                        lbThongTinHD.setText(hoaDon.getMaHoaDon());
                        lbMaKH.setText(hoaDon.getKhachHang().getMaKhachHang());
                        lbHoVaTen.setText(hoaDon.getKhachHang().getTenKhachHang());
                        lbCCCD.setText(hoaDon.getKhachHang().getCCCD());
                        lbNgaySinh.setText(NumberFormat.dateFormat(hoaDon.getKhachHang().getNgaySinh()));
                        lbSDT.setText(hoaDon.getKhachHang().getSoDienThoai());
                        lbThoiGianBatDauO.setText(NumberFormat.dateFormat(hoaDon.getNgayNhanPhong().toLocalDate()));
                        ghiChu.setText(hoaDon.getGhiChu());

                        // Tính tổng tiền của hóa đơn

                        lbThoiGianLapHD.setText(NumberFormat.dateFormat(hoaDon.getNgayTraPhong().toLocalDate()));


                        double soTienDaTT = 0;
                        if (hoaDon.getMaHoaDon().contains("PD")) {
                            String maPD = hoaDon.getMaHoaDon().substring(2);
                            PhieuDatPhong phieuDatPhong = QuanLyPhieuDat_Business.getPhieuDatPhongTheoMaPhieuDat(maPD);

                            soTienDaTT = phieuDatPhong.getTienCoc();
                        }
                        double phiBoSung = hoaDon.getPhiBoSung();

                        LocalDateTime now = LocalDateTime.now();
                        // Tính phí bổ sung khi trả phòng trễ
                        double phi = 0;
                        // Nếu trả sau ngày hiện tại phí bằng 1 ngày thuê phòng
                        ArrayList<PhongThue> list = hoaDon.getListPhongThue();


                        if (hoaDon.getNgayTraPhong().toLocalDate().isAfter(now.toLocalDate())) {
                            double tongTienPhongTraSom = 0;
                            for (PhongThue phongThue : hoaDon.getListPhongThue()) {
                                if (phongThue.getNgayKetThucO().toLocalDate().isAfter(now.toLocalDate())) {
                                    tongTienPhongTraSom += phongThue.getDonGia();
                                }
                            }
                            long soNgaySom = ChronoUnit.DAYS.between(now, hoaDon.getNgayTraPhong());
                            phi = tongTienPhongTraSom * soNgaySom * 0.5;
                            for (PhongThue phongThue : hoaDon.getListPhongThue()) {
                                if (phongThue.getNgayBatDauO().toLocalDate().isAfter(now.toLocalDate())) {
                                    phongThue.setNgayKetThucO(now);
                                    phongThue.setNgayBatDauO(now);
                                } else {
                                    phongThue.setNgayKetThucO(now);
                                }
                            }
                            if (phi > 0) {
                                ghiChu.appendText("\nPhí trả phòng sớm " + soNgaySom + " ngày:" + "(" + NumberFormat.dateTimeFormat(now) + "): " + NumberFormat.currencyFormat(phi));
                                phiBoSung += phi;
                            }
                        } else if (hoaDon.getNgayTraPhong().toLocalDate().isEqual(now.toLocalDate())) {
                            double tongTienPhong = 0;
                            for (PhongThue phongThue : hoaDon.getListPhongThue()) {
                                if (phongThue.getNgayKetThucO().toLocalDate().isEqual(now.toLocalDate())) {
                                    tongTienPhong += phongThue.getDonGia();
                                }
                            }
                            if (now.getHour() > 14 && now.getHour() <= 16) {
                                phi = tongTienPhong * 0.3;
                            } else if (now.getHour() > 16 && now.getHour() <= 18) {
                                phi = tongTienPhong * 0.5;
                            } else if (now.getHour() > 18 ) {
                                phi = tongTienPhong;
                            }
                            if (phi > 0) {
                                ghiChu.appendText("\nPhí trả phòng trễ: ("+ NumberFormat.dateTimeFormat(LocalDateTime.now()) + "):" + NumberFormat.currencyFormat(phi));
                                phiBoSung += phi;
                            }
                        } else if (hoaDon.getNgayTraPhong().toLocalDate().isBefore(now.toLocalDate())) {
                            double tongTienPhong = list.stream().mapToDouble(PhongThue::getDonGia).sum();
                            phi = tongTienPhong;
                            if (phi > 0) {
                                ghiChu.appendText("\nPhí trả phòng trễ: ("+ NumberFormat.dateTimeFormat(LocalDateTime.now()) + "):" + NumberFormat.currencyFormat(phi));
                                phiBoSung += phi;
                            }
                        }
                        double tongTien = 0;
                        for (PhongThue phongThue : hoaDon.getListPhongThue()) {
                            tongTien += phongThue.getThanhTien();
                        }

                        // Tính toán tổng tiền với thuế và chiết khấu
                        double tongTieSauThue = tongTien + (tongTien * 10 / 100);

                        double soTienCanThanhToan = tongTieSauThue - soTienDaTT + phiBoSung;
                        if (soTienCanThanhToan < 0) {
                            soTienCanThanhToan = 0;
                        }

                        lbTongTien.setText(NumberFormat.currencyFormat(tongTien));
                        lbPhiBoSung.setText(NumberFormat.currencyFormat(phiBoSung));
                        lbSoTienDaTT.setText(NumberFormat.currencyFormat(soTienDaTT));
                        lbSoTienCanTT.setText(NumberFormat.currencyFormat(soTienCanThanhToan));
                        updateDSChiTiet();
                        goiYTien(soTienCanThanhToan);
                    } else {
                        ShowDialog.showDialog(stackPane, "Lỗi", "Hóa đơn đã được thanh toán", "OK");
                    }
                }
            });


            btnHoanThanh.setOnAction(event -> {
                System.out.println(caLamViec);
                if (lbThongTinHD.getText().isEmpty()) {
                    ShowDialog.showDialog(stackPane, "Lỗi", "Chưa có thông tin", "OK");
                    return;
                }
                if (lbSoTIenKhachTra.getText().isEmpty()) {
                    ShowDialog.showDialog(stackPane, "Lỗi", "Vui lòng nhập số tiền khách trả", "OK");
                } else {
                    double soTienKhachTra = 0;
                    double soTienCanThanhToan = 0;
                    try {
                        soTienKhachTra = NumberFormat.currencyUnFormat(lbSoTIenKhachTra.getText() + ",000 VND");
                        soTienCanThanhToan = NumberFormat.currencyUnFormat(lbSoTienCanTT.getText());
                    } catch (Exception e) {
                        ShowDialog.showDialog(stackPane, "Lỗi", "Số tiền không đúng định dạng", "OK");
                    }
                    // Kiểm tra số tiền khách hàng thanh toán
                    if (soTienKhachTra < soTienCanThanhToan - 1) {
                        ShowDialog.showDialog(stackPane, "Lỗi", "Số tiền không hợp lệ", "OK");
                    } else {
                        try {
                            hoaDon.setTrangThai(true);
                            hoaDon.setGhiChu(ghiChu.getText());
                            hoaDon.setPhiBoSung(NumberFormat.currencyUnFormat(lbPhiBoSung.getText()));
                            System.out.println(NumberFormat.currencyUnFormat(lbPhiBoSung.getText()));
                            hoaDon.setNgayTraPhong(LocalDateTime.now());
                            hoaDon.getListPhongThue().forEach(phongThue -> {
                                phongThue.setNgayKetThucO(LocalDateTime.now());
                            });
                            hoaDon.setCaLamViec(caLamViec);

                            byte[] pdfBaos = ExportFile.getHoaDonPDF(hoaDon, lbSoTienDaTT.getText(), lbSoTienCanTT.getText());
                            QuanLyHoaDon_Business.updateHoaDon(hoaDon);
                            printPdf(pdfBaos, "HoaDon" + hoaDon.getMaHoaDon());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        ShowDialog.showDialog(stackPane, "Thành công", "Thanh toán thành công", "OK");
                        resetScene();
                    }
                }
            });
        });
    }

    // Hàm gợi ý số tiền
    public void goiYTien(double soTien) {
        double soTienGoiY1 = Math.round(soTien / 1000) * 1000;
        btnGoiYTien1.setText(NumberFormat.currencyFormat(soTienGoiY1));
        btnGoiYTien1.setOnAction(event -> {
            lbSoTIenKhachTra.setText(formatGoiYTien(soTienGoiY1 / 1000));
        });
        double soTienGoiY2 = Math.floor(soTien / 100000) * 100000 + 100000;
        btnGoiYTien2.setText(NumberFormat.currencyFormat(soTienGoiY2));
        btnGoiYTien2.setOnAction(event -> {
            lbSoTIenKhachTra.setText(formatGoiYTien(soTienGoiY2 / 1000));
        });
        double soTienGoiY3 = Math.floor(soTien / 500000) * 500000 + 500000;
        btnGoiYTien3.setText(NumberFormat.currencyFormat(soTienGoiY3));
        btnGoiYTien3.setOnAction(event -> {
            lbSoTIenKhachTra.setText(formatGoiYTien(soTienGoiY3 / 1000));
        });
    }

    public String formatGoiYTien(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAmount = decimalFormat.format(amount);
        return formattedAmount;
    }

    // Hàm cập nhật và vẽ giao diện danh sách chi tiết phòng và dịch vụ
    public void updateDSChiTiet() {
        updateTitleDS();
        ArrayList<PhongThue> listCTHDPhong = hoaDon.getListPhongThue();
        for (int i = 0; i < listCTHDPhong.size(); i++) {
            PhongThue cthd = listCTHDPhong.get(i);
            ArrayList<ChiTietDichVu> listCTHDDV = cthd.getListChiTietDichVu();
            createDS(cthd);
            for (int j = 0; j < listCTHDDV.size(); j++) {
                createDS(listCTHDDV.get(j));
            }
        }
    }

    public void createDS(PhongThue cthd) {
        HBox dvBoxSelect = new HBox();
        dvBoxSelect.getStyleClass().add("dv_select");

        Label lbHangMuc = new Label(cthd.getPhong().getMaPhong());
        lbHangMuc.getStyleClass().addAll("lbMain", "lbFontBold");
        lbHangMuc.setAlignment(Pos.CENTER);
        lbHangMuc.setPrefWidth(90);
        dvBoxSelect.getChildren().add(lbHangMuc);
        
        Period period = Period.between(cthd.getNgayBatDauO().toLocalDate(), cthd.getNgayKetThucO().toLocalDate());
        long days = period.getDays();
        Label lbSoLuong = new Label();

        DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("dd/MM/yy");
        lbSoLuong.setText(cthd.getNgayBatDauO().toLocalDate().format(dinhDang) + "\n" + cthd.getNgayKetThucO().toLocalDate().format(dinhDang));
        lbSoLuong.getStyleClass().add("lbMain");
        lbSoLuong.setStyle("-fx-border-color: #ffffff #ffffff #000000 #ffffff; -fx-background-color: white;");
        lbSoLuong.setAlignment(Pos.CENTER);
        lbSoLuong.setPrefWidth(140);
        HBox.setMargin(lbSoLuong, new Insets(0, 10, 0, 10));
        dvBoxSelect.getChildren().add(lbSoLuong);

        Label lbGiaDV = new Label(NumberFormat.currencyFormat(cthd.getDonGia()));
        lbGiaDV.getStyleClass().add("lbMain");
        lbGiaDV.setStyle("-fx-background-color: white; -fx-border-color: #ffffff #ffffff #000000 #ffffff;");
        lbGiaDV.setAlignment(Pos.CENTER);
        lbGiaDV.setPrefWidth(120);
        HBox.setMargin(lbGiaDV, new Insets(0, 20, 0, 20));
        dvBoxSelect.getChildren().add(lbGiaDV);

        Label lbThanhTien = new Label(NumberFormat.currencyFormat(cthd.getTienPhong()));
        lbThanhTien.getStyleClass().add("lbMain");
        lbThanhTien.setStyle("-fx-background-color: white; -fx-border-color: #ffffff #ffffff #000000 #ffffff;");
        lbThanhTien.setAlignment(Pos.CENTER);
        lbThanhTien.setPrefWidth(120);
        HBox.setMargin(lbThanhTien, new Insets(0, 20, 0, 20));
        dvBoxSelect.getChildren().add(lbThanhTien);

        tbChiTiet.getChildren().add(dvBoxSelect);
    }

    public void createDS(ChiTietDichVu ctdc) {
        HBox dvBoxSelect = new HBox();
        dvBoxSelect.getStyleClass().add("dv_select");

        Label lbHangMuc = new Label(ctdc.getDichVu().getTenDichVu());
        lbHangMuc.getStyleClass().addAll("lbMain", "lbFontBold");
        lbHangMuc.setAlignment(Pos.CENTER);
        lbHangMuc.setPrefWidth(90);
        dvBoxSelect.getChildren().add(lbHangMuc);

        Label lbSoLuong = new Label(ctdc.getSoLuong() + " " + ctdc.getDichVu().getDonViTinh());
        lbSoLuong.getStyleClass().add("lbMain");
        lbSoLuong.setStyle("-fx-border-color: #ffffff #ffffff #000000 #ffffff; -fx-background-color: white;");
        lbSoLuong.setAlignment(Pos.CENTER);
        lbSoLuong.setPrefWidth(140);
        HBox.setMargin(lbSoLuong, new Insets(0, 10, 0, 10));
        dvBoxSelect.getChildren().add(lbSoLuong);

        Label lbGiaDV = new Label(NumberFormat.currencyFormat(ctdc.getDonGia()));
        lbGiaDV.getStyleClass().add("lbMain");
        lbGiaDV.setStyle("-fx-background-color: white; -fx-border-color: #ffffff #ffffff #000000 #ffffff;");
        lbGiaDV.setAlignment(Pos.CENTER);
        lbGiaDV.setPrefWidth(120);
        HBox.setMargin(lbGiaDV, new Insets(0, 20, 0, 20));
        dvBoxSelect.getChildren().add(lbGiaDV);

        Label lbThanhTien = new Label(NumberFormat.currencyFormat(ctdc.getThanhTien()));
        lbThanhTien.getStyleClass().add("lbMain");
        lbThanhTien.setStyle("-fx-background-color: white; -fx-border-color: #ffffff #ffffff #000000 #ffffff;");
        lbThanhTien.setAlignment(Pos.CENTER);
        lbThanhTien.setPrefWidth(120);
        HBox.setMargin(lbThanhTien, new Insets(0, 20, 0, 20));
        dvBoxSelect.getChildren().add(lbThanhTien);

        tbChiTiet.getChildren().add(dvBoxSelect);
    }

    // Hàm tạo ra title cho danh sách chi tiết
    public void updateTitleDS() {
        tbChiTiet.getChildren().clear();

        HBox dvBoxTitle = new HBox();
        dvBoxTitle.getStyleClass().add("dv_select");

        Label lbHangMuc = new Label("Hạng mục");
        lbHangMuc.getStyleClass().addAll("lbMain", "lbFontBold");
        lbHangMuc.setAlignment(Pos.CENTER);
        lbHangMuc.setPrefWidth(140);
        dvBoxTitle.getChildren().add(lbHangMuc);

        Label lbSoLuong = new Label("Số lượng");
        lbSoLuong.getStyleClass().addAll("lbMain", "lbFontBold");
//        lbSoLuong.setStyle("-fx-border-color: #ffffff #ffffff #000000 #ffffff; -fx-background-color: white;");
        lbSoLuong.setAlignment(Pos.CENTER_LEFT);
        lbSoLuong.setPrefWidth(80);
        HBox.setMargin(lbSoLuong, new Insets(0, 10, 0, 10));
        dvBoxTitle.getChildren().add(lbSoLuong);

        Label lbGiaDV = new Label("Đơn giá");
        lbGiaDV.getStyleClass().addAll("lbMain", "lbFontBold");
//        lbGiaDV.setStyle("-fx-background-color: white; -fx-border-color: #ffffff #ffffff #000000 #ffffff;");
        lbGiaDV.setAlignment(Pos.CENTER);
        lbGiaDV.setPrefWidth(120);
        HBox.setMargin(lbGiaDV, new Insets(0, 20, 0, 20));
        dvBoxTitle.getChildren().add(lbGiaDV);

        Label lbThanhTien = new Label("Thành tiền");
        lbThanhTien.getStyleClass().addAll("lbMain", "lbFontBold");
//        lbThanhTien.setStyle("-fx-background-color: white; -fx-border-color: #ffffff #ffffff #000000 #ffffff;");
        lbThanhTien.setAlignment(Pos.CENTER);
        lbThanhTien.setPrefWidth(120);
        HBox.setMargin(lbThanhTien, new Insets(0, 20, 0, 20));
        dvBoxTitle.getChildren().add(lbThanhTien);

        tbChiTiet.getChildren().add(dvBoxTitle);
    }

    private void resetScene() {
        lbThongTinHD.setText("Trống");
        lbMaKH.setText("Trống");
        lbHoVaTen.setText("Trống");
        lbCCCD.setText("Trống");
        lbNgaySinh.setText("Trống");
        lbSDT.setText("Trống");
        lbThoiGianBatDauO.setText("Trống");
        ghiChu.setText("");
        lbThoiGianLapHD.setText("Trống");
        lbTongTien.setText("Trống");
        lbSoTienDaTT.setText("Trống");
        lbPhiBoSung.setText("Trống");
        lbSoTienCanTT.setText("Trống");
        lbSoTIenKhachTra.setText("");
        searchBarSDTOrCCCD.setText("");
        tbChiTiet.getChildren().clear();
    }


}
