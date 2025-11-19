package gui.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class About_controller implements Initializable {
    @FXML
    private JFXButton btn_dong;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_dong.setOnAction(event -> {
            Stage currentStage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
            currentStage.close();
        });
    }
}
