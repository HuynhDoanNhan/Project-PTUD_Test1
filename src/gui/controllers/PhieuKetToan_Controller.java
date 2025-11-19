package gui.controllers;

import bus.QuanLyHoaDon_Business;
import bus.QuanLyPhieuDat_Business;
import bus.ThongKeVaBaoCao_Business;
import com.jfoenix.controls.JFXButton;
import dto.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.ExportFile;
import utils.NumberFormat;
import utils.ShowDialog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PhieuKetToan_Controller {
    @FXML
    private JFXButton btnDongCa;

    @FXML
    private JFXButton btnDongCaAndIn;

    @FXML
    private JFXButton btnThemPhieuKiemTien;

    @FXML
    private Label lbCaLamViec;

    @FXML
    private Label lbNgayMoCa;

    @FXML
    private Label lbSLHoaDon;

    @FXML
    private Label lbSLHoaDonTra;

    @FXML
    private Label lbSLPhieuDat;

    @FXML
    private Label lbSoTienChenhLech;

    @FXML
    private Label lbTenNV;

    @FXML
    private Label lbTienMatCuoiCa;

    @FXML
    private Label lbTienMatDauCa;

    @FXML
    private Label lbTienMatThucTe;

    @FXML
    private Label lbTienMatTrongCa;

    @FXML
    private Label lbTongTienHoaDon;

    @FXML
    private Label lbTongTienHoaDonTra;

    @FXML
    private Label lbTongTienPhieuDat;

    @FXML
    private StackPane root;

    private CaLamViec caLamViec;
    
    private PhieuKiemTien phieuKiemTienDauCa;
    private PhieuKiemTien phieuKiemTienCuoiCa;

    public PhieuKetToan_Controller(CaLamViec caLamViec) {
        loadFXML();
        this.caLamViec = caLamViec;
        lbCaLamViec.setText(caLamViec.getMaCa());
        lbNgayMoCa.setText(NumberFormat.dateTimeFormat(caLamViec.getThoiGianBatDau()));
        lbTenNV.setText(caLamViec.getNhanVien().getHoTen());
        phieuKiemTienDauCa = ThongKeVaBaoCao_Business.layPhieuKiemTienMoiNhat();
        double tienMatDauCa = phieuKiemTienDauCa.getTongTien();
        lbTienMatDauCa.setText(NumberFormat.currencyFormat(tienMatDauCa));

        Map<String, Double> soLuongVaTongTienHoaDon = QuanLyHoaDon_Business.getSoLuongVaTongTienHoaDon(caLamViec);
        lbSLHoaDon.setText(NumberFormat.formatNumberNoneHasDecimal(soLuongVaTongTienHoaDon.get("soLuong")));
        lbTongTienHoaDon.setText(NumberFormat.currencyFormat(soLuongVaTongTienHoaDon.get("tongTien")));
        double tongTienHoaDon = soLuongVaTongTienHoaDon.get("tongTien");

        Map<String, Double> soLuongVaTongTienPhieuDat = QuanLyPhieuDat_Business.getSoLuongVaTongTienPhieuDat(caLamViec);;
        lbSLPhieuDat.setText(NumberFormat.formatNumberNoneHasDecimal(soLuongVaTongTienPhieuDat.get("soLuong")));
        lbTongTienPhieuDat.setText(NumberFormat.currencyFormat(soLuongVaTongTienPhieuDat.get("tongTien")));
        double tongTienPhieuDat = soLuongVaTongTienPhieuDat.get("tongTien");

        Map<String, Double> soLuongVaTongTienHoaDonTra = QuanLyHoaDon_Business.getSoLuongVaTongTienHoaDonTra(caLamViec);
        lbSLHoaDonTra.setText(NumberFormat.formatNumberNoneHasDecimal(soLuongVaTongTienHoaDonTra.get("soLuong")));
        lbTongTienHoaDonTra.setText(NumberFormat.currencyFormat(soLuongVaTongTienHoaDonTra.get("tongTien")));
        double tongTienHoaDonTra = soLuongVaTongTienHoaDonTra.get("tongTien");

        double tienMatTrongCa = tongTienHoaDon + tongTienPhieuDat - tongTienHoaDonTra;
        lbTienMatTrongCa.setText(NumberFormat.currencyFormat(tienMatTrongCa));

        double tienMatCuoiCa = tienMatDauCa + tienMatTrongCa;
        lbTienMatCuoiCa.setText(NumberFormat.currencyFormat(tienMatCuoiCa));
        
        lbSoTienChenhLech.setText(NumberFormat.currencyFormat(tienMatCuoiCa));

        btnThemPhieuKiemTien.setOnAction(event -> {
            try {
                showDialog((Stage) root.getScene().getWindow(), tienMatCuoiCa);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        btnDongCa.setOnAction(event -> {
        	caLamViec.setThoiGianKetThuc(LocalDateTime.now());
        	
        	if (phieuKiemTienCuoiCa == null) {
        		ShowDialog.showDialog(root, "Lỗi", "Chưa kiểm tiền cuối ca", "OK");
        		return;
        	}
        	
        	PhieuKetToan phieuKetToan = new PhieuKetToan(LocalDateTime.now(), caLamViec, phieuKiemTienDauCa, phieuKiemTienCuoiCa, tienMatTrongCa);
        	ThongKeVaBaoCao_Business.ketCa(phieuKetToan);   
            try {
                moTrangDangNhap();
                
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        
        btnDongCaAndIn.setOnAction(event -> {
        	caLamViec.setThoiGianKetThuc(LocalDateTime.now());
        	
        	if (phieuKiemTienCuoiCa == null) {
        		ShowDialog.showDialog(root, "Lỗi", "Chưa kiểm tiền cuối ca", "OK");
        		return;
        	}
        	
        	PhieuKetToan phieuKetToan = new PhieuKetToan(LocalDateTime.now(), caLamViec, phieuKiemTienDauCa, phieuKiemTienCuoiCa, tienMatTrongCa);
//        	ThongKeVaBaoCao_Business.ketCa(phieuKetToan);
            byte[] pdfBaos = ExportFile.getPhieuKetCaPDF(phieuKetToan, soLuongVaTongTienHoaDon, soLuongVaTongTienPhieuDat, soLuongVaTongTienHoaDonTra);
            try {
            	ExportFile.printPdf(pdfBaos, "PhieuKetToan" + phieuKetToan.getMaPhieuKetToan());
                moTrangDangNhap();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }

    private void showDialog(Stage primaryStage, double tienMatCuoiCa) throws Exception {

        PhieuKiemTien_Controller controller = new PhieuKiemTien_Controller(caLamViec.getNhanVien());
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Phiếu kiểm tiền");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(primaryStage);

        Scene scene = new Scene(controller.getRoot());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        double tongTienThucTe = 0;
        if (!controller.getTongTien().equals("Chưa có")) {
            tongTienThucTe = NumberFormat.currencyUnFormat(controller.getTongTien());
        }
        lbTienMatThucTe.setText(NumberFormat.currencyFormat(tongTienThucTe));
        lbSoTienChenhLech.setText(NumberFormat.currencyFormat(tienMatCuoiCa - tongTienThucTe));
        phieuKiemTienCuoiCa = controller.getPhieuKiemTien();
    }

    public StackPane getRoot() {
        return root;
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/PhieuKetToan_view.fxml")));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed");
        }
    }

    public void moTrangDangNhap() throws IOException {
    	
    	Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    	
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/TrangDangNhap_view.fxml")));

        Stage loginStage = new Stage();
        Scene scene = new Scene(loader.load());
        Image icon = new Image(getClass().getResource("/image/Remove-bg.ai_1732089798306.png").toExternalForm());
        stage.getIcons().add(icon);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/TrangDangNhap_CSS.css")).toExternalForm());
        loginStage.setScene(scene);
        loginStage.setTitle("Đăng nhập");
        loginStage.show();
    }
}
