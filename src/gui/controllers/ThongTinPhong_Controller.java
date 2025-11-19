package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bus.QuanLyPhong_Bussiness;
import dto.KhachHang;
import dto.Phong;
import utils.NumberFormat;

import javax.swing.*;

public class ThongTinPhong_Controller {
    @FXML
    private Label lblMaPhong;

    @FXML
    private Label lblTenPhong; // Cần tạo thêm label cho tên phòng

    @FXML
    private ImageView imgPhong; // Thêm ImageView để hiển thị ảnh

    @FXML
    private Label loaigiuong;

    @FXML
    private Label maphong;

    @FXML
    private Label songuoi;

    @FXML
    private Label tenphong;

    @FXML
    private Label giaPhong;

    @FXML
    private Label trangthai;
    @FXML
    private Button btnhuy_x;
    private QuanLyPhong_Bussiness Phong_Bussines=new QuanLyPhong_Bussiness();
    @FXML
    private Label tenkhachhang;
    @FXML
    private Label sodienthoai;
    @FXML
    private Label ngaythue;

    @FXML
    private Label ngaytra;
    @FXML
    private Label khachluutru;
    public void setRoomDetails(Phong p) {
        // Hiển thị ảnh
        ImageView imageView = new ImageView(getClass().getResource("/image/hinhPhong/" +p.getMaPhong() + ".jpg").toExternalForm());
        imageView.setLayoutX(10.0);
        imageView.setLayoutY(20.0);
        imageView.setFitWidth(170); // Set width
        imageView.setFitHeight(130); // Set height
        imageView.setPreserveRatio(false); // Allow non-proportional resizing
        imageView.getStyleClass().add("img");
        imgPhong.setImage(imageView.getImage());

        Map<String, String> trangThaiPhong = new HashMap<>();
        trangThaiPhong.put("DLX", "Deluxe");
        trangThaiPhong.put("STD", "Standard");
        trangThaiPhong.put("SUP", "Superior");
        trangThaiPhong.put("SUT", "Suite");
        String tenLoaiPhong = trangThaiPhong.get(p.getLoaiPhong().getMaLoaiPhong());

        maphong.setText(p.getMaPhong());
        tenphong.setText(tenLoaiPhong);
        Map<String, String> trangThaiMap = new HashMap<>();
        trangThaiMap.put("CONTRONG", "Phòng trống");
        trangThaiMap.put("DANGDON", "Phòng đang dọn");
        trangThaiMap.put("DANGTHUE", "Phòng được thuê");
        trangThaiMap.put("DANGBAOTRI", "Phòng đang bảo trì");
        trangThaiMap.put("NGUNGHOATDONG", "Ngừng hoạt động");
        String trangThai = trangThaiMap.get(p.getTrangThaiPhong().name());
        trangthai.setText(trangThai);
        loaigiuong.setText(p.getLoaiGiuongSuDung().getMaLoaiGiuong());
        this.songuoi.setText(String.valueOf(p.getSoNguoi()));
        this.giaPhong.setText(String.valueOf(NumberFormat.currencyFormat(p.getGiaPhong())));
        
        Map<String, String> thongTinKhachHang = Phong_Bussines.layThongTinKhachHangTheoPhong(p.getMaPhong());
        String ngayBatDauFormatted = null;
        String ngayBatDau = thongTinKhachHang.get("ngayBatDauO");
        if (ngayBatDau != null && !ngayBatDau.trim().isEmpty()) {
            ngayBatDauFormatted = dinhDangNgay(ngayBatDau);
        } else {
            ngayBatDauFormatted = "Trống"; 
        }
        String ngayTraFormatted = null;
        String ngayKetThuc = thongTinKhachHang.get("ngayKetThucO");
        if (ngayKetThuc != null && !ngayKetThuc.trim().isEmpty()) {
            ngayTraFormatted = dinhDangNgay(ngayKetThuc);
        } else {
            ngayTraFormatted = "Trống"; 
        }
        String hoTen = thongTinKhachHang.containsKey("hoTen") ? thongTinKhachHang.get("hoTen") : "Trống";
        String soDienThoai = thongTinKhachHang.containsKey("soDienThoai") ? thongTinKhachHang.get("soDienThoai") : "Trống";

        tenkhachhang.setText(hoTen != null ? hoTen : "Trống");
        sodienthoai.setText(soDienThoai != null ? soDienThoai : "Trống");
        ngaythue.setText(ngayBatDauFormatted);
        ngaytra.setText(ngayTraFormatted);
        
        List<KhachHang> khachthue = Phong_Bussines.getDskhachhang(p.getMaPhong());
        String kh = "";

        if (khachthue != null && !khachthue.isEmpty()) {
            for (KhachHang k : khachthue) {
            	kh += k.getTenKhachHang() + "\n";
            }
        }
        khachluutru.setText(kh.isEmpty() ? "Trống" : kh.trim());
    }

    @FXML
    void handcapnhap(ActionEvent event) {
        String currentStatus = trangthai.getText();
        if (currentStatus.equalsIgnoreCase("Phòng được thuê")) {
            JOptionPane.showMessageDialog(null, "Không thể cập nhật thông tin phòng vì phòng đang được thuê.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CapNhapThongTinPhong_view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Cập nhật thông tin phòng");

            CapNhapThongTinPhong_Controller capnhapthongtinphong = loader.getController();
            capnhapthongtinphong.setRoomDetails(
                    maphong.getText(),
                    tenphong.getText(),
                    loaigiuong.getText(),
                    Integer.parseInt(songuoi.getText()),
                    Double.parseDouble(giaPhong.getText().replaceAll("[^\\d.]", "")),
                    trangthai.getText()
            );

            capnhapthongtinphong.setParentStage((Stage) btnhuy_x.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void btnHuy(ActionEvent event) {
        Stage stage = (Stage) btnhuy_x.getScene().getWindow();
        stage.close();
    }
    private String dinhDangNgay(String ngayGoc) {
	    SimpleDateFormat dinhDangCu = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dinhDangMoi = new SimpleDateFormat("dd-MM-yyyy");
	    try {
	        Date date = dinhDangCu.parse(ngayGoc);
	        return dinhDangMoi.format(date);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ngayGoc;
	    }
	}
}
