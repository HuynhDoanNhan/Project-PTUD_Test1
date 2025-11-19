package gui.controllers;

import bus.QuanLyDichVu_Business;
import bus.QuanLyHoaDon_Business;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.*;
import dto.enum_class.LoaiDichVu;
import dto.enum_class.TrangThaiDichVu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatDichVu_Controller {

    @FXML
    private JFXButton fillterDVAll;

    @FXML
    private JFXButton fillterDVSale;

    @FXML
    private JFXButton fillterDVHire;

    @FXML
    private Label lbThongTin;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton searchBtn;

    @FXML
    private TextField searchDVBar;

    @FXML
    private TextField searchKHBar;
    @FXML
    private FontAwesomeIconView leftSwapTab;
    @FXML
    private FontAwesomeIconView rightSwapTab;
    @FXML
    private TextField lbSwapTab;

    @FXML
    private FlowPane danhSachDV;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private AnchorPane rightPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label lbTongTien;

    private HBox box_btnPhong = new HBox();
    private VBox danhSachDVDaChon = new VBox();

    private ScrollPane scrBox_btnPhong = new ScrollPane();
    private ScrollPane scrollPane = new ScrollPane();
    private ArrayList<DichVu> listDichVu = new ArrayList<>();
    private ArrayList<DichVu> listDichVuBan = new ArrayList<>();
    private ArrayList<DichVu> listDichVuThue = new ArrayList<>();
    private ArrayList<DichVu> listDichVuTimKiem = new ArrayList<>();
    private ArrayList<ChiTietDichVu> listDichVuDaChon;
    private ArrayList<ButtonPhongDatDichVuView_Controller> listBtnPhong = new ArrayList<>();
    private ChiTietPhongThue chiTietPhongThue = null;
    public void initialize() {
        // Lấy dữ liệu dịch vụ
        listDichVu = QuanLyDichVu_Business.getDichVuList();

        // Lọc dịch vụ theo loại
        listDichVu.stream().filter(dv -> dv.getLoaiDichVu().equals(LoaiDichVu.DICHVUBAN)).forEach(listDichVuBan::add);
        listDichVu.stream().filter(dv -> dv.getLoaiDichVu().equals(LoaiDichVu.DICHVUCHOTHUE)).forEach(listDichVuThue::add);

        danhSachDV.setVgap(10);
        updateDanhSachDV(listDichVu, 1);

        leftSwapTab.getStyleClass().add("btn_hover");
        rightSwapTab.getStyleClass().add("btn_hover");
        fillterDVAll.getStyleClass().add("selected");
        lbSwapTab.setTextFormatter(createTextFormatter());
        danhSachDVDaChon.setPrefWidth(570);
        scrollPane.setContent(danhSachDVDaChon);
        scrollPane.setLayoutX(20);
        scrollPane.setLayoutY(70);
        scrollPane.setPrefWidth(580);
        scrollPane.setPrefHeight(390);
        scrollPane.setPadding(new Insets(5));
        scrollPane.getStyleClass().add("scrollPane");

        scrollPane.setFitToWidth(true);
        rightPane.getChildren().add(scrollPane);
        box_btnPhong.setSpacing(10);
        box_btnPhong.setPrefWidth(550);
        scrBox_btnPhong.setContent(box_btnPhong);
        scrBox_btnPhong.setPrefWidth(580);
        scrBox_btnPhong.setPrefHeight(50);
        scrBox_btnPhong.setLayoutX(20);
        scrBox_btnPhong.setLayoutY(10);
        scrBox_btnPhong.setPadding(new Insets(5));
        scrBox_btnPhong.getStyleClass().add("scrollPane");
        scrBox_btnPhong.setFitToWidth(true);
        rightPane.getChildren().add(scrBox_btnPhong);
        danhSachDVDaChon.setSpacing(15);

        saveBtn.setOnMouseClicked(e -> {
            if (lbThongTin.getText().equals("Trống")) {
                showDialog(stackPane, "Thông báo", "Vui lòng chọn dịch vụ.", "OK");
                return;
            }
            listBtnPhong.forEach(item -> {
            	item.getListChiTietDichVu().forEach(ctdv -> {
            		ctdv.setPhong(item.getPhong());
            		QuanLyHoaDon_Business.themChiTietHoaDonDV(ctdv);
            	});
            });
            showDialog(stackPane, "Thông báo", "Đặt dịch vụ thành công.", "OK");
            reset();
        });

        searchBtn.setOnMouseClicked(e -> {
            if (searchKHBar.getText().isEmpty()) {
                return;
            }
            String keyWord = searchKHBar.getText();
           
            chiTietPhongThue = QuanLyHoaDon_Business.UDP_GetChiTietPhongThueCuaKHHienTaiDangThue(keyWord);
            if (chiTietPhongThue == null) {
                showDialog(stackPane, "Thông báo", "Không tìm thấy thông tin thuê phòng của khách hàng.", "OK");
                return;
            } else {
                // lấy dữ liệu từ chi tiết phòng và tạo ra các phòng theo hóa đơn
                lbThongTin.setText(chiTietPhongThue.getKhachHang().getMaKhachHang() + " - " + chiTietPhongThue.getKhachHang().getTenKhachHang() + " - " + chiTietPhongThue.getKhachHang().getSoDienThoai());
                box_btnPhong.getChildren().clear();
                listBtnPhong.clear();
                ButtonPhongDatDichVuView_Controller btnBox = new ButtonPhongDatDichVuView_Controller(chiTietPhongThue.getPhong());
                listBtnPhong.add(btnBox);
                box_btnPhong.getChildren().add(btnBox.getButton());
                btnBox.getButton().setOnMouseClicked(event -> {
                    listBtnPhong.forEach(item -> {
                        if (item.isSelected()) {
                            item.setListChiTietDichVu(listDichVuDaChon);
                        }
                        item.setSelected(false);
                    });
                    listDichVuDaChon = btnBox.getListChiTietDichVu();
                    btnBox.setSelected(true);
                    updateDSDVDaChon();
                });
                if (!listBtnPhong.isEmpty()) {
                    listBtnPhong.get(0).setSelected(true);
                    listDichVuDaChon = listBtnPhong.get(0).getListChiTietDichVu();
                }
            }
        });

        // Lọc các dịch vụ theo loại, cập nhật theo index của thành nav
        fillterDVAll.setOnMouseClicked(e -> {
            searchDVBar.setText("");
            lbSwapTab.setText("1");
            int quan = Integer.parseInt(lbSwapTab.getText());
            if (quan > listDichVu.size() / 10 + 1) {
                lbSwapTab.setText(String.valueOf(listDichVu.size() / 10 + 1));
            }
            updateDanhSachDV(listDichVu, quan);
            clearSelectedStyles();
            fillterDVAll.getStyleClass().add("selected");
        });

        fillterDVSale.setOnMouseClicked(e -> {
            searchDVBar.setText("");
            lbSwapTab.setText("1");
            int quan = Integer.parseInt(lbSwapTab.getText());
            if (quan > listDichVuBan.size() / 10 + 1) {
                lbSwapTab.setText(String.valueOf(listDichVu.size() / 10 + 1));
            }
            updateDanhSachDV(listDichVuBan, quan);
            clearSelectedStyles();
            fillterDVSale.getStyleClass().add("selected");
        });

        fillterDVHire.setOnMouseClicked(e -> {
            searchDVBar.setText("");
            lbSwapTab.setText("1");
            int quan = Integer.parseInt(lbSwapTab.getText());
            if (quan > listDichVuThue.size() / 10 + 1) {
                lbSwapTab.setText(String.valueOf(listDichVu.size() / 10 + 1));
            }
            updateDanhSachDV(listDichVuThue, quan);
            clearSelectedStyles();
            fillterDVHire.getStyleClass().add("selected");
        });

        // Chuyển trang danh sách dịch vụ theo index
        leftSwapTab.setOnMouseClicked(e -> {
            ArrayList<DichVu> danhSachDV = getListSelected();
            if (!searchDVBar.getText().isEmpty()) {
                danhSachDV = listDichVuTimKiem;
            }
            int quan = Integer.parseInt(lbSwapTab.getText());
            if (quan > 1) {
                quan--;
                lbSwapTab.setText(String.valueOf(quan));
                updateDanhSachDV(danhSachDV, quan);
            }
        });
        rightSwapTab.setOnMouseClicked(e -> {
            ArrayList<DichVu> danhSachDV = getListSelected();
            if (!searchDVBar.getText().isEmpty()) {
                danhSachDV = listDichVuTimKiem;
            }
            int quan = Integer.parseInt(lbSwapTab.getText());
            if (quan < danhSachDV.size() / 10 + 1) {
                quan++;
                lbSwapTab.setText(String.valueOf(quan));
                updateDanhSachDV(danhSachDV, quan);
            }
        });

        // Nhận sự kiện khi thay đổi số trang thì cập nhật lại danh sách
        lbSwapTab.textProperty().addListener((observable,oldValue, newValue) -> {
            int quan = Integer.parseInt(newValue);
            if (quan < 1) {
                lbSwapTab.setText("1");
            }
            if (quan > listDichVu.size() / 10 + 1) {
                lbSwapTab.setText(String.valueOf(listDichVu.size() / 10 + 1));
            }
            updateDanhSachDV(listDichVu, Integer.parseInt(lbSwapTab.getText()));
        });

        // Nhận sự kiện khi thay đổi text trong ô tìm kiếm thì cập nhật số trang
        searchDVBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null || !newValue.isEmpty()) {
                listDichVuTimKiem = getListSelected().stream().filter(dv -> dv.getTenDichVu().toLowerCase().contains(newValue.toLowerCase()) || dv.getMaDichVu().toLowerCase().contains(newValue.toLowerCase())) .collect(Collectors.toCollection(ArrayList::new));
                updateDanhSachDV(listDichVuTimKiem, 1);
                lbSwapTab.setText("1");
            } else {
                updateDanhSachDV(getListSelected(), 1);
                lbSwapTab.setText("1");
            }
        });

        cancelBtn.setOnMouseClicked(e -> {
            reset();
        });
    }

    // Xóa tất cả thông tin trong trang
    public void reset() {
        searchKHBar.setText("");
        lbThongTin.setText("Trống");
        box_btnPhong.getChildren().clear();
        listDichVuDaChon.clear();
        listBtnPhong.clear();
        updateDSDVDaChon();
    }

    // Thêm một dịch vụ dã chọn vào danh sách dịch vụ đã chọn
    // Nếu đã có thì cập nhật số lượng
    public void addItemToDSDVSelect(DichVu dichVu) throws Exception {
        ChiTietDichVu ct = new ChiTietDichVu();
        ct.setDichVu(dichVu);
        ct.setHoaDon(chiTietPhongThue.getPhongThue().getHoaDon());
        ct.setDonGia(dichVu.getDonGia());
        if (listDichVuDaChon.contains(ct)) {
            listDichVuDaChon.get(listDichVuDaChon.indexOf(ct)).setSoLuong(listDichVuDaChon.get(listDichVuDaChon.indexOf(ct)).getSoLuong() + 1);
        } else {
            ct.setSoLuong(1);
            listDichVuDaChon.add(ct);
        }
    }

    // Cập nhật giao diện và danh sách dịch vụ đã chọn
    public void updateDSDVDaChon() {
        double tongTien = 0;
        this.danhSachDVDaChon.getChildren().clear();
        for (int i = 0; i < listDichVuDaChon.size(); i++) {
            ChiTietDichVu ctdv = listDichVuDaChon.get(i);
            DichVu dv = listDichVu.stream().filter(item -> item.getMaDichVu() == ctdv.getDichVu().getMaDichVu()).findFirst().get();
            tongTien += ctdv.getThanhTien();
            HBox dvBoxSelect = new HBox();
            dvBoxSelect.getStyleClass().add("dv_select");
            this.danhSachDVDaChon.getChildren().add(dvBoxSelect);

            Label lbSTT = new Label(String.valueOf(i + 1));
            lbSTT.getStyleClass().addAll("lbMain", "lbFontBold");
            lbSTT.setAlignment(Pos.CENTER);
            lbSTT.setPrefWidth(42);
            dvBoxSelect.getChildren().add(lbSTT);

            Label lbTenDV = new Label(ctdv.getDichVu().getTenDichVu() + " (" + ctdv.getDichVu().getDonViTinh() + ")");
            lbTenDV.getStyleClass().addAll("lbMain", "lbFontBold");
            lbTenDV.setAlignment(Pos.CENTER_LEFT);
            lbTenDV.setPrefWidth(150);
            dvBoxSelect.getChildren().add(lbTenDV);

            FontAwesomeIconView decrease = new FontAwesomeIconView(FontAwesomeIcon.MINUS_SQUARE_ALT);
            decrease.setSize(String.valueOf(20));
            decrease.getStyleClass().add("btn_hover");
            decrease.setOnMouseClicked(e -> {
                if (ctdv.getSoLuong() > 1) {
                    try {
                    	ctdv.setSoLuong(ctdv.getSoLuong() - 1);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    updateDSDVDaChon();
                }
            });
            dvBoxSelect.getChildren().add(decrease);

            TextField lbSoLuong = new TextField(String.valueOf(ctdv.getSoLuong()));
            lbSoLuong.getStyleClass().add("lbMain");
            lbSoLuong.setStyle("-fx-border-color: #ffffff #ffffff #000000 #ffffff; -fx-background-color: white;");
            lbSoLuong.setAlignment(Pos.CENTER);
            lbSoLuong.setPrefWidth(65);
            lbSoLuong.setTextFormatter(createTextFormatter());
            lbSoLuong.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                	int soLuongMax = dv.getSoLuong();
                	int soLuongHienTai = Integer.parseInt(newValue);
                	if (soLuongHienTai <= soLuongMax) {
                		ctdv.setSoLuong(soLuongHienTai);
                	}
                } catch (Exception ex) {
                    lbSoLuong.setText(oldValue);
                }
                updateDSDVDaChon();
                stackPane.requestFocus();
            });
            HBox.setMargin(lbSoLuong, new Insets(0, 10, 0, 10));
            dvBoxSelect.getChildren().add(lbSoLuong);

            FontAwesomeIconView increase = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE_ALT);
            increase.setSize(String.valueOf(20));
            increase.getStyleClass().add("btn_hover");
            increase.setOnMouseClicked(e -> {
                try {
                	ctdv.setSoLuong(ctdv.getSoLuong() + 1);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                updateDSDVDaChon();
            });
            dvBoxSelect.getChildren().add(increase);

            Label lbGiaDV = new Label(formatCurrency(ctdv.getThanhTien()));
            lbGiaDV.getStyleClass().add("lbMain");
            lbGiaDV.setStyle("-fx-background-color: white; -fx-border-color: #ffffff #ffffff #000000 #ffffff;");
            lbGiaDV.setAlignment(Pos.CENTER);
            lbGiaDV.setPrefWidth(180);
            HBox.setMargin(lbGiaDV, new Insets(0, 20, 0, 20));
            dvBoxSelect.getChildren().add(lbGiaDV);

            FontAwesomeIconView option = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
            option.setSize(String.valueOf(20));
            option.getStyleClass().add("btn_hover");
            option.setOnMouseClicked(e -> {
                listDichVuDaChon.remove(ctdv);
                updateDSDVDaChon();
            });
            dvBoxSelect.getChildren().add(option);
        }

        lbTongTien.setText(formatCurrency(tongTien));
    }

    // Lấy danh sách dịch vụ đang chọn
    public ArrayList<DichVu> getListSelected() {
        if (fillterDVHire.getStyleClass().contains("selected")) {
            return listDichVuThue;
        } else if (fillterDVSale.getStyleClass().contains("selected")) {
            return listDichVuBan;
        } else {
            return listDichVu;
        }
    }

    // Tạo text formatter cho ô số lượng
    // Format chỉ được đúng theo số nguyên
    public TextFormatter<String> createTextFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d+")) {
                return change;
            }
            return null;
        });
    }

    private void clearSelectedStyles() {
        fillterDVAll.getStyleClass().remove("selected");
        fillterDVSale.getStyleClass().remove("selected");
        fillterDVHire.getStyleClass().remove("selected");
    }

    // Cập nhật giao diện danh sách dịch vụ
    public void updateDanhSachDV(ArrayList<DichVu> danhSachDV, int index) {
        this.danhSachDV.getChildren().clear();
        int start = (index - 1) * 10;
        int end = index * 10;
        if (end > danhSachDV.size()) {
            end = danhSachDV.size();
        }
        for (int i = start; i < end; i++ ) {
            DichVu dv = danhSachDV.get(i);
            if (dv.getTrangThaiDichVu() != TrangThaiDichVu.DANGHOATDONG) {
                continue;
            }
            HBox dvBox = new HBox();
            dvBox.setOnMouseClicked(e -> {
                if (lbThongTin.getText().equals("Trống")) {
                    showDialog(stackPane, "Thông báo", "Vui lòng nhập thông tin khách hàng.", "OK");
                    return;
                }
                try {
                    addItemToDSDVSelect(dv);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                updateDSDVDaChon();
            });
            dvBox.getStyleClass().add("dv_box");
            this.danhSachDV.getChildren().add(dvBox);

            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/hinhDichVu/"+ dv.getMaDichVu() +".jpg")));
            Rectangle rectangle = new Rectangle(70, 70);
            rectangle.setFill(new ImagePattern(img));
            rectangle.getStyleClass().add("img_rectangle");
            dvBox.getChildren().add(rectangle);

            VBox dvInfo = new VBox();
            dvBox.getChildren().add(dvInfo);

            Label lbTenDV = new Label(dv.getTenDichVu() + " (" + dv.getDonViTinh() + ")");
            lbTenDV.getStyleClass().add("lbTenDV");
            dvInfo.getChildren().add(lbTenDV);

            Label lbGiaDV = new Label(formatCurrency(dv.getDonGia()));
            lbGiaDV.getStyleClass().add("lbGiaDV");
            dvInfo.getChildren().add(lbGiaDV);
        }
    }

    // Format tiền tệ
    public static String formatCurrency(double amount) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        String formattedNumber = numberFormat.format(amount);

        return formattedNumber + " VND";
    }

    // Hiển thị dialog thông báo
    public static void showDialog(StackPane root, String title, String content, String buttonText) {
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