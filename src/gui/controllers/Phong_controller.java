package gui.controllers;

import com.jfoenix.controls.JFXButton;
import dto.Phong;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class Phong_controller {

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane an_loaiPhong;

    @FXML
    private JFXButton btn_chon;

    @FXML
    private ImageView imgView_trangThaiPhong;

    @FXML
    private Label lb_giaPhong;

    @FXML
    private Label lb_loaiGiuong;

    @FXML
    private Label lb_trangThai;

    @FXML
    private Label lb_loaiPhong;

    @FXML
    private Label lb_maPhong;

    @FXML
    private Label lb_soNguoi;

    private ThuePhong_Controller thuePhong_controller;
    private Phong phong;
    private boolean isSelected = false;

    public void setThuePhong_controller(ThuePhong_Controller thuePhong_controller) {
        this.thuePhong_controller = thuePhong_controller;
    }

    public void isSelected() {
        this.isSelected = !this.isSelected;
    }

    public AnchorPane getRoot(){
        return root;
    }

    public Phong getPhong() {
        return phong;
    }

    public void updateChoose(boolean isSelected){
        if (isSelected){
            root.setBackground(new Background(
                    new BackgroundFill(
                            Color.web("lightgreen"),
                            new CornerRadii(10),
                            Insets.EMPTY
                    )
            ));
        }
        else {
            root.setBackground(new Background(
                    new BackgroundFill(
                            Color.web("#b8b8b8"),
                            new CornerRadii(10),
                            Insets.EMPTY
                    )
            ));
        }

        btn_chon.setVisible(!isSelected);
    }

    public Phong_controller(Phong phong) {
        this.phong = phong;
        createNode();
    }

    public Phong_controller(Phong phong, boolean isSelected) {
        this.phong = phong;
        this.isSelected = isSelected;

        createNode();
    }

    public void createNode(){
        loadFXML();

        this.phong = phong;

        lb_maPhong.setText(phong.getMaPhong());

        lb_loaiPhong.setText(phong.getLoaiPhong().getMaLoaiPhong());
        switch (phong.getLoaiPhong().getMaLoaiPhong()){
            case "STD": {
                an_loaiPhong.setStyle("-fx-background-color: #11DD03; -fx-background-radius: 5;");
                break;
            }
            case "SUP": {
                an_loaiPhong.setStyle("-fx-background-color: #FFA500; -fx-background-radius: 5;");
                break;
            }
            case "DLX": {
                an_loaiPhong.setStyle("-fx-background-color: #00A1FF; -fx-background-radius: 5;");
                break;
            }
            case "SUT": {
                an_loaiPhong.setStyle("-fx-background-color: #FF4500; -fx-background-radius: 5;");
                break;
            }
        }
        lb_loaiGiuong.setText("Loại giường: " + phong.getLoaiGiuongSuDung().getMaLoaiGiuong());
        lb_soNguoi.setText("Số người: " + phong.getSoNguoi());
        lb_giaPhong.setText(formatCurrency(phong.getGiaPhong()));

        switch (phong.getTrangThaiPhong().toString()){
            case "CONTRONG": {
                lb_trangThai.setText("Còn trống");
                break;
            }
            case "DANGTHUE": {
                lb_trangThai.setText("Đang thuê");
                break;
            }
            case "DANGDON": {
                lb_trangThai.setText("Đang dọn");
                break;
            }
            case "DANGBAOTRI": {
                lb_trangThai.setText("Đang bảo trì");
                break;
            }
            case "NGUNGHOATDONG": {
                lb_trangThai.setText("Ngừng hoạt động");
                break;
            }
        }

        if (isSelected){
            root.setBackground(new Background(
                    new BackgroundFill(
                            Color.web("lightgreen"),
                            new CornerRadii(10),
                            Insets.EMPTY
                    )
            ));
            btn_chon.setVisible(false);
        }
        else if(!phong.getTrangThaiPhong().toString().equalsIgnoreCase("CONTRONG")){
            btn_chon.setVisible(false);
            root.setBackground(new Background(
                    new BackgroundFill(
                            Color.web("#4c4c50"),
                            new CornerRadii(10),
                            Insets.EMPTY
                    )
            ));
            lb_loaiGiuong.setStyle("-fx-text-fill: white;");
            lb_soNguoi.setStyle("-fx-text-fill: white;");
            lb_giaPhong.setStyle("-fx-text-fill: white;");
        }
        else {
            root.setBackground(new Background(
                    new BackgroundFill(
                            Color.web("#b8b8b8"),
                            new CornerRadii(10),
                            Insets.EMPTY
                    )
            ));
        }

        imgView_trangThaiPhong.setImage(new Image(getClass().getResource("/image/" + phong.getTrangThaiPhong().toString() + ".png").toExternalForm()));

        btn_chon.setOnAction(event -> {
            if (thuePhong_controller.getSoNguoiThue() == 0 || thuePhong_controller.getKhachHang() == null){
                thuePhong_controller.callDialog("Lỗi", "Vui lòng tìm khách hàng và điền số người trước khi chọn phòng", "OK");
                return;
            }

            if (thuePhong_controller.getKhachHang().getNhomTuoi().toString().equals("TREEM")){
                thuePhong_controller.callDialog("Lỗi", "Khách hàng chưa là người lớn không thể thuê phòng", "OK");
                return;
            }

            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ThuePhong_dialog_view.fxml"));
                root = loader.load();
                ThuePhong_dialog_controller thuePhong_dialog_controller = loader.getController();
                thuePhong_dialog_controller.setPhong(phong);
                thuePhong_dialog_controller.setThuePhong_controller(thuePhong_controller);
                thuePhong_dialog_controller.setPhong_controller(this);
                Stage thuePhong_dialog_stage = new Stage();
                Image icon = new Image(getClass().getResource("/image/Remove-bg.ai_1732089798306.png").toExternalForm());
                thuePhong_dialog_stage.getIcons().add(icon);
                thuePhong_dialog_stage.setScene(new Scene(root));
                thuePhong_dialog_stage.centerOnScreen();
                thuePhong_dialog_stage.initModality(Modality.APPLICATION_MODAL);
                thuePhong_dialog_stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String formatCurrency(double giaTien) {
        Locale locale = new Locale("vi", "VN"); // Định dạng Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String formattedAmount = currencyFormatter.format(giaTien);
        formattedAmount = formattedAmount.replace("₫", "VND").trim();

        return formattedAmount;
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/Phong_view.fxml")));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load ButtonPhong_DatDichVu_View.fxml");
        }
    }
}
