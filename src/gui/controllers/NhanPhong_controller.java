package gui.controllers;

import bus.QuanLiKhachHang_Business;
import bus.QuanLyHoaDon_Business;
import bus.QuanLyPhieuDat_Business;
import bus.QuanLyPhong_Bussiness;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import dto.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.NumberFormat;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class NhanPhong_controller implements Initializable {

    @FXML
    private JFXButton btn_timKH;

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField tf_cccd;

    @FXML
    private TextField tf_sdt;

    @FXML
    private TextField tf_tenKH;

    @FXML
    private TextField tf_timKH;

    @FXML
    private FlowPane fp_phieuDatPhong_view;

    @FXML
    private FlowPane fl_displayPhongThue;

    @FXML
    private VBox vb_phongDatView;

    @FXML
    private TextField tf_thanhTien;


    @FXML
    private JFXButton btn_nhanPhong;

    @FXML
    private Label lb_phiBoSung;

    private KhachHang khachHang;
    private PhieuDatPhong phieuDatPhong_isSelected;
    private CaLamViec caLamViec;
    private ArrayList<PhieuDatPhong> listPhieuDatPhong;
    private ArrayList<PhongDat> listPhongDat_isSelected = new ArrayList<>();
    private ArrayList<PhongThue> listPhongThue = new ArrayList<>();
    private int phiBoSung = 0;
    private String ghiChu = "";
    private String maHoaDon = "";
    private Set<KhachHang> setKhachHang = new HashSet<>();
    private ArrayList<Phong> lisPhongCuaPhieuDat;
    private boolean isCoHoaDon;

    public void setCaLamViec(CaLamViec caLamViec) {
        this.caLamViec = caLamViec;
    }

    public Set<KhachHang> getSetKhachHang() {
        return setKhachHang;
    }

    public void setSetKhachHang(Set<KhachHang> setKhachHang) {
        this.setKhachHang = setKhachHang;
    }

    public FlowPane getFp_displayPhongThue(){
        return fl_displayPhongThue;
    }

    public void themPhongThue(PhongThue phongThue) {
        this.listPhongThue.add(phongThue);
    }

    public ArrayList<PhongThue> getListPhongThue() {
        return listPhongThue;
    }

    public ArrayList<PhongDat> getListPhongDat_isSelected() {
        return listPhongDat_isSelected;
    }

    public void themPhongDatDuocChon(PhongDat phongDat) {
        this.listPhongDat_isSelected.add(phongDat);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            tf_thanhTien.setText("0 VND");

            btn_timKH.setOnAction(event -> {
                timKhachHang();
            });

            btn_nhanPhong.setOnAction(event -> {
               if (khachHang == null){
                   showDialog(stackPane, "Lỗi", "Vui lòng tìm kiếm khách hàng trước khi nhận phòng", "OK");
                   return;
               }

               if (listPhongDat_isSelected.isEmpty()){
                   showDialog(stackPane, "Lỗi", "Vui lòng chọn các phòng muốn nhận", "OK");
                   return;
               }

               if (taoHoaDon()){
                     showDialog(stackPane, "Thành công", "Nhận phòng thành công", "OK");
                     updateListPhieuDatPhong(khachHang);
                     reloadPage();
                } else {
                     showDialog(stackPane, "Lỗi", "Nhận phòng thất bại", "OK");
               }
            });
        });
    }

    public boolean taoHoaDon(){
        boolean trangThai = false;

        HoaDon hoaDon = new HoaDon(maHoaDon, caLamViec, khachHang, LocalDate.now().atStartOfDay(), phieuDatPhong_isSelected.getNgayTraPhong().atStartOfDay(), phiBoSung, trangThai, ghiChu);
        hoaDon.setListPhongThue(listPhongThue);
        isCoHoaDon = QuanLyHoaDon_Business.kiemTraMaHoaDon(maHoaDon);

        QuanLyHoaDon_Business.taoHoaDon(hoaDon, isCoHoaDon);
        QuanLyPhieuDat_Business.capNhatTrangThaiPhongDat(phieuDatPhong_isSelected, listPhongDat_isSelected, "DANHAN");
        QuanLyPhong_Bussiness.updateTrangThai(listPhongThue, "DANGTHUE");

        return true;
    }

    public void timKhachHang(){
        if (tf_timKH.getText().isEmpty()) {
            showDialog(stackPane, "Lỗi", "Vui lòng nhập số điện thoại hoặc căn cước công dân khách hàng", "OK");
            tf_timKH.requestFocus();
            vb_phongDatView.getChildren().clear();
            return;
        }

        if (!tf_timKH.getText().matches("\\d*")){
            showDialog(stackPane, "Lỗi", "Số điện thoại hoặc căn cước công dân không hợp lệ", "OK");
            tf_timKH.requestFocus();
            vb_phongDatView.getChildren().clear();
            return;
        }

        khachHang = QuanLiKhachHang_Business.timKhachHangTheoSDTorCCCD(tf_timKH.getText());

        if (khachHang == null) {
            showDialog(stackPane, "Lỗi", "Không tìm thấy khách hàng", "OK");
            tf_timKH.setText("");
            tf_timKH.requestFocus();
            tf_tenKH.setText("");
            tf_cccd.setText("");
            tf_sdt.setText("");
            vb_phongDatView.getChildren().clear();
            return;
        }

        tf_tenKH.setText(khachHang.getTenKhachHang());
        tf_cccd.setText(khachHang.getCCCD());
        tf_sdt.setText(khachHang.getSoDienThoai());

        updateListPhieuDatPhong(khachHang);
        updateFPPhieuDatPhongView();
        vb_phongDatView.getChildren().clear();
    }

    public void updateListPhieuDatPhong(KhachHang khachHang){
        listPhieuDatPhong = QuanLyPhieuDat_Business.layDanhSachPhieuDatPhongTheoKhachHang(khachHang, "CHONHAN");
    }

    public void updateFPPhieuDatPhongView(){
        fp_phieuDatPhong_view.getChildren().clear();
        for (PhieuDatPhong phieuDatPhong : listPhieuDatPhong) {
            PhieuDatPhongView_Controller phieuDatPhongView_controller = new PhieuDatPhongView_Controller(phieuDatPhong);
            AnchorPane phieuDatPhongView = phieuDatPhongView_controller.getRoot();
            if (!phieuDatPhong.getNgayNhanPhong().isAfter(LocalDate.now().plusDays(1))){
                phieuDatPhongView.setOnMouseClicked(event -> {
                    phieuDatPhong_isSelected = phieuDatPhong;
                    maHoaDon = "HD" + phieuDatPhong.getMaPhieu();
                    if (phieuDatPhong.getNgayNhanPhong().isAfter(LocalDate.now())){
                        phiBoSung = 50000;
                        ghiChu = "Nhận phòng trước 1 ngày phí bổ sung " + NumberFormat.currencyFormat(phiBoSung);
                        lb_phiBoSung.setText("Nhận phòng trước phí bổ sung: " + NumberFormat.currencyFormat(phiBoSung));
                    }

                    updateAnPhongDatView(phieuDatPhongView_controller.getPhieuDatPhong());
                });
            }
            fp_phieuDatPhong_view.getChildren().add(phieuDatPhongView);
        }
    }

    public void updateAnPhongDatView(PhieuDatPhong phieuDatPhong){
        vb_phongDatView.getChildren().clear();
        lisPhongCuaPhieuDat = QuanLyPhong_Bussiness.layDanhSachPhongCuaPhieuDat(phieuDatPhong);

        for (PhongDat phongDat : phieuDatPhong.getListPhongDat()) {
            Phong phong_temp = lisPhongCuaPhieuDat.stream()
                    .filter(phong -> phong.equals(phongDat.getPhong()))
                    .findFirst()
                    .orElse(null);
            boolean isDuocNhan = phong_temp.getTrangThaiPhong().toString().equalsIgnoreCase("CONTRONG");
            vb_phongDatView.getChildren().add(createPhongDatView(phongDat, isDuocNhan));
        }
    }

    public AnchorPane createPhongDatView(PhongDat phongDat, boolean isDuocNhan){
        AnchorPane phongDatView = new AnchorPane();
        phongDatView.setPrefHeight(100);
        phongDatView.setStyle("-fx-background-color: #e6e6e6; -fx-background-radius: 10;");

        AnchorPane anMaPhong = new AnchorPane();
        anMaPhong.setPrefWidth(200);
        anMaPhong.setStyle("-fx-background-radius: 10 0 0 10;");

        Object loaiPhong = QuanLyPhong_Bussiness.getTruongPhongTheoMa(phongDat.getPhong().getMaPhong(), "maLoaiPhong");
        String anMaPhongStyle = anMaPhong.getStyle();
        switch (loaiPhong.toString()){
            case "STD": {
                anMaPhong.setStyle(anMaPhongStyle + "-fx-background-color: #11DD03;");
                break;
            }
            case "SUP": {
                anMaPhong.setStyle(anMaPhongStyle + "-fx-background-color: #FFA500;");
                break;
            }
            case "DLX": {
                anMaPhong.setStyle(anMaPhongStyle + "-fx-background-color: #00A1FF;");
                break;
            }
            case "SUT": {
                anMaPhong.setStyle(anMaPhongStyle + "-fx-background-color: #FF4500;");
                break;
            }
        }

        Label maPhong = new Label(phongDat.getPhong().getMaPhong());
        maPhong.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD, 50));
        maPhong.setTextFill(javafx.scene.paint.Color.WHITE);

        anMaPhong.getChildren().add(maPhong);
        AnchorPane.setTopAnchor(maPhong, 48.0);
        AnchorPane.setLeftAnchor(maPhong, 35.5);
        AnchorPane.setRightAnchor(maPhong, 35.5);
        AnchorPane.setBottomAnchor(maPhong, 48.0);

        Label donGia = new Label("Đơn giá: " + NumberFormat.currencyFormat(phongDat.getDonGia()));
        donGia.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD, 18));

        Label soNguoiO = new Label("Số người ở: " + phongDat.getSoNguoi());
        soNguoiO.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD, 18));

        String trangThaiPhongDat = phongDat.getTrangThaiPhongDat().toString();
        switch (trangThaiPhongDat){
            case "DANHAN": {
                trangThaiPhongDat = "Đã nhận";
                break;
            }
            case "CHONHAN": {
                trangThaiPhongDat = "Chờ nhận";
                break;
            }
            case "DAHUY": {
                trangThaiPhongDat = "Đã hủy";
                break;
            }
        }
        Label trangThai = new Label("Trạng thái phòng đặt: " + trangThaiPhongDat);
        trangThai.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD, 18));

        phongDatView.getChildren().addAll(anMaPhong, donGia, soNguoiO, trangThai);
        AnchorPane.setTopAnchor(anMaPhong, 0.0);
        AnchorPane.setLeftAnchor(anMaPhong, 0.0);
        AnchorPane.setBottomAnchor(anMaPhong, 0.0);
        AnchorPane.setTopAnchor(donGia, 20.0);
        AnchorPane.setLeftAnchor(donGia, 225.0);
        AnchorPane.setLeftAnchor(soNguoiO, 225.0);
        AnchorPane.setTopAnchor(soNguoiO, 65.0);
        AnchorPane.setLeftAnchor(trangThai, 225.0);
        AnchorPane.setBottomAnchor(trangThai, 20.0);

        if (phongDat.getTrangThaiPhongDat().toString().equalsIgnoreCase("CHONHAN") && isDuocNhan){
            phongDatView.onMouseClickedProperty().set(event -> {
                Parent root = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NhanPhong_dialog_view.fxml"));
                    root = loader.load();
                    NhanPhong_dialog_controller nhanPhong_dialog_controller = loader.getController();
                    nhanPhong_dialog_controller.setPhongDat(phongDat);
                    nhanPhong_dialog_controller.setMaHoaDon(maHoaDon);
                    nhanPhong_dialog_controller.setPhieuDatPhong(phieuDatPhong_isSelected);
                    nhanPhong_dialog_controller.setPhongDatView(phongDatView);
                    nhanPhong_dialog_controller.setSet_KhachHang(setKhachHang);
                    nhanPhong_dialog_controller.setNhanPhong_controller(this);
                    Stage nhanPhong_dialog_stage = new Stage();
                    Image icon = new Image(getClass().getResource("/image/Remove-bg.ai_1732089798306.png").toExternalForm());
                    nhanPhong_dialog_stage.getIcons().add(icon);
                    nhanPhong_dialog_stage.setScene(new Scene(root));
                    nhanPhong_dialog_stage.centerOnScreen();
                    nhanPhong_dialog_stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                tinhTien();
            });
        }
        else {
            Label trangThaiPhong = new Label("Phòng không trống");
            trangThaiPhong.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD, 18));
            trangThaiPhong.setTextFill(Color.WHITE);
            phongDatView.getChildren().add(trangThaiPhong);

            AnchorPane.setTopAnchor(trangThaiPhong, 10.0);
            AnchorPane.setLeftAnchor(trangThaiPhong, 10.0);
        }
        return phongDatView;
    }

    public void tinhTien(){
        int tongTien = 0;
        for (PhongThue phongThue : listPhongThue) {
            tongTien += phongThue.getThanhTien();
        }

        tf_thanhTien.setText(NumberFormat.currencyFormat(tongTien));
    }

    public void reloadPage(){
        updateFPPhieuDatPhongView();
        vb_phongDatView.getChildren().clear();
        phieuDatPhong_isSelected = null;
        fl_displayPhongThue.getChildren().clear();
        tf_thanhTien.setText("0 VND");
        lb_phiBoSung.setText("");
    }

    public void showDialog(StackPane root, String title, String content, String buttonText) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(title));
        dialogLayout.setBody(new Text(content));

        JFXButton closeButton = new JFXButton(buttonText);
        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.CENTER);
        dialog.getStyleClass().add("lbMain");

        closeButton.setOnAction(event -> dialog.close());
        dialogLayout.setActions(closeButton);
        dialog.show();
    }
}
