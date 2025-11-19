package gui.controllers;

import com.jfoenix.controls.JFXButton;

import dto.CaLamViec;
import dto.NhanVien;
import bus.QuanLyCa_business;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.NumberFormat;
import utils.ShowDialog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class TaoCaLamViec_Controller {

    @FXML
    private JFXButton btnDangXuat;

    @FXML
    private JFXButton btnMoCa;

    @FXML
    private JFXButton btnThemPhieuKiemTien;

    @FXML
    private Label lbGioBatDau;

    @FXML
    private Label lbMaNV;

    @FXML
    private StackPane root;

    @FXML
    private Label lbTienMatDauCa;

    private NhanVien nhanVien;	
    
    private StringProperty timeProperty = new SimpleStringProperty();
    private CaLamViec caLamViec;

    public TaoCaLamViec_Controller(NhanVien nhanVien, Stage primaryStage, Stage dialogStage) {
        loadFXML();
        this.nhanVien = nhanVien;
        lbMaNV.setText(nhanVien.getMaNhanVien());
//        lbGioBatDau.setText(NumberFormat.dateTimeFormat(LocalDateTime.now()));
        lbGioBatDau.textProperty().bind(timeProperty);
        
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Cập nhật thời gian hiện tại
            	Platform.runLater(() -> {
            		String currentTime = NumberFormat.dateTimeFormat(LocalDateTime.now());
                    timeProperty.set(currentTime);
                });
            }
        }, 0, 1000); // Cập nhật mỗi giây
        
        dialogStage.setOnCloseRequest(event -> {
        	primaryStage.close();
        });

        btnThemPhieuKiemTien.setOnMouseClicked(e -> {
            try {
            
                showDialog(dialogStage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        
        btnDangXuat.setOnAction(event -> {
        	try {
				moTrangDangNhap();
				primaryStage.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
       
        
        btnMoCa.setOnAction(event -> {
        	if (lbTienMatDauCa.getText().equals("Chưa có")) {
        		ShowDialog.showDialog(root, "Lỗi", "Chưa có thông tin về phiếu kiểm tiền", "OK");
        		return;
        	}
        	caLamViec = new CaLamViec(nhanVien, LocalDateTime.now(), null);
        	QuanLyCa_business.taoCaLamViec(caLamViec);
//        	QuanLyPhong_business.updateTrangThaiPhongTheoNgay();
        	dialogStage.close();
        });
    }
    
    public CaLamViec getCaLamViec() {
    	return caLamViec;
    }
   

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/TaoCaLamViec_view.fxml")));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load");
        }
    }

    private void showDialog(Stage primaryStage) throws IOException {

        PhieuKiemTien_Controller controller = new PhieuKiemTien_Controller(nhanVien);
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Phiếu kiểm tiền");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(primaryStage);

        Scene scene = new Scene(controller.getRoot());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        lbTienMatDauCa.setText(controller.getTongTien());
    }

    public StackPane getRoot() {
        return root;
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
