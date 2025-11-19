package gui.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import bus.QuanLyPhieuDat_Business;
import dto.PhieuDatPhong;
import dto.PhongDat;
import dto.enum_class.TrangThaiPhieuDat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import utils.NumberFormat;

public class PhieuDatPhongView_Controller {
	@FXML
	private AnchorPane root;

    @FXML
    private VBox phieuDatPhongView;

    @FXML
    private TextField tfNgayNhan;

    @FXML
    private TextField tfPhongDat;

    @FXML
    private TextField tfTTKhachHang;

    @FXML
    private TextField tfTienDaCoc;

    @FXML
    private TextField tfTongTien;

    @FXML
    private VBox clTrangThai;
    
    private PhieuDatPhong phieuDatPhong;
    
    public PhieuDatPhongView_Controller(PhieuDatPhong phieuDatPhong) {
    	this.phieuDatPhong = phieuDatPhong;
    	loadFXML();
    	clTrangThai.setStyle("-fx-background-color: " + getMauTrangThai(phieuDatPhong.getTrangThaiPhieuDat().toString()) + "; -fx-background-radius:  10 10 0 0" );
    	tfTTKhachHang.setText(phieuDatPhong.getKhachHang().getTenKhachHang() + " - " + phieuDatPhong.getKhachHang().getSoDienThoai());
        tfNgayNhan.setText(NumberFormat.dateFormat(phieuDatPhong.getNgayNhanPhong()));
        String listMaPhong = phieuDatPhong.getListPhongDat().stream()
        	    .map(phongDat -> phongDat.getPhong().getMaPhong())
        	    .collect(Collectors.joining(", "));
        tfPhongDat.setText(listMaPhong);
        tfTongTien.setText(NumberFormat.currencyFormat(phieuDatPhong.getTongTien()));
        if (phieuDatPhong.getTrangThaiPhieuDat() == TrangThaiPhieuDat.DAHUY) {
            tfTienDaCoc.setText("0 VND");
        } else {
            tfTienDaCoc.setText(NumberFormat.currencyFormat(phieuDatPhong.getTienCoc()));
        }
    }

    public AnchorPane getRoot() {
    	return root;
    }
    
    public PhieuDatPhong getPhieuDatPhong() {
    	return phieuDatPhong;
    }
    
    public VBox getPhieuDatPhongView() {
    	return phieuDatPhongView;
    }
    
    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/PhieuDatPhong_View.fxml")));
        loader.setController(this);
        try {
            root = loader.load();
//            root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/PhieuDatPhong_View.css")).toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load PhieuDatPhong_View.fxml");
        }
    }

    
    private String getMauTrangThai(String trangThai) {
        if (Objects.equals(trangThai, "CHONHAN") && phieuDatPhong.getNgayNhanPhong().equals(LocalDate.now())) {
            return "#ff9616";
        }
    	switch (trangThai) {
			case "CHONHAN" -> {
				return "#c37f55";
			}
			case "DANHAN" -> {
				return "#279656";
			}
			default -> {
				return "#677484";
			}
    	}
    }
}

