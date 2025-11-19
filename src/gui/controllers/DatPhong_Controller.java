package gui.controllers;

import bus.KhachHang_Business;
import bus.QuanLyPhong_Bussiness;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import dao.PhieuDatPhong_DAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Consumer;

public class DatPhong_Controller implements Initializable {
	@FXML
	public AnchorPane root;
	@FXML
    public StackPane stackPane;
    @FXML
    public DatePicker DP_ngayNhanPhong;
    @FXML
    private JFXButton after_btn;

    @FXML
    private FlowPane bangChonPhong;

    @FXML
    private VBox bangThuePhong;

    @FXML
    private JFXButton before_btn;

    @FXML
    private JFXButton lamMoi_btn;

    @FXML
    private TextField lb_CCCD;

    @FXML
    private TextField lb_SDT;

    @FXML
    private Label lb_soLuongPhong;

    @FXML
    private Label lb_soPhongThue;

    @FXML
    private TextField lb_tenKH;

    @FXML
    private TextField tf_cccd;

    @FXML
    private TextField tf_sdt;

    @FXML
    private TextField tf_tang;

    @FXML
    private JFXButton thuePhong_btn;

    @FXML
    private JFXButton datPhong_btn;

    @FXML
    private JFXButton tim_btn;

    @FXML
    private JFXButton loc_btn;

    @FXML
    private DatePicker DP_ngayTraPhong;
    
    @FXML
    private TextField lb_ngaySinh;
    
    @FXML
    private TextField tfSoLuongKhach;
    
    private DatPhong_Controller datPhong_Controller;

	private QuanLyPhong_Bussiness quanLyPhong_bussiness = new QuanLyPhong_Bussiness();
    private KhachHang_Business quanLyKhachHang_business = new KhachHang_Business();
    private List<Phong> danhSachPhong = new ArrayList<>();
    private KhachHang khachHang = null;
    private String cssFile = Objects.requireNonNull(getClass().getResource("/css/DatPhong.css")).toExternalForm();
    private List<Phong> dsPhongThue = new ArrayList<>();
    private int tangToiDa = quanLyPhong_bussiness.getTangToiDaDat();
    private NhanVien nhanVien = new NhanVien();

    private PhieuDatPhong_DAO datPhieuDat = new PhieuDatPhong_DAO();
    private CaLamViec caLamViec;
    
    private ObservableList<Integer> soLuongKhachList = FXCollections.observableArrayList();

    public PhongDat_controller phongDatController;

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

	public List<Phong> getDsPhongThue() {
		return dsPhongThue;
	}

    public PhongDat_controller getPhongDatController() {
        return phongDatController;
    }
    public void setCaLamViec(CaLamViec caLamViec) {
        this.caLamViec = caLamViec;
    }

	@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DP_ngayNhanPhong.setValue(LocalDate.now().plusDays(1));
        DP_ngayTraPhong.setValue(DP_ngayNhanPhong.getValue().plusDays(1));

