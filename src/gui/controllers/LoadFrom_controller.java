package gui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoadFrom_controller {
    @FXML
    private ProgressBar progressBar; // Thanh tiến trình

    public void initialize() {
        // Tạo hiệu ứng tăng giá trị progress bar từ 0 đến 1 trong 2 giây
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2.2))
        );

        // Khi timeline hoàn thành, chuyển sang TrangDangNhap_view.fxml
        timeline.setOnFinished(event -> {
            try {
                // Load file TrangDangNhap_view.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TrangDangNhap_view.fxml"));
                Parent root = loader.load();

                // Lấy stage hiện tại từ progressBar
                Stage stage = (Stage) progressBar.getScene().getWindow();

                // Đặt scene mới
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.getIcons().add(new Image(getClass().getResource("/image/Remove-bg.ai_1732089798306.png").toExternalForm()));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Chạy timeline
        timeline.play();
    }
}
