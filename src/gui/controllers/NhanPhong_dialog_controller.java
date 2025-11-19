package gui.controllers;

import bus.QuanLiKhachHang_Business;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class NhanPhong_dialog_controller implements Initializable {

    @FXML
    private JFXButton btn_huy;

    @FXML
    private JFXButton btn_them;

    @FXML
    private JFXButton btn_tim;

    @FXML
    private JFXButton btn_xacNhan;

    @FXML
    private FlowPane fp_display;

    @FXML
    private Label lb_maPhong;

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField tf_cCCD;

    @FXML
    private TextField tf_sdt;

    @FXML
    private TextField tf_tenKH;

    @FXML
    private TextField tf_tim;

    private AnchorPane phongDatView;
    private Set<KhachHang> set_KhachHang = new HashSet<>();
    private Set<KhachHang> set_KhachHang_current = new HashSet<>();
    private NhanPhong_controller nhanPhong_controller;
    private PhieuDatPhong phieuDatPhong;
    private PhongDat phongDat;
    private String maHoaDon;
    private KhachHang khachHang;

    public void setPhongDatView(AnchorPane phongDatView) {
        this.phongDatView = phongDatView;
    }

    public void setPhongDat(PhongDat phongDat) {
        this.phongDat = phongDat;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        this.phieuDatPhong = phieuDatPhong;
    }

    public void setNhanPhong_controller(NhanPhong_controller nhanPhong_controller) {
        this.nhanPhong_controller = nhanPhong_controller;
    }

    public void setSet_KhachHang(Set<KhachHang> set_KhachHang_current) {
        this.set_KhachHang = set_KhachHang_current;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            lb_maPhong.setText(phongDat.getPhong().getMaPhong());

            btn_tim.setOnAction(event -> {
                khachHang = QuanLiKhachHang_Business.timKhachHangTheoSDTorCCCD(tf_tim.getText());

                if (khachHang != null) {
                    tf_tenKH.setText(khachHang.getTenKhachHang());
                    tf_cCCD.setText(khachHang.getCCCD());
                    tf_sdt.setText(khachHang.getSoDienThoai());
                }
                else {
                    tf_tenKH.setText("");
                    tf_cCCD.setText("");
                    tf_sdt.setText("");
                    tf_tim.requestFocus();
                    showDialog(stackPane, "Lỗi", "Không tìm thấy khách hàng", "OK");
                }
            });

            btn_them.setOnAction(event -> {
                if(khachHang == null) {
                    showDialog(stackPane, "Lỗi", "Hãy tìm thông tin khách hàng trước!", "OK");
                    tf_tim.setText("");
                    tf_tenKH.setText("");
                    tf_cCCD.setText("");
                    tf_sdt.setText("");
                    tf_tim.requestFocus();
                    return;
                }
                if (set_KhachHang.contains(khachHang)) {
                    showDialog(stackPane, "Lỗi", "Khách hàng đã được thêm trước đó vui lòng kiểm tra", "OK");
                    tf_tim.setText("");
                    tf_tenKH.setText("");
                    tf_cCCD.setText("");
                    tf_sdt.setText("");
                    tf_tim.requestFocus();
                    return;
                }
                if (set_KhachHang_current.size() == phongDat.getSoNguoi()) {
                    showDialog(stackPane, "Lỗi", "Số lượng khách hàng đã đủ", "OK");
                    tf_tim.setText("");
                    tf_tenKH.setText("");
                    tf_cCCD.setText("");
                    tf_sdt.setText("");
                    tf_tim.requestFocus();
                    return;
                }

                set_KhachHang.add(khachHang);
                set_KhachHang_current.add(khachHang);

                addKhachHangView(khachHang);

                tf_tim.setText("");
                tf_tenKH.setText("");
                tf_cCCD.setText("");
                tf_sdt.setText("");
                tf_tim.requestFocus();
                khachHang = null;
            });

            btn_huy.setOnAction(event -> {
                Stage currentStage = (Stage) btn_huy.getScene().getWindow();
                currentStage.close();
            });

            btn_xacNhan.setOnAction(event -> {
                if (set_KhachHang_current.size() != phongDat.getSoNguoi()) {
                    showDialog(stackPane, "Lỗi", "Số lượng khách hàng chưa đủ hãy thêm khách hàng", "OK");
                    return;
                }

                ArrayList<ChiTietPhongThue> dsChiTietPhongThue = new ArrayList<>();

                HoaDon hoaDon = new HoaDon(maHoaDon);
                PhongThue phongThue = new PhongThue(hoaDon, phongDat.getPhong(), phongDat.getSoNguoi(), LocalDateTime.now(), phieuDatPhong.getNgayTraPhong().atStartOfDay(), phongDat.getDonGia());

                for (KhachHang khachHang : set_KhachHang_current) {
                    ChiTietPhongThue chiTietPhongThue = new ChiTietPhongThue(phongThue, phongDat.getPhong(), khachHang);
                    dsChiTietPhongThue.add(chiTietPhongThue);
                }

                phongThue.setListChiTietPhongThue(dsChiTietPhongThue);
                nhanPhong_controller.setSetKhachHang(set_KhachHang);
                nhanPhong_controller.themPhongThue(phongThue);
                nhanPhong_controller.themPhongDatDuocChon(phongDat);
                nhanPhong_controller.tinhTien();
                nhanPhong_controller.getFp_displayPhongThue().getChildren().add(createPhongThue(phongThue));

                phongDatView.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 10;");
                Stage currentStage = (Stage) btn_huy.getScene().getWindow();
                currentStage.close();
            });
        });
    }

    public void addKhachHangView(KhachHang khachHang) {
        AnchorPane an_KhachHang = new AnchorPane();
        an_KhachHang.setPrefSize(555, 60);
        an_KhachHang.setStyle("-fx-background-color:  #505050; -fx-background-radius: 10px;");
        Button btn_xoa = new Button("");
        FontAwesomeIconView iconView = new FontAwesomeIconView();
        iconView.setGlyphName("TRASH_ALT");
        iconView.setSize("25");
        iconView.setFill(javafx.scene.paint.Color.WHITE);
        btn_xoa.setGraphic(iconView);
        btn_xoa.getStylesheets().add(getClass().getResource("/css/KhungGiaoDien.css").toExternalForm());
        btn_xoa.getStyleClass().add("btn");
        iconView.getStyleClass().add("icon_white");
        HBox hb_info = new HBox();
        hb_info.setPadding(new javafx.geometry.Insets(20, 0, 0, 0));
        hb_info.setSpacing(20);
        Label lb_tenKH = new Label(khachHang.getTenKhachHang());
        lb_tenKH.setStyle("-fx-text-fill: white;");
        lb_tenKH.setFont(new javafx.scene.text.Font("Tahoma", 15));
        Label lb_sdt = new Label("SDT: " + khachHang.getSoDienThoai());
        lb_sdt.setStyle("-fx-text-fill: white;");
        lb_sdt.setFont(new javafx.scene.text.Font("Tahoma", 15));
        Label lb_cccd = new Label("CCCD: " + khachHang.getCCCD());
        lb_cccd.setStyle("-fx-text-fill: white;");
        lb_cccd.setFont(new javafx.scene.text.Font("Tahoma", 15));
        hb_info.getChildren().addAll(lb_tenKH, lb_sdt, lb_cccd);
        AnchorPane.setTopAnchor(btn_xoa, 13.0);
        AnchorPane.setRightAnchor(btn_xoa, 15.0);
        AnchorPane.setLeftAnchor(hb_info, 20.0);
        AnchorPane.setTopAnchor(hb_info, 0.0);
        AnchorPane.setBottomAnchor(hb_info, 0.0);
        AnchorPane.setRightAnchor(hb_info, 0.0);
        an_KhachHang.getChildren().addAll(hb_info, btn_xoa);
        fp_display.getChildren().add(an_KhachHang);

        btn_xoa.setOnAction(event -> {
            set_KhachHang.remove(khachHang);
            set_KhachHang_current.remove(khachHang);
            fp_display.getChildren().remove(an_KhachHang);
        });
    }

    public AnchorPane createPhongThue(PhongThue phongThue){
        AnchorPane phongThue_view = new AnchorPane();
        phongThue_view.setPrefWidth(290);
        phongThue_view.setStyle("-fx-background-color:  #2f6f4f; -fx-background-radius: 10px;");

        Label lb_maPhong = new Label(phongThue.getPhong().getMaPhong());
        lb_maPhong.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD,24));
        lb_maPhong.setStyle("-fx-text-fill: white;");

        Button btn_xoa = new Button("");
        FontAwesomeIconView iconView = new FontAwesomeIconView();
        iconView.setGlyphName("TRASH_ALT");
        iconView.setSize("25");
        iconView.setFill(javafx.scene.paint.Color.WHITE);
        btn_xoa.setGraphic(iconView);
        btn_xoa.getStylesheets().add(getClass().getResource("/css/KhungGiaoDien.css").toExternalForm());
        btn_xoa.getStyleClass().add("btn");
        iconView.getStyleClass().add("icon_white");

        VBox vb_info = new VBox();
        vb_info.setSpacing(5);
        for (ChiTietPhongThue chiTietPhongThue : phongThue.getListChiTietPhongThue()){
            VBox vb_KhachHang = new VBox();
            Label lb_tenKH = new Label(chiTietPhongThue.getKhachHang().getTenKhachHang());
            lb_tenKH.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD, 14));
            lb_tenKH.setStyle("-fx-text-fill: yellow;");

            Label lb_sdt = new Label("SDT: " + chiTietPhongThue.getKhachHang().getSoDienThoai());
            lb_sdt.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD, 14));
            lb_sdt.setStyle("-fx-text-fill: white;");

            Label lb_cccd = new Label("CCCD: " + chiTietPhongThue.getKhachHang().getCCCD());
            lb_cccd.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.BOLD, 14));
            lb_cccd.setStyle("-fx-text-fill: white;");

            vb_KhachHang.getChildren().addAll(lb_tenKH, lb_sdt, lb_cccd);
            vb_KhachHang.setSpacing(5);

            vb_info.getChildren().add(vb_KhachHang);
        }

        phongThue_view.getChildren().addAll(lb_maPhong, vb_info, btn_xoa);

        AnchorPane.setTopAnchor(lb_maPhong, 10.0);
        AnchorPane.setLeftAnchor(lb_maPhong, 15.0);

        AnchorPane.setTopAnchor(btn_xoa, 10.0);
        AnchorPane.setRightAnchor(btn_xoa, 15.0);

        AnchorPane.setTopAnchor(vb_info, 45.0);
        AnchorPane.setLeftAnchor(vb_info, 30.0);
        AnchorPane.setRightAnchor(vb_info, 10.0);
        AnchorPane.setBottomAnchor(vb_info, 10.0);

        btn_xoa.setOnAction(event -> {
            for (ChiTietPhongThue chiTietPhongThue : phongThue.getListChiTietPhongThue()){
                nhanPhong_controller.getSetKhachHang().remove(chiTietPhongThue.getKhachHang());
            }

            nhanPhong_controller.getListPhongThue().remove(phongThue);
            nhanPhong_controller.getListPhongDat_isSelected().remove(phongDat);
            nhanPhong_controller.tinhTien();

            phongDatView.setStyle("-fx-background-color: #e6e6e6; -fx-background-radius: 10;");

            nhanPhong_controller.getFp_displayPhongThue().getChildren().remove(phongThue_view);
        });

        return phongThue_view;
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
