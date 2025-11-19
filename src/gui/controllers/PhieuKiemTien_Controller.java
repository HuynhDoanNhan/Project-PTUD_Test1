package gui.controllers;

import bus.QuanLyNhanVien_Business;
import bus.ThongKeVaBaoCao_Business;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.*;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.NumberFormat;
import utils.ShowDialog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class PhieuKiemTien_Controller {
    @FXML
    private JFXButton btnHoanThanh;

    @FXML
    private JFXButton btnHuy;

    @FXML
    private FontAwesomeIconView btnThemNV2;

    @FXML
    private TextField tfTimNV2;

    @FXML
    private FontAwesomeIconView iconInfo;

    @FXML
    private Label maNV1;

    @FXML
    private Label maNV2;

    @FXML
    private Label ngayLap;

    @FXML
    private Label tenNV1;

    @FXML
    private Label tenNV2;

    @FXML
    private Label tongCong;

    @FXML
    private StackPane root;

    @FXML
    private VBox panelChuaMenhGia;

    private NhanVien nhanVien2;

    private final ObservableList<MenhGiaTien_Controller> listMenhGia = FXCollections.observableArrayList();
    private PhieuKiemTien phieuKiemTien;

    public PhieuKiemTien_Controller(NhanVien nhanVien1) {
        loadFXML();
        khoiTaoToolTip();
        ngayLap.setText(NumberFormat.dateTimeFormat(LocalDateTime.now()));
        tenNV1.setText(nhanVien1.getHoTen());
        maNV1.setText(nhanVien1.getMaNhanVien());
        khoiTaoListMenhGia();
        tongCong.setText("0 VND");

        tongCong.textProperty().bind(Bindings.createStringBinding(() ->
                        NumberFormat.currencyFormat(listMenhGia.stream().mapToDouble(MenhGiaTien_Controller::getTongTien).sum()),
                listMenhGia.stream().map(MenhGiaTien_Controller::getTongTienProperty).toArray(Observable[]::new)));

        btnThemNV2.setOnMouseClicked(e -> {
            String maNV = tfTimNV2.getText();
            if (maNV.isEmpty() || !maNV.contains("NV")) {
                ShowDialog.showDialog(root, "Lỗi", "Mã nhân viên không hợp lệ", "OK");
                return;
            }
            nhanVien2 = QuanLyNhanVien_Business.timNhanVien(maNV);
            if (nhanVien2 == null) {
                ShowDialog.showDialog(root, "Lỗi", "Không tìm thấy nhân viên", "OK");
            } else if (nhanVien1.equals(nhanVien2)) {
                ShowDialog.showDialog(root, "Lỗi", "Nhân viên kiểm tiền không thể trùng với nhân viên lập phiếu", "OK");
            }
            else {
                tenNV2.setText(nhanVien2.getHoTen());
                maNV2.setText(nhanVien2.getMaNhanVien());
            }
        });

        btnHuy.setOnMouseClicked(e -> {
        	for (MenhGiaTien_Controller controller: listMenhGia) {
        		controller.resetSoLuong();
        	}
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });

        btnHoanThanh.setOnMouseClicked(e -> {
            if (nhanVien2 == null) {
                ShowDialog.showDialog(root, "Lỗi", "Chưa chọn nhân viên kiểm tiền", "OK");
                return;
            }

            phieuKiemTien = new PhieuKiemTien(LocalDateTime.now(), nhanVien1, nhanVien2);
            ArrayList<ChiTietKiemTien> listChiTietKiemTien = new ArrayList<>();
            for (MenhGiaTien_Controller menhGia : listMenhGia) {
                try {
                    ChiTietKiemTien chiTietKiemTien = menhGia.getChiTietKiemTien(phieuKiemTien);
                    if (chiTietKiemTien != null) {
                        listChiTietKiemTien.add(chiTietKiemTien);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            phieuKiemTien.setListChiTietKiemTien(listChiTietKiemTien);

            ThongKeVaBaoCao_Business.taoPhieuKiemTien(phieuKiemTien);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getResource("/view/PhieuKiemTien_view.fxml")));
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load PhieuKiemTien_view.fxml");
        }
    }

    public void khoiTaoListMenhGia() {
        listMenhGia.add(new MenhGiaTien_Controller(500000, 1));
        listMenhGia.add(new MenhGiaTien_Controller(200000, 2));
        listMenhGia.add(new MenhGiaTien_Controller(100000, 3));
        listMenhGia.add(new MenhGiaTien_Controller(50000, 4));
        listMenhGia.add(new MenhGiaTien_Controller(20000, 5));
        listMenhGia.add(new MenhGiaTien_Controller(10000, 6));
        listMenhGia.add(new MenhGiaTien_Controller(5000, 7));
        listMenhGia.add(new MenhGiaTien_Controller(2000, 8));
        listMenhGia.add(new MenhGiaTien_Controller(1000, 9));
        listMenhGia.add(new MenhGiaTien_Controller(500, 10));

        for (MenhGiaTien_Controller menhGia : listMenhGia) {
            panelChuaMenhGia.getChildren().add(menhGia.getRoot());
        }
    }

    public void khoiTaoToolTip() {
        Tooltip tooltip = new Tooltip("Số lượng tờ tiền ứng với mệnh giá tương ứng");
        // Hoặc nếu bạn muốn custom vị trí tooltip
        tooltip.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial'; -fx-text-fill: black; -fx-background-color: white;");
        tooltip.setAutoHide(true); //ẩn tooltip sau 1 khoảng thời gian
        tooltip.setHideDelay(Duration.seconds(0)); //thời gian ẩn tooltip
        tooltip.setShowDelay(Duration.seconds(0)); //thời gian hiển thị tooltip
        tooltip.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT); //vị trí hiển thị tooltip
        Tooltip.install(iconInfo, tooltip);
    }

    public StackPane getRoot() {
        return root;
    }

    public String getTongTien() {
        String tongTien = tongCong.getText();
        if (Objects.equals(tongTien, "0 VND")) {
            tongTien = "Chưa có";
        }
        return tongTien;
    }
    
    public PhieuKiemTien getPhieuKiemTien() {
        return phieuKiemTien;
    }
}
