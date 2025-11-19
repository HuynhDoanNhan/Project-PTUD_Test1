package gui.controllers;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import dto.CaLamViec;
import dto.NhanVien;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KhungGiaoDien_controller implements Initializable {

    @FXML
    private Button btn_capNhatPhong;

    @FXML
    private Button btn_ketCa;

    @FXML
    private Button btn_capNhatKH;

    @FXML
    private Button btn_capNhatDV;

    @FXML
    private Button btn_capNhatNV;

    @FXML
    private Button btn_thuePhong;

    @FXML
    private Button btn_nhanPhong;

    @FXML
    private Button btn_capNhatPhieuDat;

    @FXML
    private Button btn_thongKe;

    @FXML
    private Button btn_chuyenPhong;

    @FXML
    private Button btn_datPhong;

    @FXML
    private Button btn_themDichVu;

    @FXML
    private Button btn_huyDatPhong;

    @FXML
    private Label lbcapBac;

    @FXML
    private Label lbhoTen;

    @FXML
    private Button btn_about;

    @FXML
    private Button btn_help;

    @FXML
    private Button btn_traPhong;

    @FXML
    private Tooltip tt_about;

    @FXML
    private Tooltip tt_help;

    @FXML
    private AnchorPane an_display;

    @FXML
    private VBox vb_submenuQuanLy;

    private NhanVien nv = new NhanVien();
    private CaLamViec caLamViec;

    public void setNhanVien(NhanVien nv) {
        this.nv = nv;
    }

    public CaLamViec getCaLamViec() {
        return this.caLamViec;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set thời gian mở của tooltip là 300ms
        tt_about.setShowDelay(Duration.millis(300));
        tt_help.setShowDelay(Duration.millis(300));

        Platform.runLater(() -> {
            // set thông tin nhân viên lên giao diện
            lbhoTen.setText(nv.getHoTen());
            lbcapBac.setText(nv.getCapBac().toString().equalsIgnoreCase("NHANVIENLETAN") ? "Nhân viên lễ tân" : "Quản lý");

            // set an_display mặc định là QuanLyPhong_view.fxml
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QuanLyPhong_view.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                an_display.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(nv.getCapBac().toString().equals("NHANVIENLETAN")){
                // show dialog tạo ca làm việc
                showDialogTaoCaLamViec();

                vb_submenuQuanLy.getChildren().remove(btn_capNhatNV);
            }

            // set sự kiện quay về trang chủ
            btn_capNhatPhong.setOnMouseClicked(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QuanLyPhong_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang ThuePhong_view.fxml
            btn_thuePhong.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ThuePhong_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    ThuePhong_Controller thuePhong_controller = fxmlLoader.getController();
                    thuePhong_controller.setNhanVien(nv);
                    thuePhong_controller.setCaLamViec(caLamViec);
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang NhanPhong_view.fxml
            btn_nhanPhong.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NhanPhong_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    NhanPhong_controller nhanPhongController = fxmlLoader.getController();
                    nhanPhongController.setCaLamViec(caLamViec);
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang TrangQuanLyPhieuDat.fxml
            btn_capNhatPhieuDat.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TrangQuanLyPhieuDat_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang ChuyenPhong_view.fxml
            btn_chuyenPhong.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ChuyenPhong_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    ChuyenPhong_Controller chuyenPhong_controller = fxmlLoader.getController();
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang DatPhong_view.fxml
            btn_datPhong.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/DatPhong_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    DatPhong_Controller datPhong_controller = fxmlLoader.getController();
                    datPhong_controller.setNhanVien(nv);
                    datPhong_controller.setCaLamViec(caLamViec);
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang ThemDichVu_view.fxml
            btn_themDichVu.setOnAction(e -> {
                try {
                    Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/view/DatDichVu_view.fxml"));
                    fxmlLoader.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/DatDichVu_CSS.css")).toExternalForm());
                    an_display.getChildren().clear();
                    an_display.getChildren().add(fxmlLoader);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang HuyDatPhong_view.fxml
            btn_huyDatPhong.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HuyDatPhong_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    HuyDatPhong_Controller huyDatPhong_controller = fxmlLoader.getController();
                    huyDatPhong_controller.setKhungGiaoDien_controller(this);
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang TraPhong_view.fxml
            btn_traPhong.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TraPhong_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    TraPhong_Controller traPhongController = fxmlLoader.getController();
                    root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/TraPhong_CSS.css")).toExternalForm());
                    traPhongController.setCaLamViec(caLamViec);
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set an_display sang trang ThongKe_view.fxml
            btn_thongKe.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ThongKe_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set sự kiện mở tổng quan ứng dụng
            btn_about.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/About_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.getIcons().add(new Image(getClass().getResource("/image/Remove-bg.ai_1732089798306.png").toExternalForm()));
                    stage.centerOnScreen();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set sự kiện chuyển trang QuanLyNhanVien
            btn_capNhatNV.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NhanVien_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set sự kiện chuyển trang QuanLyDichVu
            btn_capNhatDV.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QuanLiDichVu_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set sự kiện chuyển trang QuanLyKhachHang
            btn_capNhatKH.setOnAction(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QuanLiKhachHang_view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    an_display.getChildren().clear();
                    an_display.getChildren().add(root);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            // set sự kiện chuyển trang KetCa
            btn_ketCa.setOnAction(e -> {
                PhieuKetToan_Controller controller = new PhieuKetToan_Controller(caLamViec);
                an_display.getChildren().clear();
                an_display.getChildren().add(controller.getRoot());
            });

            // set sự kiện mở web của btn_help
            btn_help.setOnAction(e -> {
                InputStream filePDF = getClass().getResourceAsStream("/7_ApplicationDevelopment_UserManual.pdf");

                if (filePDF != null) {
                    try {
                        // Tạo một tệp tạm thời để lưu nội dung PDF
                        File tempFile = File.createTempFile("UserManual", ".pdf");
                        tempFile.deleteOnExit(); // Tệp sẽ tự động xóa khi chương trình kết thúc

                        // Ghi nội dung từ InputStream vào tệp tạm thời
                        try (FileOutputStream out = new FileOutputStream(tempFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = filePDF.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                        }

                        // Mở tệp PDF bằng ứng dụng mặc định
                        Desktop.getDesktop().open(tempFile);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    System.err.println("Không tìm thấy tệp /7_ApplicationDevelopment_UserManual.pdf");
                }

            });
        });
    }

    public void showDialogTaoCaLamViec() {

        Stage primaryStage = (Stage) an_display.getScene().getWindow();


        // Tạo Stage mới để hiển thị AnchorPane
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Tạo ca làm việc");
        dialogStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ chính

        TaoCaLamViec_Controller controller = new TaoCaLamViec_Controller(nv, primaryStage, dialogStage);
        // Tạo Scene với kích thước cố định
        Scene scene = new Scene(controller.getRoot()); // Kích thước giao diện
        dialogStage.setScene(scene);
        dialogStage.getIcons().add(new Image(getClass().getResource("/image/Remove-bg.ai_1732089798306.png").toExternalForm()));

        // Hiển thị cửa sổ
        dialogStage.showAndWait();
        caLamViec = controller.getCaLamViec();
    }
}
