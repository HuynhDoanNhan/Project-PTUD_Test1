package gui.controllers;

import bus.Access_Business;
import bus.QuanLyNhanVien_Business;
import dto.NhanVien;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class QuenMatKhauDialog_Controller implements Initializable {

    @FXML
    private TextField cCCD;

    @FXML
    private TextField maNV;

    @FXML
    private JFXButton resetPass;

    @FXML
    private TextField sdt;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetPass.setOnAction(actionEvent -> {
            String cCCDString = cCCD.getText();
            String maNVString = maNV.getText();
            String sdtString = sdt.getText();

            if (cCCDString.isEmpty() || maNVString.isEmpty() || sdtString.isEmpty()) {
                System.out.println("Vui lòng nhập đầy đủ thông tin");
            } else {
                Boolean isSuccess = Access_Business.resetPassword(maNVString, sdtString, cCCDString);
                if (isSuccess) {
                   Stage stage = (Stage) resetPass.getScene().getWindow();
                   stage.close();
                } else {
                    System.out.println("Reset mật khẩu thất bại");
                }
            }
        });
    }
}

