package gui.controllers;

import bus.KhachHang_Business;
import dao.KhachHang_DAO;
import dto.KhachHang;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QuanLiKhachHang_controller implements Initializable {
		@FXML
	    private StackPane stackPane;
        @FXML
        private Button btnCapNhat;

        @FXML
        private Button btnLamMoi;

        @FXML
        private Button btnThem;

        @FXML
        private TableView<KhachHang> table;

        @FXML
        private TextField txtCCCD;

        @FXML
        private TextField txtMaKH;

        @FXML
        private DatePicker txtNgaySinh;

        @FXML
        private TextField txtSDT;

        @FXML
        private TextField txtTenKH;

        @FXML
        private TextField txtTimKiem;

        @FXML
        private TableColumn<KhachHang, String> cccdColumn = new TableColumn<>("cCCD");

        @FXML
        private TableColumn<KhachHang, String> maKHColumn;

        @FXML
        private TableColumn<KhachHang, Date> ngaySinhColumn;

        @FXML
        private TableColumn<KhachHang, String> sdtColumn;

        @FXML
        private TableColumn<KhachHang, String> tenKHColumn;
        @FXML
        private TableColumn<KhachHang, String> thanhVienCol;

        private  ObservableList<KhachHang> list;

        private  KhachHang_DAO daoKH = new KhachHang_DAO();
//        private TheThanhVien_DAO daoTheThanhVien = new TheThanhVien_DAO();
        List<KhachHang> khachHangList;
        private KhachHang_Business quanLyKhachHang_business = new KhachHang_Business();

        private void clearFields() {
                txtMaKH.clear();
                txtTenKH.clear();
                txtSDT.clear();
                txtCCCD.clear();
                txtNgaySinh.setValue(null);
        }
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                // Thiết lập ánh xạ dữ liệu cho các cột
                maKHColumn.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
                tenKHColumn.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
                cccdColumn.setCellValueFactory(celldata -> {
                	 KhachHang khachHang = (KhachHang) celldata.getValue();
                	 return new SimpleObjectProperty<>(khachHang.getCCCD());
                });
                ngaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
                sdtColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
                txtMaKH.setDisable(true);
                // Đọc dữ liệu vào TableView
                DocDuLieuDatabaseVaoTable();

                btnThem.setOnAction(actionEvent -> {
                        //Lấy dữ liệu tưf textField
                        KhachHang khachHang = valid();
                        try{
                                daoKH.create(khachHang);
                            	showDialog(stackPane, "Thông báo", "Thêm khách hàng thành công", "OK");
                                DocDuLieuDatabaseVaoTable();
                        }catch(Exception e){
                        	showDialog(stackPane, "Lỗi", "Lỗi khi thêm khách hàng", "OK");
                        }
                        clearFields();
                });
                btnCapNhat.setOnAction(actionEvent -> {
                        // Kiểm tra khách hàng đã được chọn từ bảng chưa
                        KhachHang chonKhachHang = table.getSelectionModel().getSelectedItem();

                        if (chonKhachHang != null) {
                                // Lấy thông tin mới từ các trường TextField
                                KhachHang khachHangMoi = valid();

                                if (khachHangMoi != null) {  // Kiểm tra thông tin hợp lệ
                                        // Gán lại mã khách hàng để đảm bảo đúng thông tin khách hàng được cập nhật
                                        khachHangMoi.setMaKhachHang(chonKhachHang.getMaKhachHang());
                                        try {
                                                boolean capNhatThanhCong = daoKH.update(khachHangMoi);

                                                if (capNhatThanhCong) {
                                                		showDialog(stackPane, "Thông báo", "Cập nhật khách hàng thành công", "OK"); 
                                                        DocDuLieuDatabaseVaoTable();
                                                } else {
                                                		showDialog(stackPane, "Thông báo", "Cập nhật khách hàng thất bại", "OK");
                                                }
                                        } catch (Exception e) {
                                        	showDialog(stackPane, "Lỗi", "Lỗi khi cập nhật khách hàng", "OK");
                                        }
                                }
                        } else {
                        	showDialog(stackPane, "Lỗi", "Vui lòng chọn một khách hàng từ bảng để cập nhật", "OK");
                        }

                        // Sau khi cập nhật, xóa các trường nhập liệu
                        clearFields();
                });

                btnLamMoi.setOnAction(actionEvent -> {
                        //Sự kiên làm mới
                        DocDuLieuDatabaseVaoTable();
                        txtTimKiem.clear();
                        clearFields();
                });
                txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
                        String keyWord = txtTimKiem.getText().trim();

                        // Nếu ô tìm kiếm trống thì hiển thị lại toàn bộ danh sách khách hàng
                        if (keyWord.isEmpty()) {
                                table.setItems(FXCollections.observableArrayList(khachHangList));
                        } else {
                                ArrayList<KhachHang> temp = daoKH.timKhachHangSDT(keyWord);

                                // Chuyển đổi kết quả thành ObservableList
                                ObservableList<KhachHang> list = FXCollections.observableArrayList(temp);
                                table.setItems(list);
                        }
                });

                table.setOnMouseClicked(mouseEvent -> {
                        KhachHang chonKhachHang = table.getSelectionModel().getSelectedItem();
                        if (chonKhachHang != null) {
                                // Đổ dữ liệu vào các textField
                                txtMaKH.setText(chonKhachHang.getMaKhachHang());
                                txtTenKH.setText(chonKhachHang.getTenKhachHang());
                                txtSDT.setText(chonKhachHang.getSoDienThoai());
                                txtCCCD.setText(chonKhachHang.getCCCD());
                                txtNgaySinh.setValue(chonKhachHang.getNgaySinh());
                        }
                });
        }

        public KhachHang valid() {
                String tenKH = txtTenKH.getText();
                String sdt = txtSDT.getText();
                String cccd = txtCCCD.getText();
                LocalDate ngaySinh = txtNgaySinh.getValue();

                if (tenKH == null || tenKH.trim().isEmpty() ||
                        sdt == null || sdt.trim().isEmpty() ||
                        cccd == null || cccd.trim().isEmpty() ||
                        ngaySinh == null) {
                        showDialog(stackPane, "Lỗi", "Vui lòng nhập đầy đủ thông tin", "OK");
                        return null;
                }
                if (tenKH.trim().split("\\s+").length < 2) {
                        showDialog(stackPane, "Lỗi", "Họ và tên khách hàng phải có ít nhất 2 từ", "OK");
                        return null;
                }
                if (cccd.length() != 12 || !cccd.matches("\\d{12}")) {
                        showDialog(stackPane, "Lỗi", "Số CCCD của khách hàng phải gồm 12 ký tự số", "OK");
                        return null;
                }
                if (ngaySinh.isAfter(LocalDate.now())) {
                		showDialog(stackPane, "Lỗi", "Ngày sinh phải trước ngày hiện tại", "OK");  
                        return null;
                } else {
                        //int tuoi = Period.between(ngaySinh, LocalDate.now()).getYears();
                        long tuoi = ChronoUnit.YEARS.between(ngaySinh, LocalDate.now());
                        if (tuoi < 18) {
                                showDialog(stackPane, "Lỗi", "Tuổi không được nhỏ hơn 18", "OK"); 
                                return null;
                        }
                }
                if (sdt.length() != 10 || !sdt.matches("\\d{10}")) {
                        showDialog(stackPane, "Lỗi", "Số điện thoại phải gồm 10 chữ số", "OK"); 
                        return null;
                }
                //KhachHang khachHang = new KhachHang(txtMaKH.getText() ,tenKH,cccd, Date.valueOf(ngaySinh),sdt);
                KhachHang khachHang = new KhachHang(txtMaKH.getText(), tenKH, cccd, ngaySinh, sdt);
                return khachHang;
        }


        //Hàm đoc từ Database
        public void DocDuLieuDatabaseVaoTable() {
                khachHangList = daoKH.getKhachHangGioiHan();
                list = FXCollections.observableArrayList(khachHangList);
                table.setItems(list);
                table.refresh();
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
