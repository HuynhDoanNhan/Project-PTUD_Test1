package gui.controllers;

import bus.Access_Business;
import bus.QuanLyNhanVien_Business;
import bus.QuanLyPhong_Bussiness;
import dto.NhanVien;
import dto.TaiKhoan;
import utils.ShowDialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrangDangNhap_Controller implements Initializable {

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField matKhau;

    @FXML
    private TextField tenTaiKhoan;

    @FXML
    private Rectangle rectangle;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button forgotPassBtn;
    @FXML
    private TextField hienMatKhau;
    @FXML
    private CheckBox cbHienMatKhau;

    private TaiKhoan taiKhoan = new TaiKhoan();

    private Access_Business accessBusiness = new Access_Business();
    private QuanLyNhanVien_Business quanLyNhanVien_business = new QuanLyNhanVien_Business();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(getClass().getResource("/image/Remove-bg.ai_1732089798306.png").toExternalForm());
        rectangle.setFill(new ImagePattern(image));
        hienMatKhau.setVisible(false);
        hienMatKhau.setManaged(false);

        matKhau.textProperty().bindBidirectional(hienMatKhau.textProperty());

        cbHienMatKhau.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                hienMatKhau.setManaged(true);
                hienMatKhau.setVisible(true);
                matKhau.setManaged(false);
                matKhau.setVisible(false);
            } else {
                hienMatKhau.setManaged(false);
                hienMatKhau.setVisible(false);
                matKhau.setManaged(true);
                matKhau.setVisible(true);
            }
        });

        btnLogin.setOnAction(actionEvent -> {
            taiKhoan = accessBusiness.getTaiKhoan(tenTaiKhoan.getText(), matKhau.getText());
            if (taiKhoan != null) {
      
                NhanVien nhanVien = quanLyNhanVien_business.timNhanVien(taiKhoan.getNhanVien().getMaNhanVien());
                try {
                	System.out.println("Đăng nhập thành công");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/KhungGiaoDien_view.fxml"));
                    Parent root = loader.load();

                    KhungGiaoDien_controller khungGiaoDien_controller = loader.getController();
                    khungGiaoDien_controller.setNhanVien(nhanVien);

                    Stage KhungGiaoDien_stage = new Stage();
                    Image icon = new Image(getClass().getResource("/image/Remove-bg.ai_1732089798306.png").toExternalForm());
                    KhungGiaoDien_stage.getIcons().add(icon);
                    KhungGiaoDien_stage.setScene(new Scene(root));
                    KhungGiaoDien_stage.centerOnScreen();
                    KhungGiaoDien_stage.show();

                    QuanLyPhong_Bussiness.updateTrangThaiPhongTheoNgay();
                    Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                ShowDialog.showDialog(stackPane, "Thông báo", "Tên tài khoản hoặc mật khẩu không đúng", "OK");
            }
        });

        forgotPassBtn.setOnAction(actionEvent -> {
            Stage currentStage = (Stage) forgotPassBtn.getScene().getWindow();
            showDialog(currentStage);
        });
    }

    private void showDialog(Stage parentStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với các cửa sổ khác
        dialogStage.initOwner(parentStage); // Đặt chủ sở hữu là cửa sổ chính
        dialogStage.setTitle("Quên mật khẩu");
        dialogStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuenMatKhau_dialog_view.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return; // Dừng nếu không load được FXML
        }

        Scene dialogScene = new Scene(anchorPane);
        dialogStage.setScene(dialogScene);

        // Hiển thị dialog
        dialogStage.showAndWait(); // Chờ người dùng đóng trước khi tiếp tục
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }
}