        DP_ngayNhanPhong.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                if (date.isBefore(today)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #D3D3D3;");
                }
                if (date.equals(today)) {
                    setStyle("-fx-background-color: #FF6347;");
                }
            }
        });

        DP_ngayTraPhong.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                LocalDate ngayNhanPhong = DP_ngayNhanPhong.getValue();

                if (ngayNhanPhong != null && date.isBefore(ngayNhanPhong)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #D3D3D3;");
                }

                if (date.equals(ngayNhanPhong)) {
                    setStyle("-fx-background-color: #FF6347;");
                }
            }
        });


        Platform.runLater(() -> {
            before_btn.setVisible(false);
            tf_tang.setText("1");
            updateDSPhong(tf_tang.getText());
            if (danhSachPhong.size() <= 0) {
                lb_soLuongPhong.setVisible(true);
            } else {
                hienThiPhongTrong(danhSachPhong);
                lb_soLuongPhong.setVisible(false);
            }

            after_btn.setOnAction(actionEvent -> {
                String tang = tf_tang.getText();
                int tangInt = Integer.parseInt(tang);
                tangInt++;
                if (tangInt <= tangToiDa) {
                    updateDSPhong(String.valueOf(tangInt));
                    if (danhSachPhong.size() > 0) {
                        hienThiPhongTrong(danhSachPhong);
                        lb_soLuongPhong.setVisible(false);
                    } else {
                        bangChonPhong.getChildren().clear();
                        lb_soLuongPhong.setVisible(true);
                    }
                    tf_tang.setText(String.valueOf(tangInt));
                    before_btn.setVisible(true);
                }
            });

            before_btn.setOnAction(actionEvent -> {
                String tang = tf_tang.getText();
                int tangInt = Integer.parseInt(tang);
                tangInt--;
                tf_tang.setText(String.valueOf(tangInt));
                updateDSPhong(tf_tang.getText());
                if (danhSachPhong.size() > 0) {
                    hienThiPhongTrong(danhSachPhong);
                    lb_soLuongPhong.setVisible(false);
                } else {
                    bangChonPhong.getChildren().clear();
                    lb_soLuongPhong.setVisible(true);
                }
                if (tangInt == 1) {
                    before_btn.setVisible(false);
                }
            });

            tf_tang.setOnAction(event -> {
                String tang = tf_tang.getText();
                int tangInt = Integer.parseInt(tang);
                if (tangInt > tangToiDa) {
                    tangInt = tangToiDa;
                } else if (tangInt < 1) {
                    tangInt = 1;
                }

                tf_tang.setText(String.valueOf(tangInt));
                updateDSPhong(tf_tang.getText());
                if (danhSachPhong.size() > 0) {
                    hienThiPhongTrong(danhSachPhong);
                    lb_soLuongPhong.setVisible(false);
                } else {
                    bangChonPhong.getChildren().clear();
                    lb_soLuongPhong.setVisible(true);
                }
                before_btn.setVisible((tangInt != 1));
            });

            lamMoi_btn.setOnAction(actionEvent -> {
                if (dsPhongThue.size() > 0) {
                    dsPhongThue.clear();
                    updateDSPhong(tf_tang.getText());                 
                    hienThiPhongTrong(danhSachPhong);
                    bangThuePhong.getChildren().clear();
                    lb_soPhongThue.setVisible(true);
                }
            });

            tim_btn.setOnAction(actionEvet -> {
                //String tenKH = tf_ten.getText();
                String cccd = tf_cccd.getText();
                String sdt = tf_sdt.getText();
                
                //khachHang = quanLyKhachHang_business.timKhachHang(sdt);
                khachHang = quanLyKhachHang_business.timKhachHangNhiu("", sdt, cccd);

                if (khachHang != null) {
                    lb_tenKH.setText(khachHang.getTenKhachHang());
                    lb_CCCD.setText(khachHang.getCCCD());
                    lb_SDT.setText(khachHang.getSoDienThoai());
                    lb_ngaySinh.setText(khachHang.getNgaySinh().toString());

                } else {
                    tf_cccd.setText("");
                    tf_sdt.setText("");

                    lb_tenKH.setText("");
                    lb_CCCD.setText("");
                    lb_SDT.setText("");
                    lb_ngaySinh.setText("");
                }
            });
            
            datPhong_btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (dsPhongThue.size() <= 0) {
                    showDialog(stackPane, "Chưa chọn phòng", "Vui lòng chọn phòng trước khi thực hiện đặt phòng", "OK");
                    return;
                }
                if(tf_cccd.getText().trim().isEmpty() && tf_sdt.getText().trim().isEmpty()) {
                    showDialog(stackPane, "Chưa có thông tin khách hàng", "Vui lòng kiểm tra lại thông tin khách hàng", "OK");
                    return;
                }
                if (DP_ngayNhanPhong.getValue() == null && DP_ngayTraPhong.getValue() == null) {
                    showDialog(stackPane, "Chưa ngày nhận phòng và ngày trả phòng", "Vui lòng kiểm tra lại ngày nhận và ngày trả", "OK");
                    return;
                }
                if (DP_ngayTraPhong.getValue() == null && DP_ngayTraPhong.getValue() == null) {
                    showDialog(stackPane, "Chưa ngày nhận phòng và ngày trả phòng", "Vui lòng kiểm tra lại ngày nhận và ngày trả", "OK");
                    return;
                }
                if (khachHang == null) {
                    showDialog(stackPane, "Chưa có thông tin khách hàng", "Vui lòng kiểm tra lại thông tin khách hàng", "OK");
                    return;
                }
                int tuoi = Period.between(khachHang.getNgaySinh(), LocalDate.now()).getYears();
                System.out.println(tuoi);
                if (tuoi < 18) {
                	showDialog(stackPane, "Lỗi", "Khách hàng chưa là người lớn không thể đặt phòng", "OK");
                    return;
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DatPhong_dialog_view.fxml"));
                    Parent root = loader.load();
                    DatPhong_dialog_Controller controller = loader.getController();
                    controller.setDatPhong_Controller(this);
                    String ngayNhanPhong = DP_ngayNhanPhong.getValue().toString();
                    String ngayTraPhong = DP_ngayTraPhong.getValue().toString();
                    controller.setNgayNhanTraPhong(ngayNhanPhong, ngayTraPhong);
                    controller.setNhanVien(nhanVien);
                    controller.setKhachHang(khachHang);
                    controller.setDsPhongThue(dsPhongThue);
                    controller.setCaLamViec(caLamViec);

                    Stage thuePhong_dialog_stage = new Stage();
                    thuePhong_dialog_stage.setScene(new Scene(root));
                    thuePhong_dialog_stage.centerOnScreen();
                    thuePhong_dialog_stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            //Kiểm tra phòng đó đã có ai đặt trước chưa
            DP_ngayNhanPhong.setOnAction(event -> {
                DP_ngayTraPhong.setValue(null);
            });

            DP_ngayTraPhong.setOnAction(event -> {
                if (DP_ngayNhanPhong.getValue() != null) {
                    checkNgayNhanTra();
                }
            });

        });
    }

    public void updateDSPhong(String tang) {
        danhSachPhong.clear();
        LocalDate ngaynhan = DP_ngayNhanPhong.getValue() != null ? DP_ngayNhanPhong.getValue() : LocalDate.now();
        LocalDate ngaytra = DP_ngayTraPhong.getValue() != null ? DP_ngayTraPhong.getValue() : ngaynhan;
        danhSachPhong = quanLyPhong_bussiness.getPhongTrongTheoNgay(ngaynhan, ngaytra, tang);
    }

    public void hienThiPhongTrong(List<Phong> danhSachPhong) {
        bangChonPhong.getChildren().clear();
        for (Phong phong : danhSachPhong) {
        	boolean isSelected = false; // Xác định trạng thái chọn ban đầu
            AnchorPane phongPane = taoPhong(phong);
            bangChonPhong.getChildren().add(phongPane);   	
        }
    }
    
    public AnchorPane taoPhong(Phong phong) {
    	boolean isSelected = false;
    	Stage stage = new Stage();
    	PhongDat_controller phongDatController = new PhongDat_controller(phong,isSelected);
        phongDatController.setThuePhong_controller(this); // Liên kết DatPhong_Controller nếu cần
        return phongDatController.getRoot();
    }

    public void capNhatBangThuePhong(List<Phong> dsPhongThue) {
        bangThuePhong.getChildren().clear();
        if (dsPhongThue.size() > 0) {
            for (Phong phongThue : dsPhongThue) {
                bangThuePhong.getChildren().add(taoPhongThue(phongThue));
            }
            lb_soPhongThue.setVisible(false);
        } else {
            lb_soPhongThue.setVisible(true);
        }
    }

    public HBox taoPhongThue(Phong phongThue) {
        HBox HB_phongThue = new HBox();
        HB_phongThue.setAlignment(javafx.geometry.Pos.CENTER);
        HB_phongThue.setSpacing(40);

        // Đặt gradient làm màu nền cho HBox
        HB_phongThue.setStyle("-fx-background-color: linear-gradient(to right, #FFFFE0, #FFFFFF); -fx-background-radius: 10;");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        HB_phongThue.setEffect(dropShadow);

        Label maPhong_lb = new Label("Phòng " + phongThue.getMaPhong());
        maPhong_lb.setText(phongThue.getMaPhong());
        maPhong_lb.setFont(new javafx.scene.text.Font("Tahoma Bold", 18));

        double giaCoBan = 0;
        switch (phongThue.getLoaiPhong().getMaLoaiPhong().toString()) {
        case "STD":
        	giaCoBan = 200000;
            break;
        case "SUP":
        	giaCoBan = 500000;
        case "DLX":
        	giaCoBan = 1000000;
            break;
        case "SUT":
        	giaCoBan = 2000000;
            break;
        }
		
        double heSoGia = 1;
        switch (phongThue.getLoaiGiuongSuDung().getMaLoaiGiuong().toString()) {
        case "DOUBLE":
        	heSoGia = 1.2;
            break;
        case "KING":
        	heSoGia = 1.2;
        case "QUEEN":
        	heSoGia = 1;
            break;
        case "SINGLE":
        	heSoGia = 1;
            break;
        }
        double giaPhong = giaCoBan * heSoGia;  
        
        Label giaPhong_lb = new Label("Giá: " + formatCurrency(giaPhong));
        giaPhong_lb.setFont(new javafx.scene.text.Font("Tahoma Bold", 18));
        giaPhong_lb.setTextFill(Color.web("orange"));

        Button btn_xoa = new Button();
        btn_xoa.setFont(new javafx.scene.text.Font("Tahoma Bold", 20));
        btn_xoa.setStyle("-fx-background-color: none");

        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setIcon(FontAwesomeIcon.TRASH_ALT);
        btn_xoa.setGraphic(icon);

        btn_xoa.setOnAction(event -> {
            dsPhongThue.remove(phongThue);
            capNhatBangThuePhong(dsPhongThue);
            for (Node node : bangChonPhong.getChildren()) {
                if (node instanceof AnchorPane) {
                    AnchorPane phong = (AnchorPane) node;
                    Node labelNode = phong.getChildren().get(1);
                    if (labelNode instanceof Label) {
                        Label lbMaPhong = (Label) labelNode;
                        if (lbMaPhong.getText().equals(phongThue.getMaPhong())) {
                            Node imageNode = phong.getChildren().get(2);
                            if (imageNode instanceof ImageView) {
                                ImageView imageView = (ImageView) imageNode;
                                imageView.setVisible(false);
                            }
                        }
                    }
                }
            }

            hienThiPhongTrong(danhSachPhong);
        });
        HB_phongThue.getChildren().addAll(maPhong_lb, giaPhong_lb, btn_xoa);
        return HB_phongThue;
    }

    public static String formatCurrency(double amount) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        String formattedNumber = numberFormat.format(amount);

        return formattedNumber + " VND";
    }

    //hàm kiểm tra ngày nhận phòng
    public  void checkNgayNhanTra(){
        if(DP_ngayNhanPhong.getValue() != null && DP_ngayTraPhong.getValue() != null){
            LocalDate ngayNhan = DP_ngayNhanPhong.getValue();
            LocalDate ngayTra = DP_ngayTraPhong.getValue();

            if (ngayNhan.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(null,"Ngày nhận phòng phải từ hôm nay trở đi.");
                DP_ngayNhanPhong.setValue(null);
                return;
            }
            if(!ngayNhan.isBefore(ngayTra.minusDays(-1))){
                JOptionPane.showMessageDialog(null, "Ngày nhận phải trước ngày trả phòng.");
                DP_ngayNhanPhong.setValue(null);
                return;
            }

            danhSachPhong = quanLyPhong_bussiness.getPhongTrongTheoNgay(ngayNhan, ngayTra, tf_tang.getText());

            dsPhongThue.clear();
            capNhatBangThuePhong(dsPhongThue);

            if(danhSachPhong.size() > 0){
                hienThiPhongTrong(danhSachPhong);
                lb_soLuongPhong.setVisible(false);
            }else{
                bangChonPhong.getChildren().clear();
                lb_soLuongPhong.setVisible(true);
            }
        }
    }
    
    public void Taodialog(String title, String buttonText, Consumer<Integer> onConfirm) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(title));

        // Giao diện nhập số lượng khách
        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        Label lblPrompt = new Label("Nhập số lượng khách lưu trú:");
        tfSoLuongKhach = new TextField();

        contentBox.getChildren().addAll(lblPrompt, tfSoLuongKhach);
        
        dialogLayout.setBody(contentBox);

        // Nút hành động
        JFXButton confirmButton = new JFXButton(buttonText);
        JFXButton cancelButton = new JFXButton("Hủy");

        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);

        confirmButton.setOnAction(event -> {
            try {
                int soKhach = Integer.parseInt(tfSoLuongKhach.getText());
                if ( soKhach > 0 && soKhach <= 3){
                    onConfirm.accept(soKhach);
                    soLuongKhachList.add(soKhach);
                    dialog.close();
                }else{
                    showDialog(stackPane, "Thông báo", "Số khách thuê phải nhỏ hơn số khách tối đa trong phòng", "OK");
                }
            } catch (NumberFormatException e) {
                showDialog(stackPane, "Thông báo", "Vui lòng nhập số hợp lệ", "OK");
            }
        });

        cancelButton.setOnAction(event -> dialog.close());

        dialogLayout.setActions(confirmButton, cancelButton);
        
        dialog.show();

    }
    
    
    
    public ObservableList<Integer> getSoLuongKhachList() {
		return soLuongKhachList;
	}

	public TextField getTfSoLuongKhach() {
		return tfSoLuongKhach;
	}

	public void callDialog( String title, String content, String buttonText) {
    	showDialog(stackPane, title, content, buttonText);
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

    public void reload(){
        if (dsPhongThue.size() > 0) {
            dsPhongThue.clear();
            updateDSPhong(tf_tang.getText());
            hienThiPhongTrong(danhSachPhong);
            bangThuePhong.getChildren().clear();
            lb_soPhongThue.setVisible(true);
            tf_cccd.setText("");
            tf_sdt.setText("");

            lb_tenKH.setText("");
            lb_CCCD.setText("");
            lb_SDT.setText("");
            lb_ngaySinh.setText("");
        }
    }
}
